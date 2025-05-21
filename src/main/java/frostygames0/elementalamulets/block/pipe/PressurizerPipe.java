package frostygames0.elementalamulets.block.pipe;

import com.mojang.serialization.MapCodec;
import frostygames0.elementalamulets.block.entity.pipe.PressurizerPipeBlockEntity;
import frostygames0.elementalamulets.initialization.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

public class PressurizerPipe extends AxisPipeBlock {
    public static final MapCodec<PressurizerPipe> CODEC = simpleCodec(PressurizerPipe::new);
    public static final Property<Boolean> ENABLED = BlockStateProperties.ENABLED;

    public PressurizerPipe(Properties properties) {
        super(properties);
        registerDefaultState(getStateDefinition().any().setValue(ENABLED, true));
    }

    public static boolean isPressurizerPipe(BlockState state) {
        return state.getBlock() instanceof PressurizerPipe;
    }

    @Override
    public MapCodec<? extends PressurizerPipe> codec() {
        return CODEC;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(ENABLED);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (player.isShiftKeyDown()) {
            level.setBlock(pos, state.setValue(ENABLED, !state.getValue(ENABLED)), Block.UPDATE_ALL);
            return InteractionResult.SUCCESS;
        }

        return super.useWithoutItem(state, level, pos, player, hitResult);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new PressurizerPipeBlockEntity(pos, state);
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        if (blockEntityType != ModBlockEntities.PRESSURIZER_PIPE.get()) {
            return null;
        }

        return (level1, pos, state1, blockEntity) -> ((PressurizerPipeBlockEntity) blockEntity).tick();
    }
}
