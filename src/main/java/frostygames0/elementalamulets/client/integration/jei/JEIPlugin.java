package frostygames0.elementalamulets.client.integration.jei;

import frostygames0.elementalamulets.ElementalAmulets;
import frostygames0.elementalamulets.blocks.containers.ElementalCombinatorContainer;
import frostygames0.elementalamulets.core.init.ModBlocks;
import frostygames0.elementalamulets.core.init.ModItems;
import frostygames0.elementalamulets.core.init.ModRecipes;
import frostygames0.elementalamulets.core.init.ModTags;
import frostygames0.elementalamulets.items.amulets.AmuletItem;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.registration.*;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;


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
        for(AmuletItem item : ModItems.getAmulets()) {
            registration.registerSubtypeInterpreter(item, (stack, ctx) -> String.valueOf(item.getTier(stack)));
        }
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {

        // Adding list of my recipes to jei
        registration.addRecipes(ModRecipes.getRecipes(Minecraft.getInstance().world), ElementalCombinationCategory.ID);

        // Item descriptions
        registration.addIngredientInfo(ModItems.getAmulets().stream().map(ItemStack::new).collect(Collectors.toList()), VanillaTypes.ITEM, new TranslationTextComponent("jei.elementalamulets.amulets.description"));
        registration.addIngredientInfo(ModTags.ELEMENTS.getAllElements().stream().map(ItemStack::new).collect(Collectors.toList()), VanillaTypes.ITEM, new TranslationTextComponent("jei.elementalamulets.elements.description"));

        // Elements description
        registration.addIngredientInfo(ModTags.AIR_ELEMENT_CONVERTIBLE.getAllElements().stream().map(ItemStack::new).collect(Collectors.toList()), VanillaTypes.ITEM, new TranslationTextComponent("jei.elementalamulets.convertibles.description", new TranslationTextComponent(ModItems.AIR_ELEMENT.get().getTranslationKey()).mergeStyle(TextFormatting.GRAY)));
        registration.addIngredientInfo(ModTags.WATER_ELEMENT_CONVERTIBLE.getAllElements().stream().map(ItemStack::new).collect(Collectors.toList()), VanillaTypes.ITEM, new TranslationTextComponent("jei.elementalamulets.convertibles.description", new TranslationTextComponent(ModItems.WATER_ELEMENT.get().getTranslationKey()).mergeStyle(TextFormatting.AQUA)));
        registration.addIngredientInfo(ModTags.FIRE_ELEMENT_CONVERTIBLE.getAllElements().stream().map(ItemStack::new).collect(Collectors.toList()), VanillaTypes.ITEM, new TranslationTextComponent("jei.elementalamulets.convertibles.description"), new TranslationTextComponent(ModItems.FIRE_ELEMENT.get().getTranslationKey()).mergeStyle(TextFormatting.RED));
        registration.addIngredientInfo(ModTags.EARTH_ELEMENT_CONVERTIBLE.getAllElements().stream().map(ItemStack::new).collect(Collectors.toList()), VanillaTypes.ITEM, new TranslationTextComponent("jei.elementalamulets.convertibles.description"), new TranslationTextComponent(ModItems.EARTH_ELEMENT.get().getTranslationKey()).mergeStyle(TextFormatting.DARK_GREEN));
        registration.addIngredientInfo(ModItems.AETHER_ELEMENT.get().getDefaultInstance(), VanillaTypes.ITEM, new TranslationTextComponent("jei.elementalamulets.aether_element.description"));
        // Misc. Description
        registration.addIngredientInfo(new ItemStack(ModItems.GUIDE_BOOK.get()), VanillaTypes.ITEM, new TranslationTextComponent("jei.elementalamulets.guide_book.description"));
        registration.addIngredientInfo(new ItemStack(ModItems.ELEMENTAL_COMBINATOR_BLOCK.get()), VanillaTypes.ITEM, new TranslationTextComponent("jei.elementalamulets.elemental_combinator.description"));
    }

    @Override
    public void registerRecipeTransferHandlers(IRecipeTransferRegistration registration) {
        registration.addRecipeTransferHandler(ElementalCombinatorContainer.class, ElementalCombinationCategory.ID, 1, 9, 10, 36);
    }
}
