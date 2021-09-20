package frostygames0.elementalamulets.items;

import frostygames0.elementalamulets.ElementalAmulets;
import frostygames0.elementalamulets.config.ModConfig;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;


import javax.annotation.Nullable;
import java.util.List;

/**
 * I made this class because I need elements to have description and multi-name
 * @author Frostygames0
 * @date 01.06.2021 17:40
 */
public class ElementItem extends Item {

    public ElementItem(Rarity color) {
        super(new Item.Properties().tab(ElementalAmulets.GROUP).rarity(color));
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        super.appendHoverText(stack, worldIn, tooltip, flagIn);
        tooltip.add(new TranslationTextComponent(this.getOrCreateDescriptionId()+".tooltip").withStyle(TextFormatting.GRAY));
    }

    @Override
    public String getDescriptionId() {
        if (ModConfig.cached.USE_LATIN_ELEMENT_NAMES) {
            return this.getOrCreateDescriptionId() + ".latin_variant";
        }
        return super.getDescriptionId();
    }

}
