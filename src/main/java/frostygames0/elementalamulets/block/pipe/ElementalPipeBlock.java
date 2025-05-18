package frostygames0.elementalamulets.block.pipe;

import com.mojang.serialization.MapCodec;
import frostygames0.elementalamulets.block.entity.pipe.ElementalPipeBlockEntity;
import frostygames0.elementalamulets.block.entity.pipe.PipeHelper;
import frostygames0.elementalamulets.initialization.ModBlockEntities;
import frostygames0.elementalamulets.initialization.ModBlocks;
import frostygames0.elementalamulets.initialization.ModCapabilities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.PipeBlock;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.level.redstone.Orientation;
import net.minecraft.world.ticks.TickPriority;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;

public class ElementalPipeBlock extends PipeBlock implements SimpleWaterloggedBlock, EntityBlock {
    public static final MapCodec<ElementalPipeBlock> CODEC = simpleCodec(ElementalPipeBlock::new);

    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    public static final float APOTHEM = 2 / 16F;

    public ElementalPipeBlock(Properties properties) {
        super(APOTHEM, properties);
        registerDefaultState(
                getStateDefinition().any()
                        .setValue(WATERLOGGED, false)
                        .setValue(NORTH, false)
                        .setValue(EAST, false)
                        .setValue(SOUTH, false)
                        .setValue(WEST, false)
                        .setValue(DOWN, false)
                        .setValue(UP, false)
        );
    }

    public static boolean isPipe(BlockState state) {
        return state.getBlock() instanceof ElementalPipeBlock;
    }

    public static boolean canConnectTo(BlockAndTintGetter levelReader, BlockPos blockPos, Direction direction) {
        var relativeBlockPos = blockPos.relative(direction);
        var neighboringBlock = levelReader.getBlockState(relativeBlockPos);

        if (neighboringBlock.is(ModBlocks.TEST_BLOCK)) {
            return true;
        }

        if (levelReader instanceof Level level) {
            var storage = level.getCapability(ModCapabilities.ELEMENT_STORAGE_BLOCK, relativeBlockPos, null);
            if (storage != null) {
                return true;
            }
        }

        return isPipe(neighboringBlock);
    }

    public static boolean shouldHaveRim(LevelReader levelReader, BlockPos blockPos, Direction direction) {
        var relativeBlockPos = blockPos.relative(direction);
        var neighboringBlock = levelReader.getBlockState(relativeBlockPos);

        if (levelReader instanceof Level level) {
            var storage = level.getCapability(ModCapabilities.ELEMENT_STORAGE_BLOCK, relativeBlockPos, null);
            if (storage != null) {
                return true;
            }
        }

        if (!isPipe(neighboringBlock)) {
            return true;
        }

        return !canConnectTo(levelReader, blockPos, direction);
    }

    @Override
    protected MapCodec<? extends ElementalPipeBlock> codec() {
        return CODEC;
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        if (blockEntityType != ModBlockEntities.ELEMENTAL_PIPE.get()) {
            return null;
        }

        return (level1, pos, state1, blockEntity) -> ((ElementalPipeBlockEntity) blockEntity).tick();
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new ElementalPipeBlockEntity(pos, state);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(NORTH, EAST, SOUTH, WEST, UP, DOWN, WATERLOGGED);
    }

    @Override
    protected void neighborChanged(BlockState state, Level level, BlockPos pos, Block neighborBlock, @Nullable Orientation orientation, boolean movedByPiston) {
        super.neighborChanged(state, level, pos, neighborBlock, orientation, movedByPiston);
        if (!level.isClientSide) {
            level.scheduleTick(pos, this, 1, TickPriority.HIGH);
        }
    }

    @Override
    protected void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
        if (state.getBlock() != newState.getBlock() && !level.isClientSide) {
            PipeHelper.propagateChangedPipe(level, pos, state);
        }

        super.onRemove(state, level, pos, newState, movedByPiston);
    }

    @Override
    protected void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) {
        super.onPlace(state, level, pos, oldState, movedByPiston);
        if (state != oldState && !level.isClientSide) {
            level.scheduleTick(pos, this, 1, TickPriority.HIGH);
        }
    }


    @Override
    protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        super.tick(state, level, pos, random);
        PipeHelper.propagateChangedPipe(level, pos, state);
    }

    @Override
    protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess scheduledTickAccess, BlockPos pos, Direction direction, BlockPos neighborPos, BlockState neighborState, RandomSource random) {
        if (state.getValue(BlockStateProperties.WATERLOGGED)) {
            scheduledTickAccess.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
        }

        return updatePipeState(level, state, pos, direction);
    }

    // Thanks to Create and it's creators!
    private static BlockState updatePipeState(BlockAndTintGetter level, BlockState state, BlockPos blockPos, Direction lookingDirection) {
        var previousState = state;
        var previouslyConnectedSides = Arrays.stream(Direction.values())
                .map(PROPERTY_BY_DIRECTION::get)
                .filter(previousState::getValue)
                .count();

        var connectedCounter = 0;
        Direction connectedDirection = null;
        for (var dir : Direction.values()) {
            var canConnect = canConnectTo(level, blockPos, dir);
            state = state.setValue(PROPERTY_BY_DIRECTION.get(dir), canConnect);

            if (canConnect) {
                connectedCounter++;
                connectedDirection = dir;
            }
        }

        if (connectedCounter > 1) {
            return state;
        }

        if (connectedDirection != null) {
            return state.setValue(PROPERTY_BY_DIRECTION.get(connectedDirection.getOpposite()), true);
        }

        if (previouslyConnectedSides == 2) {
            return previousState;
        }

        return state.setValue(PROPERTY_BY_DIRECTION.get(lookingDirection), true).setValue(PROPERTY_BY_DIRECTION.get(lookingDirection.getOpposite()), true);
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext context) {
        var level = context.getLevel();
        var blockPos = context.getClickedPos();
        var lookingDirection = context.getNearestLookingDirection();
        var state = super.getStateForPlacement(context);
        var fluidState = level.getFluidState(blockPos);

        return updatePipeState(level, state, blockPos, lookingDirection)
                .setValue(WATERLOGGED, fluidState.getType().isSame(Fluids.WATER));
    }

    @Override
    protected boolean isPathfindable(BlockState state, PathComputationType pathComputationType) {
        return false;
    }

    @Override
    protected FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }
}
