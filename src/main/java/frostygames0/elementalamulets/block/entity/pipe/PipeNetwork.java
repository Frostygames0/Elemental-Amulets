package frostygames0.elementalamulets.block.entity.pipe;

import com.mojang.logging.LogUtils;
import frostygames0.elementalamulets.block.entity.pipe.source.FlowSource;
import frostygames0.elementalamulets.element.Element;
import frostygames0.elementalamulets.element.storage.IElementStorage;
import frostygames0.elementalamulets.element.storage.OperationMode;
import frostygames0.elementalamulets.network.debug.ModDebugPackets;
import frostygames0.elementalamulets.util.BlockFace;
import frostygames0.elementalamulets.util.ICapabilityProvider;
import frostygames0.elementalamulets.util.MutablePair;
import frostygames0.elementalamulets.util.MutableTriple;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import java.lang.ref.WeakReference;
import java.util.*;
import java.util.function.Supplier;

public class PipeNetwork {
    private static final Logger LOGGER = LogUtils.getLogger();

    public static final int FIND_ATTEMPTS = 16;
    private static final Direction[] DIRECTIONS = Direction.values();

    private final Level level;
    private final BlockFace startBlockFace;
    private final Supplier<ICapabilityProvider<IElementStorage>> sourceSupplier;

    private final List<MutableTriple<Integer, BlockFace, FlowSource>> targets = new ArrayList<>();
    private final List<MutablePair<Integer, BlockFace>> queued = new ArrayList<>();
    private final Set<MutableTriple<Integer, BlockFace, PipeConnection>> toVisit = new HashSet<>();
    private final Set<BlockPos> visited = new HashSet<>();
    private final Map<BlockPos, WeakReference<BaseElementalPipeBlockEntity>> cache = new HashMap<>();

    @Nullable
    private Holder<Element> element = null;
    @Nullable
    private ICapabilityProvider<IElementStorage> source;

    private int transferSpeed;
    private int pauseBeforeFirstTick;

    public PipeNetwork(Level level, BlockFace startBlockFace, Supplier<ICapabilityProvider<IElementStorage>> sourceSupplier) {
        this.level = level;
        this.startBlockFace = startBlockFace;
        this.sourceSupplier = sourceSupplier;

        LOGGER.debug("Pipe network was created at starting pos [{}]", startBlockFace.pos().toShortString());
        reset();

        ModDebugPackets.sendPipeNetworkDebugInfo(startBlockFace);
    }

    public void reset() {
        toVisit.clear();
        visited.clear();
        targets.clear();
        queued.clear();

        element = null;
        queued.add(MutablePair.of(1, startBlockFace));
        pauseBeforeFirstTick = 2;
    }

    public void tick() {
        if (pauseBeforeFirstTick > 0) {
            pauseBeforeFirstTick--;
            return;
        }

        findTargetsForTransfer();

        if (!tryAndGetSourceFromSupplier()) {
            return;
        }

        if (hasNoTargets()) {
            return;
        }

        setSourcesForTargetsIfNotSet();
//        transferFromSourceToTargets();
        transfer();
    }

    private boolean hasNoTargets() {
        return targets.isEmpty();
    }

