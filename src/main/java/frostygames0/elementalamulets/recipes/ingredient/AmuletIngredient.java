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

package frostygames0.elementalamulets.recipes.ingredient;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import frostygames0.elementalamulets.items.amulets.AmuletItem;
import frostygames0.elementalamulets.util.AmuletUtil;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.crafting.IIngredientSerializer;
import net.minecraftforge.registries.ForgeRegistries;


import javax.annotation.Nullable;
import java.util.stream.Stream;

public class AmuletIngredient extends Ingredient {
    private final ItemStack stack;

    public AmuletIngredient(ItemStack stack) {
        super(Stream.of(new Ingredient.ItemValue(stack)));
        this.stack = stack;
    }

    @Override
    public boolean test(@Nullable ItemStack test) {
        if (test == null) return false;
        if (test.getItem() instanceof AmuletItem) {
            return AmuletUtil.compareAmulets(this.stack, test);
        }
        return this.stack.getItem() == test.getItem();
    }

    public ItemStack getMatchingStack() {
        return this.stack;
    }

    @Override
    public JsonElement toJson() {
        JsonObject json = new JsonObject();
        json.addProperty("item", stack.getItem().getRegistryName().toString());
        if (stack.getItem() instanceof AmuletItem item) {
            if (item.hasTier()) {
                json.addProperty("tier", item.getTier(stack));
            }
        }
        return json;
    }

    private static ItemStack getAmuletFromJson(JsonObject json) {
        String itemName = GsonHelper.getAsString(json, "item");

        Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(itemName));
        if (item == null) throw new JsonSyntaxException("Item: " + itemName + " does not exist!");
        if (item instanceof AmuletItem) {
            int tier = GsonHelper.getAsInt(json, "tier", 1);
            if (tier > AmuletItem.MAX_TIER || tier < 0) {
                throw new JsonSyntaxException("Incorrect Tier! Can't be higher than 4 and lower than 0! Your tier is " + tier);
            }
            return AmuletUtil.setStackTier(item, tier);
        }
        return new ItemStack(item, 1);
    }

    @Override
    public IIngredientSerializer<? extends Ingredient> getSerializer() {
        return Serializer.INSTANCE;
    }

    public static class Serializer implements IIngredientSerializer<AmuletIngredient> {
        public static final Serializer INSTANCE = new Serializer();

        @Override
        public AmuletIngredient parse(FriendlyByteBuf buffer) {
            return new AmuletIngredient(buffer.readItem());
        }

        @Override
        public AmuletIngredient parse(JsonObject json) {
            return new AmuletIngredient(getAmuletFromJson(json));
        }

        @Override
        public void write(FriendlyByteBuf buffer, AmuletIngredient ingredient) {
            buffer.writeItem(ingredient.stack);
        }
    }
}

