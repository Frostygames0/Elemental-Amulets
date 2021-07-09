package frostygames0.elementalamulets.blocks.containers;

import frostygames0.elementalamulets.core.init.ModBlocks;
import frostygames0.elementalamulets.core.init.ModContainers;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIntArray;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;

public class ElementalCombinatorContainer extends Container {
    private final TileEntity tileEntity;
    private final PlayerEntity playerEntity;
    private final IItemHandler playerInventory;
    private final IIntArray data;

    public ElementalCombinatorContainer(int id, World world, BlockPos pos, PlayerInventory playerInventory, PlayerEntity player, IIntArray data) {
        super(ModContainers.ELEMENTAL_COMBINATOR_CONTAINER.get(), id);
        this.tileEntity = world.getTileEntity(pos);
        this.playerEntity = player;
        this.playerInventory = new InvWrapper(playerInventory);
        this.data = data;
        if (this.tileEntity != null) {
            tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(h -> {
                this.addSlot(new SlotItemHandler(h, 0, 134, 34));

                this.addSlot(new SlotItemHandler(h, 1, 35, 34));
                this.addSlot(new SlotItemHandler(h, 2, 35, 10));
                this.addSlot(new SlotItemHandler(h, 3, 55, 14));
                this.addSlot(new SlotItemHandler(h, 4, 59, 34));
                this.addSlot(new SlotItemHandler(h, 5, 55, 54));
                this.addSlot(new SlotItemHandler(h, 6, 35, 58));
                this.addSlot(new SlotItemHandler(h, 7, 15, 54));
                this.addSlot(new SlotItemHandler(h, 8, 11, 34));
                this.addSlot(new SlotItemHandler(h, 9, 15, 14));
            });
        }
        this.bindPlayerInventory(8,83);
        this.trackIntArray(data);
    }

    public IIntArray getCombinatorData() {
        return this.data;
    }

    @Override
    public ItemStack transferStackInSlot(PlayerEntity playerIn, int index) {
        ItemStack itemStack = ItemStack.EMPTY;
        Slot slot = this.getSlot(index);
        if(slot != null && slot.getHasStack()) {
            ItemStack itemStack1 = slot.getStack();
            itemStack = itemStack1.copy();
            if(index == 0) {
                if(!this.mergeItemStack(itemStack1, 10, 46, true)) {
                    return ItemStack.EMPTY;
                }
                slot.onSlotChange(itemStack1, itemStack);
            } else if(index >= 10 && index < 46) {
                if (!this.mergeItemStack(itemStack1, 1, 10, false)) {
                    if (index < 37) {
                        if (!this.mergeItemStack(itemStack1, 37, 46, false)) {
                            return ItemStack.EMPTY;
                        }
                    } else if (!this.mergeItemStack(itemStack1, 10, 37, false)) {
                        return ItemStack.EMPTY;
                    }
                }
            } else if (!this.mergeItemStack(itemStack1, 10, 46, false)) {
                return ItemStack.EMPTY;
            }

            if(itemStack1.isEmpty()) {
                slot.putStack(ItemStack.EMPTY);
            } else {
                slot.onSlotChanged();
            }
            if(itemStack1.getCount() == itemStack.getCount()) {
                return ItemStack.EMPTY;
            }
            slot.onTake(playerIn, itemStack1);
        }
        return itemStack;
    }

    @Override
    public boolean canInteractWith(PlayerEntity playerIn) {
        return isWithinUsableDistance(IWorldPosCallable.of(tileEntity.getWorld(), tileEntity.getPos()), playerEntity, ModBlocks.ELEMENTAL_COMBINATOR.get());
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

    public int getCombinationTimeScaled() {
        int i = this.getCombinatorData().get(0);
        int j = this.getCombinatorData().get(1);
        return j != 0 && i != 0 ? i * 25 / j : 0;
    }
}