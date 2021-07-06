package frostygames0.elementalamulets.items.amulets;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

// STUB Class for upcoming item)
public class TerraProtectionAmulet extends AmuletItem {
    public TerraProtectionAmulet(Properties properties) {
        super(properties, true);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);
        tooltip.add(new StringTextComponent("Hey, Listen!!! This amulet is not finished and may not work properly!").mergeStyle(TextFormatting.GOLD));
        tooltip.add(new StringTextComponent("Use it at your own risk").mergeStyle(TextFormatting.RED, TextFormatting.UNDERLINE));
    }

    @Override
    public boolean canRender(String identifier, int index, LivingEntity livingEntity, ItemStack stack) {
        return false;
    }
}
