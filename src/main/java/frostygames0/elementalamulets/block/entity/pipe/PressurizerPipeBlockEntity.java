package frostygames0.elementalamulets.block.entity.pipe;

import com.mojang.datafixers.util.Pair;
import frostygames0.elementalamulets.block.pipe.PressurizerPipeBlock;
import frostygames0.elementalamulets.element.ElementalHelper;
import frostygames0.elementalamulets.initialization.ModBlockEntities;
import frostygames0.elementalamulets.util.BlockFace;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;
import java.util.*;

public class PressurizerPipeBlockEntity extends BaseElementalPipeBlockEntity {
    private boolean frontNeedsUpdate;
    private boolean backNeedsUpdate;

    public PressurizerPipeBlockEntity(BlockPos pos, BlockState blockState) {
        super(ModBlockEntities.PRESSURIZER_PIPE.get(), pos, blockState);
    }

    @Override
    public boolean canHaveFlowToward(Direction side) {
        var blockState = getBlockState();
        if (!(blockState.getBlock() instanceof PressurizerPipeBlock)) {
            return false;
        }

        return PressurizerPipeBlock.isOpen(blockState, side);
    }

    @Override
    public void tick() {
        super.tick();

        if (level.isClientSide()) {
            return;
        }

        if (frontNeedsUpdate) {
            distributePressureTo(getFrontFace());
            frontNeedsUpdate = false;
        }

        if (backNeedsUpdate) {
            distributePressureTo(getFrontFace().getOpposite());
            backNeedsUpdate = false;
        }
    }

    protected boolean isFrontSide(Direction side) {
        return side == getFrontFace();
    }

    @Nullable
    protected Direction getFrontFace() {
        if (!PressurizerPipeBlock.isPressurizerPipe(getBlockState())) {
            return null;
        }

        return getBlockState().getValue(PressurizerPipeBlock.FACING);
    }

    protected boolean isEnabled() {
        return PressurizerPipeBlock.isPressurizerPipe(getBlockState()) ? getBlockState().getValue(PressurizerPipeBlock.ENABLED) : false;
    }

    public void updatePressure() {
        for (var entry : interfaces.entrySet()) {
            boolean pull = isPullingOnSide(isFrontSide(entry.getKey()));

            var connection = entry.getValue();
            connection.setPressure(pull, isEnabled() ? PipeConnection.MAX_PRESSURE : 0);
            connection.setPressure(!pull, 0);
        }

        markDirtyAndSync();
    }

    @Override
    public void onLoad() {
        super.onLoad();
        updatePressure();
    }

    public void updatePipesOnSide(Direction side) {
        if (isFrontSide(side)) {
            frontNeedsUpdate = true;
        } else {
            backNeedsUpdate = true;
        }

        resetConnections();
        updatePressure();
    }

    public boolean isPullingOnSide(boolean front) {
        return !front;
    }

    // OPTIMIZE I don't really like this distribution, but it'll do for now
    protected void distributePressureTo(Direction side) {
        if (!isEnabled()) {
            return;
        }

        BlockFace start = new BlockFace(worldPosition, side);
        boolean pull = isPullingOnSide(isFrontSide(side));
        Set<BlockFace> targets = new HashSet<>();
        Map<BlockPos, Pair<Integer, Map<Direction, Boolean>>> pipeGraph = new HashMap<>();

        if (!pull) {
            PipeHelper.traversePipesAndResetNetworks(level, worldPosition, side.getOpposite());
        }

        if (!isValidEndpoint(level, start, pull)) {

            pipeGraph.computeIfAbsent(worldPosition, $ -> Pair.of(0, new IdentityHashMap<>()))
                    .getSecond()
                    .put(side, pull);
            pipeGraph.computeIfAbsent(start.getConnectedPos(), $ -> Pair.of(1, new IdentityHashMap<>()))
                    .getSecond()
                    .put(side.getOpposite(), !pull);

            List<Pair<Integer, BlockPos>> frontier = new ArrayList<>();
            Set<BlockPos> visited = new HashSet<>();

            int maxDistance = (int) PipeConnection.MAX_PRESSURE;
            frontier.add(Pair.of(1, start.getConnectedPos()));

            while (!frontier.isEmpty()) {
                Pair<Integer, BlockPos> entry = frontier.removeFirst();
                int distance = entry.getFirst();
                BlockPos currentPos = entry.getSecond();

                if (!level.isLoaded(currentPos)) {
                    continue;
                }
                if (visited.contains(currentPos)) {
                    continue;
                }
                visited.add(currentPos);

                var pipeOptional = PipeHelper.getPipeBlockEntity(level, currentPos);
                if (pipeOptional.isEmpty()) {
                    continue;
                }

                var pipe = pipeOptional.get();

                for (Direction face : PipeHelper.getPipeConnections(level.getBlockState(currentPos), pipe)) {
                    BlockFace blockFace = new BlockFace(currentPos, face);
                    BlockPos connectedPos = blockFace.getConnectedPos();

                    if (!level.isLoaded(connectedPos)) {
                        continue;
                    }
                    if (blockFace.isEquivalent(start)) {
                        continue;
                    }
                    if (isValidEndpoint(level, blockFace, pull)) {
                        pipeGraph.computeIfAbsent(currentPos, $ -> Pair.of(distance, new IdentityHashMap<>()))
                                .getSecond()
                                .put(face, pull);
                        targets.add(blockFace);
                        continue;
                    }

                    if (PipeHelper.getPipeBlockEntity(level, connectedPos).isEmpty()) {
                        continue;
                    }

                    if (visited.contains(connectedPos)) {
                        continue;
                    }

                    if (distance + 1 >= maxDistance) {
                        pipeGraph.computeIfAbsent(currentPos, $ -> Pair.of(distance, new IdentityHashMap<>()))
                                .getSecond()
                                .put(face, pull);
                        targets.add(blockFace);
                        continue;
                    }

                    pipeGraph.computeIfAbsent(currentPos, $ -> Pair.of(distance, new IdentityHashMap<>()))
                            .getSecond()
                            .put(face, pull);
                    pipeGraph.computeIfAbsent(connectedPos, $ -> Pair.of(distance + 1, new IdentityHashMap<>()))
                            .getSecond()
                            .put(face.getOpposite(), !pull);
                    frontier.add(Pair.of(distance + 1, connectedPos));
                }
            }
        }

        // DFS
        Map<Integer, Set<BlockFace>> validFaces = new HashMap<>();
        searchForEndpointRecursively(pipeGraph, targets, validFaces,
                new BlockFace(start.pos(), start.getOppositeFace()), pull);

        float pressure = Math.abs(PipeConnection.MAX_PRESSURE);
        for (Set<BlockFace> set : validFaces.values()) {
            int parallelBranches = Math.max(1, set.size() - 1);
            for (BlockFace face : set) {
                BlockPos pipePos = face.pos();
                Direction pipeSide = face.face();

                if (pipePos.equals(worldPosition)) {
                    continue;
                }

                boolean inbound = pipeGraph.get(pipePos)
                        .getSecond()
                        .get(pipeSide);

                var pipeOptional = PipeHelper.getPipeBlockEntity(level, pipePos);
                if (pipeOptional.isEmpty()) {
                    continue;
                }

                pipeOptional.get().addPressure(pipeSide, inbound, pressure / parallelBranches);
            }
        }

    }

