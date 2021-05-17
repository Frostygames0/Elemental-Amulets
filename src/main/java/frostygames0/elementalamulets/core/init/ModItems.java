package frostygames0.elementalamulets.core.init;

import frostygames0.elementalamulets.ElementalAmulets;
import frostygames0.elementalamulets.core.util.ElementalColors;
import frostygames0.elementalamulets.items.*;
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
    public static final RegistryObject<Item> ELEMENTAL_SHARDS = ITEMS.register("elemental_shards",
            () -> new Item(new Item.Properties().group(ElementalAmulets.GROUP)));
    // Mod BlockItems
    public static final RegistryObject<BlockItem> ELEMENTAL_COMBINATOR_BLOCK = ITEMS.register("elemental_combinator",
            () -> new BlockItem(ModBlocks.ELEMENTAL_COMBINATOR.get(), new Item.Properties().group(ElementalAmulets.GROUP)));
    public static final RegistryObject<BlockItem> ELEMENTAL_STONE = ITEMS.register("elemental_ore",
            () -> new BlockItem(ModBlocks.ELEMENTAL_STONE.get(), new Item.Properties().group(ElementalAmulets.GROUP)));
    // Fire amulet and it's tiers
    public static final RegistryObject<Item> FIRE_AMULET = ITEMS.register("fire_amulet",
            () -> new FireAmulet(new Item.Properties().group(ElementalAmulets.GROUP).rarity(ElementalColors.FIRE).maxDamage(1000).isImmuneToFire(), 1));
    public static final RegistryObject<Item> FIRE_AMULET_TIER2 = ITEMS.register("fire_amulet_tier2",
            () -> new FireAmulet(new Item.Properties().group(ElementalAmulets.GROUP).rarity(ElementalColors.FIRE).maxDamage(1200).isImmuneToFire(), 2));
    public static final RegistryObject<Item> FIRE_AMULET_TIER3 = ITEMS.register("fire_amulet_tier3",
            () -> new FireAmulet(new Item.Properties().group(ElementalAmulets.GROUP).rarity(ElementalColors.FIRE).maxDamage(1400).isImmuneToFire(), 3));
    public static final RegistryObject<Item> FIRE_AMULET_TIER4 = ITEMS.register("fire_amulet_tier4",
            () -> new FireAmulet(new Item.Properties().group(ElementalAmulets.GROUP).rarity(ElementalColors.FIRE).maxDamage(1600).isImmuneToFire(), 4));
    // Speed amulet and it's tiers
    public static final RegistryObject<Item> SPEED_AMULET = ITEMS.register("speed_amulet",
            () -> new SpeedAmulet(new Item.Properties().group(ElementalAmulets.GROUP).rarity(ElementalColors.SPEED).maxDamage(1000).isImmuneToFire(),1));
    public static final RegistryObject<Item> SPEED_AMULET_TIER2 = ITEMS.register("speed_amulet_tier2",
            () -> new SpeedAmulet(new Item.Properties().group(ElementalAmulets.GROUP).rarity(ElementalColors.SPEED).maxDamage(1200).isImmuneToFire(),2));
    public static final RegistryObject<Item> SPEED_AMULET_TIER3 = ITEMS.register("speed_amulet_tier3",
            () -> new SpeedAmulet(new Item.Properties().group(ElementalAmulets.GROUP).rarity(ElementalColors.SPEED).maxDamage(1400).isImmuneToFire(),3));
    public static final RegistryObject<Item> SPEED_AMULET_TIER4 = ITEMS.register("speed_amulet_tier4",
            () -> new SpeedAmulet(new Item.Properties().group(ElementalAmulets.GROUP).rarity(ElementalColors.SPEED).maxDamage(1600).isImmuneToFire(),4));
    // Jump amulet and it's tiers
    public static final RegistryObject<Item> JUMP_AMULET = ITEMS.register("jump_amulet",
            () -> new JumpAmulet(new Item.Properties().group(ElementalAmulets.GROUP).rarity(ElementalColors.JUMP).maxDamage(1000).isImmuneToFire(), 1));
    public static final RegistryObject<Item> JUMP_AMULET_TIER2 = ITEMS.register("jump_amulet_tier2",
            () -> new JumpAmulet(new Item.Properties().group(ElementalAmulets.GROUP).rarity(ElementalColors.JUMP).maxDamage(1200).isImmuneToFire(), 2));
    public static final RegistryObject<Item> JUMP_AMULET_TIER3 = ITEMS.register("jump_amulet_tier3",
            () -> new JumpAmulet(new Item.Properties().group(ElementalAmulets.GROUP).rarity(ElementalColors.JUMP).maxDamage(1400).isImmuneToFire(), 3));
    public static final RegistryObject<Item> JUMP_AMULET_TIER4 = ITEMS.register("jump_amulet_tier4",
            () -> new JumpAmulet(new Item.Properties().group(ElementalAmulets.GROUP).rarity(ElementalColors.JUMP).maxDamage(1600).isImmuneToFire(), 4));

    // Invisibility amulet(Has no tiers)
    public static final RegistryObject<Item> INVISIBILITY_AMULET = ITEMS.register("invisibility_amulet",
            () -> new InvisibilityAmulet(new Item.Properties().group(null).rarity(ElementalColors.INVISIBILITY).maxDamage(1000).isImmuneToFire()));


}
