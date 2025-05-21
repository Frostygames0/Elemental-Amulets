package frostygames0.elementalamulets.block.entity.pipe;

import com.mojang.datafixers.util.Pair;
import frostygames0.elementalamulets.block.pipe.ElementalPipeBlock;
import frostygames0.elementalamulets.block.pipe.PressurizerPipe;
import frostygames0.elementalamulets.initialization.ModCapabilities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

import java.util.*;

public class PipeHelper {
    public static void propagateChangedPipe(LevelAccessor world, BlockPos pipePos, BlockState pipeState) {
        List<Pair<Integer, BlockPos>> frontier = new ArrayList<>();
        Set<BlockPos> visited = new HashSet<>();
        Set<Pair<PressurizerPipeBlockEntity, Direction>> discoveredPressurizers = new HashSet<>();

        frontier.add(Pair.of(0, pipePos));

        // Visit all connected pumps to update their network
        while (!frontier.isEmpty()) {
            Pair<Integer, BlockPos> pair = frontier.removeFirst();
            BlockPos currentPos = pair.getSecond();
            if (visited.contains(currentPos)) {
                continue;
            }
            visited.add(currentPos);
            var pipeOptional = getPipeBlockEntity(world, currentPos);
            if (pipeOptional.isEmpty()) {
                continue;
            }

            var pipe = pipeOptional.get();
            pipe.resetConnections();

            for (Direction direction : getPipeConnections(pipe)) {
                BlockPos target = currentPos.relative(direction);
                if (world instanceof Level l && !l.isLoaded(target)) {
                    continue;
                }

                BlockEntity blockEntity = world.getBlockEntity(target);
                BlockState targetState = world.getBlockState(target);
                if (PressurizerPipe.isPressurizerPipe(targetState)) {
                    if (blockEntity instanceof PressurizerPipeBlockEntity pressurizerPipe) {
                        if (!PressurizerPipe.isOpen(targetState, direction)) {
                            continue;
                        }

                        discoveredPressurizers.add(Pair.of(pressurizerPipe, direction.getOpposite()));
                        continue;
                    }
                }

                if (visited.contains(target)) {
                    continue;
                }

                var targetPipeOptional = getPipeBlockEntity(world, target);
                if (targetPipeOptional.isEmpty()) {
                    continue;
                }

                var targetPipe = targetPipeOptional.get();
                Integer distance = pair.getFirst();
                if (distance >= 16 && !targetPipe.hasAnyPressure()) {
                    continue;
                }
                if (targetPipe.canHaveFlowToward(direction.getOpposite())) {
                    frontier.add(Pair.of(distance + 1, target));
                }
            }
        }

        discoveredPressurizers.forEach(pair -> pair.getFirst().updatePipesOnSide(pair.getSecond()));
    }

    // TODO Maybe I can replace this
    public static List<Direction> getPipeConnections(BaseElementalPipeBlockEntity pipe) {
        List<Direction> list = new ArrayList<>();
        for (Direction d : Direction.values()) {
            if (pipe.canHaveFlowToward(d)) {
                list.add(d);
            }
        }
        return list;
    }

    public static void traversePipesAndResetNetworks(LevelAccessor level, BlockPos start, Direction side) {
        var frontier = new ArrayList<BlockPos>();
        var visited = new HashSet<BlockPos>();
        frontier.add(start);

        while (!frontier.isEmpty()) {
            BlockPos pos = frontier.removeFirst();
            if (visited.contains(pos)) {
                continue;
            }
            visited.add(pos);
            var pipeOptional = getPipeBlockEntity(level, pos);
            if (pipeOptional.isEmpty()) {
                continue;
            }

            var pipe = pipeOptional.get();

            for (Direction d : Direction.values()) {
                if (pos.equals(start) && d != side) {
                    continue;
                }
                BlockPos target = pos.relative(d);
                if (visited.contains(target)) {
                    continue;
                }

                PipeConnection connection = pipe.getConnection(d);

                if (connection == null) {
                    continue;
                }

                if (!connection.hasFlow()) {
                    continue;
                }

                if (!connection.isFlowInbound()) {
                    continue;
                }

                connection.resetNetwork();
                frontier.add(target);
            }
        }
    }

    public static Optional<BaseElementalPipeBlockEntity> getPipeBlockEntity(BlockGetter level, BlockPos blockPos) {
        return level.getBlockEntity(blockPos) instanceof BaseElementalPipeBlockEntity pipe ? Optional.of(pipe) : Optional.empty();
    }

    public static Direction validateNeighbourChange(BlockState state, LevelReader level, BlockPos pos, BlockState neighborState, BlockPos neighborPos) {
        if (level.isClientSide()) {
            return null;
        }

        var otherBlock = neighborState.getBlock();
        if (otherBlock instanceof ElementalPipeBlock) {
            return null;
        }
        if (otherBlock instanceof PressurizerPipe) {
            return null;
        }
        for (Direction d : Direction.values()) {
            if (!pos.relative(d).equals(neighborPos)) {
                continue;
            }
            return d;
        }
        return null;
    }

    public static boolean isOpenEnd(BlockGetter getter, BlockPos pos, Direction side) {
        BlockPos connectedPos = pos.relative(side);
        BlockState connectedState = getter.getBlockState(connectedPos);
        var pipeOptional = getPipeBlockEntity(getter, connectedPos);
        if (pipeOptional.isPresent() && pipeOptional.get().canHaveFlowToward(side.getOpposite())) {
            return false;
        }

        if (PressurizerPipe.isPressurizerPipe(connectedState) && PressurizerPipe.isOpen(connectedState, side)) {
            return false;
        }

        if (Block.isFaceFull(connectedState.getCollisionShape(getter, connectedPos), side.getOpposite())) {
            return false;
        }

        if (hasElementStorageCap(getter, connectedPos, side.getOpposite())) {
            return false;
        }

        return connectedState.canBeReplaced() && connectedState.getDestroySpeed(getter, connectedPos) != -1
                || connectedState.hasProperty(BlockStateProperties.WATERLOGGED);
    }

    public static boolean hasElementStorageCap(BlockGetter getter, BlockPos blockPos, Direction side) {
        var blockEntity = getter.getBlockEntity(blockPos);
        if (blockEntity == null || blockEntity.getLevel() == null) {
            return false;
        }

        var capability = blockEntity.getLevel().getCapability(ModCapabilities.ELEMENT_STORAGE_BLOCK, blockPos, side);
        return capability != null;
    }
}
