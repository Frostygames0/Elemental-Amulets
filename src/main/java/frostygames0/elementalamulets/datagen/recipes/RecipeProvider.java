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

package frostygames0.elementalamulets.datagen.recipes;

import frostygames0.elementalamulets.init.ModBlocks;
import frostygames0.elementalamulets.init.ModItems;
import frostygames0.elementalamulets.init.ModTags;
import frostygames0.elementalamulets.items.amulets.AmuletItem;
import frostygames0.elementalamulets.recipes.ElementalCombination;
import net.minecraft.advancements.criterion.InventoryChangeTrigger;
import net.minecraft.block.Block;
import net.minecraft.data.*;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.IItemProvider;
import net.minecraftforge.common.Tags;


import java.util.function.Consumer;

import static frostygames0.elementalamulets.ElementalAmulets.modPrefix;

/*
TODO: While it works fine and doesn't affect gameplay at all, it's still a very painful piece of shit that contains my early code :)
Basically, do not worry about this and release mod
*/
public class RecipeProvider extends net.minecraft.data.RecipeProvider {
    public RecipeProvider(DataGenerator generatorIn) {
        super(generatorIn);
    }


    @Override
    protected void buildShapelessRecipes(Consumer<IFinishedRecipe> consumer) {
        this.registerVanillaRecipes(consumer);
        this.registerModRecipes(consumer);
    }

    // Vanilla recipes(crafting, smelting, etc.)
    private void registerVanillaRecipes(Consumer<IFinishedRecipe> consumer) {
        ShapedRecipeBuilder.shaped(ModBlocks.ELEMENTAL_COMBINATOR.get())
                .pattern("_=_")
                .pattern("#4#")
                .pattern("000")
                .define('_', Items.RED_CARPET)
                .define('=', Items.IRON_INGOT)
                .define('4', ModItems.ELEMENTAL_SHARDS.get())
                .define('0', Tags.Items.COBBLESTONE)
                .define('#', ItemTags.PLANKS)
                .unlockedBy("has_item", InventoryChangeTrigger.Instance.hasItems(ModItems.ELEMENTAL_SHARDS.get()))
                .save(consumer);
        ShapelessRecipeBuilder.shapeless(ModItems.ELEMENTAL_GUIDE.get())
                .requires(Items.BOOK)
                .requires(ModTags.Items.ELEMENTS)
                .unlockedBy("has_item", InventoryChangeTrigger.Instance.hasItems(ModItems.ELEMENTAL_SHARDS.get()))
                .save(consumer);
        ShapedRecipeBuilder.shaped(ModItems.EMPTY_AMULET.get())
                .pattern(" ##")
                .pattern("xx#")
                .pattern("xx ")
                .define('#', Tags.Items.STRING)
                .define('x', ModItems.ELEMENTAL_SHARDS.get())
                .unlockedBy("has_item", InventoryChangeTrigger.Instance.hasItems(ModItems.ELEMENTAL_SHARDS.get()))
                .save(consumer);
        ShapedRecipeBuilder.shaped(ModBlocks.CELESTIAL_FOCUS.get())
                .pattern("/0/")
                .pattern("/ /")
                .pattern(". .")
                .define('/', Tags.Items.RODS_WOODEN)
                .define('0', ModItems.ALL_SEEING_LENS.get())
                .define('.', Items.IRON_NUGGET)
                .unlockedBy("has_item", InventoryChangeTrigger.Instance.hasItems(ModItems.ALL_SEEING_LENS.get()))
                .save(consumer);
        oreSmelting(ModBlocks.ELEMENTAL_ORE.get(), ModItems.ELEMENTAL_SHARDS.get(), 0.7f, 200, consumer);

        shardsBlockRecipe(ModBlocks.ELEMENTAL_SHARDS_BLOCK.get(), ModItems.ELEMENTAL_SHARDS.get(), consumer);
        shardsBlockRecipe(ModBlocks.WATER_SHARDS_BLOCK.get(), ModItems.WATER_ELEMENT.get(), consumer);
        shardsBlockRecipe(ModBlocks.EARTH_SHARDS_BLOCK.get(), ModItems.EARTH_ELEMENT.get(), consumer);
        shardsBlockRecipe(ModBlocks.FIRE_SHARDS_BLOCK.get(), ModItems.FIRE_ELEMENT.get(), consumer);
        shardsBlockRecipe(ModBlocks.AIR_SHARDS_BLOCK.get(), ModItems.AIR_ELEMENT.get(), consumer);
    }

