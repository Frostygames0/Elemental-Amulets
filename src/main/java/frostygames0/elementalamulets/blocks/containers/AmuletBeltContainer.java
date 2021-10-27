/*
 *     Copyright (c) 2021
 *
 *     This file is part of Elemental Amulets, a Minecraft Mod.
 *
 *     Elemental Amulets is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Elemental Amulets is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with Elemental Amulets.  If not, see <https://www.gnu.org/licenses/>.
 */

package frostygames0.elementalamulets.blocks.containers;

import frostygames0.elementalamulets.init.ModContainers;
import frostygames0.elementalamulets.items.AmuletBeltItem;
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
        belt.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(h -> layoutBelt(h, 0, 44, 54, 5, 18));
        this.bindPlayerInventory(8, 83);
    }

    @Override
    public ItemStack quickMoveStack(PlayerEntity pPlayer, int index) {
        ItemStack itemStack = ItemStack.EMPTY;
        /*Slot slot = this.getSlot(index);
        if(slot != null && slot.hasItem()) {
            ItemStack itemStack1 = slot.getItem();
            itemStack = itemStack1.copy();

            if(index >= 4 && index < 41) {
                if (!this.moveItemStackTo(itemStack1, 0, 4, false)) {
                    if (index < 32) {
                        if (!this.moveItemStackTo(itemStack1, 32, 41, false)) {
                            return ItemStack.EMPTY;
                        }
                    } else if (!this.moveItemStackTo(itemStack1, 5, 32, false)) {
                        return ItemStack.EMPTY;
                    }
                }
            } else if (!this.moveItemStackTo(itemStack1, 5, 41, false)) {
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
            slot.onTake(pPlayer, itemStack1);
        }*/
        return itemStack;
    }

    @Override
    public boolean stillValid(PlayerEntity playerIn) {
        return !belt.isEmpty() && belt.getItem() instanceof AmuletBeltItem;
    }

    private int layoutBelt(IItemHandler handler, int index, int x, int y, int amount, int dx) {
        for (int i = 0 ; i < amount ; i++) {
            this.addSlot(new SlotItemHandler(handler, index, x, y) {
                @Override
                public ItemStack onTake(PlayerEntity pPlayer, ItemStack amulet) {
                    /*Item itemAmulet = amulet.getItem();
                    LazyOptional<ICurio> curio = CuriosApi.getCuriosHelper().getCurio(amulet);
                    if(curio.isPresent() && itemAmulet instanceof AmuletItem) {
                        if(((AmuletItem) itemAmulet).usesCurioMethods()) {
                            curio.orElseThrow(NullPointerException::new).onUnequip(new SlotContext("necklace", pPlayer), ItemStack.EMPTY);
                        }
                    }*/
                    return super.onTake(pPlayer, amulet);
                }
            });
            x += dx;
            index++;
        }
        return index;
    }

    private int addSlotRange(IItemHandler handler, int index1, int x, int y, int amount, int dx) {
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
