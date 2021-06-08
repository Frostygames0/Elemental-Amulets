package frostygames0.elementalamulets.items.amulets;

import frostygames0.elementalamulets.items.amulets.interfaces.IJumpItem;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;


import javax.annotation.Nullable;
import java.util.List;

public class JumpAmulet extends AmuletItem implements IJumpItem {
    public JumpAmulet(Item.Properties properties) {
        super(properties);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);
        tooltip.add(new TranslationTextComponent("item.elementalamulets.jump_amulet.tooltip").mergeStyle(TextFormatting.GRAY));
    }

    @Override
    public String getTranslationKey() {
        return "item.elementalamulets.jump_amulet";
    }

    @Override
    public float getJump(ItemStack stack) {
        return 0.3f*getTier(stack);
    }

    @Override
    public float getFallResist(ItemStack stack) {
        return getJump(stack)*10;
    }

}
