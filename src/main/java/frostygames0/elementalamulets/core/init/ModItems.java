package frostygames0.elementalamulets.core.init;

import frostygames0.elementalamulets.ElementalAmulets;
import frostygames0.elementalamulets.items.*;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.Rarity;
import net.minecraft.util.DamageSource;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, ElementalAmulets.MOD_ID);

    /*
    All amulets registered here
    TODO: Probably make amulet tiers as item's NBT data rather than different item
     */

    // Fire amulet and it's tiers
    public static final RegistryObject<Item> FIRE_AMULET = ITEMS.register("fire_amulet",
            () -> new FireAmulet(new Item.Properties().group(ElementalAmulets.GROUP).rarity(Rarity.RARE).maxDamage(1000).isImmuneToFire()));
    public static final RegistryObject<Item> FIRE_AMULET_TIER2 = ITEMS.register("fire_amulet_tier2",
            () -> new FireAmulet(new Item.Properties().group(ElementalAmulets.GROUP).rarity(Rarity.RARE).maxDamage(1200).isImmuneToFire()));
    public static final RegistryObject<Item> FIRE_AMULET_TIER3 = ITEMS.register("fire_amulet_tier3",
            () -> new FireAmulet(new Item.Properties().group(ElementalAmulets.GROUP).rarity(Rarity.RARE).maxDamage(1400).isImmuneToFire()));
    public static final RegistryObject<Item> FIRE_AMULET_TIER4 = ITEMS.register("fire_amulet_tier4",
            () -> new FireAmulet(new Item.Properties().group(ElementalAmulets.GROUP).rarity(Rarity.RARE).maxDamage(1600).isImmuneToFire()));
    // Speed amulet and it's tiers
    public static final RegistryObject<Item> SPEED_AMULET = ITEMS.register("speed_amulet",
            () -> new SpeedAmulet(new Item.Properties().group(ElementalAmulets.GROUP).rarity(Rarity.RARE).maxDamage(1000).isImmuneToFire(),1));
    public static final RegistryObject<Item> SPEED_AMULET_TIER2 = ITEMS.register("speed_amulet_tier2",
            () -> new SpeedAmulet(new Item.Properties().group(ElementalAmulets.GROUP).rarity(Rarity.RARE).maxDamage(1200).isImmuneToFire(),2));
    public static final RegistryObject<Item> SPEED_AMULET_TIER3 = ITEMS.register("speed_amulet_tier3",
            () -> new SpeedAmulet(new Item.Properties().group(ElementalAmulets.GROUP).rarity(Rarity.RARE).maxDamage(1400).isImmuneToFire(),3));
    public static final RegistryObject<Item> SPEED_AMULET_TIER4 = ITEMS.register("speed_amulet_tier4",
            () -> new SpeedAmulet(new Item.Properties().group(ElementalAmulets.GROUP).rarity(Rarity.RARE).maxDamage(1600).isImmuneToFire(),4));
    // Jump amulet and it's tiers
    public static final RegistryObject<Item> JUMP_AMULET = ITEMS.register("jump_amulet",
            () -> new JumpAmulet(new Item.Properties().group(ElementalAmulets.GROUP).rarity(Rarity.RARE).maxDamage(1000).isImmuneToFire(), 1));
    public static final RegistryObject<Item> JUMP_AMULET_TIER2 = ITEMS.register("jump_amulet_tier2",
            () -> new JumpAmulet(new Item.Properties().group(ElementalAmulets.GROUP).rarity(Rarity.RARE).maxDamage(1200).isImmuneToFire(), 2));
    public static final RegistryObject<Item> JUMP_AMULET_TIER3 = ITEMS.register("jump_amulet_tier3",
            () -> new JumpAmulet(new Item.Properties().group(ElementalAmulets.GROUP).rarity(Rarity.RARE).maxDamage(1400).isImmuneToFire(), 3));
    public static final RegistryObject<Item> JUMP_AMULET_TIER4 = ITEMS.register("jump_amulet_tier4",
            () -> new JumpAmulet(new Item.Properties().group(ElementalAmulets.GROUP).rarity(Rarity.RARE).maxDamage(1600).isImmuneToFire(), 4));

    // Invisibility amulet(Has no tiers)
    public static final RegistryObject<Item> INVISIBILITY_AMULET = ITEMS.register("invisibility_amulet",
            () -> new InvisibilityAmulet(new Item.Properties().group(ElementalAmulets.GROUP).rarity(Rarity.RARE).maxDamage(1000).isImmuneToFire()));

    // Poor thingy (TODO: probably remove it. Since it has not usage)
    public static final RegistryObject<Item> CURSED_AMULET = ITEMS.register("cursed_amulet",
            () -> new CursedAmulet(new Item.Properties().group(ElementalAmulets.GROUP).rarity(Rarity.RARE).maxDamage(1000).isImmuneToFire()));

    // Mod BlockItems
    public static final RegistryObject<BlockItem> ELEMENTAL_CRAFTER_BLOCK = ITEMS.register("elemental_crafter",
            () -> new BlockItem(ModBlocks.ELEMENTAL_CRAFTER.get(), new Item.Properties().group(ElementalAmulets.GROUP)));
}
