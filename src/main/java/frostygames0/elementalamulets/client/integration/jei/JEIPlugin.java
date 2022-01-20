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

package frostygames0.elementalamulets.client.integration.jei;

import frostygames0.elementalamulets.ElementalAmulets;
import frostygames0.elementalamulets.blocks.menu.ElementalCombinatorMenu;
import frostygames0.elementalamulets.client.screens.ElementalCombinatorScreen;
import frostygames0.elementalamulets.init.ModBlocks;
import frostygames0.elementalamulets.init.ModItems;
import frostygames0.elementalamulets.init.ModRecipes;
import frostygames0.elementalamulets.init.ModTags;
import frostygames0.elementalamulets.items.amulets.AmuletItem;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.registration.*;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.Tag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;


import java.util.List;
import java.util.stream.Collectors;

import static frostygames0.elementalamulets.ElementalAmulets.modPrefix;

@JeiPlugin
public class JEIPlugin implements IModPlugin {
    @Override
    public ResourceLocation getPluginUid() {
        return modPrefix(ElementalAmulets.MOD_ID);
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(new ElementalCombinationCategory(registration.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.ELEMENTAL_COMBINATOR.get()), ElementalCombinationCategory.ID);
    }

    @Override
    public void registerItemSubtypes(ISubtypeRegistration registration) {
        for (AmuletItem item : ModItems.AMULETS.get()) {
            registration.registerSubtypeInterpreter(item, (stack, ctx) -> String.valueOf(item.getTier(stack)));
        }
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {

        // Adding the list of my recipes to jei
        registration.addRecipes(ModRecipes.getRecipes(Minecraft.getInstance().level), ElementalCombinationCategory.ID);

        // Item descriptions
        registration.addIngredientInfo(ModItems.AMULETS.get().stream().map(ItemStack::new).collect(Collectors.toList()), VanillaTypes.ITEM, new TranslatableComponent("jei.elementalamulets.amulets.description"));
        registration.addIngredientInfo(mapTagAsItemList(ModTags.Items.ELEMENTS), VanillaTypes.ITEM, new TranslatableComponent("jei.elementalamulets.elements.description"));
        registration.addIngredientInfo(new ItemStack(ModItems.ALL_SEEING_LENS.get()), VanillaTypes.ITEM, new TranslatableComponent("jei.elementalamulets.all_seeing_lens.description"));

        registration.addIngredientInfo(mapTagAsItemList(ModTags.Items.SHARD_BLOCKS), VanillaTypes.ITEM, new TranslatableComponent("jei.elementalamulets.shards_blocks.description"));
        // Elements description
        registration.addIngredientInfo(mapTagAsItemList(ModTags.Items.AIR_ELEMENT_CONVERTIBLE), VanillaTypes.ITEM, new TranslatableComponent("jei.elementalamulets.convertibles.description", new TranslatableComponent(ModItems.AIR_ELEMENT.get().getDescriptionId()).withStyle(ChatFormatting.GRAY)));
        registration.addIngredientInfo(mapTagAsItemList(ModTags.Items.WATER_ELEMENT_CONVERTIBLE), VanillaTypes.ITEM, new TranslatableComponent("jei.elementalamulets.convertibles.description", new TranslatableComponent(ModItems.WATER_ELEMENT.get().getDescriptionId()).withStyle(ChatFormatting.AQUA)));
        registration.addIngredientInfo(mapTagAsItemList(ModTags.Items.FIRE_ELEMENT_CONVERTIBLE), VanillaTypes.ITEM, new TranslatableComponent("jei.elementalamulets.convertibles.description"), new TranslatableComponent(ModItems.FIRE_ELEMENT.get().getDescriptionId()).withStyle(ChatFormatting.RED));
        registration.addIngredientInfo(mapTagAsItemList(ModTags.Items.EARTH_ELEMENT_CONVERTIBLE), VanillaTypes.ITEM, new TranslatableComponent("jei.elementalamulets.convertibles.description"), new TranslatableComponent(ModItems.EARTH_ELEMENT.get().getDescriptionId()).withStyle(ChatFormatting.DARK_GREEN));
        registration.addIngredientInfo(ModItems.AETHER_ELEMENT.get().getDefaultInstance(), VanillaTypes.ITEM, new TranslatableComponent("jei.elementalamulets.aether_element.description"));
        // Misc. Description
        registration.addIngredientInfo(new ItemStack(ModItems.ELEMENTAL_GUIDE.get()), VanillaTypes.ITEM, new TranslatableComponent("jei.elementalamulets.guide_book.description"));
        registration.addIngredientInfo(new ItemStack(ModBlocks.ELEMENTAL_COMBINATOR.get()), VanillaTypes.ITEM, new TranslatableComponent("jei.elementalamulets.elemental_combinator.description"));
        registration.addIngredientInfo(new ItemStack(ModBlocks.ELEMENTAL_ORE.get()), VanillaTypes.ITEM, new TranslatableComponent("jei.elementalamulets.elemental_ore.whereabouts"));
    }

    private static List<ItemStack> mapTagAsItemList(Tag.Named<Item> tag) {
        return tag.getValues().stream().map(ItemStack::new).collect(Collectors.toList());
    }

    @Override
    public void registerRecipeTransferHandlers(IRecipeTransferRegistration registration) {
        registration.addRecipeTransferHandler(ElementalCombinatorMenu.class, ElementalCombinationCategory.ID, 1, 9, 10, 36);
    }

    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration) {
        registration.addRecipeClickArea(ElementalCombinatorScreen.class, 91, 35, 24, 15, ElementalCombinationCategory.ID);
    }
}
