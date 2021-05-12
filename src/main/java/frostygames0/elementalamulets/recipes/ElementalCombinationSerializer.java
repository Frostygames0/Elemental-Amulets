package frostygames0.elementalamulets.recipes;

import com.google.gson.*;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapedRecipe;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;

public class ElementalCombinationSerializer<T extends ElementalCombination> extends net.minecraftforge.registries.ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<T>{
    private final ElementalCombinationSerializer.IFactory<T> factory;
    public ElementalCombinationSerializer(ElementalCombinationSerializer.IFactory<T> factory) {
        this.factory = factory;
    }
    @Override
    public T read(ResourceLocation recipeId, JsonObject json) {
        // Ingredient
        JsonElement jsonelement = JSONUtils.isJsonArray(json, "elemental") ? JSONUtils.getJsonArray(json, "elemental") : JSONUtils.getJsonObject(json, "elemental");
        Ingredient elemental = Ingredient.deserialize(jsonelement);

        NonNullList<Ingredient> nonNullList = readIngredients(JSONUtils.getJsonArray(json, "ingredients"));
        if(nonNullList.isEmpty()) { // There must be at least 1 ingredient
            throw new JsonParseException("No ingredients found!");
        } else if(nonNullList.size() > 8) { // There must be only 8 or lower ingredients
            throw new JsonParseException("Too many ingredients! Should be <= 8!");
        } else {
            ItemStack resultStack = ShapedRecipe.deserializeItem(JSONUtils.getJsonObject(json, "result")); // Result
            int cooldown = JSONUtils.getInt(json, "cooldown", 40); // How long combinator will recharge
            boolean tagTransfer = JSONUtils.getBoolean(json, "tag_transfer", false); // Does name, damage, enchantments etc move onto result? Made it for "upgrade" recipes
            return this.factory.create(recipeId, nonNullList, elemental,  resultStack, cooldown, tagTransfer);
        }
    }

    /**
     * Reads JsonArray and deserializes each element of it
     * @param jsonArray that is made out of ingredients
     * @return NonNullList made out of ingredients
     */
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
        int cooldown = buffer.readVarInt();
        boolean tagTransfer = buffer.readBoolean();
        return this.factory.create(recipeId, nonNullList, elemental, resultStack, cooldown, tagTransfer);
    }

    @Override
    public void write(PacketBuffer buffer, T recipe) {
        recipe.elemental.write(buffer);
        buffer.writeVarInt(recipe.ingredients.size());
        for(Ingredient ingr : recipe.ingredients) {
            ingr.write(buffer);
        }
        buffer.writeItemStack(recipe.result);
        buffer.writeVarInt(recipe.cooldown);
        buffer.writeBoolean(recipe.tagTransfer);

    }
    public interface IFactory<T extends ElementalCombination> {
        T create(ResourceLocation idIn, NonNullList<Ingredient> ingredientsIn, Ingredient elementalIn, ItemStack resultIn, int cooldown, boolean tagTransfer);
    }
}
