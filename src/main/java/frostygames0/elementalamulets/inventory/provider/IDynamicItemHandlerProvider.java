package frostygames0.elementalamulets.inventory.provider;

import net.minecraft.world.level.block.state.BlockState;

public interface IDynamicItemHandlerProvider extends IItemHandlerProvider {
    void updateSides(BlockState newState);
}
