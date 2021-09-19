package frostygames0.elementalamulets.core.init;

import frostygames0.elementalamulets.ElementalAmulets;
import frostygames0.elementalamulets.core.util.ElementalRarity;
import frostygames0.elementalamulets.items.AmuletBelt;
import frostygames0.elementalamulets.items.ElementItem;
import frostygames0.elementalamulets.items.GuideBook;
import frostygames0.elementalamulets.items.amulets.*;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.Rarity;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;


import java.util.ArrayList;
import java.util.List;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, ElementalAmulets.MOD_ID);

    // Mod's guide book
    public static final RegistryObject<Item> GUIDE_BOOK = ITEMS.register("guide_book",
            () -> new GuideBook(new Item.Properties().tab(ElementalAmulets.GROUP).stacksTo(1)));

    // Mod BlockItems
    public static final RegistryObject<BlockItem> ELEMENTAL_COMBINATOR_BLOCK = ITEMS.register("elemental_combinator",
            () -> new BlockItem(ModBlocks.ELEMENTAL_COMBINATOR.get(), new Item.Properties().tab(ElementalAmulets.GROUP)));
    public static final RegistryObject<BlockItem> CELESTIAL_FOCUS = ITEMS.register("celestial_focus",
            () -> new BlockItem(ModBlocks.CELESTIAL_FOCUS.get(), new Item.Properties().tab(ElementalAmulets.GROUP)));
    public static final RegistryObject<BlockItem> ELEMENTAL_ORE = ITEMS.register("elemental_ore",
            () -> new BlockItem(ModBlocks.ELEMENTAL_STONE.get(), new Item.Properties().tab(ElementalAmulets.GROUP)));

    // Elements
    public static final RegistryObject<Item> ELEMENTAL_SHARDS = ITEMS.register("elemental_shards",
            () -> new ElementItem(Rarity.COMMON));
    public static final RegistryObject<Item> FIRE_ELEMENT = ITEMS.register("fire_element",
            () -> new ElementItem(ElementalRarity.FIRE, new TranslationTextComponent("item.elementalamulets.fire_element.tooltip").withStyle(TextFormatting.GRAY)));
    public static final RegistryObject<Item> WATER_ELEMENT = ITEMS.register("water_element",
            () -> new ElementItem(ElementalRarity.WATER, new TranslationTextComponent("item.elementalamulets.water_element.tooltip").withStyle(TextFormatting.GRAY)));
    public static final RegistryObject<Item> EARTH_ELEMENT = ITEMS.register("earth_element",
            () -> new ElementItem(ElementalRarity.EARTH, new TranslationTextComponent("item.elementalamulets.earth_element.tooltip").withStyle(TextFormatting.GRAY)));
    public static final RegistryObject<Item> AIR_ELEMENT = ITEMS.register("air_element",
            () -> new ElementItem(ElementalRarity.AIR, new TranslationTextComponent("item.elementalamulets.air_element.tooltip").withStyle(TextFormatting.GRAY)));
    public static final RegistryObject<Item> AETHER_ELEMENT = ITEMS.register("aether_element",
            () -> new ElementItem(Rarity.RARE, new TranslationTextComponent("item.elementalamulets.aether_element.tooltip").withStyle(TextFormatting.GRAY)));

    public static final RegistryObject<Item> ALL_SEEING_LENS = ITEMS.register("all_seeing_lens",
            () -> new Item(new Item.Properties().tab(ElementalAmulets.GROUP).stacksTo(16).rarity(Rarity.UNCOMMON)));

    // Amulets
    public static final RegistryObject<Item> EMPTY_AMULET = ITEMS.register("empty_amulet",
            () -> new Item(new Item.Properties().tab(ElementalAmulets.GROUP).stacksTo(1)));
    public static final RegistryObject<Item> FIRE_AMULET = ITEMS.register("fire_amulet",
            () -> new FireAmulet(new Item.Properties().tab(ElementalAmulets.GROUP).rarity(ElementalRarity.FIRE).durability(1000).fireResistant()));
    public static final RegistryObject<Item> SPEED_AMULET = ITEMS.register("speed_amulet",
            () -> new SpeedAmulet(new Item.Properties().tab(ElementalAmulets.GROUP).rarity(ElementalRarity.SPEED).durability(1000)));
    public static final RegistryObject<Item> JUMP_AMULET = ITEMS.register("jump_amulet",
            () -> new JumpAmulet(new Item.Properties().tab(ElementalAmulets.GROUP).rarity(ElementalRarity.JUMP).durability(1000)));
    public static final RegistryObject<Item> INVISIBILITY_AMULET = ITEMS.register("invisibility_amulet",
            () -> new InvisibilityAmulet(new Item.Properties().tab(ElementalAmulets.GROUP).rarity(ElementalRarity.AIR).durability(1000)));
    public static final RegistryObject<Item> TERRA_PROTECTION_AMULET = ITEMS.register("protection_amulet",
            () -> new TerraProtectionAmulet(new Item.Properties().tab(ElementalAmulets.GROUP).rarity(ElementalRarity.EARTH).durability(1000)));
    public static final RegistryObject<Item> WATER_AMULET = ITEMS.register("water_amulet",
            () -> new WaterAmulet(new Item.Properties().tab(ElementalAmulets.GROUP).durability(1000)));
    public static final RegistryObject<Item> AIR_AMULET = ITEMS.register("air_amulet",
            () -> new AirAmulet(new Item.Properties().tab(ElementalAmulets.GROUP).durability(1000)));

    public static final RegistryObject<Item> AMULET_BELT = ITEMS.register("amulet_belt",
            () -> new AmuletBelt(new Item.Properties().tab(null).stacksTo(1)));

    public static List<AmuletItem> getAmulets() {
        List<AmuletItem> items = new ArrayList<>();
        for(RegistryObject<Item> reg : ITEMS.getEntries()) {
            Item item = reg.get();
            if(item instanceof AmuletItem) items.add((AmuletItem) item);
        }
        return items;
    }



}
