package frostygames0.elementalamulets.block;

import frostygames0.elementalamulets.initialization.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class SimpleGeneratorBlockEntity extends BlockEntity {
    public SimpleGeneratorBlockEntity(BlockPos pos, BlockState blockState) {
        super(ModBlockEntities.SIMPLE_GENERATOR.get(), pos, blockState);
    }

    public void serverTick() {

    }
}
