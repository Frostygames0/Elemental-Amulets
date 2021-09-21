package frostygames0.elementalamulets.blocks.containers;

import frostygames0.elementalamulets.core.init.ModContainers;
import frostygames0.elementalamulets.items.AmuletBelt;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;

/**
 * @author Frostygames0
 * @date 10.09.2021 23:54
 */
public class AmuletBeltContainer extends Container {
    private final ItemStack belt;
    private final IItemHandler playerInventory;

    public AmuletBeltContainer(int id, PlayerInventory playerInventory, ItemStack belt) {
        super(ModContainers.AMULET_BELT_CONTAINER.get(), id);
        this.belt = belt;
        this.playerInventory = new InvWrapper(playerInventory);
        belt.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(h -> addSlotRange(h, 0, 44, 54, 5, 18));
        this.bindPlayerInventory(8, 83);
    }

    @Override
    public boolean stillValid(PlayerEntity playerIn) {
        return !belt.isEmpty() && belt.getItem() instanceof AmuletBelt;
    }

    private int addSlotRange(IItemHandler handler, int index1, int x, int y, int amount, int dx) {
        int index = index1;
        for (int i = 0 ; i < amount ; i++) {
            this.addSlot(new SlotItemHandler(handler, index1, x, y));
            x += dx;
            index1++;
        }
        return index1;
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
