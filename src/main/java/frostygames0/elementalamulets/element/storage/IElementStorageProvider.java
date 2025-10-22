package frostygames0.elementalamulets.element.storage;

import net.minecraft.core.Direction;
import org.jetbrains.annotations.Nullable;

public interface IElementStorageProvider {
    /**
     * @param side null if accessing internal storage
     * @return storage
     */
    IElementStorage getElementStorage(@Nullable Direction side);
}
