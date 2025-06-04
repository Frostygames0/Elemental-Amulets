package frostygames0.elementalamulets.block.entity;

import frostygames0.elementalamulets.element.storage.ElementStorage;
import frostygames0.elementalamulets.element.storage.IElementStorage;
import frostygames0.elementalamulets.element.storage.IElementStorageProvider;
import frostygames0.elementalamulets.initialization.ModBlockEntities;
import frostygames0.elementalamulets.initialization.ModBlocks;
import frostygames0.elementalamulets.inventory.menu.SimpleStorageMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class SimpleStorageBlockEntity extends BlockEntity implements IElementStorageProvider, MenuProvider {
    public static final String TAG_STORAGE = "Storage";
    public static final int STORAGE_MAX_CAPACITY = 1000;

    private final ElementStorage storage = new ElementStorage(STORAGE_MAX_CAPACITY) {
        @Override
        public void onChanged() {
            setChanged();
        }
    };

    public SimpleStorageBlockEntity(BlockPos pos, BlockState blockState) {
        super(ModBlockEntities.SIMPLE_STORAGE.get(), pos, blockState);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        storage.deserializeNBT(registries, tag.getCompound(TAG_STORAGE));
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.put(TAG_STORAGE, storage.serializeNBT(registries));
    }

    @Override
    public IElementStorage getElementStorage() {
        return storage;
    }

    @Override
    public Component getDisplayName() {
        return ModBlocks.SIMPLE_STORAGE.get().getName();
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player) {
        return SimpleStorageMenu.forServer(containerId, playerInventory, storage, ContainerLevelAccess.create(level, worldPosition));
    }
}
