package frostygames0.elementalamulets.items;

import frostygames0.elementalamulets.ElementalAmulets;
import frostygames0.elementalamulets.config.ModConfig;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;

/**
 * I made this class because I need elements to have description and multi-name
 * @author Frostygames0
 * @date 01.06.2021 17:40
 */
public class ElementItem extends Item {
    private final List<ITextComponent> description;

    public ElementItem(Rarity color, @Nullable ITextComponent... description) {
        super(new Item.Properties().group(ElementalAmulets.GROUP).rarity(color));
        this.description = Arrays.asList(description);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);
        if (description != null) tooltip.addAll(description);
    }

    @Override
    public String getTranslationKey() {
        if (ModConfig.cached.USE_LATIN_ELEMENT_NAMES) {
            return this.getDefaultTranslationKey() + ".latin_variant";
        }
        return super.getTranslationKey();
    }

}
