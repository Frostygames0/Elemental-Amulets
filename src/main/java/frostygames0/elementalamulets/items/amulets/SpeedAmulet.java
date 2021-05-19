package frostygames0.elementalamulets.items.amulets;

import frostygames0.elementalamulets.config.ModConfig;
import frostygames0.elementalamulets.items.amulets.interfaces.ISpeedItem;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class SpeedAmulet extends AmuletItem implements ISpeedItem {
    public SpeedAmulet(Properties properties, int tier) {
        super(properties, tier);
    }


    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);
        tooltip.add(new TranslationTextComponent("item.elementalamulets.speed_amulet.tooltip").mergeStyle(TextFormatting.GRAY));
        tooltip.add(new StringTextComponent("NOT WORKING!").mergeStyle(TextFormatting.RED));
    }

    @Override
    public String getTranslationKey() {
        return "item.elementalamulets.speed_amulet";
    }

    @Override
    public float getSpeed() {
        return 1.25f*this.getTier();
    }

    @Override
    public int getDamageOnUse() {
        return ModConfig.cached.SPEED_AMULET_USAGE_DMG*getTier();
    }

}
