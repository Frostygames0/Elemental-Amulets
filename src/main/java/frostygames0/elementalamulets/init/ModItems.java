/*
 *  Copyright (c) 2021
 *
 *     This file is part of Elemental Amulets, a Minecraft Mod.
 *
 *     Elemental Amulets is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Lesser General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Elemental Amulets is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Lesser General Public License for more details.
 *
 *     You should have received a copy of the GNU Lesser General Public License
 *     along with Elemental Amulets.  If not, see <https://www.gnu.org/licenses/>.
 */

package frostygames0.elementalamulets.init;

import frostygames0.elementalamulets.ElementalAmulets;
import frostygames0.elementalamulets.items.AmuletBeltItem;
import frostygames0.elementalamulets.items.AncientTabletItem;
import frostygames0.elementalamulets.items.ElementItem;
import frostygames0.elementalamulets.items.ElementalGuideItem;
import frostygames0.elementalamulets.items.amulets.*;
import frostygames0.elementalamulets.util.ElementalRarity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;


import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, ElementalAmulets.MOD_ID);

    // Mod's guide book
    public static final RegistryObject<Item> ELEMENTAL_GUIDE = ITEMS.register("guide_book",
            () -> new ElementalGuideItem(new Item.Properties().tab(ElementalAmulets.GROUP).stacksTo(1)));

    public static final RegistryObject<Item> ANCIENT_TABLET = ITEMS.register("ancient_tablet",
            () -> new AncientTabletItem(new Item.Properties().tab(ElementalAmulets.GROUP).rarity(Rarity.RARE).durability(100)));

    // Mod BlockItems
    // TODO Maybe I should use registry event with foreach loop instead of creating constants that I'm not using
    public static final RegistryObject<BlockItem> ELEMENTAL_COMBINATOR = ITEMS.register("elemental_combinator",
            () -> new BlockItem(ModBlocks.ELEMENTAL_COMBINATOR.get(), new Item.Properties().tab(ElementalAmulets.GROUP)));
    public static final RegistryObject<BlockItem> CELESTIAL_FOCUS = ITEMS.register("celestial_focus",
            () -> new BlockItem(ModBlocks.CELESTIAL_FOCUS.get(), new Item.Properties().tab(ElementalAmulets.GROUP)));
    public static final RegistryObject<BlockItem> ELEMENTAL_ORE = ITEMS.register("elemental_ore",
            () -> new BlockItem(ModBlocks.ELEMENTAL_ORE.get(), new Item.Properties().tab(ElementalAmulets.GROUP)));
    public static final RegistryObject<BlockItem> ELEMENTAL_SHARDS_BLOCK = ITEMS.register("elemental_shards_block",
            () -> new BlockItem(ModBlocks.ELEMENTAL_SHARDS_BLOCK.get(), new Item.Properties().tab(ElementalAmulets.GROUP)));
    public static final RegistryObject<BlockItem> FIRE_SHARDS_BLOCK = ITEMS.register("fire_shards_block",
            () -> new BlockItem(ModBlocks.FIRE_SHARDS_BLOCK.get(), new Item.Properties().tab(ElementalAmulets.GROUP).rarity(ElementalRarity.FIRE)) {
                @Override
                public int getBurnTime(ItemStack itemStack, @Nullable IRecipeType<?> recipeType) {
                    if (recipeType == IRecipeType.BLASTING) {
                        return 22000;
                    }
                    return 20000;
                }
            });
    public static final RegistryObject<BlockItem> WATER_SHARDS_BLOCK = ITEMS.register("water_shards_block",
            () -> new BlockItem(ModBlocks.WATER_SHARDS_BLOCK.get(), new Item.Properties().tab(ElementalAmulets.GROUP).rarity(ElementalRarity.WATER)));
    public static final RegistryObject<BlockItem> EARTH_SHARDS_BLOCK = ITEMS.register("earth_shards_block",
            () -> new BlockItem(ModBlocks.EARTH_SHARDS_BLOCK.get(), new Item.Properties().tab(ElementalAmulets.GROUP).rarity(ElementalRarity.EARTH)));
    public static final RegistryObject<BlockItem> AIR_SHARDS_BLOCK = ITEMS.register("air_shards_block",
            () -> new BlockItem(ModBlocks.AIR_SHARDS_BLOCK.get(), new Item.Properties().tab(ElementalAmulets.GROUP).rarity(ElementalRarity.AIR)));

    // Elements
    public static final RegistryObject<Item> ELEMENTAL_SHARDS = ITEMS.register("elemental_shards",
            () -> new ElementItem(Rarity.COMMON));
    public static final RegistryObject<Item> WATER_ELEMENT = ITEMS.register("water_element",
            () -> new ElementItem(ElementalRarity.WATER));
    public static final RegistryObject<Item> EARTH_ELEMENT = ITEMS.register("earth_element",
            () -> new ElementItem(ElementalRarity.EARTH));
    public static final RegistryObject<Item> FIRE_ELEMENT = ITEMS.register("fire_element",
            () -> new ElementItem(ElementalRarity.FIRE) {
                @Override
                public int getBurnTime(ItemStack itemStack, @Nullable IRecipeType<?> recipeType) {
                    if (recipeType == IRecipeType.BLASTING) {
                        return 5000;
                    }
                    return 4500;
                }
            });
    public static final RegistryObject<Item> AIR_ELEMENT = ITEMS.register("air_element",
            () -> new ElementItem(ElementalRarity.AIR));
    public static final RegistryObject<Item> AETHER_ELEMENT = ITEMS.register("aether_element",
            () -> new ElementItem(Rarity.RARE));

    public static final RegistryObject<Item> ALL_SEEING_LENS = ITEMS.register("all_seeing_lens",
            () -> new Item(new Item.Properties().tab(ElementalAmulets.GROUP).stacksTo(16).rarity(Rarity.UNCOMMON)));
    public static final RegistryObject<Item> AMULET_BELT = ITEMS.register("amulet_belt",
            () -> new AmuletBeltItem(new Item.Properties().tab(ElementalAmulets.GROUP).stacksTo(1).rarity(Rarity.UNCOMMON)));

    // Amulets
    public static final RegistryObject<Item> EMPTY_AMULET = ITEMS.register("empty_amulet",
            () -> new Item(new Item.Properties().tab(ElementalAmulets.GROUP).stacksTo(1)));
    public static final RegistryObject<Item> WATER_AMULET = ITEMS.register("water_amulet",
            () -> new WaterAmuletItem(new Item.Properties().tab(ElementalAmulets.GROUP).rarity(ElementalRarity.WATER).durability(1000)));
    public static final RegistryObject<Item> EARTH_AMULET = ITEMS.register("earth_amulet",
            () -> new EarthAmuletItem(new Item.Properties().tab(ElementalAmulets.GROUP).rarity(ElementalRarity.EARTH).durability(1000)));
    public static final RegistryObject<Item> FIRE_AMULET = ITEMS.register("fire_amulet",
            () -> new FireAmuletItem(new Item.Properties().tab(ElementalAmulets.GROUP).rarity(ElementalRarity.FIRE).fireResistant().durability(1000)));
    public static final RegistryObject<Item> AIR_AMULET = ITEMS.register("air_amulet",
            () -> new AirAmuletItem(new Item.Properties().tab(ElementalAmulets.GROUP).rarity(ElementalRarity.AIR).durability(1000)));
    public static final RegistryObject<Item> SPEED_AMULET = ITEMS.register("speed_amulet",
            () -> new SpeedAmuletItem(new Item.Properties().tab(ElementalAmulets.GROUP).rarity(ElementalRarity.SPEED).durability(1000)));
    public static final RegistryObject<Item> JUMP_AMULET = ITEMS.register("jump_amulet",
            () -> new JumpAmuletItem(new Item.Properties().tab(ElementalAmulets.GROUP).rarity(ElementalRarity.JUMP).durability(1000)));
    public static final RegistryObject<Item> TERRA_PROTECTION_AMULET = ITEMS.register("protection_amulet",
            () -> new TerraProtectionAmuletItem(new Item.Properties().tab(ElementalAmulets.GROUP).rarity(ElementalRarity.EARTH).durability(1000)));
    public static final RegistryObject<Item> INVISIBILITY_AMULET = ITEMS.register("invisibility_amulet",
            () -> new InvisibilityAmuletItem(new Item.Properties().tab(ElementalAmulets.GROUP).durability(1000)));
    public static final RegistryObject<Item> PACIFYING_AMULET = ITEMS.register("pacifying_amulet",
            () -> new PacifyingAmuletItem(new Item.Properties().tab(ElementalAmulets.GROUP).rarity(Rarity.EPIC).durability(2000)));
    public static final RegistryObject<Item> KNOCKBACK_AMULET = ITEMS.register("knockback_amulet",
            () -> new KnockbackAmuletItem(new Item.Properties().tab(ElementalAmulets.GROUP).rarity(Rarity.RARE).durability(1000)));


    public static List<AmuletItem> getAmulets() {
        List<AmuletItem> items = new ArrayList<>();
        for (RegistryObject<Item> reg : ITEMS.getEntries()) {
            Item item = reg.get();
            if (item instanceof AmuletItem) items.add((AmuletItem) item);
        }
        return items;
    }


}
