package frostygames0.elementalamulets.block.extractor;

import com.mojang.serialization.MapCodec;
import frostygames0.elementalamulets.block.entity.extractor.PrimitiveElementalExtractorBlockEntity;
import frostygames0.elementalamulets.registration.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class PrimitiveElementalExtractorBlock extends AbstractElementalExtractorBlock {
    public static final MapCodec<PrimitiveElementalExtractorBlock> CODEC = simpleCodec(PrimitiveElementalExtractorBlock::new);

    public PrimitiveElementalExtractorBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected MapCodec<? extends AbstractElementalExtractorBlock> codec() {
        return CODEC;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new PrimitiveElementalExtractorBlockEntity(pos, state);
    }

    @Override
    protected void openContainer(Level level, BlockPos pos, Player player) {
        if (level.getBlockEntity(pos) instanceof PrimitiveElementalExtractorBlockEntity blockEntity) {
            player.openMenu(blockEntity);
        }
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        return createExtractorTicker(level, blockEntityType, ModBlockEntities.PRIMITIVE_ELEMENTAL_EXTRACTOR.get());
    }
}