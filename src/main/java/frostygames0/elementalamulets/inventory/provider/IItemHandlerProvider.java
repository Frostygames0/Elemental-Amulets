package frostygames0.elementalamulets.inventory.provider;

import net.minecraft.core.Direction;
import net.neoforged.neoforge.items.IItemHandler;
import org.jetbrains.annotations.Nullable;

@FunctionalInterface
public interface IItemHandlerProvider {
    IItemHandler getItemHandlerForDirection(@Nullable Direction direction);
}

