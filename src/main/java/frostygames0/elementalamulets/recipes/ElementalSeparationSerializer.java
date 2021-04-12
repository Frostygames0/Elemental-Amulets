package frostygames0.elementalamulets.recipes;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapedRecipe;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;

public class ElementalSeparationSerializer<T extends ElementalSeparation> extends net.minecraftforge.registries.ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<T>{
    private final ElementalSeparationSerializer.IFactory<T> factory;
    public ElementalSeparationSerializer(ElementalSeparationSerializer.IFactory<T> factory) {
        this.factory = factory;
    }
    @Override
    public T read(ResourceLocation recipeId, JsonObject json) {
        // Ingredient
        JsonElement jsonelement1 = (JsonElement)(JSONUtils.isJsonArray(json, "ingredient") ? JSONUtils.getJsonArray(json, "ingredient") : JSONUtils.getJsonObject(json, "ingredient"));
        Ingredient ingredient = Ingredient.deserialize(jsonelement1);
        // Cookie
        JsonElement jsonelement2 = (JsonElement)(JSONUtils.isJsonArray(json, "elemental") ? JSONUtils.getJsonArray(json, "elemental") : JSONUtils.getJsonObject(json, "elemental"));
        Ingredient elemental = Ingredient.deserialize(jsonelement2);

        ItemStack resultStack;
        if(!json.has("result")) {
            throw new JsonSyntaxException("Result can't be null!");
        }
        else if (json.get("result").isJsonObject()) {
            resultStack = ShapedRecipe.deserializeItem(JSONUtils.getJsonObject(json, "result"));
        } else {
            String resultString = JSONUtils.getString(json, "result");
            ResourceLocation resultRS = new ResourceLocation(resultString);
            resultStack = new ItemStack(ForgeRegistries.ITEMS.getValue(resultRS));
        }
        return this.factory.create(recipeId, ingredient, elemental, resultStack);
    }

    @Nullable
    @Override
    public T read(ResourceLocation recipeId, PacketBuffer buffer) {
        Ingredient ingredient = Ingredient.read(buffer);
        Ingredient elemental = Ingredient.read(buffer);
        ItemStack resultStack = buffer.readItemStack();
        return this.factory.create(recipeId, ingredient, elemental, resultStack);
    }

    @Override
    public void write(PacketBuffer buffer, T recipe) {
        recipe.ingredient.write(buffer);
        recipe.elemental.write(buffer);
        buffer.writeItemStack(recipe.result);

    }
    public interface IFactory<T extends ElementalSeparation> {
        T create(ResourceLocation id, Ingredient ingredient, Ingredient elemental, ItemStack result);
    }
}
