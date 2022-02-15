/*
 *  Copyright (c) 2021
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

package frostygames0.elementalamulets.client.patchouli;

import frostygames0.elementalamulets.ElementalAmulets;
import frostygames0.elementalamulets.init.ModRecipes;
import frostygames0.elementalamulets.recipes.ElementalCombination;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.ModList;


import javax.annotation.Nullable;
import java.util.Map;

/**
 * @author Frostygames0
 * @date 02.06.2021 18:24
 */
public class PatchouliUtils {
    private static boolean playerWarned;

    private static final String CRAFTTWEAKER = "crafttweaker";

    /*
     * Since removal of any recipe that mentioned in Patchouli book by CraftTweaker will result in error!
     * I made this helper method that will try to find CraftTweaker replacement in case when recipe mysteriously removed
     * If there is no replacement, it will be logged;
     * P.S: and yes I looked how botania does it :p Also use only for patchouli related stuff!
     */
    @Nullable
    public static ElementalCombination getRecipe(ResourceLocation name) {
        Map<ResourceLocation, ElementalCombination> recipes = ModRecipes.getRecipesMap(ModRecipes.ELEMENTAL_COMBINATION_TYPE, Minecraft.getInstance().level);

        ElementalCombination recipe = recipes.get(name);
        if (recipe != null) return recipe;
        recipe = recipes.get(craftTweaker(name.getPath()));
        if (recipe != null) return recipe;
        recipe = recipes.get(craftTweaker("autogenerated/" + name.getPath()));
        if (recipe != null) return recipe;

        ElementalAmulets.LOGGER.warn("Elemental Guide's template references nonexistent recipe {} of {}", name, ModRecipes.ELEMENTAL_COMBINATION_TYPE);

        if (!playerWarned) {
            playerWarned = true;
            if (ModList.get().isLoaded(CRAFTTWEAKER)) {
                ElementalAmulets.LOGGER.warn("CraftTweaker detected! It looks like you deleted the recipe {} and didn't add a replacement for it!\n" +
                        " To add a replacement for it, just add a new recipe with the same path as the deleted recipe.", name.toString());
            }
        }
        return null;
    }

    private static ResourceLocation craftTweaker(String path) {
        return new ResourceLocation(CRAFTTWEAKER, path);
    }
}
