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

package frostygames0.elementalamulets.datagen.recipes;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import frostygames0.elementalamulets.ElementalAmulets;
import frostygames0.elementalamulets.init.ModRecipes;
import frostygames0.elementalamulets.recipes.ingredient.AmuletIngredient;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tags.ITag;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;


import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * @author Frostygames0
 * @date 31.05.2021 23:28
 */
public class ElementalCombinationBuilder {
    private final ItemStack result;
    private AmuletIngredient elemental;
    private final List<Ingredient> ingredients = new ArrayList<>();
    private int combinationTime;
    private boolean tagTransfer;

    private ElementalCombinationBuilder(ItemStack item) {
        this.result = item;
    }

    public static ElementalCombinationBuilder create(IItemProvider item) {
        return new ElementalCombinationBuilder(new ItemStack(item.asItem()));
    }

    public static ElementalCombinationBuilder create(ItemStack stack) {
        return new ElementalCombinationBuilder(stack);
    }

    public ElementalCombinationBuilder addElemental(ItemStack stack) {
        this.elemental = new AmuletIngredient(stack);
        return this;
    }

    public ElementalCombinationBuilder addElemental(IItemProvider item) {
        this.addElemental(new ItemStack(item.asItem()));
        return this;
    }

    public ElementalCombinationBuilder addIngredient(ITag<Item> tag, int quantity) {
        for (int i = 0; i < quantity; i++) {
            this.ingredients.add(Ingredient.of(tag));
        }
        return this;
    }

    public ElementalCombinationBuilder addIngredient(ITag<Item> tag) {
        this.addIngredient(tag, 1);
        return this;
    }

    public ElementalCombinationBuilder addIngredient(int quantity, IItemProvider... item) {
        for (int i = 0; i < quantity; i++) {
            this.ingredients.add(Ingredient.of(item));
        }
        return this;
    }

    public ElementalCombinationBuilder addIngredient(IItemProvider... item) {
        this.addIngredient(1, item);
        return this;
    }

    public ElementalCombinationBuilder setCombinationTime(int value) {
        this.combinationTime = value;
        return this;
    }

    public ElementalCombinationBuilder isTagTransferred() {
        this.tagTransfer = true;
        return this;
    }

    public void build(Consumer<IFinishedRecipe> consumerIn) {
        ResourceLocation id = new ResourceLocation(ElementalAmulets.MOD_ID, ForgeRegistries.ITEMS.getKey(this.result.getItem()).getPath()); // Replacing namespace so recipe would go to correct folder
        this.build(consumerIn, id);
    }

    public void build(Consumer<IFinishedRecipe> consumerIn, ResourceLocation id) {
        this.validate();
        consumerIn.accept(new Result(id, elemental, ingredients, result, combinationTime, tagTransfer));
    }

    private void validate() {
        if (this.elemental == null || this.elemental.getMatchingStack().isEmpty())
            throw new IllegalStateException("Elemental cannot be empty!");
        if (this.ingredients.size() > 8)
            throw new IllegalStateException("Elemental combinator has only 8 ingredient slots!");
        if (this.ingredients.isEmpty()) throw new IllegalStateException("Ingredients cannot be empty!");
    }


    public static class Result implements IFinishedRecipe {
        private final ResourceLocation id;
        private final AmuletIngredient elemental;
        private final List<Ingredient> ingredients;
        private final ItemStack result;
        private final int combinationTime;
        private final boolean tagTransfer;

        public Result(ResourceLocation id, AmuletIngredient elemental, List<Ingredient> ingredients, ItemStack result, int combinationTime, boolean tagTransfer) {
            this.id = id;
            this.elemental = elemental;
            this.ingredients = ingredients;
            this.result = result;
            this.combinationTime = combinationTime;
            this.tagTransfer = tagTransfer;
        }

        @Override
        public void serializeRecipeData(JsonObject json) {
            // Elemental
            json.add("elemental", elemental.toJson());

            // Ingredients
            JsonArray array = new JsonArray();
            for (Ingredient ingr : this.ingredients) {
                array.add(ingr.toJson());
            }
            json.add("ingredients", array);

            // Misc settings
            if (this.combinationTime > 0) {
                json.addProperty("combination_time", combinationTime);
            }
            if (tagTransfer) {
                json.addProperty("tag_transfer", tagTransfer);
            }

            // Result
            JsonObject resultJson = new JsonObject();
            resultJson.addProperty("item", result.getItem().getRegistryName().toString());
            if (result.getCount() > 1) resultJson.addProperty("count", result.getCount());
            if (result.hasTag()) {
                CompoundNBT copy = result.getTag().copy();
                if (copy.contains("Damage")) copy.remove("Damage"); // Please don't ask why am I removing damage's tag
                resultJson.addProperty("nbt", copy.toString());
            }
            json.add("result", resultJson);
        }

        @Override
        public ResourceLocation getId() {
            return new ResourceLocation(id.getNamespace(), "elemental_combination/" + id.getPath()); // Makes any recipe go into elemental_combination folder
        }

        @Override
        public IRecipeSerializer<?> getType() {
            return ModRecipes.ELEMENTAL_COMBINATION.get();
        }

        @Nullable
        @Override
        public JsonObject serializeAdvancement() {
            return null;
        }

        @Nullable
        @Override
        public ResourceLocation getAdvancementId() {
            return null;
        }
    }
}
