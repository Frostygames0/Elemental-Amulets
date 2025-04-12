package frostygames0.elementalamulets.inventory.menu.extractor;

import frostygames0.elementalamulets.block.entity.extractor.AbstractElementalExtractorBlockEntity;
import frostygames0.elementalamulets.element.storage.IElementStorage;
import frostygames0.elementalamulets.inventory.menu.SyncedElementalStorageMenu;
import frostygames0.elementalamulets.inventory.menu.slots.ElementalExtractorFuelSlot;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.SlotItemHandler;

public abstract class AbstractElementalExtractorMenu extends SyncedElementalStorageMenu {
    public static final int INPUT_SLOT = AbstractElementalExtractorBlockEntity.INPUT_SLOT;
    public static final int FUEL_SLOT = AbstractElementalExtractorBlockEntity.FUEL_SLOT;

    public static final int BASE_INVENTORY_SIZE = AbstractElementalExtractorBlockEntity.BASE_INVENTORY_SIZE;

    protected final IItemHandler baseInventory;

    protected final ContainerLevelAccess access;
    protected final Level level;

    private final int firstAdditionalSlotIndex;
    private final int lastSlotIndex;
    private final ElementalExtractorSlotDefinitions.AdditionalQuickMoveHandler additionalQuickMove;

    protected AbstractElementalExtractorMenu(MenuType<?> menuType, int containerId,
                                             Inventory playerInventory, IItemHandler baseInventory,
                                             IElementStorage storage, ContainerLevelAccess access,
                                             ElementalExtractorSlotDefinitions slotDefinitions) {
        super(menuType, containerId, playerInventory.player, storage);

        this.baseInventory = baseInventory;
        this.access = access;
        this.level = this.player.level();

        this.addSlot(new SlotItemHandler(this.baseInventory, INPUT_SLOT, 44, 11));
        this.addSlot(new ElementalExtractorFuelSlot(this.baseInventory, FUEL_SLOT, 44, 47, this.level));

        this.addInputSlot(slotDefinitions);
        this.addFuelSlot(slotDefinitions);
        this.addAdditionalSlots(slotDefinitions);

        this.firstAdditionalSlotIndex = slotDefinitions.getFirstAdditionalSlotIndex();
        this.lastSlotIndex = slotDefinitions.getLastSlotIndex();
        this.additionalQuickMove = slotDefinitions.getAdditionalQuickMoveHandler();

        this.addStandardInventorySlots(playerInventory, 8, 84);
    }

    private void addInputSlot(ElementalExtractorSlotDefinitions slotDefinitions) {
        var slotDefinition = slotDefinitions.getInputSlot();
        this.addSlot(new ElementalExtractorFuelSlot(this.baseInventory, slotDefinition.slot(), slotDefinition.x(), slotDefinition.y(), this.level));
    }

    private void addFuelSlot(ElementalExtractorSlotDefinitions slotDefinitions) {
        var slotDefinition = slotDefinitions.getFuelSlot();
        this.addSlot(new ElementalExtractorFuelSlot(this.baseInventory, slotDefinition.slot(), slotDefinition.x(), slotDefinition.y(), this.level));
    }

    private void addAdditionalSlots(ElementalExtractorSlotDefinitions slotDefinitions) {
        for (var additionalSlot : slotDefinitions.getAdditionalSlots()) {
            if (additionalSlot.factory().isEmpty()) {
                throw new IllegalArgumentException("Factory may not be empty for an additional slot!");
            }

            var factory = additionalSlot.factory().get();

            var slot = factory.create(additionalSlot.slot(), additionalSlot.x(), additionalSlot.y());
            this.addSlot(slot);
        }
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);

        if (slot != null && slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();
            int inventoryStart = this.getInventorySlotStart();
            int inventoryEnd = this.getUseRowEnd();

            if (index == INPUT_SLOT || index == FUEL_SLOT) {
                if (!this.moveItemStackTo(itemstack1, inventoryStart, inventoryEnd, true)) {
                    return ItemStack.EMPTY;
                }
            } else {
                if (index >= this.getFirstAdditionalSlotIndex() && index <= this.getLastSlotIndex()) {
                    //return this.additionalQuickMove.apply(player, index);
                } else if (index >= this.getInventorySlotStart() && index < this.getInventorySlotEnd()) {
                    if (!this.moveItemStackTo(itemstack1, this.getUseRowStart(), this.getUseRowEnd(), false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (index >= this.getUseRowStart() && index < this.getUseRowEnd()) {
                    if (!this.moveItemStackTo(itemstack1, this.getInventorySlotStart(), this.getInventorySlotEnd(), false)) {
                        return ItemStack.EMPTY;
                    }
                }
            }


//            if (index == this.getResultSlot()) {
//                if (!this.moveItemStackTo(itemstack1, i, j, true)) {
//                    return ItemStack.EMPTY;
//                }
//
//                slot.onQuickCraft(itemstack1, itemstack);
//            } else if (index >= 0 && index < this.getResultSlot()) {
//                if (!this.moveItemStackTo(itemstack1, i, j, false)) {
//                    return ItemStack.EMPTY;
//                }
//            } else if (this.canMoveIntoInputSlots(itemstack1) && index >= this.getInventorySlotStart() && index < this.getUseRowEnd()) {
//                if (!this.moveItemStackTo(itemstack1, 0, this.getResultSlot(), false)) {
//                    return ItemStack.EMPTY;
//                }
//            } else if (index >= this.getInventorySlotStart() && index < this.getInventorySlotEnd()) {
//                if (!this.moveItemStackTo(itemstack1, this.getUseRowStart(), this.getUseRowEnd(), false)) {
//                    return ItemStack.EMPTY;
//                }
//            } else if (index >= this.getUseRowStart() && index < this.getUseRowEnd() && !this.moveItemStackTo(itemstack1, this.getInventorySlotStart(), this.getInventorySlotEnd(), false)) {
//                return ItemStack.EMPTY;
//            }

            if (itemstack1.isEmpty()) {
                slot.setByPlayer(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }

            if (itemstack1.getCount() == itemstack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(player, itemstack1);
        }

        return itemstack;
    }

    public int getFirstAdditionalSlotIndex() {
        return this.firstAdditionalSlotIndex;
    }

    public int getLastSlotIndex() {
        return this.lastSlotIndex;
    }

    private int getInventorySlotStart() {
        return this.getLastSlotIndex() + 1;
    }

    private int getInventorySlotEnd() {
        return this.getInventorySlotStart() + 27;
    }

    private int getUseRowStart() {
        return this.getInventorySlotEnd();
    }

    private int getUseRowEnd() {
        return this.getUseRowStart() + 9;
    }
}
