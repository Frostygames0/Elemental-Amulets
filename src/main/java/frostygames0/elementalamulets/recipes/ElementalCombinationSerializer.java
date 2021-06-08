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

public class ElementalCombinationSerializer extends net.minecraftforge.registries.ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<ElementalCombination>{
    //private final ElementalCombinationSerializer.IFactory<T> factory;
    @Override
    public ElementalCombination read(ResourceLocation recipeId, JsonObject json) {
        // Ingredient
        //JsonElement jsonelement = JSONUtils.isJsonArray(json, "elemental") ? JSONUtils.getJsonArray(json, "elemental") : JSONUtils.getJsonObject(json, "elemental");
        AmuletIngredient elemental = AmuletIngredient.Serializer.INSTANCE.parse(JSONUtils.getJsonObject(json, "elemental"));
        NonNullList<Ingredient> nonNullList = readIngredients(JSONUtils.getJsonArray(json, "ingredients"));
        if(nonNullList.isEmpty()) { // There must be at least 1 ingredient
            throw new JsonParseException("No ingredients found!");
        } else if(nonNullList.size() > ElementalCombination.MAX_INGREDIENTS) { // There must be only 8 or lower ingredients
            throw new JsonParseException("Too many ingredients! Should be <= 8!");
        } else {
            if(!json.has("result")) throw new JsonSyntaxException("Missing result, expected to find an a string or object");
            ItemStack resultStack;
            if(json.get("result").isJsonObject()) resultStack = ShapedRecipe.deserializeItem(JSONUtils.getJsonObject(json, "result"));
            else {
                String s1 = JSONUtils.getString(json, "result");
                ResourceLocation resourcelocation = new ResourceLocation(s1);
                Item item = ForgeRegistries.ITEMS.getValue(resourcelocation);
                if(item == null) throw new JsonSyntaxException("Item: "+s1+" does not exist!");
                resultStack = new ItemStack(item);
            }
            int cooldown = JSONUtils.getInt(json, "cooldown", 40); // How long combinator will recharge
            boolean tagTransfer = JSONUtils.getBoolean(json, "tag_transfer", false); // Does name, damage, enchantments etc move onto result? Made it for "upgrade" recipes
            return new ElementalCombination(recipeId, nonNullList, elemental,  resultStack, cooldown, tagTransfer);
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
    public ElementalCombination read(ResourceLocation recipeId, PacketBuffer buffer) {
        AmuletIngredient elemental = AmuletIngredient.Serializer.INSTANCE.parse(buffer);
        int i = buffer.readVarInt();
        NonNullList<Ingredient> nonNullList = NonNullList.withSize(i, Ingredient.EMPTY);
        for(int j = 0; j < nonNullList.size(); ++j) {
            nonNullList.set(j, Ingredient.read(buffer));
        }
        ItemStack resultStack = buffer.readItemStack();
        int cooldown = buffer.readVarInt();
        boolean tagTransfer = buffer.readBoolean();
        return new ElementalCombination(recipeId, nonNullList, elemental, resultStack, cooldown, tagTransfer);
    }

    @Override
    public void write(PacketBuffer buffer, ElementalCombination recipe) {
        AmuletIngredient.Serializer.INSTANCE.write(buffer, recipe.elemental);
        buffer.writeVarInt(recipe.ingredients.size());
        for(Ingredient ingr : recipe.ingredients) {
            ingr.write(buffer);
        }
        buffer.writeItemStack(recipe.result);
        buffer.writeVarInt(recipe.cooldown);
        buffer.writeBoolean(recipe.tagTransfer);

    }
    /*public interface IFactory<T extends ElementalCombination> {
        T create(ResourceLocation idIn, NonNullList<Ingredient> ingredientsIn, AmuletIngredient elementalIn, ItemStack resultIn, int cooldown, boolean tagTransfer);
    }*/
}
