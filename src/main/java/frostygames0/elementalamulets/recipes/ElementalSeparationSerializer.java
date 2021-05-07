package frostygames0.elementalamulets.recipes;

import com.google.gson.*;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CookingRecipeSerializer;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapedRecipe;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;

public class ElementalSeparationSerializer<T extends ElementalSeparation> extends net.minecraftforge.registries.ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<T>{
    private final ElementalSeparationSerializer.IFactory<T> factory;
    public ElementalSeparationSerializer(ElementalSeparationSerializer.IFactory<T> factory) {
        this.factory = factory;
    }
    @Override
    public T read(ResourceLocation recipeId, JsonObject json) {
        // Ingredient
        JsonElement jsonelement = JSONUtils.isJsonArray(json, "elemental") ? JSONUtils.getJsonArray(json, "elemental") : JSONUtils.getJsonObject(json, "elemental");
        Ingredient elemental = Ingredient.deserialize(jsonelement);

        NonNullList<Ingredient> nonNullList = readIngredients(JSONUtils.getJsonArray(json, "ingredients"));
        if(nonNullList.isEmpty()) {
            throw new JsonParseException("No ingredients for elemental recipe");
        } else if(nonNullList.size() > 8) {
            throw new JsonParseException("Array is too large! Should be <= 8!");
        } else {
            ItemStack resultStack = ShapedRecipe.deserializeItem(JSONUtils.getJsonObject(json, "result"));
            return this.factory.create(recipeId, nonNullList, elemental,  resultStack);
        }
    }

    private static NonNullList<Ingredient> readIngredients(JsonArray jsonArray) {
            NonNullList<Ingredient> nonnulllist = NonNullList.create();
            for(int i = 0; i < jsonArray.size(); ++i) {
                Ingredient ingredient = Ingredient.deserialize(jsonArray.get(i));
                if (!ingredient.hasNoMatchingItems()) {
                    nonnulllist.add(ingredient);
                }
            }
            return nonnulllist;
    }

    @Nullable
    @Override
    public T read(ResourceLocation recipeId, PacketBuffer buffer) {
        Ingredient elemental = Ingredient.read(buffer);
        int i = buffer.readVarInt();
        NonNullList<Ingredient> nonNullList = NonNullList.withSize(i, Ingredient.EMPTY);
        for(int j = 0; j < nonNullList.size(); ++j) {
            nonNullList.set(j, Ingredient.read(buffer));
        }
        ItemStack resultStack = buffer.readItemStack();
        return this.factory.create(recipeId, nonNullList, elemental, resultStack);
    }

    @Override
    public void write(PacketBuffer buffer, T recipe) {
        recipe.elemental.write(buffer);
        buffer.writeVarInt(recipe.ingredients.size());
        for(Ingredient ingr : recipe.ingredients) {
            ingr.write(buffer);
        }
        buffer.writeItemStack(recipe.result);

    }
    public interface IFactory<T extends ElementalSeparation> {
        T create(ResourceLocation idIn, NonNullList<Ingredient> ingredientsIn, Ingredient elementalIn, ItemStack resultIn);
    }
}
