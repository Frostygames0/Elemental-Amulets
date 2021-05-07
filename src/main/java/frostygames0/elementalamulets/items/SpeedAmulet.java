package frostygames0.elementalamulets.items;

import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;
import frostygames0.elementalamulets.ElementalAmulets;
import frostygames0.elementalamulets.config.ModConfig;
import frostygames0.elementalamulets.items.interfaces.ISpeedItem;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import top.theillusivec4.curios.api.SlotContext;

import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;

public class SpeedAmulet extends AmuletItem implements ISpeedItem {
    private final float speedFactor = 1.25f;
    public SpeedAmulet(Properties properties, int tier) {
        super(properties, tier);
    }


    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);
        tooltip.add(new TranslationTextComponent("item.elementalamulets.speed_amulet.tooltip", TextFormatting.GRAY));
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(SlotContext slotContext, UUID uuid, ItemStack stack) {
        Multimap<Attribute, AttributeModifier> map = LinkedHashMultimap.create();
        map.put(Attributes.MOVEMENT_SPEED, new AttributeModifier(uuid, ElementalAmulets.MOD_ID + ":speed_bonus", speedFactor*getTier(), AttributeModifier.Operation.MULTIPLY_TOTAL));
        return map;
    }

    @Override
    public float getSpeed() {
        return speedFactor;
    }

    @Override
    public int getDamageOnUse() {
        return ModConfig.cached.SPEED_AMULET_USAGE_DMG*getTier();
    }

}
