package frostygames0.elementalamulets.capability;

import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandlerModifiable;


import javax.annotation.Nonnull;

public class AutomationItemHandler implements IItemHandlerModifiable {
    private final IItemHandlerModifiable handler;
    public AutomationItemHandler(IItemHandlerModifiable handler) {
        this.handler = handler;
    }
    @Override
    public void setStackInSlot(int slot, @Nonnull ItemStack stack) {
        handler.setStackInSlot(slot, stack);
    }

    @Override
    public int getSlots() {
        return handler.getSlots();
    }

    @Nonnull
    @Override
    public ItemStack getStackInSlot(int slot) {
        return handler.getStackInSlot(slot);
    }

    @Nonnull
    @Override
    public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
        return slot == 0 ? stack : handler.insertItem(slot, stack, simulate);
    }

    @Nonnull
    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        return handler.extractItem(slot, amount, simulate);
    }

    @Override
    public int getSlotLimit(int slot) {
        return handler.getSlotLimit(slot);
    }

    @Override
    public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
        return slot != 0;
    }


}
