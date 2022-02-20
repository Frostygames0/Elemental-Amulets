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

package frostygames0.elementalamulets.init;

import frostygames0.elementalamulets.ElementalAmulets;
import frostygames0.elementalamulets.mixin.accessors.InvokerRecipeManager;
import frostygames0.elementalamulets.recipes.ElementalCombination;
import frostygames0.elementalamulets.recipes.ingredient.AmuletIngredient;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;


import java.util.Map;

import static frostygames0.elementalamulets.ElementalAmulets.modPrefix;

public class ModRecipes {

    public static final RecipeType<ElementalCombination> ELEMENTAL_COMBINATION_TYPE = RecipeType.register(ElementalAmulets.MOD_ID + ":elemental_combination");

    public static final RegistryObject<RecipeSerializer<ElementalCombination>> ELEMENTAL_COMBINATION = RegistryObject.of(modPrefix("elemental_combination"), ForgeRegistries.RECIPE_SERIALIZERS);

    public static void registerSerializers(RegistryEvent.Register<RecipeSerializer<?>> event) {
        CraftingHelper.register(modPrefix("amulet_ingredient"), AmuletIngredient.Serializer.INSTANCE);

        event.getRegistry().register(new ElementalCombination.Serializer().setRegistryName(modPrefix("elemental_combination")));
    }

    public static <T extends Recipe<C>, C extends Container> Map<ResourceLocation, T> getRecipesMap(RecipeType<T> type, Level world) {
        return ((InvokerRecipeManager) world.getRecipeManager()).callByType(type);
    }
}
