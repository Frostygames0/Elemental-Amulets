package frostygames0.elementalamulets.client.patchouli.processors;

import com.google.gson.JsonSyntaxException;
import frostygames0.elementalamulets.ElementalAmulets;
import frostygames0.elementalamulets.recipes.ElementalCombination;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
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
        IRecipe<?> recipe1 = manager.getRecipe(new ResourceLocation(ElementalAmulets.MOD_ID, recipeId)).orElseThrow(IllegalArgumentException::new);
        if(recipe1 instanceof ElementalCombination) {
            recipe = (ElementalCombination) recipe1;
        } else throw new JsonSyntaxException("Expected to find Elemental Combination recipe!");
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
                //AmuletIngredient ingredient = recipe.getElemental();
                return IVariable.from(recipe.getElemental().getMatchingStack());//IVariable.wrapList(Arrays.stream(ingredient.getMatchingStacks()).map(IVariable::from).collect(Collectors.toList()));
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
