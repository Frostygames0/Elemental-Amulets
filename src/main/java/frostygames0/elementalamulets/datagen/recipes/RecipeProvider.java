package frostygames0.elementalamulets.datagen.recipes;

import frostygames0.elementalamulets.core.init.ModBlocks;
import frostygames0.elementalamulets.core.init.ModItems;
import frostygames0.elementalamulets.core.init.ModTags;
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
        oreSmelting(ModBlocks.ELEMENTAL_STONE.get(), ModItems.ELEMENTAL_SHARDS.get(), 0.7f, 200, consumer);
    }
    // Elemental Combination recipes
    private void registerModRecipes(Consumer<IFinishedRecipe> consumer) {
        classicElementRecipe(ModItems.AIR_ELEMENT.get(), ModTags.AIR_ELEMENT_CONVERTIBLE, consumer);
        classicElementRecipe(ModItems.FIRE_ELEMENT.get(), ModTags.FIRE_ELEMENT_CONVERTIBLE, consumer);
        classicElementRecipe(ModItems.WATER_ELEMENT.get(), ModTags.WATER_ELEMENT_CONVERTIBLE, consumer);
        classicElementRecipe(ModItems.EARTH_ELEMENT.get(), ModTags.EARTH_ELEMENT_CONVERTIBLE, consumer);

        ElementalCombinationBuilder.create(ModItems.JUMP_ELEMENT.get())
                .addElemental(ModItems.ELEMENTAL_SHARDS.get())
                .addIngredient(ModItems.AIR_ELEMENT.get())
                .addIngredient(2, Items.SLIME_BALL)
                .addIngredient(1, ModItems.FIRE_ELEMENT.get())
                .addIngredient(1, Items.SLIME_BALL)
                .addIngredient(1, ModItems.FIRE_ELEMENT.get())
                .addIngredient(2, Items.SLIME_BALL)
                .build(consumer, modPrefix("elements/"+ModItems.JUMP_ELEMENT.getId().getPath()));
        amuletRecipeTier1(ModItems.JUMP_AMULET.get().getDefaultInstance(), ModItems.JUMP_ELEMENT.get(), consumer);
        amuletRecipeTier1(ModItems.FIRE_AMULET.get().getDefaultInstance(), ModItems.FIRE_ELEMENT.get(), consumer);
        amuletRecipeTier1(ModItems.SPEED_AMULET.get().getDefaultInstance(), ModItems.SPEED_ELEMENT.get(), consumer);
        amuletRecipeTier1(ModItems.INVISIBILITY_AMULET.get().getDefaultInstance(), ModItems.INVISIBLE_ELEMENT.get(), consumer);
    }

    /* Helper Methods! */

    private static void amuletRecipeTier1(ItemStack amulet, IItemProvider element, Consumer<IFinishedRecipe> consumer) {
        ElementalCombinationBuilder.create(amulet)
                .addElemental(ModItems.EMPTY_AMULET.get())
                .addIngredient(3, element)
                .addIngredient(Items.LAPIS_LAZULI)
                .addIngredient(3, element)
                .addIngredient(Items.LAPIS_LAZULI)
                .setCombinationTime(200)
                .isTagTransferred()
                .build(consumer, modPrefix("amulets/"+amulet.getItem().getRegistryName().getPath()+"_1"));
    }

    private static void classicElementRecipe(IItemProvider elementIn, ITag<Item> convertibles, Consumer<IFinishedRecipe> consumerIn) {
        ElementalCombinationBuilder.create(new ItemStack(elementIn, 2))
                .addElemental(ModItems.ELEMENTAL_SHARDS.get())
                .addIngredient(convertibles, ElementalCombination.MAX_INGREDIENTS)
                .build(consumerIn, modPrefix("elements/"+elementIn.asItem().getRegistryName().getPath()));
    }

    private static void oreSmelting(Block oreBlock, Item resultOre, float exp, int cookTime, Consumer<IFinishedRecipe> consumerIn) {
        CookingRecipeBuilder.smeltingRecipe(Ingredient.fromItems(oreBlock),
                resultOre, exp, cookTime)
                .addCriterion("has_item", InventoryChangeTrigger.Instance.forItems(oreBlock))
                .build(consumerIn, resultOre.getRegistryName());
        CookingRecipeBuilder.blastingRecipe(Ingredient.fromItems(oreBlock),
                resultOre, exp, cookTime/2)
                .addCriterion("has_item", InventoryChangeTrigger.Instance.forItems(oreBlock))
                .build(consumerIn, resultOre.getRegistryName()+"_from_blasting");
    }
}
