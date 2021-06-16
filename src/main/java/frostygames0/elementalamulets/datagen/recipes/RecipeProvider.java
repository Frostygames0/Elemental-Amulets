package frostygames0.elementalamulets.datagen.recipes;

import frostygames0.elementalamulets.core.init.ModBlocks;
import frostygames0.elementalamulets.core.init.ModItems;
import frostygames0.elementalamulets.core.init.ModTags;
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


public class RecipeProvider extends net.minecraft.data.RecipeProvider {
    public RecipeProvider(DataGenerator generatorIn) {
        super(generatorIn);
    }


    @Override
    protected void registerRecipes(Consumer<IFinishedRecipe> consumer) {
        this.registerVanillaRecipes(consumer);
        this.registerModRecipes(consumer);
    }
    // Vanilla recipes(crafting, smelting, etc.)
    private void registerVanillaRecipes(Consumer<IFinishedRecipe> consumer) {
        ShapedRecipeBuilder.shapedRecipe(ModItems.ELEMENTAL_COMBINATOR_BLOCK.get())
                .patternLine("_=_")
                .patternLine("#4#")
                .patternLine("000")
                .key('_', Items.RED_CARPET)
                .key('=', Items.IRON_INGOT)
                .key('4', ModItems.ELEMENTAL_SHARDS.get())
                .key('0', Tags.Items.COBBLESTONE)
                .key('#', ItemTags.PLANKS)
                .addCriterion("has_item", InventoryChangeTrigger.Instance.forItems(ModItems.ELEMENTAL_SHARDS.get()))
                .build(consumer);
        ShapelessRecipeBuilder.shapelessRecipe(ModItems.GUIDE_BOOK.get())
                .addIngredient(Items.BOOK)
                .addIngredient(ModTags.ELEMENTS)
                .addCriterion("has_item", InventoryChangeTrigger.Instance.forItems(ModItems.ELEMENTAL_SHARDS.get()))
                .build(consumer);
        ShapedRecipeBuilder.shapedRecipe(ModItems.EMPTY_AMULET.get())
                .patternLine(" ##")
                .patternLine("xx#")
                .patternLine("xx ")
                .key('#', Tags.Items.STRING)
                .key('x', ModItems.ELEMENTAL_SHARDS.get())
                .addCriterion("has_item", InventoryChangeTrigger.Instance.forItems(ModItems.ELEMENTAL_SHARDS.get()))
                .build(consumer);
        oreSmelting(ModBlocks.ELEMENTAL_STONE.get(), ModItems.ELEMENTAL_SHARDS.get(), 2, 230, consumer);
    }
    // Elemental Combination recipes
    private void registerModRecipes(Consumer<IFinishedRecipe> consumer) {
        elementRecipe(ModItems.AIR_ELEMENT.get(), ModTags.AIR_ELEMENT_CONVERTIBLE, consumer);
        elementRecipe(ModItems.FIRE_ELEMENT.get(), ModTags.FIRE_ELEMENT_CONVERTIBLE, consumer);
        elementRecipe(ModItems.WATER_ELEMENT.get(), ModTags.WATER_ELEMENT_CONVERTIBLE, consumer);
        elementRecipe(ModItems.EARTH_ELEMENT.get(), ModTags.EARTH_ELEMENT_CONVERTIBLE, consumer);
        ElementalCombinationBuilder.create(AmuletItem.getStackWithTier(new ItemStack(ModItems.FIRE_AMULET.get()), 1))
                .addElemental(ModItems.EMPTY_AMULET.get())
                .addIngredient(8, ModItems.FIRE_ELEMENT.get())
                .setCombinationTime(300)
                .build(consumer);
        ElementalCombinationBuilder.create(AmuletItem.getStackWithTier(new ItemStack(ModItems.JUMP_AMULET.get()), 1))
                .addElemental(ModItems.EMPTY_AMULET.get())
                .addIngredient(8, ModItems.JUMP_ELEMENT.get())
                .setCombinationTime(300)
                .build(consumer);
    }

    /* Helper Methods! */

    private static void oneIngredientRecipe(ItemStack elemental, ITag<Item> tag, ItemStack result, Consumer<IFinishedRecipe> consumer) {
        oneIngredientRecipe(elemental.getItem().getRegistryName().toString(), elemental, tag, result, consumer);
    }

    private static void oneIngredientRecipe(String id, ItemStack elemental, ITag<Item> tag, ItemStack result, Consumer<IFinishedRecipe> supplier) {
        ElementalCombinationBuilder.create(result)
                .addElemental(elemental)
                .addIngredient(tag)
                .build(supplier, id);
    }

    private static void elementRecipe(IItemProvider elementIn, ITag<Item> convertibles, Consumer<IFinishedRecipe> consumerIn) {
        ElementalCombinationBuilder.create(elementIn)
                .addElemental(ModItems.ELEMENTAL_SHARDS.get())
                .addIngredient(convertibles, ElementalCombination.MAX_INGREDIENTS)
                .build(consumerIn, modPrefix("elements/"+elementIn.asItem().getRegistryName().getPath()));
    }

    private static void oreSmelting(Block oreBlock, Item resultOre, int exp, int cookTime, Consumer<IFinishedRecipe> consumerIn) {
        CookingRecipeBuilder.smeltingRecipe(Ingredient.fromItems(oreBlock),
                resultOre, exp, cookTime)
                .addCriterion("has_item", InventoryChangeTrigger.Instance.forItems(oreBlock))
                .build(consumerIn, resultOre.getRegistryName()+"_smelting");
        CookingRecipeBuilder.blastingRecipe(Ingredient.fromItems(oreBlock),
                resultOre, exp, cookTime-50)
                .addCriterion("has_item", InventoryChangeTrigger.Instance.forItems(oreBlock))
                .build(consumerIn, resultOre.getRegistryName()+"_blasting");
    }
}
