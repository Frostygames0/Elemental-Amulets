package frostygames0.elementalamulets.block.pipe;

import frostygames0.elementalamulets.block.entity.pipe.PipeHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.ticks.TickPriority;

public abstract class BasePipeBlock extends BaseEntityBlock {
    protected BasePipeBlock(Properties properties) {
        super(properties);
    }

    public abstract boolean isOpen(BlockState state, Direction side);

    protected BlockState updatePipeState(BlockAndTintGetter level, BlockState state, BlockPos blockPos, Direction lookingDirection) {
        return state;
    }

    @Override
    protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess scheduledTickAccess, BlockPos pos, Direction direction, BlockPos neighborPos, BlockState neighborState, RandomSource random) {
        state = updatePipeState(level, state, pos, direction);

        var d = PipeHelper.validateNeighbourChange(state, level, pos, neighborState, neighborPos);
        if (d != null) {
            if (isOpen(state, direction)) {
                scheduledTickAccess.scheduleTick(pos, this, 1, TickPriority.HIGH);
            }
        }

        return state;
    }

    @Override
    protected void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
        if (!state.is(newState.getBlock()) && !level.isClientSide) {
            PipeHelper.propagateChangedPipe(level, pos, state);
        }

        super.onRemove(state, level, pos, newState, movedByPiston);
    }

    @Override
    protected void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) {
        if (state != oldState && !level.isClientSide) {
            level.scheduleTick(pos, this, 1, TickPriority.HIGH);
        }
    }

    @Override
    protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        PipeHelper.propagateChangedPipe(level, pos, state);
    }

    @Override
    protected boolean isPathfindable(BlockState state, PathComputationType pathComputationType) {
        return false;
    }
}
