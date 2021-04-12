package frostygames0.elementalamulets.recipes;

import frostygames0.elementalamulets.core.init.ModBlocks;
import frostygames0.elementalamulets.core.init.ModRecipes;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.*;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.items.wrapper.RecipeWrapper;

public class ElementalSeparation implements IRecipe<RecipeWrapper> {

    private final IRecipeType<?> type;
    final ResourceLocation id;
    final Ingredient ingredient;
    final Ingredient elemental;
    final ItemStack result;

    public ElementalSeparation(ResourceLocation idIn, Ingredient ingredientIn, Ingredient elementalIn, ItemStack resultIn) {
        this.type = ModRecipes.ELEMENTAL_SEPARATION_RECIPE;
        this.id = idIn;
        this.ingredient = ingredientIn;
        this.elemental = elementalIn;
        this.result = resultIn;
    }

    @Override
    public boolean matches(RecipeWrapper inv, World worldIn) {
        return this.ingredient.test(inv.getStackInSlot(0)) && this.elemental.test(inv.getStackInSlot(1));
    }

    @Override
    public ItemStack getCraftingResult(RecipeWrapper inv) {
        return this.result.copy();
    }

    @Override
    public boolean canFit(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getRecipeOutput() {
        return this.result;
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        NonNullList<Ingredient> list = NonNullList.withSize(2, Ingredient.EMPTY);
        list.add(this.ingredient);
        list.add(this.elemental);
        return list;
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
        return this.type;
    }
}
