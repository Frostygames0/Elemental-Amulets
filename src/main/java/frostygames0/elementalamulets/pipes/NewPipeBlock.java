package frostygames0.elementalamulets.pipes;

import com.google.common.collect.ImmutableMap;
import com.mojang.serialization.MapCodec;
import frostygames0.elementalamulets.element.ElementalHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.redstone.Orientation;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.ticks.TickPriority;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Map;

public class NewPipeBlock extends BaseEntityBlock implements SimpleWaterloggedBlock {
    private static final Direction[] DIRECTIONS = Direction.values();

    public static final MapCodec<NewPipeBlock> CODEC = simpleCodec(NewPipeBlock::new);

    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    public static final BooleanProperty NORTH = BlockStateProperties.NORTH;
    public static final BooleanProperty EAST = BlockStateProperties.EAST;
    public static final BooleanProperty SOUTH = BlockStateProperties.SOUTH;
    public static final BooleanProperty WEST = BlockStateProperties.WEST;
    public static final BooleanProperty UP = BlockStateProperties.UP;
    public static final BooleanProperty DOWN = BlockStateProperties.DOWN;

    public static final Map<Direction, BooleanProperty> PROPERTY_BY_DIRECTION =
            ImmutableMap.<Direction, BooleanProperty>builder()
                    .put(Direction.NORTH, NORTH)
                    .put(Direction.EAST, EAST)
                    .put(Direction.SOUTH, SOUTH)
                    .put(Direction.WEST, WEST)
                    .put(Direction.UP, UP)
                    .put(Direction.DOWN, DOWN)
                    .build();

    public static final float APOTHEM = 2 / 16F;

    private final VoxelShape[] shapeByIndex;

    public NewPipeBlock(Properties properties) {
        super(properties);
        shapeByIndex = makeShapes();
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(NORTH, EAST, SOUTH, WEST, UP, DOWN, WATERLOGGED);
    }

    public static boolean isPipe(BlockState state) {
        return state.getBlock() instanceof NewPipeBlock;
    }
    
    public static boolean isConnected(BlockState state, Direction side) {
        return state.getValue(PROPERTY_BY_DIRECTION.get(side));
    }

    public static boolean canConnectTo(BlockAndTintGetter levelReader, BlockPos blockPos, Direction direction) {
        var adjacentBlockPos = blockPos.relative(direction);
        var adjacentBlockState = levelReader.getBlockState(adjacentBlockPos);

        if (ElementalHelper.hasElementStorage(levelReader, adjacentBlockPos, direction.getOpposite())) {
            return true;
        }

        if (isPipe(adjacentBlockState)) {
            return true;
        }

        return false;
    }

    private static BlockState updatePipeState(BlockAndTintGetter level, BlockState state, BlockPos blockPos, Direction lookingDirection) {
        var previousState = state;
        var previouslyConnectedSides = Arrays.stream(Direction.values())
                .map(PROPERTY_BY_DIRECTION::get)
                .filter(previousState::getValue)
                .count();

        var connectionsCount = 0;
        Direction connectedDirection = null;
        for (var direction : Direction.values()) {
            var isConnected = canConnectTo(level, blockPos, direction);
            state = state.setValue(PROPERTY_BY_DIRECTION.get(direction), isConnected);

            if (isConnected) {
                connectionsCount++;
                connectedDirection = direction;
            }
        }

        if (connectionsCount > 1) {
            return state;
        }

        if (connectedDirection != null) {
            return state.setValue(PROPERTY_BY_DIRECTION.get(connectedDirection.getOpposite()),true);
        }

        if (previouslyConnectedSides == 2) {
            return previousState;
        }

        return state.setValue(PROPERTY_BY_DIRECTION.get(lookingDirection), true).setValue(PROPERTY_BY_DIRECTION.get(lookingDirection.getOpposite()), true);
    }

    @Override
    protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess scheduledTickAccess, BlockPos pos, Direction direction, BlockPos neighborPos, BlockState neighborState, RandomSource random) {
        if (state.getValue(WATERLOGGED)) {
            scheduledTickAccess.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
        }

        return updatePipeState(level, state, pos, direction);
    }

    @Override
    protected void neighborChanged(BlockState state, Level level, BlockPos pos, Block neighborBlock, @Nullable Orientation orientation, boolean movedByPiston) {
        level.scheduleTick(pos, this, 1, TickPriority.HIGH);
    }

    @Override
    protected void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) {
        if (state.is(oldState.getBlock())) {
            return;
        }

        level.scheduleTick(pos, this, 1, TickPriority.HIGH);
    }

    @Override
    protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (level.getBlockEntity(pos) instanceof NewPipeBlockEntity blockEntity) {
            blockEntity.updateConnections();
        }
    }

    @Override
    protected void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
        if (!newState.is(state.getBlock())) {
            if (level.getBlockEntity(pos) instanceof NewPipeBlockEntity pipe) {
                pipe.onPipeRemoved();
            }
        }

        super.onRemove(state, level, pos, newState, movedByPiston);
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext context) {
        var level = context.getLevel();
        var blockPos = context.getClickedPos();
        var lookingDirection = context.getNearestLookingDirection();
        var state = super.getStateForPlacement(context);
        var fluidstate = level.getFluidState(blockPos);

        return updatePipeState(level, state, blockPos, lookingDirection).setValue(WATERLOGGED, fluidstate.is(Fluids.WATER));
    }

    @Override
    protected FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new NewPipeBlockEntity(pos, state);
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        if (level.getBlockEntity(pos) instanceof NewPipeBlockEntity newPipeBlockEntity) {

        }
    }

    private static VoxelShape[] makeShapes() {
        float f = 0.5F - APOTHEM;
        float f1 = 0.5F + APOTHEM;
        VoxelShape voxelshape = Block.box(
                f * 16.0F, f * 16.0F, f * 16.0F, f1 * 16.0F, f1 * 16.0F, f1 * 16.0F
        );
        VoxelShape[] avoxelshape = new VoxelShape[DIRECTIONS.length];

        for (int i = 0; i < DIRECTIONS.length; i++) {
            Direction direction = DIRECTIONS[i];
            avoxelshape[i] = Shapes.box(
                    0.5 + Math.min(-APOTHEM, (double) direction.getStepX() * 0.5),
                    0.5 + Math.min(-APOTHEM, (double) direction.getStepY() * 0.5),
                    0.5 + Math.min(-APOTHEM, (double) direction.getStepZ() * 0.5),
                    0.5 + Math.max(APOTHEM, (double) direction.getStepX() * 0.5),
                    0.5 + Math.max(APOTHEM, (double) direction.getStepY() * 0.5),
                    0.5 + Math.max(APOTHEM, (double) direction.getStepZ() * 0.5)
            );
        }

        VoxelShape[] avoxelshape1 = new VoxelShape[64];

        for (int k = 0; k < 64; k++) {
            VoxelShape voxelshape1 = voxelshape;

            for (int j = 0; j < DIRECTIONS.length; j++) {
                if ((k & 1 << j) != 0) {
                    voxelshape1 = Shapes.or(voxelshape1, avoxelshape[j]);
                }
            }

            avoxelshape1[k] = voxelshape1;
        }

        return avoxelshape1;
    }

    private static int getAABBIndex(BlockState state) {
        int i = 0;

        for (int j = 0; j < DIRECTIONS.length; j++) {
            if (state.getValue(PROPERTY_BY_DIRECTION.get(DIRECTIONS[j]))) {
                i |= 1 << j;
            }
        }

        return i;
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return shapeByIndex[getAABBIndex(state)];
    }
}
