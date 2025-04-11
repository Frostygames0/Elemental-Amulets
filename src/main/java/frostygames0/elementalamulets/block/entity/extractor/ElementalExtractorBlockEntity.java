package frostygames0.elementalamulets.block.entity.extractor;

import frostygames0.elementalamulets.inventory.menu.extractor.ElementalExtractorMenu;
import frostygames0.elementalamulets.registration.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Containers;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.IItemHandler;
import org.jetbrains.annotations.Nullable;

public class ElementalExtractorBlockEntity extends AbstractElementalExtractorBlockEntity {
    public ElementalExtractorBlockEntity(BlockPos pos, BlockState state, int storageCapacity, int distinctElementCount) {
        super(ModBlockEntities.PRIMITIVE_ELEMENTAL_EXTRACTOR.get(), pos, state, storageCapacity, distinctElementCount);
    }

    @Override
    public void dropContents() {
        var blockPos = this.worldPosition;

        for (int i = 0; i < this.baseInventory.getSlots(); i++) {
            Containers.dropItemStack(level, blockPos.getX(), blockPos.getY(), blockPos.getZ(), this.baseInventory.getStackInSlot(i));
        }
    }

    @Override
    public IItemHandler getItemHandlerForDirection(@Nullable Direction direction) {
        return switch (direction) {
            case null, default -> this.baseInventory;
        };
    }

    @Override
    public Component getDisplayName() {
        return Component.literal("Elemental Extractor");
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player) {
        return new ElementalExtractorMenu(containerId, playerInventory, this.baseInventory, this.elementStorage, ContainerLevelAccess.create(this.level, this.worldPosition));
    }
}
