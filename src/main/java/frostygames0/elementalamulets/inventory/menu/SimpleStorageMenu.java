package frostygames0.elementalamulets.inventory.menu;

import frostygames0.elementalamulets.block.entity.SimpleStorageBlockEntity;
import frostygames0.elementalamulets.element.storage.ElementStorage;
import frostygames0.elementalamulets.element.storage.IElementStorage;
import frostygames0.elementalamulets.initialization.ModBlocks;
import frostygames0.elementalamulets.initialization.ModMenuTypes;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.item.ItemStack;

public class SimpleStorageMenu extends SyncedElementalStorageMenu {
    private final ContainerLevelAccess access;

    public SimpleStorageMenu(int containerId, Inventory playerInventory, IElementStorage storage, ContainerLevelAccess access) {
        super(ModMenuTypes.SIMPLE_STORAGE_MENU.get(), containerId, playerInventory.player, storage);
        this.access = access;

        addStandardInventorySlots(playerInventory, 8, 51);
    }

    public static SimpleStorageMenu forServer(int containerId, Inventory playerInventory, IElementStorage storage, ContainerLevelAccess access) {
        return new SimpleStorageMenu(containerId, playerInventory, storage, access);
    }

    public static SimpleStorageMenu forClient(int containerId, Inventory playerInventory) {
        return new SimpleStorageMenu(containerId,
                playerInventory,
                new ElementStorage(SimpleStorageBlockEntity.STORAGE_MAX_CAPACITY),
                ContainerLevelAccess.NULL);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean stillValid(Player player) {
        return AbstractContainerMenu.stillValid(access, player, ModBlocks.SIMPLE_STORAGE.get());
    }
}
