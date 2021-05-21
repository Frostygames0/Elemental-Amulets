package frostygames0.elementalamulets.items.amulets.interfaces;

import net.minecraft.item.ItemStack;

//For all amulets that somehow alter player's speed
public interface ISpeedItem {
    float getSpeed(ItemStack stack);
}
