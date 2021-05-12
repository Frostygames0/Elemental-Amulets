package frostygames0.elementalamulets.client.patchouli.processors;

import frostygames0.elementalamulets.ElementalAmulets;
import frostygames0.elementalamulets.recipes.ElementalCombination;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.RecipeManager;
import net.minecraft.util.ResourceLocation;
import vazkii.patchouli.api.IComponentProcessor;
import vazkii.patchouli.api.IVariable;
import vazkii.patchouli.api.IVariableProvider;

import java.util.Arrays;
import java.util.stream.Collectors;

public class ElementalCombinationProcessor implements IComponentProcessor {
    private ElementalCombination recipe;
    @Override
    public void setup(IVariableProvider variables) {
        String recipeId = variables.get("recipe").asString();
        RecipeManager manager = Minecraft.getInstance().world.getRecipeManager();
        recipe = (ElementalCombination) manager.getRecipe(new ResourceLocation(ElementalAmulets.MOD_ID, recipeId)).orElseThrow(IllegalArgumentException::new);
    }

    @Override
    public IVariable process(String key) {
        if(recipe != null) {
            if (key.startsWith("item")) {
                int index = Integer.parseInt(key.substring(4)) - 1;
                if(!(index >= recipe.getOnlyIngredients().size())) {
                    Ingredient ingredient = recipe.getOnlyIngredients().get(index);
                    return IVariable.wrapList(Arrays.stream(ingredient.getMatchingStacks()).map(IVariable::from).collect(Collectors.toList()));
                }
                return IVariable.from(ItemStack.EMPTY);
            } else if (key.equals("elemental")) {
                Ingredient ingredient = recipe.getElemental();
                return IVariable.wrapList(Arrays.stream(ingredient.getMatchingStacks()).map(IVariable::from).collect(Collectors.toList()));
            } else if(key.equals("result")) {
                return IVariable.from(recipe.getRecipeOutput());
            } else if(key.equals("icon")) {
                return IVariable.from(recipe.getIcon());
            } else if(key.equals("cooldown")) {
                return IVariable.from(recipe.getCooldown());
            }
        }
        return null;
    }
}
