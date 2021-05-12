package frostygames0.elementalamulets.client.integration.jei;

import frostygames0.elementalamulets.ElementalAmulets;
import frostygames0.elementalamulets.core.init.ModBlocks;
import frostygames0.elementalamulets.core.init.ModItems;
import frostygames0.elementalamulets.core.init.ModRecipes;
import frostygames0.elementalamulets.items.AmuletItem;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.registration.*;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.fml.RegistryObject;

import java.util.List;
import java.util.stream.Collectors;

@JeiPlugin
public class JEIPlugin implements IModPlugin {
    @Override
    public ResourceLocation getPluginUid() {
        return new ResourceLocation(ElementalAmulets.MOD_ID, "jei_plugin");
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(new ElementalCombinationCategory(registration.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.ELEMENTAL_COMBINATOR.get()), ElementalCombinationCategory.CATEGORY_ID);
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {

        //Elemental combination JEI category
        registration.addRecipes(Minecraft.getInstance().world.getRecipeManager().getRecipesForType(ModRecipes.ELEMENTAL_SEPARATION_RECIPE), ElementalCombinationCategory.CATEGORY_ID);

        // Item descriptions
        List<ItemStack> amulets = ModItems.ITEMS.getEntries().stream().map(RegistryObject::get).filter(item -> item instanceof AmuletItem).map(ItemStack::new).collect(Collectors.toList());
        registration.addIngredientInfo(amulets, VanillaTypes.ITEM, new TranslationTextComponent("jei.elementalamulets.amulets.description"));
        registration.addIngredientInfo(new ItemStack(ModItems.GUIDE_BOOK.get()), VanillaTypes.ITEM, new TranslationTextComponent("jei.elementalamulets.guide_book.description"));
        registration.addIngredientInfo(new ItemStack(ModItems.ELEMENTAL_COMBINATOR_BLOCK.get()), VanillaTypes.ITEM, new TranslationTextComponent("jei.elementalamulets.elemental_combinator.description"));
    }
}
