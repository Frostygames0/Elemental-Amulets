package frostygames0.elementalamulets.items;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class ControlAmulet extends AmuletItem{
    public ControlAmulet(Properties properties) {
        super(properties, 1);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);
        if(Screen.hasShiftDown()) {
            tooltip.add(new StringTextComponent("This amulet allows you to control elemental machines.").mergeStyle(TextFormatting.GRAY));
        }
    }

    @Override
    public int getDamageOnUse() {
        return 1;
    }
}
