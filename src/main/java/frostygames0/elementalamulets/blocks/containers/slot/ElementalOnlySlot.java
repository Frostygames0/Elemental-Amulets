package frostygames0.elementalamulets.blocks.containers.slot;

import net.minecraft.inventory.container.CraftingResultSlot;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class ElementalOnlySlot extends SlotItemHandler {

    public ElementalOnlySlot(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
        super(itemHandler, index, xPosition, yPosition);
    }
}
