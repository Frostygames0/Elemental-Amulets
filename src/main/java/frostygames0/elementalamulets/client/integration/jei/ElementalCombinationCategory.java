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

package frostygames0.elementalamulets.client.integration.jei;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.mojang.blaze3d.matrix.MatrixStack;
import frostygames0.elementalamulets.client.screens.ElementalCombinatorScreen;
import frostygames0.elementalamulets.init.ModBlocks;
import frostygames0.elementalamulets.recipes.ElementalCombination;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
import mezz.jei.api.gui.ingredient.IGuiItemStackGroup;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;


import java.util.Collections;
import java.util.List;

import static frostygames0.elementalamulets.ElementalAmulets.modPrefix;

public class ElementalCombinationCategory implements IRecipeCategory<ElementalCombination> {
    private static final int OUTPUT_SLOT = 0;
    private static final int ELEMENTAL_SLOT = 1;

    public static final ResourceLocation ID = modPrefix("elemental_combination");
    private final IDrawable background;
    private final IDrawable icon;
    private final LoadingCache<Integer, IDrawableAnimated> cachedArrows;
    private final ITextComponent localizedName;

    public ElementalCombinationCategory(IGuiHelper helper) {
        this.background = helper.drawableBuilder(ElementalCombinatorScreen.GUI, 6, 5, 149, 74)
        .addPadding(0, 10, 0, 0).build();
        this.icon = helper.createDrawableIngredient(new ItemStack(ModBlocks.ELEMENTAL_COMBINATOR.get()));
        this.cachedArrows = CacheBuilder.newBuilder()
                .maximumSize(25)
                .build(new CacheLoader<Integer, IDrawableAnimated>() {
                    @Override
                    public IDrawableAnimated load(Integer combinationTime) {
                        return helper.drawableBuilder(ElementalCombinatorScreen.GUI, 176, 0, 26, 16)
                                .buildAnimated(combinationTime, IDrawableAnimated.StartDirection.LEFT, false);
                    }
                });
        this.localizedName = new TranslationTextComponent("jei.elementalamulets.elemental_separation");
    }

    private IDrawableAnimated getArrow(ElementalCombination recipe) {
        int combinationTime = recipe.getCombinationTime();
        if(combinationTime < 1) {
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
    public String getTitle() {
        return this.localizedName.getString();
    }

    @Override
    public ITextComponent getTitleAsTextComponent() {
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
    public void setIngredients(ElementalCombination recipe, IIngredients ingredients) {
        ingredients.setInputIngredients(recipe.getIngredients());
        ingredients.setOutput(VanillaTypes.ITEM, recipe.getResultItem());
    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, ElementalCombination recipe, IIngredients ingredients) {
        IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();
        // Elemental Slot
        guiItemStacks.init(ELEMENTAL_SLOT, true, 28, 28);
        // Ingredient slots
        guiItemStacks.init(2, true, 28, 4);
        guiItemStacks.init(3, true, 48, 8);
        guiItemStacks.init(4, true, 52, 28);
        guiItemStacks.init(5, true, 48, 48);
        guiItemStacks.init(6, true, 28, 52);
        guiItemStacks.init(7, true, 8, 48);
        guiItemStacks.init(8, true, 4, 28);
        guiItemStacks.init(9, true,8, 8);
        // Output slot
        guiItemStacks.init(OUTPUT_SLOT, false, 127, 28);

        guiItemStacks.set(ingredients);

        recipeLayout.setShapeless();
    }

    @Override
    public void draw(ElementalCombination recipe, MatrixStack matrixStack, double mouseX, double mouseY) {
        matrixStack.pushPose();
        // Drawing arrow
        this.getArrow(recipe).draw(matrixStack, 84, 29);

        // Drawing text
        ITextComponent cooldown = new TranslationTextComponent("jei.elementalamulets.cooldown", recipe.getCombinationTime()/20.0f);
        Minecraft.getInstance().font.draw(matrixStack, cooldown, 4, 77, 0xFF808080);
        matrixStack.popPose();
    }

    @Override
    public List<ITextComponent> getTooltipStrings(ElementalCombination recipe, double mouseX, double mouseY) {
        if(mouseX >= 139 && mouseX <= 148 && mouseY <= 5 && mouseY >= 0) {
            return Collections.singletonList(new TranslationTextComponent("jei.elementalamulets.shapeless"));
        }
        return Collections.emptyList();
    }
}