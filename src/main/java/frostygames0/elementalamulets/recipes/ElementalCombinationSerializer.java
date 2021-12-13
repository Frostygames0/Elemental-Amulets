/*
 *  Copyright (c) 2021
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
import frostygames0.elementalamulets.recipes.ingredient.AmuletIngredient;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapedRecipe;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;


import javax.annotation.Nullable;

public class ElementalCombinationSerializer extends net.minecraftforge.registries.ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<ElementalCombination> {
    @Override
    public ElementalCombination fromJson(ResourceLocation recipeId, JsonObject json) {
        AmuletIngredient elemental = AmuletIngredient.Serializer.INSTANCE.parse(JSONUtils.getAsJsonObject(json, "elemental"));
        NonNullList<Ingredient> nonNullList = readIngredients(JSONUtils.getAsJsonArray(json, "ingredients"));
        if (nonNullList.isEmpty()) {
            throw new JsonParseException("No ingredients found!");
        } else if (nonNullList.size() > ElementalCombination.MAX_INGREDIENTS) {
            throw new JsonParseException("Too many ingredients! Should be <= 8!");
        } else {
            if (!json.has("result"))
                throw new JsonSyntaxException("Missing result, expected to find a string or object");
            ItemStack resultStack;
            if (json.get("result").isJsonObject())
                resultStack = ShapedRecipe.itemFromJson(JSONUtils.getAsJsonObject(json, "result"));
            else {
                String s1 = JSONUtils.getAsString(json, "result");
                ResourceLocation resourcelocation = new ResourceLocation(s1);
                Item item = ForgeRegistries.ITEMS.getValue(resourcelocation);
                if (item == null) throw new JsonSyntaxException("Item: " + s1 + " does not exist!");
                resultStack = new ItemStack(item);
            }
            int combinationTime = JSONUtils.getAsInt(json, "combination_time", ElementalCombination.DEFAULT_COMBINATION); // How long combinator will recharge
            boolean tagTransfer = JSONUtils.getAsBoolean(json, "tag_transfer", false); // Does name, damage, enchantments etc move onto result? Made it for "upgrade" recipes
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
    public ElementalCombination fromNetwork(ResourceLocation recipeId, PacketBuffer buffer) {
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
    public void toNetwork(PacketBuffer buffer, ElementalCombination recipe) {
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
