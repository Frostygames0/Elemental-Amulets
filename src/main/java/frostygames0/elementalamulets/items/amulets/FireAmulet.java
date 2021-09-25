package frostygames0.elementalamulets.items.amulets;

import frostygames0.elementalamulets.config.ModConfig;
import net.minecraft.item.ItemStack;

public class FireAmulet extends AmuletItem {
    public FireAmulet(Properties p_i48487_1_) {
        super(p_i48487_1_);
    }

    public float getFireResist(ItemStack stack) {
        return (float) (ModConfig.cached.FIRE_AMULET_FIRE_RESISTANCE * this.getTier(stack));
    }

    public float getLavaResist(ItemStack stack) {
        return (float) (ModConfig.cached.FIRE_AMULET_LAVA_RESISTANCE * this.getTier(stack));
    }

    @Override
    public boolean hasSpecialEffect() {
        return true;
    }
}