    // Elemental Combination recipes
    private void registerModRecipes(Consumer<IFinishedRecipe> consumer) {
        classicElementRecipe(ModItems.AIR_ELEMENT.get(), ModTags.Items.AIR_ELEMENT_CONVERTIBLE, consumer);
        classicElementRecipe(ModItems.FIRE_ELEMENT.get(), ModTags.Items.FIRE_ELEMENT_CONVERTIBLE, consumer);
        classicElementRecipe(ModItems.WATER_ELEMENT.get(), ModTags.Items.WATER_ELEMENT_CONVERTIBLE, consumer);
        classicElementRecipe(ModItems.EARTH_ELEMENT.get(), ModTags.Items.EARTH_ELEMENT_CONVERTIBLE, consumer);
        ElementalCombinationBuilder.create(ModItems.AETHER_ELEMENT.get())
                .addElemental(ModItems.ELEMENTAL_SHARDS.get())
                .addIngredient(ModItems.FIRE_ELEMENT.get())
                .addIngredient(Items.EMERALD)
                .addIngredient(ModItems.AIR_ELEMENT.get())
                .addIngredient(Items.EMERALD)
                .addIngredient(ModItems.WATER_ELEMENT.get())
                .addIngredient(Items.EMERALD)
                .addIngredient(ModItems.EARTH_ELEMENT.get())
                .addIngredient(Items.EMERALD)
                .setCombinationTime(600)
                .build(consumer, modPrefix("elements/" + ModItems.AETHER_ELEMENT.getId().getPath()));

        amuletRecipeTier1(ModItems.FIRE_AMULET.get(), ModItems.FIRE_ELEMENT.get(), consumer);
        amuletRecipeTier1(ModItems.AIR_AMULET.get(), ModItems.AIR_ELEMENT.get(), consumer);
        amuletRecipeTier1(ModItems.WATER_AMULET.get(), ModItems.WATER_ELEMENT.get(), consumer);
        amuletRecipeTier1(ModItems.EARTH_AMULET.get(), ModItems.EARTH_ELEMENT.get(), consumer);

        ElementalCombinationBuilder.create(ModItems.SPEED_AMULET.get().getDefaultInstance())
                .addElemental(ModItems.WATER_AMULET.get().getDefaultInstance())
                .addIngredient(2, Items.ICE)
                .addIngredient(Items.SUGAR)
                .addIngredient(3, Items.ICE)
                .addIngredient(Items.SUGAR)
                .addIngredient(Items.ICE)
                .isTagTransferred()
                .setCombinationTime(350)
                .build(consumer, modPrefix("amulets/" + ModItems.SPEED_AMULET.getId().getPath() + "_tier1"));
        ElementalCombinationBuilder.create(ModItems.JUMP_AMULET.get().getDefaultInstance())
                .addElemental(ModItems.AIR_AMULET.get().getDefaultInstance())
                .addIngredient(2, Items.SLIME_BALL)
                .addIngredient(Items.RABBIT_FOOT)
                .addIngredient(3, Items.SLIME_BALL)
                .addIngredient(Items.RABBIT_FOOT)
                .addIngredient(Items.SLIME_BALL)
                .isTagTransferred()
                .setCombinationTime(350)
                .build(consumer, modPrefix("amulets/" + ModItems.JUMP_AMULET.getId().getPath() + "_tier1"));
        ElementalCombinationBuilder.create(ModItems.INVISIBILITY_AMULET.get())
                .addElemental(ModItems.EMPTY_AMULET.get())
                .addIngredient(2, ModItems.WATER_ELEMENT.get())
                .addIngredient(Items.LAPIS_LAZULI)
                .addIngredient(3, ModItems.FIRE_ELEMENT.get())
                .addIngredient(Items.LAPIS_LAZULI)
                .addIngredient(ModItems.WATER_ELEMENT.get())
                .setCombinationTime(200)
                .isTagTransferred()
                .build(consumer, modPrefix("amulets/" + ModItems.INVISIBILITY_AMULET.getId().getPath()));
        amuletRecipeSpecial(ModItems.TERRA_PROTECTION_AMULET.get(), ModItems.EARTH_AMULET.get().getDefaultInstance(), Items.SHIELD, ModItems.EARTH_ELEMENT.get(), consumer);
        amuletRecipeSpecial(ModItems.PACIFYING_AMULET.get(), ModItems.EMPTY_AMULET.get().getDefaultInstance(), ModItems.ANCIENT_TABLET.get(), ModItems.AETHER_ELEMENT.get(), consumer);
        amuletRecipeSpecial(ModItems.KNOCKBACK_AMULET.get(), ModItems.AIR_AMULET.get().getDefaultInstance(), Items.STONE_SWORD, ModItems.AIR_ELEMENT.get(), consumer);

        amuletRecipeTier2(ModItems.FIRE_AMULET.get(), Items.IRON_INGOT, ModItems.FIRE_ELEMENT.get(), consumer);
        amuletRecipeTier2(ModItems.WATER_AMULET.get(), Items.IRON_INGOT, ModItems.WATER_ELEMENT.get(), consumer);
        amuletRecipeTier2(ModItems.AIR_AMULET.get(), Items.IRON_INGOT, ModItems.AIR_ELEMENT.get(), consumer);
        amuletRecipeTier2(ModItems.EARTH_AMULET.get(), Items.IRON_INGOT, ModItems.EARTH_ELEMENT.get(), consumer);
        amuletRecipeTier2Special(ModItems.SPEED_AMULET.get(), Items.IRON_INGOT, Items.SUGAR, ModItems.WATER_ELEMENT.get(), consumer);
        amuletRecipeTier2(ModItems.TERRA_PROTECTION_AMULET.get(), Items.IRON_INGOT, ModItems.EARTH_ELEMENT.get(), consumer);
        amuletRecipeTier2Special(ModItems.JUMP_AMULET.get(), Items.IRON_INGOT, Items.SLIME_BALL, ModItems.AIR_ELEMENT.get(), consumer);
        amuletRecipeTier2Special(ModItems.KNOCKBACK_AMULET.get(), Items.IRON_SWORD, ModItems.AIR_ELEMENT.get(), ModItems.AIR_ELEMENT.get(), consumer);


        amuletRecipeTier3(ModItems.FIRE_AMULET.get(), Items.GHAST_TEAR, Items.GOLD_INGOT, ModItems.FIRE_ELEMENT.get(), consumer);
        amuletRecipeTier3(ModItems.WATER_AMULET.get(), Items.PUFFERFISH, Items.GOLD_INGOT, ModItems.WATER_ELEMENT.get(), consumer);
        amuletRecipeTier3(ModItems.EARTH_AMULET.get(), Items.BONE_MEAL, Items.GOLD_INGOT, ModItems.EARTH_ELEMENT.get(), consumer);
        amuletRecipeTier3(ModItems.AIR_AMULET.get(), Items.COBWEB, Items.GOLD_INGOT, ModItems.AIR_ELEMENT.get(), consumer);
        amuletRecipeTier3(ModItems.SPEED_AMULET.get(), Items.PACKED_ICE, Items.PACKED_ICE, ModItems.WATER_ELEMENT.get(), consumer);
        amuletRecipeTier3(ModItems.TERRA_PROTECTION_AMULET.get(), Items.OBSIDIAN, Items.DIAMOND, ModItems.EARTH_ELEMENT.get(), consumer);
        amuletRecipeTier3(ModItems.JUMP_AMULET.get(), Items.SLIME_BLOCK, Items.RABBIT_HIDE, ModItems.AIR_ELEMENT.get(), consumer);
        amuletRecipeTier3(ModItems.KNOCKBACK_AMULET.get(), Items.GOLDEN_SWORD, ModItems.EARTH_ELEMENT.get(), ModItems.AIR_ELEMENT.get(), consumer);

        amuletRecipeTier4(ModItems.FIRE_AMULET.get(), ModItems.FIRE_ELEMENT.get(), consumer);
        amuletRecipeTier4(ModItems.WATER_AMULET.get(), ModItems.WATER_ELEMENT.get(), consumer);
        amuletRecipeTier4(ModItems.EARTH_AMULET.get(), ModItems.EARTH_ELEMENT.get(), consumer);
        amuletRecipeTier4(ModItems.AIR_AMULET.get(), ModItems.AIR_ELEMENT.get(), consumer);
        amuletRecipeTier4(ModItems.SPEED_AMULET.get(), ModItems.WATER_ELEMENT.get(), consumer);
        amuletRecipeTier4(ModItems.TERRA_PROTECTION_AMULET.get(), ModItems.EARTH_ELEMENT.get(), consumer);
        amuletRecipeTier4(ModItems.JUMP_AMULET.get(), ModItems.AIR_ELEMENT.get(), consumer);
        amuletRecipeTier4(ModItems.KNOCKBACK_AMULET.get(), ModItems.AIR_ELEMENT.get(), consumer);
    }

