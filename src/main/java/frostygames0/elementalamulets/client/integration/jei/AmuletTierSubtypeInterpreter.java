package frostygames0.elementalamulets.client.integration.jei;

import frostygames0.elementalamulets.items.amulets.AmuletItem;
import mezz.jei.api.ingredients.subtypes.IIngredientSubtypeInterpreter;
import mezz.jei.api.ingredients.subtypes.UidContext;
import net.minecraft.item.ItemStack;

public class AmuletTierSubtypeInterpreter implements IIngredientSubtypeInterpreter<ItemStack> {
    public static final AmuletTierSubtypeInterpreter INSTANCE = new AmuletTierSubtypeInterpreter();
    @Override
    public String apply(ItemStack ingredient, UidContext context) {
        if(ingredient.hasTag() && ingredient.getTag().contains(AmuletItem.TIER_TAG)) {
            return String.valueOf(ingredient.getTag().getInt(AmuletItem.TIER_TAG));
        }
        return IIngredientSubtypeInterpreter.NONE;
    }
}
