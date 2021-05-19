package frostygames0.elementalamulets.datagen;

import frostygames0.elementalamulets.ElementalAmulets;
import frostygames0.elementalamulets.core.init.ModItems;
import net.minecraft.advancements.criterion.InventoryChangeTrigger;
import net.minecraft.data.*;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tags.ItemTags;

import java.util.function.Consumer;

public class RecipeProvider extends net.minecraft.data.RecipeProvider {
    public RecipeProvider(DataGenerator generatorIn) {
        super(generatorIn);
    }

    @Override
    protected void registerRecipes(Consumer<IFinishedRecipe> consumer) {
        ShapedRecipeBuilder.shapedRecipe(ModItems.ELEMENTAL_COMBINATOR_BLOCK.get())
                .patternLine("_=_")
                .patternLine("#4#")
                .patternLine("000")
                .key('_', Items.RED_CARPET)
                .key('=', Items.IRON_INGOT)
                .key('4', ModItems.ELEMENTAL_SHARDS.get())
                .key('0', ItemTags.STONE_CRAFTING_MATERIALS)
                .key('#', ItemTags.PLANKS)
                .addCriterion(ElementalAmulets.MOD_ID+"elemental_shards", InventoryChangeTrigger.Instance.forItems(ModItems.ELEMENTAL_SHARDS.get()))
                .build(consumer);
        ShapelessRecipeBuilder.shapelessRecipe(ModItems.GUIDE_BOOK.get())
                .addIngredient(Items.BOOK)
                .addIngredient(ModItems.ELEMENTAL_SHARDS.get())
                .addCriterion(ElementalAmulets.MOD_ID+"elemental_shards", InventoryChangeTrigger.Instance.forItems(ModItems.ELEMENTAL_SHARDS.get()))
                .build(consumer);
        CookingRecipeBuilder.cookingRecipe(Ingredient.fromItems(ModItems.ELEMENTAL_STONE.get()),
                ModItems.ELEMENTAL_SHARDS.get(), 1, 120, IRecipeSerializer.SMELTING)
                .addCriterion(ElementalAmulets.MOD_ID+"elemental_ore", InventoryChangeTrigger.Instance.forItems(ModItems.ELEMENTAL_STONE.get()))
                .build(consumer);
    }
}
