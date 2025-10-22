package frostygames0.elementalamulets.client.color.block;

import frostygames0.elementalamulets.element.ElementalHelper;
import net.minecraft.client.color.block.BlockColor;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class ElementalStorageBlockTintSource implements BlockColor {
    @Override
    public int getColor(BlockState state, @Nullable BlockAndTintGetter level, @Nullable BlockPos pos, int tintIndex) {
        var capabilityOptional = ElementalHelper.getElementStorage(level, pos, null);
        if (capabilityOptional.isPresent()) {
            var cap = capabilityOptional.get();
            return cap.getStored().getColor();
        }

        return 0;
    }
}
