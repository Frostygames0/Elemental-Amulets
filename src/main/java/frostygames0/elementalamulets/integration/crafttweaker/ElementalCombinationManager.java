package frostygames0.elementalamulets.integration.crafttweaker;

import com.blamejared.crafttweaker.CraftTweaker;
import com.blamejared.crafttweaker.api.CraftTweakerAPI;
import com.blamejared.crafttweaker.api.annotations.ZenRegister;
import com.blamejared.crafttweaker.api.item.IIngredient;
import com.blamejared.crafttweaker.api.item.IItemStack;
import com.blamejared.crafttweaker.api.managers.IRecipeManager;
import com.blamejared.crafttweaker.impl.actions.recipes.ActionAddRecipe;
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
     * This method adds a new Elemental Combination recipe.
     *
     * @param name name of recipe
     * @param tagTransfer will NBT tag from inserted item move to output stack
     *                    (Use when you upgrade items etc, so their enchants and name are transferred)
     * @param output single slot on the right
     * @param elemental middle slot, accepts NBT tags
     * @param ingredients slots around elemental slot, don't accept NBT tags
     * @param combinationTime how long will it craft
     *
     * @docParam name "elemental_combination_test"
     * @docParam tagTransfer null
     * @docParam output <item:minecraft:dirt>
     * @docParam elemental <item:elementalamulets:elemental_shards>
     * @docParam ingredients [<item:minecraft:gold_ingot>, <item:minecraft:gold_ingot>]
     * @docParam combinationTime {@link ElementalCombination#DEFAULT_COMBINATION}
     */
    @ZenCodeType.Method
    public void addRecipe(String name, IItemStack output, IIngredient[] ingredients, IItemStack elemental, int combinationTime, @ZenCodeType.OptionalBoolean boolean tagTransfer) {
        name = fixRecipeName(name);
        ResourceLocation id = new ResourceLocation(CraftTweaker.MODID, name);
        if(ingredients.length > ElementalCombination.MAX_INGREDIENTS) CraftTweakerAPI.logError("Recipe: "+id+" of type: "+this.getRecipeType()+" is incorrect/unobtainable! Elemental Combination can only accept "+ElementalCombination.MAX_INGREDIENTS+" ingredients! You've provided "+ingredients.length);
        CraftTweakerAPI.apply(new ActionAddRecipe(this,
                new ElementalCombination(id, Arrays.stream(ingredients).map(IIngredient::asVanillaIngredient).collect(Collectors.toCollection(NonNullList::create)),
                        new AmuletIngredient(elemental.getInternal()), output.getInternal(), combinationTime, tagTransfer)));
    }

    @Override
    public IRecipeType getRecipeType() {
        return ModRecipes.ELEMENTAL_COMBINATION_TYPE;
    }
}