    /* Helper Methods! */

    private static void amuletRecipeSpecial(AmuletItem amulet, ItemStack elemental, IItemProvider specialStack, IItemProvider element, Consumer<IFinishedRecipe> consumer) {
        ElementalCombinationBuilder.create(amulet.getDefaultInstance())
                .addElemental(elemental)
                .addIngredient(3, element)
                .addIngredient(Items.LAPIS_LAZULI)
                .addIngredient(element)
                .addIngredient(specialStack)
                .addIngredient(element)
                .addIngredient(Items.LAPIS_LAZULI)
                .setCombinationTime(350)
                .isTagTransferred()
                .build(consumer, modPrefix("amulets/" + amulet.getItem().getRegistryName().getPath() + "_tier1"));
    }

    private static void amuletRecipeTier1(AmuletItem amulet, IItemProvider element, Consumer<IFinishedRecipe> consumer) {
        ElementalCombinationBuilder.create(amulet.getDefaultInstance())
                .addElemental(ModItems.EMPTY_AMULET.get())
                .addIngredient(3, element)
                .addIngredient(Items.LAPIS_LAZULI)
                .addIngredient(3, element)
                .addIngredient(Items.LAPIS_LAZULI)
                .setCombinationTime(200)
                .isTagTransferred()
                .build(consumer, modPrefix("amulets/" + amulet.getItem().getRegistryName().getPath() + "_tier1"));
    }