    private void findTargetsForTransfer() {
        var shouldContinue = false;
        for (int i = 0; i < FIND_ATTEMPTS; i++) {
            for (var iterator = queued.iterator(); iterator.hasNext(); ) {
                var pair = iterator.next();

                var distance = pair.getFirst();
                var blockFace = pair.getSecond();

                if (!level.isLoaded(blockFace.pos())) {
                    continue;
                }

                var pipeConnection = getPipeConnection(blockFace);
                if (pipeConnection != null) {
                    // TODO Remake speed algorithm to be based on distance
                    if (blockFace.equals(startBlockFace)) {
                        transferSpeed = (int) Math.max(1, pipeConnection.getInboundPressure() / 4f);
                    }

                    toVisit.add(MutableTriple.of(distance, blockFace, pipeConnection));
                }
                iterator.remove();
            }

            for (var iterator = toVisit.iterator(); iterator.hasNext(); ) {
                var triple = iterator.next();

                var distance = triple.getFirst();
                var blockFace = triple.getSecond();
                var pipeConnection = triple.getThird();

                // If connection has no flow, then we skip it (we will check it once again up to 16 times actually)
                if (!pipeConnection.hasFlow()) {
                    continue;
                }

                // If connection has element inside, but it doesn't match our network's element, then we remove it, as there is no hope :(
                var elementInConnection = pipeConnection.getElement();
                if (element != null && !element.equals(elementInConnection)) {
                    iterator.remove();
                    continue;
                }

                // If connection is outbound, we skip it
                if (!pipeConnection.isFlowInbound()) {
                    // However if connection has strong outbound pressure, we remove it, as again, there is no hope :(
                    if (pipeConnection.comparePressure() >= 0) {
                        iterator.remove();
                    }

                    continue;
                }

                // There is no hope for the incomplete connection
                if (!pipeConnection.isFlowComplete()) {
                    continue;
                }

                // If network hasn't already decided what element it contains - do it now
                if (element == null) {
                    element = elementInConnection;
                }

                // This loop checks all other connections of the pipe
                var hasPotentialForBranch = false;
                for (var side : DIRECTIONS) {
                    // Obviously we skip ourselves
                    if (side == blockFace.face()) {
                        continue;
                    }

                    var adjacentBlockFace = blockFace.withDifferentFace(side);
                    var adjacentPipeConnection = getPipeConnection(adjacentBlockFace);
                    // If there is no connection, there is no point in continuing
                    if (adjacentPipeConnection == null) {
                        continue;
                    }

                    // If connection has no flow, but it has strong outbound, there is a potential for this connection to have flow
                    if (!adjacentPipeConnection.hasFlow()) {
                        if (adjacentPipeConnection.hasPressure() && adjacentPipeConnection.getOutboundPressure() > 0) {
                            hasPotentialForBranch = true;
                        }
                        continue;
                    }

                    // If flow is inbound but has strong outbound pressure - there is a potential
                    if (adjacentPipeConnection.isFlowInbound()) {
                        if (adjacentPipeConnection.comparePressure() > 0) {
                            hasPotentialForBranch = true;
                        }
                        continue;
                    }

                    // If pipe has flow, and it's not complete - there is a potential
                    if (!adjacentPipeConnection.isFlowComplete()) {
                        hasPotentialForBranch = true;
                        continue;
                    }

                    //If pipe has yet to find its source - it still has a potential to do so
                    if (!adjacentPipeConnection.hasSource() && !adjacentPipeConnection.tryLocateAndSetSource(level)) {
                        hasPotentialForBranch = true;
                        continue;
                    }

                    // If pipe has the source, and it's the endpoint - bingo we have found a target!
                    if (adjacentPipeConnection.hasSource() && adjacentPipeConnection.getSource().isEndpoint()) {
                        targets.add(MutableTriple.of(distance, adjacentBlockFace, adjacentPipeConnection.getSource()));
                        LOGGER.debug("Found target[{}] for network[{}] with distance: {}",
                                adjacentBlockFace.pos().toShortString(),
                                startBlockFace.pos().toShortString(),
                                distance);
                        ModDebugPackets.sendPipeNetworkDebugInfo(startBlockFace, targets);
                        continue;
                    }

                    // Add the connected position to our queue (so we can do the same thing again)
                    if (visited.add(adjacentBlockFace.getConnectedPos())) {
                        queued.add(MutablePair.of(distance + 1, adjacentBlockFace.getOpposite()));
                        shouldContinue = true;
                    }
                }

                if (!hasPotentialForBranch) {
                    iterator.remove();
                }
            }

            if (!shouldContinue) {
                break;
            }
        }
    }

    private void transferV2() {
        if (level.getGameTime() % 20 != 0) {
            return;
        }

        if (source == null) {
            return;
        }

        var sourceStorage = source.getCapability();
        if (sourceStorage == null) {
            return;
        }

        var amountTaken = 0;
        if (sourceStorage.containsElement(element) && sourceStorage.canTakeElement(element)) {
            amountTaken = sourceStorage.takeElement(element, targets.size(), OperationMode.SIMULATE);
        }

        if (amountTaken == 0) {
            return;
        }

        for (var target : targets) {
            var targetSource = target.getThird();

            var storageProvider = targetSource.getElementStorageProvider();


        }
    }

    private void transfer() {
        if (level.getGameTime() % (transferSpeed * 2L) != 0) {
            return;
        }

        if (source == null) {
            return;
        }

        var sourceStorage = source.getCapability();
        if (sourceStorage == null) {
            return;
        }

        var amountTaken = 0;
        if (sourceStorage.containsElement(element) && sourceStorage.canTakeElement(element)) {
            amountTaken = sourceStorage.takeElement(element, transferSpeed, OperationMode.SIMULATE);
        }

        if (amountTaken == 0) {
            return;
        }

        var remaining = amountTaken;

        var equalAmount = amountTaken / targets.size();
        var remainder = amountTaken % targets.size();

        for (var target : targets) {
            var amountToPut = equalAmount;
            if (remaining <= 0) {
                break;
            }

            if (remainder > 0) {
                amountToPut++;
                remainder--;
            }

            var targetSource = target.getThird();

            var storageProvider = targetSource.getElementStorageProvider();

            if (storageProvider != null) {
                var targetStorage = storageProvider.getCapability();
                if (targetStorage != null) {
                    var putInsideTargetAmount = targetStorage.addElement(element, amountToPut, OperationMode.SIMULATE);
                    if (putInsideTargetAmount == 0) {
                        continue;
                    }

                    targetStorage.addElement(element, putInsideTargetAmount, OperationMode.PERFORM);
                    remaining -= sourceStorage.takeElement(element, putInsideTargetAmount, OperationMode.PERFORM);
                    continue;
                }
            }

            remaining -= sourceStorage.takeElement(element, amountToPut, OperationMode.PERFORM);
        }
    }

