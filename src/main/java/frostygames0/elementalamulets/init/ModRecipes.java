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

package frostygames0.elementalamulets.init;

import frostygames0.elementalamulets.ElementalAmulets;
import frostygames0.elementalamulets.recipes.ElementalCombination;
import frostygames0.elementalamulets.recipes.ElementalCombinationSerializer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.RecipeManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;


import java.util.List;
import java.util.Map;

public class ModRecipes {

    public static final IRecipeType<ElementalCombination> ELEMENTAL_COMBINATION_TYPE = IRecipeType.register(ElementalAmulets.MOD_ID+":elemental_combination");

    public static final DeferredRegister<IRecipeSerializer<?>> SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, ElementalAmulets.MOD_ID);

    public static final RegistryObject<IRecipeSerializer<ElementalCombination>> ELEMENTAL_COMBINATION = SERIALIZERS.register("elemental_combination",
            ElementalCombinationSerializer::new);

    public static List<ElementalCombination> getRecipes(World world) {
        return world.getRecipeManager().getAllRecipesFor(ELEMENTAL_COMBINATION_TYPE);
    }

    public static <T extends IRecipe<C>, C extends IInventory> Map<ResourceLocation, T> getRecipesMap(IRecipeType<T> type, World world) {
        Map<IRecipeType<?>, Map<ResourceLocation, T>> recipes = ObfuscationReflectionHelper.getPrivateValue(RecipeManager.class, world.getRecipeManager(),
                "recipes");
        return recipes.get(type);
    }
}
