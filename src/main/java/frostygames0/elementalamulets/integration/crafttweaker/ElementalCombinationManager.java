package frostygames0.elementalamulets.integration.crafttweaker;

import com.blamejared.crafttweaker.CraftTweaker;
import com.blamejared.crafttweaker.api.CraftTweakerAPI;
import com.blamejared.crafttweaker.api.annotations.ZenRegister;
import com.blamejared.crafttweaker.api.item.IIngredient;
import com.blamejared.crafttweaker.api.item.IItemStack;
import com.blamejared.crafttweaker.api.managers.IRecipeManager;
import com.blamejared.crafttweaker.impl.actions.recipes.ActionAddRecipe;
import com.blamejared.crafttweaker.impl.actions.recipes.ActionRemoveRecipeByName;
import com.blamejared.crafttweaker_annotations.annotations.Document;
import frostygames0.elementalamulets.core.init.ModRecipes;
import frostygames0.elementalamulets.recipes.ElementalCombination;
import frostygames0.elementalamulets.recipes.ingredient.AmuletIngredient;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import org.openzen.zencode.java.ZenCodeType;


import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * CraftTweaker integration. Made this because why not?
 * If mod has a lot of compatibilities - +  a lot of downloads
 * @author Frostygames0
 * @date 02.06.2021 18:57
 */
@Document("mods/elementalamulets/ElementalCombination")
@ZenRegister
@ZenCodeType.Name("mods.elementalamulets.ElementalCombination")
public class ElementalCombinationManager implements IRecipeManager {

    /**
     * This method adds a new Elemental Combination recipe.
     * If you remove and сreate recipe with the same name as original one's, it will replace it.
     * If you want to replace recipe see {@link ElementalCombinationManager#replaceRecipe(String, IItemStack, IIngredient[], boolean)}
     *
     * @param name name of recipe
     * @param tagTransfer transfer all nbt data to output
     * @param output what will be crafted
     * @param elemental central slot
     * @param ingredients slots around elemental slot
     *
     * @docParam name "elemental_combination_test"
     * @docParam tagTransfer null
     * @docParam output <item:minecraft:dirt>
     * @docParam elemental <item:elementalamulets:elemental_shards>
     * @docParam ingredients [<item:minecraft:gold_ingot>, <item:minecraft:gold_ingot>]
     */
    @ZenCodeType.Method
    public void addRecipe(String name, IItemStack output, IIngredient[] ingredients, IItemStack elemental, @ZenCodeType.OptionalBoolean boolean tagTransfer) {
        name = fixRecipeName(name);
        ResourceLocation id = new ResourceLocation(CraftTweaker.MODID, name);
        if(ingredients.length > ElementalCombination.MAX_INGREDIENTS) CraftTweakerAPI.logError("Recipe: "+id+" of type: "+this.getRecipeType()+" is incorrect/unobtainable! Elemental Combination has only "+ElementalCombination.MAX_INGREDIENTS+" ingredient slots! You've provided "+ingredients.length); // Prevents people from making unobtainable recipes
        CraftTweakerAPI.apply(new ActionAddRecipe(this,
                new ElementalCombination(id, Arrays.stream(ingredients).map(IIngredient::asVanillaIngredient).collect(Collectors.toCollection(NonNullList::create)),
                        AmuletIngredient.fromStack(elemental.getInternal()), output.getInternal(), 40, tagTransfer)));
    }

    /**
     * This method replaces ingredients of recipe
     * Basically allows you to replace recipe
     * @docParam name "modid:recipe_to_replace"
     * @docParam tagTransfer null
     * @docParam elemental <item:elementalamulets:elemental_shards>
     * @docParam ingredients [<item:minecraft:gold_ingot>, <item:minecraft:gold_ingot>]
     * @see ElementalCombinationManager#addRecipe(String, IItemStack, IIngredient[], IItemStack, boolean);
     */
    @ZenCodeType.Method
    public void replaceRecipe(String name, IItemStack elemental, IIngredient[] ingredients, @ZenCodeType.OptionalBoolean boolean tagTransfer) {
        ResourceLocation resourceLocation = new ResourceLocation(name);
        CraftTweakerAPI.apply(new ActionRemoveRecipeByName(this, resourceLocation));
        this.addRecipe(resourceLocation.getPath(), this.getRecipeByName(name).getOutput(), ingredients, elemental, tagTransfer);
    }

    @Override
    public IRecipeType getRecipeType() {
        return ModRecipes.ELEMENTAL_COMBINATION_TYPE;
    }
}
