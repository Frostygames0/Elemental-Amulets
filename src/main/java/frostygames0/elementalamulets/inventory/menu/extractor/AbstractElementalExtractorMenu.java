package frostygames0.elementalamulets.inventory.menu.extractor;

import frostygames0.elementalamulets.block.entity.extractor.AbstractElementalExtractorBlockEntity;
import frostygames0.elementalamulets.element.storage.IElementStorage;
import frostygames0.elementalamulets.inventory.menu.SyncedElementalStorageMenu;
import frostygames0.elementalamulets.inventory.menu.slots.ElementalExtractorFuelSlot;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.SlotItemHandler;

public abstract class AbstractElementalExtractorMenu extends SyncedElementalStorageMenu {
    public static final int INPUT_SLOT = AbstractElementalExtractorBlockEntity.INPUT_SLOT;
    public static final int FUEL_SLOT = AbstractElementalExtractorBlockEntity.FUEL_SLOT;

    public static final int BASE_INVENTORY_SIZE = AbstractElementalExtractorBlockEntity.BASE_INVENTORY_SIZE;
    public static final int BASE_CONTAINER_DATA_SIZE = AbstractElementalExtractorBlockEntity.BASE_CONTAINER_DATA_SIZE;

    protected final IItemHandler baseInventory;

    protected final ContainerLevelAccess access;
    protected final Level level;

    protected final ContainerData baseContainerData;

    private final int lastSlotIndex;

    protected AbstractElementalExtractorMenu(MenuType<?> menuType, int containerId,
                                             Inventory playerInventory, IItemHandler baseInventory,
                                             IElementStorage storage, ContainerLevelAccess access,
                                             ContainerData baseContainerData,
                                             ElementalExtractorSlotDefinitions slotDefinitions) {
        super(menuType, containerId, playerInventory.player, storage);

        this.baseInventory = baseInventory;
        this.access = access;
        this.level = this.player.level();

        this.baseContainerData = baseContainerData;
        this.addDataSlots(this.baseContainerData);

        this.addInputSlot(slotDefinitions);
        this.addFuelSlot(slotDefinitions);
        this.addAdditionalSlots(slotDefinitions);

        this.lastSlotIndex = slotDefinitions.getLastSlotIndex();

        this.addStandardInventorySlots(playerInventory, 8, 84);
    }

    private void addInputSlot(ElementalExtractorSlotDefinitions slotDefinitions) {
        var slotDefinition = slotDefinitions.getInputSlot();
        this.addSlot(new SlotItemHandler(this.baseInventory, slotDefinition.slot(), slotDefinition.x(), slotDefinition.y()));
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

    protected int getLastSlotIndex() {
        return this.lastSlotIndex;
    }

    protected int getInventorySlotStart() {
        return this.getLastSlotIndex() + 1;
    }

    protected int getInventorySlotEnd() {
        return this.getInventorySlotStart() + 27;
    }

    protected int getUseRowStart() {
        return this.getInventorySlotEnd();
    }

    protected int getUseRowEnd() {
        return this.getUseRowStart() + 9;
    }

    public boolean isLit() {
        return this.baseContainerData.get(2) > 0;
    }

    public float getLitProgress() {
        int i = this.baseContainerData.get(3);
        if (i == 0) {
            i = 200;
        }

        return Mth.clamp((float) this.baseContainerData.get(2) / (float) i, 0.0F, 1.0F);
    }

    public float getExtractionProgress() {
        int i = this.baseContainerData.get(0);
        int j = this.baseContainerData.get(1);
        return j != 0 && i != 0 ? Mth.clamp((float) i / (float) j, 0.0F, 1.0F) : 0.0F;
    }
}
