package frostygames0.elementalamulets.block.entity.pipe;

import com.mojang.datafixers.util.Pair;
import com.mojang.logging.LogUtils;
import frostygames0.elementalamulets.block.entity.pipe.source.FlowSource;
import frostygames0.elementalamulets.element.ElementType;
import frostygames0.elementalamulets.element.storage.IElementStorage;
import frostygames0.elementalamulets.network.debug.ModDebugPackets;
import frostygames0.elementalamulets.util.BlockFace;
import frostygames0.elementalamulets.util.capability.ICapabilityProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.util.Mth;
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

    private final List<TransferTarget> targets = new ArrayList<>();
    private final List<QueuedBlockFace> queued = new ArrayList<>();
    private final Set<ConnectionToVisit> toVisit = new HashSet<>();

    private final Set<BlockPos> visitedPositions = new HashSet<>();

    private final Map<BlockPos, WeakReference<BaseElementalPipeBlockEntity>> pipesCache = new HashMap<>();

    @Nullable
    private Holder<ElementType> element = null;
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
        visitedPositions.clear();
        targets.clear();
        queued.clear();

        element = null;
        queued.add(new QueuedBlockFace(1, startBlockFace));
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

                var distance = pair.distanceFromSource();
                var blockFace = pair.blockFace();

                if (!level.isLoaded(blockFace.pos())) {
                    continue;
                }

                var pipeConnection = getPipeConnection(blockFace);
                if (pipeConnection != null) {
                    // TODO Remake speed algorithm to be based on distanceFromSource
                    if (blockFace.equals(startBlockFace)) {
                        transferSpeed = (int) Math.max(1, pipeConnection.getInboundPressure() / 4f);
                    }

                    toVisit.add(new ConnectionToVisit(distance, blockFace, pipeConnection));
                }
                iterator.remove();
            }

            for (var iterator = toVisit.iterator(); iterator.hasNext(); ) {
                var connectionToVisit = iterator.next();

                var distance = connectionToVisit.distanceFromSource();
                var blockFace = connectionToVisit.blockFace();
                var pipeConnection = connectionToVisit.pipeConnection();

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
                        targets.add(new TransferTarget(distance, adjacentBlockFace, adjacentPipeConnection.getSource()));
                        LOGGER.debug("Found target[{}] for network[{}] with distanceFromSource: {}",
                                adjacentBlockFace.pos().toShortString(),
                                startBlockFace.pos().toShortString(),
                                distance);
                        ModDebugPackets.sendPipeNetworkDebugInfo(startBlockFace, targets);
                        continue;
                    }

                    // Add the connected position to our queue (so we can do the same thing again)
                    if (visitedPositions.add(adjacentBlockFace.getConnectedPos())) {
                        queued.add(new QueuedBlockFace(distance + 1, adjacentBlockFace.getOpposite()));
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

    private void equalTransfer() {
        if (source == null) {
            return;
        }

        var sourceStorage = source.getCapability();
        if (sourceStorage == null) {
            return;
        }

        var perTarget = 1;

        var amountTaken = 0;
        if (sourceStorage.canTakeElement(element)) {
            amountTaken = sourceStorage.takeElement(element, targets.size() * perTarget, IElementStorage.Operation.SIMULATE);
        }

        if (amountTaken == 0) {
            return;
        }

        float amountRemaining = amountTaken;

        var targetsByDistance = targets.stream().sorted((Comparator.comparingInt(target -> target.distanceFromSource))).toList();
        for (var target : targetsByDistance) {
            var toTransferAmount = Mth.floor(amountRemaining / targetsByDistance.size());
            if (toTransferAmount == 0) {
                continue;
            }

        }
    }

    private void transfer() {
        if (level.getGameTime() % 20L != 0) {
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
            amountTaken = sourceStorage.takeElement(element, targets.size(), IElementStorage.Operation.SIMULATE);
        }

        if (amountTaken == 0) {
            return;
        }

        var remaining = amountTaken;

        var equalAmount = amountTaken / targets.size();
        var remainder = amountTaken % targets.size();

        var targetsSortedByDistance = targets.stream().sorted(Comparator.comparingInt(target -> target.distanceFromSource)).toList();
        for (var target : targetsSortedByDistance) {
            var amountToPut = equalAmount;
            if (remaining <= 0) {
                break;
            }

            if (remainder > 0) {
                amountToPut++;
                remainder--;
            }

            var targetSource = target.flowSource;

            var storageProvider = targetSource.getElementStorageProvider();

            if (storageProvider != null) {
                var targetStorage = storageProvider.getCapability();
                if (targetStorage != null) {
                    var putInsideTargetAmount = targetStorage.addElement(element, amountToPut, IElementStorage.Operation.SIMULATE);
                    if (putInsideTargetAmount == 0) {
                        continue;
                    }

                    targetStorage.addElement(element, putInsideTargetAmount, IElementStorage.Operation.PERFORM);
                    remaining -= sourceStorage.takeElement(element, putInsideTargetAmount, IElementStorage.Operation.PERFORM);
                    continue;
                }
            }

            remaining -= sourceStorage.takeElement(element, amountToPut, IElementStorage.Operation.PERFORM);
        }
    }

    private void transferFromSourceToTargets() {
        if (level.getGameTime() % 20 != 0) {
            return;
        }

        int flowSpeed = transferSpeed;
        Map<IElementStorage, Integer> accumulatedFill = new IdentityHashMap<>();

        for (boolean simulate : PipeConnection.TRUE_AND_FALSE) {
            var operation = simulate ? IElementStorage.Operation.SIMULATE : IElementStorage.Operation.PERFORM;

            if (source == null) {
                return;
            }

            IElementStorage sourceCap = source.getCapability();
            if (sourceCap == null) {
                return;
            }

            Pair<Holder<ElementType>, Integer> transfer = null;
            if (sourceCap.containsElement(element)) {
                if (sourceCap.canTakeElement(element)) {
                    var taken = sourceCap.takeElement(element, flowSpeed, operation);
                    if (taken > 0) {
                        transfer = Pair.of(element, taken);
                    }
                }
            }

            if (transfer == null) {
                return;
            }

            if (simulate) {
                flowSpeed = transfer.getSecond();
            }

            List<TransferTarget> availableOutputs = new ArrayList<>(targets);
            while (!availableOutputs.isEmpty() && transfer.getSecond() > 0) {
                int dividedTransfer = transfer.getSecond() / availableOutputs.size();
                int remainder = transfer.getSecond() % availableOutputs.size();

                for (var iterator = availableOutputs.iterator(); iterator.hasNext(); ) {
                    var target = iterator.next();
                    int toTransfer = dividedTransfer;
                    if (remainder > 0) {
                        toTransfer++;
                        remainder--;
                    }

                    var targetHandlerProvider = target.flowSource.getElementStorageProvider();
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

                    transfer = Pair.of(transfer.getFirst(), transfer.getSecond() - fill);
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
        for (var target : targets) {
            if (target.flowSource != null && level.getGameTime() % 40 != 0) {
                continue;
            }

            PipeConnection pipeConnection = getPipeConnection(target.blockFace);
            if (pipeConnection == null) {
                continue;
            }
            var source = pipeConnection.getSource();
            if (source != null) {
                if (source.isEndpoint()) {
                    target.flowSource = source;
                }
            }
        }
    }

    public void onProbablyRemoved() {
        ModDebugPackets.sendPipeNetworkRemoved(startBlockFace);
    }

    private PipeConnection getPipeConnection(BlockFace blockFace) {
        var pos = blockFace.pos();
        var pipe = getPipeBE(pos);
        if (pipe == null) {
            return null;
        }

        return pipe.getConnection(blockFace.face());
    }

    private BaseElementalPipeBlockEntity getPipeBE(BlockPos blockPos) {
        var weakReference = pipesCache.get(blockPos);
        var pipe = weakReference != null ? weakReference.get() : null;

        if (pipe != null && pipe.isRemoved()) {
            pipe = null;
        }
        if (pipe == null) {
            pipe = PipeHelper.getPipeBlockEntity(level, blockPos).orElse(null);
            if (pipe != null) {
                pipesCache.put(blockPos, new WeakReference<>(pipe));
            }
        }
        return pipe;
    }

    private record ConnectionToVisit(int distanceFromSource, BlockFace blockFace, PipeConnection pipeConnection) {
    }

    private record QueuedBlockFace(int distanceFromSource, BlockFace blockFace) {
    }

    public static class TransferTarget {
        private final int distanceFromSource;
        private final BlockFace blockFace;
        private FlowSource flowSource;

        public TransferTarget(int distanceFromSource, BlockFace blockFace, FlowSource flowSource) {
            this.distanceFromSource = distanceFromSource;
            this.blockFace = blockFace;
            this.flowSource = flowSource;
        }

        public int distanceFromSource() {
            return distanceFromSource;
        }

        public BlockFace blockFace() {
            return blockFace;
        }

        public FlowSource flowSource() {
            return flowSource;
        }
    }
}
