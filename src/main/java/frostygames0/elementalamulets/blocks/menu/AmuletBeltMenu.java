/*
 *  Copyright (c) 2021
 *
 *     This file is part of Elemental Amulets, a Minecraft Mod.
 *
 *     Elemental Amulets is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Lesser General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Elemental Amulets is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Lesser General Public License for more details.
 *
 *     You should have received a copy of the GNU Lesser General Public License
 *     along with Elemental Amulets.  If not, see <https://www.gnu.org/licenses/>.
 */

package frostygames0.elementalamulets.blocks.menu;

import frostygames0.elementalamulets.init.ModMenus;
import frostygames0.elementalamulets.items.AmuletBeltItem;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;
import top.theillusivec4.curios.api.CuriosApi;


import static frostygames0.elementalamulets.items.AmuletBeltItem.HANDLER_SIZE;

/**
 * @author Frostygames0
 * @date 10.09.2021 23:54
 */
public class AmuletBeltMenu extends AbstractContainerMenu {
    private final ItemStack belt;
    private final IItemHandler playerInventory;

    public AmuletBeltMenu(int id, Inventory playerInventory, ItemStack belt) {
        super(ModMenus.AMULET_BELT_MENU.get(), id);

        this.playerInventory = new InvWrapper(playerInventory);
        this.belt = belt;
        belt.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(handler -> {
            if (handler.getSlots() < HANDLER_SIZE)
                throw new IllegalArgumentException("Elemental Combinator's container size is too small, expected 10!");

            addSlotRange(handler, 0, 44, 54, HANDLER_SIZE, 18);
        });

        this.bindPlayerInventory(8, 83);
    }

    // TODO Remake it a little
    @Override
    public ItemStack quickMoveStack(Player pPlayer, int index) {
        ItemStack itemStack = ItemStack.EMPTY;
        Slot slot = this.getSlot(index);
        if (slot.hasItem()) {
            ItemStack itemStack1 = slot.getItem();
            itemStack = itemStack1.copy();
            if (index >= 0 && index < 5) {
                if (!this.moveItemStackTo(itemStack1, 5, 41, true)) {
                    return ItemStack.EMPTY;
                }
            } else {
                if (!this.moveItemStackTo(itemStack1, 0, 5, false)) {
                    return ItemStack.EMPTY;
                }
            }

            if (itemStack1.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }

            if (itemStack1.getCount() == itemStack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(pPlayer, itemStack1);
        }
        return itemStack;
    }

    @Override
    public boolean stillValid(Player playerIn) {
        return CuriosApi.getCuriosHelper().findFirstCurio(playerIn, stack -> !AmuletBeltItem.notSame(stack, belt)).isPresent();
    }

    private int addSlotRange(IItemHandler handler, int index1, int x, int y, int amount, int dx) {
        for (int i = 0; i < amount; i++) {
            this.addSlot(new SlotItemHandler(handler, index1, x, y));
            x += dx;
            index1++;
        }
        return index1;
    }

    private void addSlotBox(IItemHandler handler, int index, int x, int y, int horAmount, int dx, int verAmount, int dy) {
        for (int j = 0; j < verAmount; j++) {
            index = addSlotRange(handler, index, x, y, horAmount, dx);
            y += dy;
        }
    }


    private void bindPlayerInventory(int x, int y) {
        this.addSlotBox(playerInventory, 9, x, y, 9, 18, 3, 18);
        y += 58;
        this.addSlotRange(playerInventory, 0, x, y, 9, 18);
    }
}
