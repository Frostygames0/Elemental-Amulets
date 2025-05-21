package frostygames0.elementalamulets.block.entity.pipe;

import frostygames0.elementalamulets.block.pipe.ElementalPipeBlock;
import frostygames0.elementalamulets.initialization.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;

public class ElementalPipeBlockEntity extends BaseElementalPipeBlockEntity {
    public ElementalPipeBlockEntity(BlockPos pos, BlockState blockState) {
        super(ModBlockEntities.ELEMENTAL_PIPE.get(), pos, blockState);
    }

    @Override
    public boolean canHaveFlowToward(Direction side) {
        return ElementalPipeBlock.isPipe(getBlockState()) && ElementalPipeBlock.isOpen(getBlockState(), side);
    }
}
