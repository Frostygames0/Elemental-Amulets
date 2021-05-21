package frostygames0.elementalamulets.core.init;

import frostygames0.elementalamulets.ElementalAmulets;
import frostygames0.elementalamulets.core.util.ElementalColors;
import frostygames0.elementalamulets.items.GuideBook;
import frostygames0.elementalamulets.items.amulets.FireAmulet;
import frostygames0.elementalamulets.items.amulets.InvisibilityAmulet;
import frostygames0.elementalamulets.items.amulets.JumpAmulet;
import frostygames0.elementalamulets.items.amulets.SpeedAmulet;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.Rarity;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, ElementalAmulets.MOD_ID);

    // Mod's guide book
    public static final RegistryObject<Item> GUIDE_BOOK = ITEMS.register("guide_book",
            () -> new GuideBook(new Item.Properties().group(ElementalAmulets.GROUP).rarity(Rarity.COMMON).isImmuneToFire().maxStackSize(1)));
    // Mod BlockItems
    public static final RegistryObject<BlockItem> ELEMENTAL_COMBINATOR_BLOCK = ITEMS.register("elemental_combinator",
            () -> new BlockItem(ModBlocks.ELEMENTAL_COMBINATOR.get(), new Item.Properties().group(ElementalAmulets.GROUP)));
    public static final RegistryObject<BlockItem> ELEMENTAL_STONE = ITEMS.register("elemental_ore",
            () -> new BlockItem(ModBlocks.ELEMENTAL_STONE.get(), new Item.Properties().group(ElementalAmulets.GROUP)));

    // Elements
    public static final RegistryObject<Item> ELEMENTAL_SHARDS = ITEMS.register("elemental_shards",
            () -> new Item(new Item.Properties().group(ElementalAmulets.GROUP)));
    public static final RegistryObject<Item> FIRE_ELEMENT = ITEMS.register("fire_element",
            () -> new Item(new Item.Properties().group(ElementalAmulets.GROUP).rarity(ElementalColors.FIRE)));
    public static final RegistryObject<Item> WATER_ELEMENT = ITEMS.register("water_element",
            () -> new Item(new Item.Properties().group(ElementalAmulets.GROUP).rarity(ElementalColors.WATER)));
    public static final RegistryObject<Item> EARTH_ELEMENT = ITEMS.register("earth_element",
            () -> new Item(new Item.Properties().group(ElementalAmulets.GROUP).rarity(ElementalColors.EARTH)));
    public static final RegistryObject<Item> AIR_ELEMENT = ITEMS.register("air_element",
            () -> new Item(new Item.Properties().group(ElementalAmulets.GROUP).rarity(ElementalColors.AIR)));

    // Amulets
    public static final RegistryObject<Item> FIRE_AMULET = ITEMS.register("fire_amulet",
            () -> new FireAmulet(new Item.Properties().group(ElementalAmulets.GROUP).rarity(ElementalColors.FIRE).maxDamage(1000).isImmuneToFire()));
    public static final RegistryObject<Item> SPEED_AMULET = ITEMS.register("speed_amulet",
            () -> new SpeedAmulet(new Item.Properties().group(ElementalAmulets.GROUP).rarity(ElementalColors.WATER).maxDamage(1000).isImmuneToFire()));
    public static final RegistryObject<Item> JUMP_AMULET = ITEMS.register("jump_amulet",
            () -> new JumpAmulet(new Item.Properties().group(ElementalAmulets.GROUP).rarity(ElementalColors.JUMP).maxDamage(1000).isImmuneToFire()));
    /*public static final RegistryObject<Item> AIR_AMULET = ITEMS.register("air_amulet",
            () -> new AirAmulet(new Item.Properties().group(ElementalAmulets.GROUP).rarity(ElementalColors.AIR).maxDamage(1600).isImmuneToFire()));*/
    public static final RegistryObject<Item> INVISIBILITY_AMULET = ITEMS.register("invisibility_amulet",
            () -> new InvisibilityAmulet(new Item.Properties().group(ElementalAmulets.GROUP).rarity(ElementalColors.AIR).maxDamage(1000).isImmuneToFire()));


}
