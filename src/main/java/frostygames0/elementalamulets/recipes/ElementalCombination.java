package frostygames0.elementalamulets.recipes;

import frostygames0.elementalamulets.core.init.ModBlocks;
import frostygames0.elementalamulets.core.init.ModRecipes;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.util.RecipeMatcher;

import java.util.ArrayList;
import java.util.List;

public class ElementalCombination implements IRecipe<IInventory> {

    protected final ResourceLocation id;
    protected final NonNullList<Ingredient> ingredients;
    protected final Ingredient elemental;
    protected final ItemStack result;
    protected final int cooldown;
    protected final boolean tagTransfer;

    public ElementalCombination(ResourceLocation idIn, NonNullList<Ingredient> ingredientsIn, Ingredient elementalIn, ItemStack resultIn,
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
                size++;
            }

        }
        return size == ingredients.size() && this.elemental.test(inv.getStackInSlot(1)) && RecipeMatcher.findMatches(inputs, ingredients) != null; // I don't know exactly if this is correct way but it works fine
    }

    @Override
    public ItemStack getCraftingResult(IInventory inv) {
        ItemStack stack = this.result.copy();
        if(this.tagTransfer) {
            CompoundNBT nbt = inv.getStackInSlot(1).getTag();
            if(nbt != null) {
                stack.setTag(nbt);
            }
        }
        return stack;
    }

    @Override
    public boolean canFit(int width, int height) {
        return true;
    }

    @Override
    public boolean isDynamic() {
        return true;
    }

    @Override
    public ItemStack getRecipeOutput() {
        return this.result;
    }

    /**
     * Returns list of all ingredients (elemental+ingredients)
     * @return NonNullList of elemental+ingredients
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

    /**
     * Returns cooldown
     * @return int cooldown
     */
    public int getCooldown() {
        return this.cooldown;
    }

    /**
     * Can tag be transferred?
     * @return boolean tagTransfer
     */
    public boolean isTagTransferred() {
        return this.tagTransfer;
    }

    @Override
    public ItemStack getIcon() {
        return new ItemStack(ModBlocks.ELEMENTAL_COMBINATOR.get());
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
