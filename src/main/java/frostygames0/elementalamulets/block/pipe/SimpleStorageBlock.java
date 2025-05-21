package frostygames0.elementalamulets.block.pipe;

import com.mojang.serialization.MapCodec;
import frostygames0.elementalamulets.block.entity.SimpleStorageBlockEntity;
import frostygames0.elementalamulets.initialization.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class SimpleStorageBlock extends BaseEntityBlock {
    public static final MapCodec<SimpleStorageBlock> CODEC = simpleCodec(SimpleStorageBlock::new);

    public SimpleStorageBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new SimpleStorageBlockEntity(pos, state);
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        return createTickerHelper(blockEntityType, ModBlockEntities.SIMPLE_STORAGE.get(), SimpleStorageBlockEntity.TICKER);
    }
}
