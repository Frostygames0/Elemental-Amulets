package frostygames0.elementalamulets.items.amulets.interfaces;

import net.minecraft.item.ItemStack;

//Interface for all amulets that somehow alter player's jump factor
public interface IJumpItem {
    float getJump(ItemStack stack);
    float getFallResist(ItemStack stack);
}
