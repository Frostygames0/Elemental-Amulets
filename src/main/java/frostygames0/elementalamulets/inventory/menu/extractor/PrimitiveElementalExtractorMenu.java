package frostygames0.elementalamulets.inventory.menu.extractor;

import frostygames0.elementalamulets.block.entity.extractor.PrimitiveElementalExtractorBlockEntity;
import frostygames0.elementalamulets.element.storage.ElementStorage;
import frostygames0.elementalamulets.element.storage.IElementStorage;
import frostygames0.elementalamulets.inventory.menu.slots.EmptyElementalShardSlot;
import frostygames0.elementalamulets.inventory.menu.slots.OutputSlot;
import frostygames0.elementalamulets.registration.ModBlocks;
import frostygames0.elementalamulets.registration.ModMenuTypes;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;

public class PrimitiveElementalExtractorMenu extends AbstractElementalExtractorMenu {
    public static final int SHARD_SLOT = PrimitiveElementalExtractorBlockEntity.EMPTY_SHARD_SLOT;
    public static final int RESULT_SLOTS_START = PrimitiveElementalExtractorBlockEntity.RESULTS_SLOTS_START;
    public static final int RESULT_SLOTS_END = PrimitiveElementalExtractorBlockEntity.RESULTS_SLOTS_END;
    public static final int RESULT_SLOTS_MAX_SIZE = PrimitiveElementalExtractorBlockEntity.RESULTS_SLOT_MAX_SIZE;

    public static final int ADDITIONAL_INVENTORY_SIZE = PrimitiveElementalExtractorBlockEntity.ADDITIONAL_INVENTORY_SIZE;
    public static final int ELEMENT_STORAGE_CAPACITY = PrimitiveElementalExtractorBlockEntity.ELEMENT_STORAGE_CAPACITY;
    public static final int MAX_DISTINCT_ELEMENTS_STORED = PrimitiveElementalExtractorBlockEntity.MAX_DISTINCT_ELEMENTS_STORED;

    public PrimitiveElementalExtractorMenu(int containerId, Inventory playerInventory,
                                           IItemHandler baseInventory, IItemHandler additionalInventory,
                                           IElementStorage storage, ContainerLevelAccess access) {
        super(ModMenuTypes.PRIMITIVE_ELEMENTAL_EXTRACTOR.get(), containerId, playerInventory,
                baseInventory, storage, access, createAdditionalSlots(additionalInventory));
    }

    public PrimitiveElementalExtractorMenu(int containerId, Inventory playerInventory) {
        this(containerId, playerInventory,
                new ItemStackHandler(BASE_INVENTORY_SIZE),
                new ItemStackHandler(ADDITIONAL_INVENTORY_SIZE),
                new ElementStorage(ELEMENT_STORAGE_CAPACITY, MAX_DISTINCT_ELEMENTS_STORED),
                ContainerLevelAccess.NULL);
    }

    private static ElementalExtractorSlotDefinitions createAdditionalSlots(IItemHandler additionalInventory) {
        var builder = ElementalExtractorSlotDefinitions.builder();

        builder.addInputSlot(INPUT_SLOT, 44, 11).addFuelSlot(FUEL_SLOT, 44, 47);

        builder.addAdditionalSlot(SHARD_SLOT, 80, 29,
                (slot, x, y) -> new EmptyElementalShardSlot(additionalInventory, slot, x, y));

        int x = 116;
        int y = 11;
        for (int i = 0; i < RESULT_SLOTS_END; i++) {
            builder.addAdditionalSlot(RESULT_SLOTS_START + i, x, y + (i * 18),
                    ((slot, x1, y1) -> new OutputSlot(additionalInventory, RESULT_SLOTS_MAX_SIZE, slot, x1, y1)));
        }

        //builder.addAdditionalSlotsQuickMoveHandler(PrimitiveElementalExtractorMenu::onQuickMove);

        return builder.build();
    }

    private static ItemStack onQuickMove(Player player, int slot) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean stillValid(Player player) {
        return AbstractContainerMenu.stillValid(this.access, player, ModBlocks.PRIMITIVE_ELEMENTAL_EXTRACTOR.get());
    }
}
