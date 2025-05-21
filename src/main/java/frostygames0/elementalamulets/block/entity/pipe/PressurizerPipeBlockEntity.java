package frostygames0.elementalamulets.block.entity.pipe;

import com.mojang.datafixers.util.Pair;
import frostygames0.elementalamulets.block.pipe.PipePressurizerBlock;
import frostygames0.elementalamulets.initialization.ModBlockEntities;
import frostygames0.elementalamulets.initialization.ModCapabilities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

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
        if (!(blockState.getBlock() instanceof PipePressurizerBlock)) {
            return false;
        }

        return PipePressurizerBlock.isOpen(blockState, side);
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

        for (var entry : interfaces.entrySet()) {
            boolean pull = isPullingOnSide(isFrontSide(entry.getKey()));

            var connection = entry.getValue();

            connection.setPressure(pull, PipeConnection.MAX_PRESSURE);
            connection.setPressure(!pull, 0);
        }
    }

    protected boolean isFrontSide(Direction side) {
        return side == getFrontFace();
    }

    protected Direction getFrontFace() {
        var state = getBlockState();
        if (!(state.getBlock() instanceof PipePressurizerBlock)) {
            return null;
        }

        return state.getValue(PipePressurizerBlock.FACING);
    }

    public void updatePipesOnSide(Direction side) {
        if (isFrontSide(side)) {
            frontNeedsUpdate = true;
        } else {
            backNeedsUpdate = true;
        }

        resetConnections();
    }

    public boolean isPullingOnSide(boolean front) {
        return !front;
    }

    // TODO There is bug somewhere (pressure distance)
    protected void distributePressureTo(Direction side) {

        PressurizerPipeBlockEntity.BlockFace start = new PressurizerPipeBlockEntity.BlockFace(worldPosition, side);
        boolean pull = isPullingOnSide(isFrontSide(side));
        Set<BlockFace> targets = new HashSet<>();
        Map<BlockPos, Pair<Integer, Map<Direction, Boolean>>> pipeGraph = new HashMap<>();

        if (!pull) {
            PipeHelper.traversePipesAndResetNetworks(level, worldPosition, side.getOpposite());
        }

        if (!hasReachedValidEndpoint(level, start, pull)) {

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

                for (Direction face : PipeHelper.getPipeConnections(pipe)) {
                    PressurizerPipeBlockEntity.BlockFace blockFace = new PressurizerPipeBlockEntity.BlockFace(currentPos, face);
                    BlockPos connectedPos = blockFace.getConnectedPos();

                    if (!level.isLoaded(connectedPos)) {
                        continue;
                    }
                    if (blockFace.isEquivalent(start)) {
                        continue;
                    }
                    if (hasReachedValidEndpoint(level, blockFace, pull)) {
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
        Map<Integer, Set<PressurizerPipeBlockEntity.BlockFace>> validFaces = new HashMap<>();
        searchForEndpointRecursively(pipeGraph, targets, validFaces,
                new PressurizerPipeBlockEntity.BlockFace(start.getPos(), start.getOppositeFace()), pull);

        float pressure = Math.abs(PipeConnection.MAX_PRESSURE);
        for (Set<PressurizerPipeBlockEntity.BlockFace> set : validFaces.values()) {
            int parallelBranches = Math.max(1, set.size() - 1);
            for (PressurizerPipeBlockEntity.BlockFace face : set) {
                BlockPos pipePos = face.getPos();
                Direction pipeSide = face.getFace();

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
                                                   Set<PressurizerPipeBlockEntity.BlockFace> targets, Map<Integer, Set<PressurizerPipeBlockEntity.BlockFace>> validFaces, PressurizerPipeBlockEntity.BlockFace currentFace, boolean pull) {
        BlockPos currentPos = currentFace.getPos();
        if (!pipeGraph.containsKey(currentPos)) {
            return false;
        }
        Pair<Integer, Map<Direction, Boolean>> pair = pipeGraph.get(currentPos);
        int distance = pair.getFirst();

        boolean atLeastOneBranchSuccessful = false;
        for (Direction nextFacing : Direction.values()) {
            if (nextFacing == currentFace.getFace()) {
                continue;
            }
            Map<Direction, Boolean> map = pair.getSecond();
            if (!map.containsKey(nextFacing)) {
                continue;
            }

            var localTarget = new PressurizerPipeBlockEntity.BlockFace(currentPos, nextFacing);
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
                    new PressurizerPipeBlockEntity.BlockFace(currentPos.relative(nextFacing), nextFacing.getOpposite()), pull)) {
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

    private boolean hasReachedValidEndpoint(Level level, PressurizerPipeBlockEntity.BlockFace blockFace, boolean pull) {
        BlockPos connectedPos = blockFace.getConnectedPos();
        BlockState connectedState = level.getBlockState(connectedPos);
        BlockEntity blockEntity = level.getBlockEntity(connectedPos);
        Direction face = blockFace.getFace();

        if (PipePressurizerBlock.isPressurizerPipe(connectedState) && PipePressurizerBlock.isOpen(connectedState, face)) {
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

        var cap = level.getCapability(ModCapabilities.ELEMENT_STORAGE_BLOCK, connectedPos, null);
        if (cap != null) {
            return true;
        }

        // open endpoint
        return true;
    }

    public static class BlockFace extends Pair<BlockPos, Direction> {

        public BlockFace(BlockPos first, Direction second) {
            super(first, second);
        }

        public boolean isEquivalent(BlockFace other) {
            if (equals(other)) {
                return true;
            }
            return getConnectedPos().equals(other.getPos()) && getPos().equals(other.getConnectedPos());
        }

        public BlockPos getPos() {
            return getFirst();
        }

        public Direction getFace() {
            return getSecond();
        }

        public Direction getOppositeFace() {
            return getSecond().getOpposite();
        }

        public BlockFace getOpposite() {
            return new BlockFace(getConnectedPos(), getOppositeFace());
        }

        public BlockPos getConnectedPos() {
            return getPos().relative(getFace());
        }

        public CompoundTag serializeNBT() {
            CompoundTag compoundNBT = new CompoundTag();
            compoundNBT.put("Pos", NbtUtils.writeBlockPos(getPos()));
            //NBTHelper.writeEnum(compoundNBT, "Face", getFace());
            return compoundNBT;
        }

        public static BlockFace fromNBT(CompoundTag compound) {
            return new BlockFace(NbtUtils.readBlockPos(compound, "Pos").orElseThrow(),
                    /*NBTHelper.readEnum(compound, "Face", Direction.class)*/null);
        }

    }
}
