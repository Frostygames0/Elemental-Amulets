package frostygames0.elementalamulets.inventory;

import net.minecraft.core.BlockPos;
import net.minecraft.world.Containers;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.items.IItemHandler;

public final class ItemHandlerHelper {
    private ItemHandlerHelper() {
    }

    public static void dropItemHandlerContents(Level level, BlockPos blockPos, IItemHandler itemHandler) {
        for (int i = 0; i < itemHandler.getSlots(); i++) {
            Containers.dropItemStack(level, blockPos.getX(), blockPos.getY(), blockPos.getZ(), itemHandler.getStackInSlot(i));
        }
    }
}
