package frostygames0.elementalamulets.datagen.recipes;

import frostygames0.elementalamulets.core.init.ModItems;
import frostygames0.elementalamulets.core.init.ModTags;
import frostygames0.elementalamulets.recipes.ElementalCombination;
import net.minecraft.advancements.criterion.InventoryChangeTrigger;
import net.minecraft.data.*;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ItemTags;

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
                .key('0', ItemTags.STONE_CRAFTING_MATERIALS)
                .key('#', ItemTags.PLANKS)
                .addCriterion("has_item", InventoryChangeTrigger.Instance.forItems(ModItems.ELEMENTAL_SHARDS.get()))
                .build(consumer);
        ShapelessRecipeBuilder.shapelessRecipe(ModItems.GUIDE_BOOK.get())
                .addIngredient(Items.BOOK)
                .addIngredient(ModTags.ELEMENTS)
                .addCriterion("has_item", InventoryChangeTrigger.Instance.forItems(ModItems.ELEMENTAL_SHARDS.get()))
                .build(consumer);
        CookingRecipeBuilder.cookingRecipe(Ingredient.fromItems(ModItems.ELEMENTAL_STONE.get()),
                ModItems.ELEMENTAL_SHARDS.get(), 1, 120, IRecipeSerializer.SMELTING)
                .addCriterion("has_item", InventoryChangeTrigger.Instance.forItems(ModItems.ELEMENTAL_STONE.get()))
                .build(consumer);
    }
    // Elemental Combination recipes
    private void registerModRecipes(Consumer<IFinishedRecipe> consumer) {
        elementRecipe(ModItems.AIR_ELEMENT.get(), ModTags.AIR_ELEMENT_CONVERTIBLE, consumer);
        elementRecipe(ModItems.FIRE_ELEMENT.get(), ModTags.FIRE_ELEMENT_CONVERTIBLE, consumer);
        elementRecipe(ModItems.WATER_ELEMENT.get(), ModTags.WATER_ELEMENT_CONVERTIBLE, consumer);
        elementRecipe(ModItems.EARTH_ELEMENT.get(), ModTags.EARTH_ELEMENT_CONVERTIBLE, consumer);
        oneIngredientRecipe(modPrefix("guide_book_alternative").toString(), new ItemStack(Items.BOOK), ModTags.ELEMENTS, new ItemStack(ModItems.GUIDE_BOOK.get()), consumer);
    }

    /* Helper Methods! */
    private static void oneIngredientRecipe(String id, ItemStack elemental, ITag<Item> tag, ItemStack result, Consumer<IFinishedRecipe> supplier) {
        ElementalCombinationBuilder.create(result)
                .addElemental(elemental)
                .addIngredient(tag)
                .build(supplier, id);
    }

    private static void elementRecipe(Item elementIn, ITag<Item> convertibles, Consumer<IFinishedRecipe> consumerIn) {
        ElementalCombinationBuilder.create(elementIn)
                .addElemental(ModItems.ELEMENTAL_SHARDS.get())
                .addIngredient(convertibles, ElementalCombination.MAX_INGREDIENTS)
                .build(consumerIn, modPrefix("elements/"+elementIn.getItem().getRegistryName().getPath()));
    }
}
