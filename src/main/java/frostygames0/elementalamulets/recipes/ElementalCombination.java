/*
 *     Copyright (c) 2021
 *
 *     This file is part of Elemental Amulets, a Minecraft Mod.
 *
 *     Elemental Amulets is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Elemental Amulets is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with Elemental Amulets.  If not, see <https://www.gnu.org/licenses/>.
 */

package frostygames0.elementalamulets.recipes;

import frostygames0.elementalamulets.init.ModBlocks;
import frostygames0.elementalamulets.init.ModRecipes;
import frostygames0.elementalamulets.items.amulets.AmuletItem;
import frostygames0.elementalamulets.recipes.ingredient.AmuletIngredient;
import frostygames0.elementalamulets.util.NBTUtil;
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

    public static final int MAX_INGREDIENTS = 8;
    public static final int DEFAULT_COMBINATION = 30;

    protected final ResourceLocation id;
    protected final NonNullList<Ingredient> ingredients;
    protected final AmuletIngredient elemental;
    protected final ItemStack result;
    protected final int combinationTime;
    protected final boolean tagTransfer;

    public ElementalCombination(ResourceLocation idIn, NonNullList<Ingredient> ingredientsIn, AmuletIngredient elementalIn, ItemStack resultIn,
                                int combinationTime, boolean tagTransfer) {
        this.id = idIn;
        this.ingredients = ingredientsIn;
        this.elemental = elementalIn;
        this.result = resultIn;
        this.combinationTime = combinationTime;
        this.tagTransfer = tagTransfer;
    }
    @Override
    public boolean matches(IInventory inv, World worldIn) {
        List<ItemStack> inputs = new ArrayList<>();
        for(int i = 2; i < inv.getContainerSize(); ++i) {
            ItemStack stack = inv.getItem(i);
            if(!stack.isEmpty()) {
                inputs.add(stack);
            }

        }
        return inputs.size() == ingredients.size() && this.elemental.test(inv.getItem(1))
                && RecipeMatcher.findMatches(inputs, ingredients) != null;
    }

    @Override
    public ItemStack assemble(IInventory inv) {
        ItemStack stack = this.result.copy();
        if(this.tagTransfer) {
            CompoundNBT nbt = inv.getItem(1).getTag();
            if(nbt != null) {
                CompoundNBT tag = nbt.copy();
                if (NBTUtil.isSafeToGet(stack, AmuletItem.TIER_TAG))
                    tag.putInt(AmuletItem.TIER_TAG, NBTUtil.getInteger(stack, AmuletItem.TIER_TAG));
                stack.setTag(tag);
            }
        }
        return stack;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    // Need to override this so vanilla's recipe book would ignore my recipes
    @Override
    public boolean isSpecial() {
        return true;
    }

    @Override
    public ItemStack getResultItem() {
        return this.result;
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        NonNullList<Ingredient> full = NonNullList.create();
        full.add(this.elemental);
        full.addAll(this.ingredients);
        return full;
    }

    public AmuletIngredient getElemental() {
        return this.elemental;
    }

    public NonNullList<Ingredient> getOnlyIngredients() {
        return this.ingredients;
    }

    public int getCombinationTime() {
        return this.combinationTime;
    }

    @Override
    public ItemStack getToastSymbol() {
        return new ItemStack(ModBlocks.ELEMENTAL_COMBINATOR.get());
    }

    @Override
    public ResourceLocation getId() {
        return this.id;
    }

    @Override
    public IRecipeSerializer<?> getSerializer() {
        return ModRecipes.ELEMENTAL_COMBINATION.get();
    }

    @Override
    public IRecipeType<?> getType() {
        return ModRecipes.ELEMENTAL_COMBINATION_TYPE;
    }
}
