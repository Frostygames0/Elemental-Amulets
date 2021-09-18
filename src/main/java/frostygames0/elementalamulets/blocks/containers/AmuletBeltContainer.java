package frostygames0.elementalamulets.blocks.containers;

import frostygames0.elementalamulets.core.init.ModContainers;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;

/**
 * @author Frostygames0
 * @date 10.09.2021 23:54
 */
public class AmuletBeltContainer extends Container {
    private final ItemStack stack;
    private final PlayerEntity playerEntity;
    private final IItemHandler playerInventory;

    public AmuletBeltContainer(int id, World world, ItemStack stack, PlayerInventory playerInventory, PlayerEntity player) {
        super(ModContainers.AMULET_BELT_CONTAINER.get(), id);
        this.stack = stack;
        this.playerEntity = player;
        this.playerInventory = new InvWrapper(playerInventory);
        stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(h -> {
            this.addSlot(new SlotItemHandler(h, 0, 55, 14));
            this.addSlot(new SlotItemHandler(h, 1, 55, 54));
            this.addSlot(new SlotItemHandler(h, 2, 35, 58));
            this.addSlot(new SlotItemHandler(h, 3, 15, 54));
            this.addSlot(new SlotItemHandler(h, 4, 15, 14));
        });
        this.bindPlayerInventory(8, 83);
    }

    @Override
    public ItemStack quickMoveStack(PlayerEntity playerIn, int index) {
        ItemStack itemStack = ItemStack.EMPTY;
        Slot slot = this.getSlot(index);
        if(slot != null && slot.hasItem()) {
            ItemStack itemStack1 = slot.getItem();
            itemStack = itemStack1.copy();
            if(index == 0) {
                if(!this.moveItemStackTo(itemStack1, 10, 46, true)) {
                    return ItemStack.EMPTY;
                }
                slot.onQuickCraft(itemStack1, itemStack);
            } else if(index >= 10 && index < 46) {
                if (!this.moveItemStackTo(itemStack1, 1, 10, false)) {
                    if (index < 37) {
                        if (!this.moveItemStackTo(itemStack1, 37, 46, false)) {
                            return ItemStack.EMPTY;
                        }
                    } else if (!this.moveItemStackTo(itemStack1, 10, 37, false)) {
                        return ItemStack.EMPTY;
                    }
                }
            } else if (!this.moveItemStackTo(itemStack1, 10, 46, false)) {
                return ItemStack.EMPTY;
            }

            if(itemStack1.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
            if(itemStack1.getCount() == itemStack.getCount()) {
                return ItemStack.EMPTY;
            }
            slot.onTake(playerIn, itemStack1);
        }
        return itemStack;
    }

    @Override
    public boolean stillValid(PlayerEntity playerIn) {
        return true;
    }

    private int addSlotRange(IItemHandler handler, int index, int x, int y, int amount, int dx) {
        for (int i = 0 ; i < amount ; i++) {
            this.addSlot(new SlotItemHandler(handler, index, x, y));
            x += dx;
            index++;
        }
        return index;
    }

    private int addSlotBox(IItemHandler handler, int index, int x, int y, int horAmount, int dx, int verAmount, int dy) {
        for (int j = 0 ; j < verAmount ; j++) {
            index = addSlotRange(handler, index, x, y, horAmount, dx);
            y += dy;
        }
        return index;
    }


    private void bindPlayerInventory(int x, int y) {
        this.addSlotBox(playerInventory, 9, x, y, 9, 18, 3, 18);
        y += 58;
        this.addSlotRange(playerInventory, 0, x, y, 9, 18);
    }
}