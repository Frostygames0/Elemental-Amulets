package frostygames0.elementalamulets.inventory.menu.slots;

import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.SlotItemHandler;

public class OutputSlot extends SlotItemHandler {
    private final int maxStackSize;

    public OutputSlot(IItemHandler container, int maxStackSize, int slot, int x, int y) {
        super(container, slot, x, y);
        this.maxStackSize = maxStackSize;
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        return false;
    }

    @Override
    public int getMaxStackSize() {
        return maxStackSize;
    }
}
