package frostygames0.elementalamulets.client.integration.jei;

import frostygames0.elementalamulets.core.util.NBTUtil;
import frostygames0.elementalamulets.items.amulets.AmuletItem;
import mezz.jei.api.ingredients.subtypes.IIngredientSubtypeInterpreter;
import mezz.jei.api.ingredients.subtypes.UidContext;
import net.minecraft.item.ItemStack;

public class AmuletTierSubtypeInterpreter implements IIngredientSubtypeInterpreter<ItemStack> {
    public static final AmuletTierSubtypeInterpreter INSTANCE = new AmuletTierSubtypeInterpreter();
    @Override
    public String apply(ItemStack ingredient, UidContext context) {
        return String.valueOf(NBTUtil.getInteger(ingredient, AmuletItem.TIER_TAG));
    }
}
