package frostygames0.elementalamulets.recipes;

import frostygames0.elementalamulets.core.init.ModBlocks;
import frostygames0.elementalamulets.core.init.ModRecipes;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.*;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.util.RecipeMatcher;
import net.minecraftforge.items.wrapper.RecipeWrapper;

import java.util.ArrayList;
import java.util.List;

public class ElementalSeparation implements IRecipe<RecipeWrapper> {

    protected final ResourceLocation id;
    protected final NonNullList<Ingredient> ingredients;
    protected final Ingredient elemental;
    protected final ItemStack result;

    public ElementalSeparation(ResourceLocation idIn, NonNullList<Ingredient> ingredientsIn, Ingredient elementalIn, ItemStack resultIn) {
        this.id = idIn;
        this.ingredients = ingredientsIn;
        this.elemental = elementalIn;
        this.result = resultIn;
    }

    @Override
    public boolean matches(RecipeWrapper inv, World worldIn) {
        List<ItemStack> inputs = new ArrayList<>();
        int size = 0;
        for(int i = 2; i < inv.getSizeInventory(); ++i) {
            ItemStack stack = inv.getStackInSlot(i);
            if(!stack.isEmpty()) {
                inputs.add(stack);
                ++size;
            }

        }
        return elemental.test(inv.getStackInSlot(1)) && size == this.ingredients.size() && RecipeMatcher.findMatches(inputs, ingredients) != null;
    }

    @Override
    public ItemStack getCraftingResult(RecipeWrapper inv) {
        return this.result.copy();
    }

    @Override
    public boolean canFit(int width, int height) {
        return width*height >= this.ingredients.size();
    }

    @Override
    public ItemStack getRecipeOutput() {
        return this.result;
    }

    /**
     * Returns list of ingredients
     * WARNING! This list does not contain elemental.
     * To get it use {@link this#getElemental()}
     * @return NonNullList of ingredients
     */
    @Override
    public NonNullList<Ingredient> getIngredients() {
        return this.ingredients;
    }

    /**
     * Returns elemental ingredient
     * WARNING! This list does not contain other ingredients
     * To get them use {@link this#getIngredients()}
     * @return Ingredient of elemental
     */
    public Ingredient getElemental() {
        return this.elemental;
    }

    @Override
    public ItemStack getIcon() {
        return new ItemStack(ModBlocks.ELEMENTAL_CRAFTER.get());
    }

    @Override
    public ResourceLocation getId() {
        return this.id;
    }

    @Override
    public IRecipeSerializer<?> getSerializer() {
        return ModRecipes.ELEMENTAL_SEPARATION.get();
    }

    @Override
    public IRecipeType<?> getType() {
        return ModRecipes.ELEMENTAL_SEPARATION_RECIPE;
    }
}
