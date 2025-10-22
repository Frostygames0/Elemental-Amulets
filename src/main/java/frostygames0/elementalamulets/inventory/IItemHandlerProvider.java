package frostygames0.elementalamulets.inventory;

import net.minecraft.core.Direction;
import net.neoforged.neoforge.items.IItemHandler;
import org.jetbrains.annotations.Nullable;

public interface IItemHandlerProvider {
    /**
     * @param side null if accessing internal storage
     * @return storage
     */
    IItemHandler getItemHandler(@Nullable Direction side);
}

