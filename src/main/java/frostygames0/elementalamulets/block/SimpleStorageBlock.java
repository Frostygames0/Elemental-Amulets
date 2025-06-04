package frostygames0.elementalamulets.block;

import com.mojang.serialization.MapCodec;
import frostygames0.elementalamulets.block.entity.SimpleStorageBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
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
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (!level.isClientSide && level.getBlockEntity(pos) instanceof SimpleStorageBlockEntity simpleStorageBlockEntity) {
            player.openMenu(simpleStorageBlockEntity);
        }

        return InteractionResult.CONSUME;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new SimpleStorageBlockEntity(pos, state);
    }
}
