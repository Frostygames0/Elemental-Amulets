package frostygames0.elementalamulets;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.text.TranslationTextComponent;

public class ElementalAmuletsGroup extends ItemGroup {
    public ElementalAmuletsGroup() {
        super("elementalamulets");
    }

    @Override
    public ItemStack createIcon() {
        return new ItemStack(Items.GRASS_BLOCK);
    }
}
