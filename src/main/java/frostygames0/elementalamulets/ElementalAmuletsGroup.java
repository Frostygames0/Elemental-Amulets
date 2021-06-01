package frostygames0.elementalamulets;

import frostygames0.elementalamulets.core.init.ModItems;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

public class ElementalAmuletsGroup extends ItemGroup {
    public ElementalAmuletsGroup() {
        super(ElementalAmulets.MOD_ID);
    }

    @Override
    public ItemStack createIcon() {
        return new ItemStack(ModItems.FIRE_AMULET.get());
    }




}
