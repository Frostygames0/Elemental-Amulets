package frostygames0.elementalamulets.blocks.containers.slot;

import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;

/*
 * Does not have any functionality right now because Elementals are not implemented yet.
 */
public class ElementalOnlySlot extends SlotItemHandler {

    public ElementalOnlySlot(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
        super(itemHandler, index, xPosition, yPosition);
    }
}
