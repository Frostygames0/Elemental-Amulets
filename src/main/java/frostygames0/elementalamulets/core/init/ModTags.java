package frostygames0.elementalamulets.core.init;

import net.minecraft.item.Item;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ItemTags;

public class ModTags {
    public static final ITag.INamedTag<Item> NECKLACE = ItemTags.makeWrapperTag("curios:necklace");
    public static final ITag.INamedTag<Item> ELEMENTS = ItemTags.makeWrapperTag("elementalamulets:elements");

    public static final ITag.INamedTag<Item> FIRE_ELEMENT_CONVERTIBLE = ItemTags.makeWrapperTag("elementalamulets:fire_element_convertible");
    public static final ITag.INamedTag<Item> WATER_ELEMENT_CONVERTIBLE = ItemTags.makeWrapperTag("elementalamulets:water_element_convertible");
    public static final ITag.INamedTag<Item> AIR_ELEMENT_CONVERTIBLE = ItemTags.makeWrapperTag("elementalamulets:air_element_convertible");
    public static final ITag.INamedTag<Item> EARTH_ELEMENT_CONVERTIBLE = ItemTags.makeWrapperTag("elementalamulets:earth_element_convertible");

}
