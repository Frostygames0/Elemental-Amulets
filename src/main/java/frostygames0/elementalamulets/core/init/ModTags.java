package frostygames0.elementalamulets.core.init;

import frostygames0.elementalamulets.ElementalAmulets;
import net.minecraft.item.Item;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;

public class ModTags {
    public static final ITag.INamedTag<Item> NECKLACE = ItemTags.makeWrapperTag("curios:necklace");
    public static final ITag.INamedTag<Item> ELEMENTS = tag("elements");

    public static final ITag.INamedTag<Item> FIRE_ELEMENT_CONVERTIBLE = tag("fire_element_convertible");
    public static final ITag.INamedTag<Item> WATER_ELEMENT_CONVERTIBLE = tag("water_element_convertible");
    public static final ITag.INamedTag<Item> AIR_ELEMENT_CONVERTIBLE = tag("air_element_convertible");
    public static final ITag.INamedTag<Item> EARTH_ELEMENT_CONVERTIBLE = tag("earth_element_convertible");

    private static ITag.INamedTag<Item> tag(String name) {
        return ItemTags.makeWrapperTag(new ResourceLocation(ElementalAmulets.MOD_ID, name).toString());
    }

    private static ITag.INamedTag<Item> forge(String name) {
        return ItemTags.createOptional(new ResourceLocation("forge", name));
    }
}
