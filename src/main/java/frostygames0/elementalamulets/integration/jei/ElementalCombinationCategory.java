/*
 *  Copyright (c) 2021-2022
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

package frostygames0.elementalamulets.integration.jei;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.mojang.blaze3d.vertex.PoseStack;
import frostygames0.elementalamulets.client.screens.ElementalCombinatorScreen;
import frostygames0.elementalamulets.init.ModBlocks;
import frostygames0.elementalamulets.recipes.ElementalCombination;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;


import java.util.List;

import static frostygames0.elementalamulets.ElementalAmulets.modPrefix;

public class ElementalCombinationCategory implements IRecipeCategory<ElementalCombination> {
    public static final ResourceLocation ID = modPrefix("elemental_combination");
    private final IDrawable background;
    private final IDrawable icon;
    private final LoadingCache<Integer, IDrawableAnimated> cachedArrows;
    private final Component localizedName;

    public ElementalCombinationCategory(IGuiHelper helper) {
        this.background = helper.drawableBuilder(ElementalCombinatorScreen.GUI, 6, 5, 149, 74)
                .addPadding(0, 10, 0, 0).build();
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM, new ItemStack(ModBlocks.ELEMENTAL_COMBINATOR.get()));
        this.cachedArrows = CacheBuilder.newBuilder()
                .maximumSize(25)
                .build(new CacheLoader<>() {
                    @Override
                    public IDrawableAnimated load(Integer combinationTime) {
                        return helper.drawableBuilder(ElementalCombinatorScreen.GUI, 176, 0, 26, 16)
                                .buildAnimated(combinationTime, IDrawableAnimated.StartDirection.LEFT, false);
                    }
                });
        this.localizedName = new TranslatableComponent("jei.elementalamulets.elemental_separation");
    }

    private IDrawableAnimated getArrow(ElementalCombination recipe) {
        int combinationTime = recipe.getCombinationTime();
        if (combinationTime < 1) {
            combinationTime = ElementalCombination.DEFAULT_COMBINATION;
        }
        return this.cachedArrows.getUnchecked(combinationTime);
    }

    @Override
    public ResourceLocation getUid() {
        return ID;
    }

    @Override
    public Class<? extends ElementalCombination> getRecipeClass() {
        return ElementalCombination.class;
    }

    @Override
    public Component getTitle() {
        return this.localizedName;
    }

    @Override
    public IDrawable getBackground() {
        return background;
    }

    @Override
    public IDrawable getIcon() {
        return icon;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, ElementalCombination recipe, IFocusGroup focuses) {
        var ingredients = recipe.getIngredients();

        var slots = List.of(
                builder.addSlot(RecipeIngredientRole.INPUT, 29, 29),
                builder.addSlot(RecipeIngredientRole.INPUT, 29, 5),
                builder.addSlot(RecipeIngredientRole.INPUT, 49, 9),
                builder.addSlot(RecipeIngredientRole.INPUT, 53, 29),
                builder.addSlot(RecipeIngredientRole.INPUT, 49, 49),
                builder.addSlot(RecipeIngredientRole.INPUT, 29, 53),
                builder.addSlot(RecipeIngredientRole.INPUT, 9, 49),
                builder.addSlot(RecipeIngredientRole.INPUT, 5, 29),
                builder.addSlot(RecipeIngredientRole.INPUT, 9, 9)
        );

        for (int i = 0; i < ingredients.size(); i++) {
            slots.get(i).addIngredients(ingredients.get(i));
        }

        builder.addSlot(RecipeIngredientRole.OUTPUT, 128, 29).addItemStack(recipe.getResultItem());

        builder.setShapeless();
    }

    @Override
    public void draw(ElementalCombination recipe, IRecipeSlotsView recipeSlotsView, PoseStack stack, double mouseX, double mouseY) {
        stack.pushPose();
        // Drawing arrow
        this.getArrow(recipe).draw(stack, 84, 29);

        // Drawing text
        Component cooldown = new TranslatableComponent("jei.elementalamulets.cooldown", recipe.getCombinationTime() / 20.0f);
        Minecraft.getInstance().font.draw(stack, cooldown, 4, 77, 0xFF808080);
        stack.popPose();
    }
}