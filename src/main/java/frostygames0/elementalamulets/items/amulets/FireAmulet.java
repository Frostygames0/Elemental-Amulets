package frostygames0.elementalamulets.items.amulets;

import frostygames0.elementalamulets.items.amulets.interfaces.IFireItem;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class FireAmulet extends AmuletItem implements IFireItem {
    public FireAmulet(Properties p_i48487_1_) {
        super(p_i48487_1_);
    }


    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);
        tooltip.add(new TranslationTextComponent("item.elementalamulets.fire_amulet.tooltip").mergeStyle(TextFormatting.GRAY));
    }

    @Override
    public String getTranslationKey() {
        return "item.elementalamulets.fire_amulet";
    }

    @Override
    public float getFireResist(ItemStack stack) {
        return 0.5f*this.getTier(stack);
    }

    @Override
    public float getLavaResist(ItemStack stack) {
        return 0.2f*this.getTier(stack);
    }

}
