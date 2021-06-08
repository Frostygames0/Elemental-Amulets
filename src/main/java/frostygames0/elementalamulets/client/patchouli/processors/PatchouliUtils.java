package frostygames0.elementalamulets.client.patchouli.processors;

import frostygames0.elementalamulets.ElementalAmulets;
import frostygames0.elementalamulets.core.init.ModRecipes;
import net.minecraft.client.Minecraft;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.ModList;


import java.util.Map;

/**
 * @author Frostygames0
 * @date 02.06.2021 18:24
 */
public class PatchouliUtils {
    private static boolean userNotified;

    /**
     * Since removal of any recipe that mentioned in Patchouli book by CraftTweaker will result in error!
     * I made this helper method that will try to find CraftTweaker replacement in case when recipe mysteriously removed
     *
     * If there is no replacement, it will be logged;
     * P.S: and yes I looked how botania does it :p Also use only for patchouli related stuff!
     */
    public static <T extends IRecipe<C>, C extends IInventory> T getRecipe(IRecipeType<T> recipeType, ResourceLocation name) {
        Map<ResourceLocation, T> recipes = ModRecipes.getRecipesMap(recipeType, Minecraft.getInstance().world);
        T defaultRecipe = recipes.get(name);
        if(defaultRecipe != null) return defaultRecipe; //
        T craftTweakedRecipe = recipes.get(new ResourceLocation("crafttweaker", name.getPath()));
        if(craftTweakedRecipe != null) return craftTweakedRecipe;

        ElementalAmulets.LOGGER.warn("Template references nonexistent recipe {} of {}", name, recipeType);

        if(!userNotified) {
            userNotified = true;
            if(ModList.get().isLoaded("crafttweaker")) {
                ElementalAmulets.LOGGER.info("CraftTweaker detected! Seems that you could deleted {} recipe and didn't add a replacement for it!\n"+
                        "To add replacement for it, simply add a new recipe with the same path as deleted recipe.", name.toString());
            }
        }
        return null;
    }
}
