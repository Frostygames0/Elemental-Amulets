package frostygames0.elementalamulets.client.patchouli.processors;

import frostygames0.elementalamulets.ElementalAmulets;
import frostygames0.elementalamulets.core.init.ModRecipes;
import frostygames0.elementalamulets.recipes.ElementalSeparation;
import frostygames0.elementalamulets.recipes.ElementalSeparationSerializer;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.RecipeManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.items.wrapper.RecipeWrapper;
import vazkii.patchouli.api.IComponentProcessor;
import vazkii.patchouli.api.IVariable;
import vazkii.patchouli.api.IVariableProvider;

public class ElementalCombinationProcessor implements IComponentProcessor {
    private ElementalSeparation recipe;
    @Override
    public void setup(IVariableProvider variables) {
        String recipeId = variables.get("recipe").asString();
        RecipeManager manager = Minecraft.getInstance().world.getRecipeManager();
        recipe = (ElementalSeparation) manager.getRecipe(new ResourceLocation(ElementalAmulets.MOD_ID, recipeId)).orElseThrow(IllegalArgumentException::new);
    }

    @Override
    public IVariable process(String key) {
        if (key.startsWith("ingredient")) {
            int index = Integer.parseInt(key.substring(10));
            Ingredient ingredient = recipe.getIngredients().get(index);
            ItemStack[] stacks = ingredient.getMatchingStacks();
            ItemStack stack = stacks.length == 0 ? ItemStack.EMPTY : stacks[0];
            return IVariable.from(stack);
        } else if(key.equals("elemental")) {
            Ingredient elemental = recipe.getElemental();
            ItemStack[] stacks = elemental.getMatchingStacks();
            ItemStack stack = stacks.length == 0 ? ItemStack.EMPTY : stacks[0];
            return IVariable.from(stack);
        } else if (key.equals("result")) {
            return null;
        }
        return null;
    }
}
