package frostygames0.elementalamulets;

import frostygames0.elementalamulets.core.init.ModItems;
import frostygames0.elementalamulets.items.interfaces.IFireItem;
import frostygames0.elementalamulets.items.interfaces.ISpeedItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.fml.RegistryObject;

public class ElementalAmuletsGroup extends ItemGroup {
    public ElementalAmuletsGroup() {
        super(ElementalAmulets.MOD_ID);
    }

    @Override
    public ItemStack createIcon() {
        return new ItemStack(ModItems.FIRE_AMULET.get());
    }
}
