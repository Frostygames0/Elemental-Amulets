package frostygames0.elementalamulets.core.init;

import frostygames0.elementalamulets.ElementalAmulets;
import frostygames0.elementalamulets.items.FireAmulet;
import frostygames0.elementalamulets.items.InvisibilityAmulet;
import frostygames0.elementalamulets.items.JumpAmulet;
import frostygames0.elementalamulets.items.SpeedAmulet;
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

    // Amulets
    public static final RegistryObject<Item> FIRE_AMULET = ITEMS.register("fire_amulet",
            () -> new FireAmulet(new Item.Properties().group(ElementalAmulets.GROUP).rarity(Rarity.RARE).maxDamage(1000).isImmuneToFire()));
    public static final RegistryObject<Item> SPEED_AMULET = ITEMS.register("speed_amulet",
            () -> new SpeedAmulet(new Item.Properties().group(ElementalAmulets.GROUP).rarity(Rarity.RARE).maxDamage(1000).isImmuneToFire()));
    public static final RegistryObject<Item> JUMP_AMULET = ITEMS.register("jump_amulet",
            () -> new JumpAmulet(new Item.Properties().group(ElementalAmulets.GROUP).rarity(Rarity.RARE).maxDamage(1000).isImmuneToFire()));
    public static final RegistryObject<Item> INVISIBILITY_AMULET = ITEMS.register("invisibility_amulet",
            () -> new InvisibilityAmulet(new Item.Properties().group(ElementalAmulets.GROUP).rarity(Rarity.RARE).maxDamage(1000).isImmuneToFire()));

    // Mod BlockItems
    public static final RegistryObject<BlockItem> ELEMENTAL_CRAFTER_BLOCK = ITEMS.register("elemental_crafter",
            () -> new BlockItem(ModBlocks.ELEMENTAL_CRAFTER.get(), new Item.Properties().group(ElementalAmulets.GROUP)));
}
