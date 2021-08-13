package frostygames0.elementalamulets.items.amulets;

import frostygames0.elementalamulets.config.ModConfig;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

public class JumpAmulet extends AmuletItem {
    public JumpAmulet(Item.Properties properties) {
        super(properties);
    }

    @Override
    protected IFormattableTextComponent getDescription(ItemStack stack, World worldIn) {
        return new TranslationTextComponent("item.elementalamulets.jump_amulet.tooltip");
    }

    public float getJump(ItemStack stack) {
        return (float) (ModConfig.cached.JUMP_AMULET_BOOST *getTier(stack));
    }

    public float getFallResist(ItemStack stack) {
        return getJump(stack)*10;
    }

}