    private static void amuletRecipeTier2(AmuletItem amulet, IItemProvider upgrader, IItemProvider element, Consumer<IFinishedRecipe> consumer) {
        ElementalCombinationBuilder.create(amulet.withTier(2))
                .addElemental(amulet.getDefaultInstance())
                .addIngredient(element)
                .addIngredient(2, upgrader)
                .addIngredient(element)
                .addIngredient(upgrader)
                .addIngredient(element)
                .addIngredient(2, upgrader)
                .setCombinationTime(400)
                .isTagTransferred()
                .build(consumer, modPrefix("amulets/" + amulet.getRegistryName().getPath() + "_tier2"));
    }

    private static void amuletRecipeTier2Special(AmuletItem amulet, IItemProvider upgrader, IItemProvider upgrader2, IItemProvider element, Consumer<IFinishedRecipe> consumer) {
        ElementalCombinationBuilder.create(amulet.withTier(2))
                .addElemental(amulet.getDefaultInstance())
                .addIngredient(upgrader)
                .addIngredient(element)
                .addIngredient(upgrader2)
                .addIngredient(element)
                .addIngredient(upgrader)
                .addIngredient(element)
                .addIngredient(upgrader2)
                .addIngredient(element)
                .setCombinationTime(400)
                .isTagTransferred()
                .build(consumer, modPrefix("amulets/" + amulet.getRegistryName().getPath() + "_tier2"));
    }

