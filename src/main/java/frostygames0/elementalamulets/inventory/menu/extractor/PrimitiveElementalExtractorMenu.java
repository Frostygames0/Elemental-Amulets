package frostygames0.elementalamulets.inventory.menu.extractor;

import frostygames0.elementalamulets.block.entity.extractor.AbstractElementalExtractorBlockEntity;
import frostygames0.elementalamulets.block.entity.extractor.PrimitiveElementalExtractorBlockEntity;
import frostygames0.elementalamulets.element.ElementalHelper;
import frostygames0.elementalamulets.element.storage.ElementStorage;
import frostygames0.elementalamulets.element.storage.IElementStorage;
import frostygames0.elementalamulets.initialization.ModBlocks;
import frostygames0.elementalamulets.initialization.ModMenuTypes;
import frostygames0.elementalamulets.inventory.menu.slots.OutputSlot;
import frostygames0.elementalamulets.inventory.menu.slots.extractor.EmptyElementalShardSlot;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;

public class PrimitiveElementalExtractorMenu extends AbstractElementalExtractorMenu {
    public static final int RESULT_SLOTS_MAX_SIZE = PrimitiveElementalExtractorBlockEntity.RESULTS_SLOT_MAX_SIZE;
    public static final int ADDITIONAL_INVENTORY_SIZE = PrimitiveElementalExtractorBlockEntity.ADDITIONAL_INVENTORY_SIZE;
    public static final int ELEMENT_STORAGE_CAPACITY = PrimitiveElementalExtractorBlockEntity.ELEMENT_STORAGE_CAPACITY;
    public static final int MAX_DISTINCT_ELEMENTS_STORED = PrimitiveElementalExtractorBlockEntity.MAX_DISTINCT_ELEMENTS_STORED;
    public static final int ADDITIONAL_CONTAINER_DATA_SIZE = PrimitiveElementalExtractorBlockEntity.ADDITIONAL_CONTAINER_DATA_SIZE;

    public static final int SHARD_SLOT = 2;

    private final ContainerData additionalContainerData;

    protected PrimitiveElementalExtractorMenu(int containerId, Inventory playerInventory,
                                              IItemHandler baseInventory, IItemHandler additionalInventory,
                                              ContainerData baseContainerData, ContainerData additionalContainerData,
                                              IElementStorage storage, ContainerLevelAccess access) {
        super(ModMenuTypes.PRIMITIVE_ELEMENTAL_EXTRACTOR.get(), containerId, playerInventory,
                baseInventory, storage, access, baseContainerData, createAdditionalSlots(additionalInventory));

        this.additionalContainerData = additionalContainerData;

        addDataSlots(this.additionalContainerData);
    }

    public static PrimitiveElementalExtractorMenu forServer(int containerId, Inventory playerInventory,
                                                            IItemHandler baseInventory, IItemHandler additionalInventory,
                                                            ContainerData baseContainerData, ContainerData additionalContainerData,
                                                            IElementStorage storage, ContainerLevelAccess access) {
        return new PrimitiveElementalExtractorMenu(containerId, playerInventory, baseInventory, additionalInventory, baseContainerData, additionalContainerData, storage, access);
    }

    public static PrimitiveElementalExtractorMenu forClient(int containerId, Inventory playerInventory) {
        return new PrimitiveElementalExtractorMenu(containerId, playerInventory,
                new ItemStackHandler(BASE_INVENTORY_SIZE),
                new ItemStackHandler(ADDITIONAL_INVENTORY_SIZE),
                new SimpleContainerData(BASE_CONTAINER_DATA_SIZE),
                new SimpleContainerData(ADDITIONAL_CONTAINER_DATA_SIZE),
                new ElementStorage(ELEMENT_STORAGE_CAPACITY, MAX_DISTINCT_ELEMENTS_STORED),
                ContainerLevelAccess.NULL);
    }

    private static ElementalExtractorSlotDefinitions createAdditionalSlots(IItemHandler additionalInventory) {
        var builder = ElementalExtractorSlotDefinitions.builder();

        builder.addInputSlot(AbstractElementalExtractorBlockEntity.INPUT_SLOT, 44, 11)
                .addFuelSlot(AbstractElementalExtractorBlockEntity.FUEL_SLOT, 44, 47);

        builder.addAdditionalSlot(PrimitiveElementalExtractorBlockEntity.EMPTY_SHARD_SLOT, 80, 29,
                (slot, x, y) -> new EmptyElementalShardSlot(additionalInventory, slot, x, y));

        int x = 116;
        int y = 11;
        for (int i = 0; i < PrimitiveElementalExtractorBlockEntity.RESULTS_SLOTS_END; i++) {
            builder.addAdditionalSlot(PrimitiveElementalExtractorBlockEntity.RESULTS_SLOTS_START + i, x, y + (i * 18),
                    ((slot, x1, y1) -> new OutputSlot(additionalInventory, RESULT_SLOTS_MAX_SIZE, slot, x1, y1)));
        }

        return builder.build();
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = slots.get(index);

        if (slot != null && slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();
            int inventoryStart = getInventorySlotStart();
            int inventoryEnd = getUseRowEnd();

            var isInsideExtractorSlot = index >= INPUT_SLOT && index <= getLastSlotIndex();

            if (isInsideExtractorSlot) {
                if (!moveItemStackTo(itemstack1, inventoryStart, inventoryEnd, true)) {
                    return ItemStack.EMPTY;
                }
            } else {
                if (ElementalHelper.isStackAnEmptyElementumShard(itemstack1)) {
                    if (!moveItemStackTo(itemstack1, SHARD_SLOT, SHARD_SLOT + 1, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (AbstractElementalExtractorBlockEntity.isFuel(itemstack1, level)) {
                    if (!moveItemStackTo(itemstack1, FUEL_SLOT, FUEL_SLOT + 1, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (ElementalHelper.hasElementalComposition(itemstack1)) {
                    if (!moveItemStackTo(itemstack1, INPUT_SLOT, INPUT_SLOT + 1, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (index >= getInventorySlotStart() && index < getInventorySlotEnd()) {
                    if (!moveItemStackTo(itemstack1, getUseRowStart(), getUseRowEnd(), false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (index >= getUseRowStart() && index < getUseRowEnd()) {
                    if (!moveItemStackTo(itemstack1, getInventorySlotStart(), getInventorySlotEnd(), false)) {
                        return ItemStack.EMPTY;
                    }
                }
            }

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

    @Override
    public boolean stillValid(Player player) {
        return AbstractContainerMenu.stillValid(access, player, ModBlocks.PRIMITIVE_ELEMENTAL_EXTRACTOR.get());
    }

    public float getConversionProgress() {
        int i = additionalContainerData.get(0);
        int j = additionalContainerData.get(1);
        return j != 0 && i != 0 ? Mth.clamp((float) i / (float) j, 0.0F, 1.0F) : 0.0F;
    }
}
