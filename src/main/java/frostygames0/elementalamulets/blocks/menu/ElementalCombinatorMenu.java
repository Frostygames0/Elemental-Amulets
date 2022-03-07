/*
 *  Copyright (c) 2021-2022
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

import frostygames0.elementalamulets.capability.RestrictiveItemHandler;
import frostygames0.elementalamulets.init.ModBlocks;
import frostygames0.elementalamulets.init.ModMenus;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;


import static frostygames0.elementalamulets.blocks.entities.ElementalCombinatorBlockEntity.DATA_SIZE;
import static frostygames0.elementalamulets.blocks.entities.ElementalCombinatorBlockEntity.HANDLER_SIZE;

public class ElementalCombinatorMenu extends AbstractContainerMenu {
    private static final int INV_SLOT_START = 10;
    private static final int INV_SLOT_END = 37;
    private static final int USE_ROW_START = 37;
    private static final int USE_ROW_END = 46;
    private static final int RESULT_SLOT = 0;

    private final Level level;
    private final BlockPos pos;

    private final Player player;
    private final IItemHandler playerInventory;

    private final ContainerData data;

    // Client constructor
    public ElementalCombinatorMenu(int id, BlockPos pos, Inventory playerInventory) {
        this(id, pos, playerInventory, new RestrictiveItemHandler(HANDLER_SIZE), new SimpleContainerData(DATA_SIZE));
    }

    // Server constructor
    public ElementalCombinatorMenu(int id, BlockPos pos, Inventory playerInventory, IItemHandler handler, ContainerData data) {
        super(ModMenus.ELEMENTAL_COMBINATOR_MENU.get(), id);

        this.playerInventory = new InvWrapper(playerInventory);
        this.player = playerInventory.player;
        this.level = player.level;
        this.pos = pos;

        // Safety checks to prevent crashes
        if (handler.getSlots() < HANDLER_SIZE)
            throw new IllegalArgumentException("Elemental Combinator's container size is too small, expected 10!");

        if (data.getCount() < DATA_SIZE)
            throw new IllegalArgumentException("Elemental Combinator's container data size is too small, expected 2!");

        this.data = data;

        this.addSlot(new SlotItemHandler(handler, RESULT_SLOT, 134, 34));

        this.addSlot(new SlotItemHandler(handler, 1, 35, 34));
        this.addSlot(new SlotItemHandler(handler, 2, 35, 10));
        this.addSlot(new SlotItemHandler(handler, 3, 55, 14));
        this.addSlot(new SlotItemHandler(handler, 4, 59, 34));
        this.addSlot(new SlotItemHandler(handler, 5, 55, 54));
        this.addSlot(new SlotItemHandler(handler, 6, 35, 58));
        this.addSlot(new SlotItemHandler(handler, 7, 15, 54));
        this.addSlot(new SlotItemHandler(handler, 8, 11, 34));
        this.addSlot(new SlotItemHandler(handler, 9, 15, 14));

        this.bindPlayerInventory(8, 83);
        this.addDataSlots(data);
    }

    public ContainerData getCombinatorData() {
        return this.data;
    }

    @Override
    public ItemStack quickMoveStack(Player playerIn, int index) {
        ItemStack itemStack = ItemStack.EMPTY;
        Slot slot = this.getSlot(index);
        if (slot.hasItem()) {
            ItemStack itemStack1 = slot.getItem();
            itemStack = itemStack1.copy();
            if (index == RESULT_SLOT) {
                if (!this.moveItemStackTo(itemStack1, INV_SLOT_START, USE_ROW_END, true)) {
                    return ItemStack.EMPTY;
                }
                slot.onQuickCraft(itemStack1, itemStack);
            } else if (index >= INV_SLOT_START && index < USE_ROW_END) {
                if (!this.moveItemStackTo(itemStack1, 1, INV_SLOT_START, false)) {
                    if (index < INV_SLOT_END) {
                        if (!this.moveItemStackTo(itemStack1, USE_ROW_START, USE_ROW_END, false)) {
                            return ItemStack.EMPTY;
                        }
                    } else if (!this.moveItemStackTo(itemStack1, INV_SLOT_START, INV_SLOT_END, false)) {
                        return ItemStack.EMPTY;
                    }
                }
            } else if (!this.moveItemStackTo(itemStack1, INV_SLOT_START, USE_ROW_END, false)) {
                return ItemStack.EMPTY;
            }

            if (itemStack1.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
            if (itemStack1.getCount() == itemStack.getCount()) {
                return ItemStack.EMPTY;
            }
            slot.onTake(playerIn, itemStack1);
        }
        return itemStack;
    }

    @Override
    public boolean stillValid(Player playerIn) {
        return stillValid(ContainerLevelAccess.create(this.level, this.pos), player, ModBlocks.ELEMENTAL_COMBINATOR.get());
    }

    private int addSlotRange(IItemHandler handler, int index, int x, int y, int amount, int dx) {
        for (int i = 0; i < amount; i++) {
            this.addSlot(new SlotItemHandler(handler, index, x, y));
            x += dx;
            index++;
        }
        return index;
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

    public int getCombinationTimeScaled() {
        int i = this.getCombinatorData().get(0);
        int j = this.getCombinatorData().get(1);
        return j != 0 && i != 0 ? i * 25 / j : 0;
    }

    public BlockPos getPos() {
        return this.pos;
    }
}