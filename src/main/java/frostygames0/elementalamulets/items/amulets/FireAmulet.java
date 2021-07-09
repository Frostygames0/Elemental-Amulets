package frostygames0.elementalamulets.items.amulets;

import frostygames0.elementalamulets.config.ModConfig;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

public class FireAmulet extends AmuletItem {
    public FireAmulet(Properties p_i48487_1_) {
        super(p_i48487_1_);
    }

    @Override
    protected IFormattableTextComponent getDescription(ItemStack stack, World worldIn) {
        return new TranslationTextComponent("item.elementalamulets.fire_amulet.tooltip");
    }

    public float getFireResist(ItemStack stack) {
        return (float) (ModConfig.cached.FIRE_AMULET_FIRE_RESISTANCE * this.getTier(stack));
    }

    public float getLavaResist(ItemStack stack) {
        return (float) (ModConfig.cached.FIRE_AMULET_LAVA_RESISTANCE * this.getTier(stack));
    }
}
