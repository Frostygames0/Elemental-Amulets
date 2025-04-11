package frostygames0.elementalamulets.inventory.menu.extractor;

import frostygames0.elementalamulets.block.entity.extractor.PrimitiveElementalExtractorBlockEntity;
import frostygames0.elementalamulets.element.storage.ElementStorage;
import frostygames0.elementalamulets.element.storage.IElementStorage;
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
    public PrimitiveElementalExtractorMenu(int containerId, Inventory playerInventory,
                                           IItemHandler baseInventory, IItemHandler additionalInventory,
                                           IElementStorage storage, ContainerLevelAccess access) {
        super(ModMenuTypes.PRIMITIVE_ELEMENTAL_EXTRACTOR.get(), containerId, playerInventory,
                baseInventory, storage, access, createAdditionalSlots(additionalInventory));
    }

    public PrimitiveElementalExtractorMenu(int containerId, Inventory playerInventory) {
        this(containerId, playerInventory,
                new ItemStackHandler(BASE_INVENTORY_SIZE),
                new ItemStackHandler(PrimitiveElementalExtractorBlockEntity.ADDITIONAL_INVENTORY_SIZE),
                new ElementStorage(PrimitiveElementalExtractorBlockEntity.ELEMENT_STORAGE_CAPACITY,
                        PrimitiveElementalExtractorBlockEntity.MAX_DISTINCT_ELEMENTS_STORED),
                ContainerLevelAccess.NULL);
    }

    private static AdditionalSlots createAdditionalSlots(IItemHandler additionalInventory) {
//        return AdditionalSlots.builder()
//                .addSlot(new EmptyElementalShardSlot(additionalInventory, 0, 80, 29))
//                .addSlotGrid((slot, x, y) -> new OutputSlot(additionalInventory, 21, slot, x, y), 1, 116, 11, 2, 3)
//                .addOnQuickMoveBehaviour(PrimitiveElementalExtractorMenu::onQuickMove)
//                .build();
        return null;
    }

    private static ItemStack onQuickMove(Player player, int slot) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean stillValid(Player player) {
        return AbstractContainerMenu.stillValid(this.access, player, ModBlocks.PRIMITIVE_ELEMENTAL_EXTRACTOR.get());
    }
}
