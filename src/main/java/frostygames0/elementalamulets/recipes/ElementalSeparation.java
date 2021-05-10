package frostygames0.elementalamulets.recipes;

import frostygames0.elementalamulets.core.init.ModBlocks;
import frostygames0.elementalamulets.core.init.ModRecipes;
import net.minecraft.block.ShulkerBoxBlock;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.*;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.util.RecipeMatcher;
import net.minecraftforge.items.wrapper.RecipeWrapper;

import java.util.ArrayList;
import java.util.List;

public class ElementalSeparation implements IRecipe<IInventory> {

    protected final ResourceLocation id;
    protected final NonNullList<Ingredient> ingredients;
    protected final Ingredient elemental;
    protected final ItemStack result;
    protected final int cooldown;
    protected final boolean tagTransfer;

    public ElementalSeparation(ResourceLocation idIn, NonNullList<Ingredient> ingredientsIn, Ingredient elementalIn, ItemStack resultIn,
                               int cooldown, boolean tagTransfer) {
        this.id = idIn;
        this.ingredients = ingredientsIn;
        this.elemental = elementalIn;
        this.result = resultIn;
        this.cooldown = cooldown;
        this.tagTransfer = tagTransfer;
    }

    @Override
    public boolean matches(IInventory inv, World worldIn) {
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
    public ItemStack getCraftingResult(IInventory inv) {
        ItemStack stack = this.result.copy();
        if(this.tagTransfer) {
            CompoundNBT nbt = inv.getStackInSlot(1).getTag();
            if(nbt != null) stack.setTag(nbt);
        }
        return stack;
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
     * Returns list of ingredients+elemental
     * @return NonNullList of ingredients+elemental
     */
    @Override
    public NonNullList<Ingredient> getIngredients() {
        NonNullList<Ingredient> full = NonNullList.create();
        full.add(this.elemental);
        full.addAll(this.ingredients);
        return full;
    }

    /**
     * Returns elemental ingredient separately
     * @return Ingredient of elemental
     */
    public Ingredient getElemental() {
        return this.elemental;
    }

    /**
     * Returns only ingredients!
     * @return Ingredients - elemental
     */
    public NonNullList<Ingredient> getOnlyIngredients() {
        return this.ingredients;
    }

    public int getCooldown() {
        return this.cooldown;
    }

    public boolean isTagTransferred() {
        return this.tagTransfer;
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
