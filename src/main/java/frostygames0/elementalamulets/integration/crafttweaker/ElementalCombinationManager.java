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
 * @author Frostygames0
 * @date 02.06.2021 18:57
 */

@Document("mods/elementalamulets/ElementalCombination")
@ZenRegister
@ZenCodeType.Name("mods.elementalamulets.ElementalCombination")
public class ElementalCombinationManager implements IRecipeManager {

    /**
     * Adds Elemental Combination recipe!
     * @param name name of recipe
     * @param cooldown how long will combinator charge
     * @param tagTransfer transfer all nbt data to output
     * @param output what will be crafted
     * @param elemental central slot
     * @param ingredients slots around elemental slot
     *
     * @docParam name "elemental_combination_test"
     * @docParam cooldown 40
     * @docParam tagTransfer false
     * @docParam output <item:minecraft:dirt>
     * @docParam elemental <item:elementalamulets:elemental_shards>
     * @docParam ingredients <item:minecraft:gold_ingot>, <item:minecraft:gold_ingot>
     */
    @ZenCodeType.Method
    public void addRecipe(String name, int cooldown, boolean tagTransfer, IItemStack output, IItemStack elemental, IIngredient... ingredients) {
        name = fixRecipeName(name);
        ResourceLocation id = new ResourceLocation(CraftTweaker.MODID, name);
        if(ingredients.length > ElementalCombination.MAX_INGREDIENTS) CraftTweakerAPI.logError("Recipe: "+id+" of type: "+this.getRecipeType()+" is incorrect/unobtainable! Elemental Combination has only "+ElementalCombination.MAX_INGREDIENTS+" ingredient slots! You've provided "+ingredients.length); // Prevents people from making unobtainable recipes
        CraftTweakerAPI.apply(new ActionAddRecipe(this,
                new ElementalCombination(id, Arrays.stream(ingredients).map(IIngredient::asVanillaIngredient).collect(Collectors.toCollection(NonNullList::create)),
                        AmuletIngredient.fromStack(elemental.getInternal()), output.getInternal(), cooldown, tagTransfer)));
    }

    /**
     * Replaces Elemental Combination recipe with provided name with your recipe
     * If recipe is not Elemental Combination it will not replaced but added
     * @docParam name "modid:recipe_to_replace"
     * @docParam cooldown 40
     * @docParam tagTransfer false
     * @docParam output <item:minecraft:dirt>
     * @docParam elemental <item:elementalamulets:elemental_shards>
     * @docParam ingredients <item:minecraft:gold_ingot>, <item:minecraft:gold_ingot>
     * @see ElementalCombinationManager#addRecipe(String, int, boolean, IItemStack, IItemStack, IIngredient...) ;
     */
    @ZenCodeType.Method
    public void replaceRecipe(String name, int cooldown, boolean tagTransfer, IItemStack output, IItemStack elemental, IIngredient... ingredients) {
        ResourceLocation resourceLocation = new ResourceLocation(name);
        CraftTweakerAPI.apply(new ActionRemoveRecipeByName(this, resourceLocation));
        this.addRecipe(resourceLocation.getPath(), cooldown, tagTransfer, output, elemental, ingredients);
    }

    @Override
    public IRecipeType getRecipeType() {
        return ModRecipes.ELEMENTAL_COMBINATION_TYPE;
    }
}
