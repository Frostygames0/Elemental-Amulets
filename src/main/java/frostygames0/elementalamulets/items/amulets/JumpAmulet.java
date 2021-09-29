package frostygames0.elementalamulets.items.amulets;

import frostygames0.elementalamulets.config.ModConfig;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class JumpAmulet extends AmuletItem {
    public JumpAmulet(Item.Properties properties) {
        super(properties);
    }

    public float getJump(ItemStack stack) {
        return (float) (ModConfig.cached.JUMP_AMULET_BOOST *getTier(stack));
    }

    public float getFallResist(ItemStack stack) {
        return getJump(stack)*10;
    }

    @Override
    public boolean usesCurioMethods() {
        return false;
    }
}
