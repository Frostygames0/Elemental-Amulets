package frostygames0.elementalamulets.inventory.menu.slots.extractor;

import frostygames0.elementalamulets.element.ElementHelper;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.SlotItemHandler;

public class EmptyElementalShardSlot extends SlotItemHandler {
    public EmptyElementalShardSlot(IItemHandler container, int slot, int x, int y) {
        super(container, slot, x, y);
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        return ElementHelper.isStackAnEmptyElementumShard(stack);
    }
}