    private void transferFromSourceToTargets() {
        if (level.getGameTime() % 20 != 0) {
            return;
        }

        int flowSpeed = transferSpeed;
        Map<IElementStorage, Integer> accumulatedFill = new IdentityHashMap<>();

        for (boolean simulate : PipeConnection.TRUE_AND_FALSE) {
            var operation = simulate ? OperationMode.SIMULATE : OperationMode.PERFORM;

            if (source == null) {
                return;
            }

            IElementStorage sourceCap = source.getCapability();
            if (sourceCap == null) {
                return;
            }

            MutablePair<Holder<Element>, Integer> transfer = null;
            if (sourceCap.containsElement(element)) {
                if (sourceCap.canTakeElement(element)) {
                    var taken = sourceCap.takeElement(element, flowSpeed, operation);
                    if (taken > 0) {
                        transfer = MutablePair.of(element, taken);
                    }
                }
            }

            if (transfer == null) {
                return;
            }

            if (simulate) {
                flowSpeed = transfer.getSecond();
            }

            List<MutableTriple<Integer, BlockFace, FlowSource>> availableOutputs = new ArrayList<>(targets);
            while (!availableOutputs.isEmpty() && transfer.getSecond() > 0) {
                int dividedTransfer = transfer.getSecond() / availableOutputs.size();
                int remainder = transfer.getSecond() % availableOutputs.size();

                for (var iterator = availableOutputs.iterator(); iterator.hasNext(); ) {
                    var triple = iterator.next();
                    int toTransfer = dividedTransfer;
                    if (remainder > 0) {
                        toTransfer++;
                        remainder--;
                    }

                    var targetHandlerProvider = triple.getThird().getElementStorageProvider();
                    if (targetHandlerProvider == null) {
                        iterator.remove();
                        continue;
                    }

                    var targetHandler = targetHandlerProvider.getCapability();
                    if (targetHandler == null) {
                        iterator.remove();
                        continue;
                    }

                    int simulatedTransfer = toTransfer;
                    if (simulate) {
                        simulatedTransfer += accumulatedFill.getOrDefault(targetHandler, 0);
                    }

                    int fill = targetHandler.addElement(transfer.getFirst(), simulatedTransfer, operation);

                    if (simulate) {
                        accumulatedFill.put(targetHandler, fill);
                        fill -= simulatedTransfer - toTransfer;
                    }

                    transfer = MutablePair.of(transfer.getFirst(), transfer.getSecond() - fill);
                    if (fill < simulatedTransfer) {
                        iterator.remove();
                    }
                }

            }

            flowSpeed -= transfer.getSecond();
        }
    }

    private boolean tryAndGetSourceFromSupplier() {
        if (source != null) {
            return true;
        }

        source = sourceSupplier.get();
        return source != null;
    }

    private void setSourcesForTargetsIfNotSet() {
        for (var triple : targets) {
            if (triple.getThird() != null && level.getGameTime() % 40 != 0) {
                continue;
            }
            PipeConnection pipeConnection = getPipeConnection(triple.getSecond());
            if (pipeConnection == null) {
                continue;
            }
            var source = pipeConnection.getSource();
            if (source != null) {
                if (source.isEndpoint()) {
                    triple.setThird(source);
                }
            }
        }
    }

    private PipeConnection getPipeConnection(BlockFace blockFace) {
        var pos = blockFace.pos();
        var pipe = getPipeBlockEntity(pos);
        if (pipe == null) {
            return null;
        }

        return pipe.getConnection(blockFace.face());
    }

    private BaseElementalPipeBlockEntity getPipeBlockEntity(BlockPos blockPos) {
        var weakReference = cache.get(blockPos);
        var pipe = weakReference != null ? weakReference.get() : null;

        if (pipe != null && pipe.isRemoved()) {
            pipe = null;
        }
        if (pipe == null) {
            pipe = PipeHelper.getPipeBlockEntity(level, blockPos).orElse(null);
            if (pipe != null) {
                cache.put(blockPos, new WeakReference<>(pipe));
            }
        }
        return pipe;
    }
}
