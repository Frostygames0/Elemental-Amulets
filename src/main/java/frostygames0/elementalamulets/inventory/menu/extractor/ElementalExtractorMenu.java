package frostygames0.elementalamulets.inventory.menu.extractor;

import frostygames0.elementalamulets.element.storage.ElementStorage;
import frostygames0.elementalamulets.element.storage.IElementStorage;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;

public class ElementalExtractorMenu extends AbstractElementalExtractorMenu {
    public ElementalExtractorMenu(int containerId, Inventory playerInventory, IItemHandler baseInventory, IElementStorage storage, ContainerLevelAccess access) {
        super(null, containerId, playerInventory, baseInventory, storage, access, null);
    }

    public ElementalExtractorMenu(int containerId, Inventory playerInventory) {
        this(containerId, playerInventory, new ItemStackHandler(1), new ElementStorage(1), ContainerLevelAccess.NULL);
    }

    @Override
    public boolean stillValid(Player player) {
        return AbstractContainerMenu.stillValid(this.access, player, null);
    }
}
