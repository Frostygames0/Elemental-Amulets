package frostygames0.elementalamulets.block.entity;

import frostygames0.elementalamulets.initialization.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class TestBlockEntity extends BlockEntity {
    public TestBlockEntity(BlockPos pos, BlockState blockState) {
        super(ModBlockEntities.TEST_BLOCK.get(), pos, blockState);
    }
}
