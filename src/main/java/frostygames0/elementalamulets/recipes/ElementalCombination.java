/*
 *  Copyright (c) 2021-2022
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

package frostygames0.elementalamulets.recipes;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import frostygames0.elementalamulets.init.ModBlocks;
import frostygames0.elementalamulets.init.ModRecipes;
import frostygames0.elementalamulets.items.amulets.AmuletItem;
import frostygames0.elementalamulets.recipes.ingredient.AmuletIngredient;
import frostygames0.elementalamulets.util.NBTUtil;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.Container;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;


import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class ElementalCombination implements Recipe<Container> {

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
    public boolean matches(Container inv, Level worldIn) {
        if (ingredients.isEmpty())
            return false;

        List<Ingredient> toFind = new ArrayList<>(ingredients);

        for (int i = 2; i < inv.getContainerSize(); ++i) {
            ItemStack stack = inv.getItem(i);

            if (stack.isEmpty()) continue;

            int index = -1;

            for (int j = 0; j < toFind.size(); j++) {
                if (toFind.get(j).test(stack)) {
                    index = j;
                    break;
                }
            }

            if (index != -1) {
                toFind.remove(index);
            } else
                return false;
        }
        return toFind.isEmpty() && this.elemental.test(inv.getItem(1));
    }


    @Override
    public ItemStack assemble(Container inv) {
        ItemStack stack = this.result.copy();
        if (this.tagTransfer) {
            CompoundTag nbt = inv.getItem(1).getTag();
            if (nbt != null) {
                CompoundTag tag = nbt.copy();
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
    public RecipeSerializer<?> getSerializer() {
        return ModRecipes.ELEMENTAL_COMBINATION.get();
    }

    @Override
    public RecipeType<?> getType() {
        return ModRecipes.ELEMENTAL_COMBINATION_TYPE;
    }

    public static class Serializer extends net.minecraftforge.registries.ForgeRegistryEntry<RecipeSerializer<?>> implements RecipeSerializer<ElementalCombination> {
        @Override
        public ElementalCombination fromJson(ResourceLocation recipeId, JsonObject json) {
            AmuletIngredient elemental = AmuletIngredient.Serializer.INSTANCE.parse(GsonHelper.getAsJsonObject(json, "elemental"));
            NonNullList<Ingredient> nonNullList = readIngredients(GsonHelper.getAsJsonArray(json, "ingredients"));
            if (nonNullList.isEmpty()) {
                throw new JsonParseException("No ingredients found!");
            } else if (nonNullList.size() > ElementalCombination.MAX_INGREDIENTS) {
                throw new JsonParseException("Too many ingredients! Should be <= 8!");
            } else {
                if (!json.has("result"))
                    throw new JsonSyntaxException("Missing result, expected to find a string or object");
                ItemStack resultStack;
                if (json.get("result").isJsonObject())
                    resultStack = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(json, "result"));
                else {
                    String s1 = GsonHelper.getAsString(json, "result");
                    ResourceLocation resourcelocation = new ResourceLocation(s1);
                    Item item = ForgeRegistries.ITEMS.getValue(resourcelocation);
                    if (item == null) throw new JsonSyntaxException("Item: " + s1 + " does not exist!");
                    resultStack = new ItemStack(item);
                }
                int combinationTime = GsonHelper.getAsInt(json, "combination_time", ElementalCombination.DEFAULT_COMBINATION); // How long combinator will recharge
                boolean tagTransfer = GsonHelper.getAsBoolean(json, "tag_transfer", false); // Does name, damage, enchantments etc move onto result? Made it for "upgrade" recipes
                return new ElementalCombination(recipeId, nonNullList, elemental, resultStack, combinationTime, tagTransfer);
            }
        }

        private static NonNullList<Ingredient> readIngredients(JsonArray jsonArray) {
            NonNullList<Ingredient> nonnulllist = NonNullList.create();
            for (int i = 0; i < jsonArray.size(); ++i) {
                Ingredient ingredient = Ingredient.fromJson(jsonArray.get(i));
                if (!ingredient.isEmpty()) {
                    nonnulllist.add(ingredient);
                }
            }
            return nonnulllist;
        }

        @Nullable
        @Override
        public ElementalCombination fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
            AmuletIngredient elemental = AmuletIngredient.Serializer.INSTANCE.parse(buffer);
            int i = buffer.readVarInt();
            NonNullList<Ingredient> nonNullList = NonNullList.withSize(i, Ingredient.EMPTY);
            for (int j = 0; j < nonNullList.size(); ++j) {
                nonNullList.set(j, Ingredient.fromNetwork(buffer));
            }
            ItemStack resultStack = buffer.readItem();
            int combinationTime = buffer.readVarInt();
            boolean tagTransfer = buffer.readBoolean();
            return new ElementalCombination(recipeId, nonNullList, elemental, resultStack, combinationTime, tagTransfer);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, ElementalCombination recipe) {
            AmuletIngredient.Serializer.INSTANCE.write(buffer, recipe.elemental);
            buffer.writeVarInt(recipe.ingredients.size());
            for (Ingredient ingr : recipe.ingredients) {
                ingr.toNetwork(buffer);
            }
            buffer.writeItem(recipe.result);
            buffer.writeVarInt(recipe.combinationTime);
            buffer.writeBoolean(recipe.tagTransfer);

        }
    }
}