    private static void amuletRecipeTier3(AmuletItem amulet, IItemProvider upgrader, IItemProvider upgrader2, IItemProvider element, Consumer<IFinishedRecipe> consumer) {
        ElementalCombinationBuilder.create(amulet.withTier(3))
                .addElemental(amulet.withTier(2))
                .addIngredient(upgrader)
                .addIngredient(element)
                .addIngredient(upgrader2)
                .addIngredient(element)
                .addIngredient(upgrader)
                .addIngredient(element)
                .addIngredient(upgrader2)
                .addIngredient(element)
                .setCombinationTime(400)
                .isTagTransferred()
                .build(consumer, modPrefix("amulets/" + amulet.getRegistryName().getPath() + "_tier3"));
    }

    private static void amuletRecipeTier4(AmuletItem amulet, IItemProvider upgrader, Consumer<IFinishedRecipe> consumer) {
        ElementalCombinationBuilder.create(amulet.withTier(4))
                .addElemental(amulet.withTier(3))
                .addIngredient(upgrader)
                .addIngredient(ModItems.AETHER_ELEMENT.get())
                .addIngredient(upgrader)
                .addIngredient(ModItems.AETHER_ELEMENT.get())
                .addIngredient(upgrader)
                .addIngredient(ModItems.AETHER_ELEMENT.get())
                .addIngredient(upgrader)
                .addIngredient(ModItems.AETHER_ELEMENT.get())
                .setCombinationTime(800)
                .isTagTransferred()
                .build(consumer, modPrefix("amulets/" + amulet.getRegistryName().getPath() + "_tier4"));
    }

    private static void classicElementRecipe(IItemProvider elementIn, ITag<Item> convertibles, Consumer<IFinishedRecipe> consumerIn) {
        ElementalCombinationBuilder.create(new ItemStack(elementIn, 2))
                .addElemental(ModItems.ELEMENTAL_SHARDS.get())
                .addIngredient(convertibles, ElementalCombination.MAX_INGREDIENTS - 2)
                .build(consumerIn, modPrefix("elements/" + elementIn.asItem().getRegistryName().getPath()));
    }

    private static void oreSmelting(Block oreBlock, Item resultOre, float exp, int cookTime, Consumer<IFinishedRecipe> consumerIn) {
        CookingRecipeBuilder.smelting(Ingredient.of(oreBlock),
                        resultOre, exp, cookTime)
                .unlockedBy("has_item", InventoryChangeTrigger.Instance.hasItems(oreBlock))
                .save(consumerIn, resultOre.getRegistryName());
        CookingRecipeBuilder.blasting(Ingredient.of(oreBlock),
                        resultOre, exp, cookTime / 2)
                .unlockedBy("has_item", InventoryChangeTrigger.Instance.hasItems(oreBlock))
                .save(consumerIn, resultOre.getRegistryName() + "_from_blasting");
    }

    private static void shardsBlockRecipe(Block shardsBlock, Item shardsItem, Consumer<IFinishedRecipe> consumerIn) {
        ShapedRecipeBuilder.shaped(shardsBlock)
                .pattern("***")
                .pattern("***")
                .pattern("***")
                .define('*', shardsItem)
                .unlockedBy("has_item", InventoryChangeTrigger.Instance.hasItems(shardsItem))
                .save(consumerIn);
    }
}
