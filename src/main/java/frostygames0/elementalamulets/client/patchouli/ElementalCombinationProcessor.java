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

package frostygames0.elementalamulets.client.patchouli;

import frostygames0.elementalamulets.recipes.ElementalCombination;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
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
        recipe = PatchouliUtils.getRecipe(new ResourceLocation(recipeId));
    }

    @Override
    public IVariable process(String key) {
        if (recipe != null) {
            if (key.startsWith("item")) {
                int index = Integer.parseInt(key.substring(4)) - 1;
                if (!(index >= recipe.getOnlyIngredients().size())) {
                    Ingredient ingredient = recipe.getOnlyIngredients().get(index);
                    return IVariable.wrapList(Arrays.stream(ingredient.getItems()).map(IVariable::from).collect(Collectors.toList()));
                }
                return IVariable.from(ItemStack.EMPTY);
            } else if (key.equals("elemental")) {
                return IVariable.from(recipe.getElemental().getMatchingStack());
            } else if (key.equals("result")) {
                return IVariable.from(recipe.getResultItem());
            } else if (key.equals("icon")) {
                return IVariable.from(recipe.getToastSymbol());
            } else if (key.equals("combination_time")) {
                return IVariable.wrap(recipe.getCombinationTime() / 20);
            }
        }
        return null;
    }
}
