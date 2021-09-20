package frostygames0.elementalamulets.core.init;

import frostygames0.elementalamulets.ElementalAmulets;
import net.minecraft.item.Item;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;

public class ModTags {
    public static final ITag.INamedTag<Item> NECKLACES = tag("necklaces");
    public static final ITag.INamedTag<Item> ELEMENTS = tag("elements");

    public static final ITag.INamedTag<Item> FIRE_ELEMENT_CONVERTIBLE = forge(ElementalAmulets.MOD_ID+"/fire_element_convertible");
    public static final ITag.INamedTag<Item> WATER_ELEMENT_CONVERTIBLE = forge(ElementalAmulets.MOD_ID+"/water_element_convertible");
    public static final ITag.INamedTag<Item> AIR_ELEMENT_CONVERTIBLE = forge(ElementalAmulets.MOD_ID+"/air_element_convertible");
    public static final ITag.INamedTag<Item> EARTH_ELEMENT_CONVERTIBLE = forge(ElementalAmulets.MOD_ID+"/earth_element_convertible");

    private static ITag.INamedTag<Item> tag(String name) {
        return ItemTags.bind(new ResourceLocation(ElementalAmulets.MOD_ID, name).toString());
    }

    private static ITag.INamedTag<Item> forge(String name) {
        return ItemTags.createOptional(new ResourceLocation("forge", name));
    }
}
