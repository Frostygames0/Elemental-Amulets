package frostygames0.elementalamulets.client.integration.jei;

import com.mojang.blaze3d.matrix.MatrixStack;
import frostygames0.elementalamulets.ElementalAmulets;
import frostygames0.elementalamulets.core.init.ModItems;
import frostygames0.elementalamulets.recipes.ElementalCombination;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IGuiItemStackGroup;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;

public class ElementalCombinationCategory implements IRecipeCategory<ElementalCombination> {
    private static final int OUTPUT_SLOT = 0;
    private static final int ELEMENTAL_SLOT = 1;

    public static final ResourceLocation CATEGORY_ID = new ResourceLocation(ElementalAmulets.MOD_ID, "elemental_combination_category");
    private final IDrawable background;
    private final IDrawable icon;
    private final ITextComponent localizedName;

    public ElementalCombinationCategory(IGuiHelper helper) {
        this.background = helper.drawableBuilder(new ResourceLocation(ElementalAmulets.MOD_ID, "textures/gui/jei/elemental_separation.png"), 0, 0, 147, 70)
        .addPadding(0, 10, 0, 0).build();
        this.icon = helper.createDrawableIngredient(new ItemStack(ModItems.ELEMENTAL_COMBINATOR_BLOCK.get()));
        this.localizedName = new TranslationTextComponent("jei.elementalamulets.elemental_separation");

    }
    @Override
    public ResourceLocation getUid() {
        return CATEGORY_ID;
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
        ingredients.setOutput(VanillaTypes.ITEM, recipe.getRecipeOutput());
    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, ElementalCombination recipe, IIngredients ingredients) {
        IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();
        // Elemental Slot
        guiItemStacks.init(ELEMENTAL_SLOT, true, 26, 26);
        // Ingredient slots
        guiItemStacks.init(2, true, 26, 2);
        guiItemStacks.init(3, true, 46, 6);
        guiItemStacks.init(4, true, 50, 26);
        guiItemStacks.init(5, true, 46, 46);
        guiItemStacks.init(6, true, 26, 50);
        guiItemStacks.init(7, true, 6, 46);
        guiItemStacks.init(8, true, 2, 26);
        guiItemStacks.init(9, true,6, 6);
        // Output slot
        guiItemStacks.init(OUTPUT_SLOT, false, 125, 26);

        guiItemStacks.set(ingredients);

        recipeLayout.setShapeless();
    }

    @Override
    public void draw(ElementalCombination recipe, MatrixStack matrixStack, double mouseX, double mouseY) {
        matrixStack.push();
        ITextComponent cooldown = new TranslationTextComponent("jei.elementalamulets.cooldown", recipe.getCooldown()/20);
        Minecraft.getInstance().fontRenderer.drawTextWithShadow(matrixStack, cooldown, 4, 72, TextFormatting.WHITE.getColor());
        matrixStack.pop();
    }


}
