package frostygames0.elementalamulets.block.pipe;

import com.mojang.serialization.MapCodec;
import frostygames0.elementalamulets.block.entity.pipe.PressurizerPipeBlockEntity;
import frostygames0.elementalamulets.initialization.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.Property;
import org.jetbrains.annotations.Nullable;

public class PipePressurizerBlock extends SpecialPipeBlock {
    public static final MapCodec<PipePressurizerBlock> CODEC = simpleCodec(PipePressurizerBlock::new);
    public static final Property<Boolean> ENABLED = BlockStateProperties.ENABLED;

    public PipePressurizerBlock(Properties properties) {
        super(properties);
    }

    public static boolean isPressurizerPipe(BlockState state)
    {
        return state.getBlock() instanceof PipePressurizerBlock;
    }

    @Override
    public MapCodec<? extends PipePressurizerBlock> codec() {
        return CODEC;
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
