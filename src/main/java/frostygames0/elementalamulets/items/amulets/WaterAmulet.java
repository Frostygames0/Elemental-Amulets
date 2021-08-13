package frostygames0.elementalamulets.items.amulets;

import net.minecraft.item.ItemStack;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

public class WaterAmulet extends AmuletItem {
    public WaterAmulet(Properties properties) {
        super(properties, true);
    }

    @Override
    protected IFormattableTextComponent getDescription(ItemStack stack, World worldIn) {
        return new TranslationTextComponent(this.getDefaultTranslationKey()+".tooltip");
    }
}