    protected boolean searchForEndpointRecursively(Map<BlockPos, Pair<Integer, Map<Direction, Boolean>>> pipeGraph,
                                                   Set<BlockFace> targets, Map<Integer, Set<BlockFace>> validFaces, BlockFace currentFace, boolean pull) {
        BlockPos currentPos = currentFace.pos();
        if (!pipeGraph.containsKey(currentPos)) {
            return false;
        }
        Pair<Integer, Map<Direction, Boolean>> pair = pipeGraph.get(currentPos);
        int distance = pair.getFirst();

        boolean atLeastOneBranchSuccessful = false;
        for (Direction nextFacing : Direction.values()) {
            if (nextFacing == currentFace.face()) {
                continue;
            }
            Map<Direction, Boolean> map = pair.getSecond();
            if (!map.containsKey(nextFacing)) {
                continue;
            }

            var localTarget = new BlockFace(currentPos, nextFacing);
            if (targets.contains(localTarget)) {
                validFaces.computeIfAbsent(distance, $ -> new HashSet<>())
                        .add(localTarget);
                atLeastOneBranchSuccessful = true;
                continue;
            }

            if (map.get(nextFacing) != pull) {
                continue;
            }
            if (!searchForEndpointRecursively(pipeGraph, targets, validFaces,
                    new BlockFace(currentPos.relative(nextFacing), nextFacing.getOpposite()), pull)) {
                continue;
            }

            validFaces.computeIfAbsent(distance, $ -> new HashSet<>())
                    .add(localTarget);
            atLeastOneBranchSuccessful = true;
        }

        if (atLeastOneBranchSuccessful) {
            validFaces.computeIfAbsent(distance, $ -> new HashSet<>())
                    .add(currentFace);
        }

        return atLeastOneBranchSuccessful;
    }

    private boolean isValidEndpoint(Level level, BlockFace blockFace, boolean pull) {
        BlockPos connectedPos = blockFace.getConnectedPos();
        BlockState connectedState = level.getBlockState(connectedPos);
        BlockEntity blockEntity = level.getBlockEntity(connectedPos);
        Direction face = blockFace.face();

        if (PressurizerPipeBlock.isPressurizerPipe(connectedState) && PressurizerPipeBlock.isOpen(connectedState, face)) {
            if (blockEntity instanceof PressurizerPipeBlockEntity pressurizerPipe) {
                return pressurizerPipe.isPullingOnSide(pressurizerPipe.isFrontSide(blockFace.getOppositeFace())) != pull;
            }
        }

        // other pipe, no endpoint
        if (blockEntity instanceof BaseElementalPipeBlockEntity pipe) {
            if (pipe.canHaveFlowToward(blockFace.getOppositeFace())) {
                return false;
            }
        }

        if (ElementalHelper.hasElementStorage(level, connectedPos, blockFace.getOppositeFace())) {
            return true;
        }

        return PipeHelper.isOpenEnd(level, blockFace.pos(), face);
    }

}
