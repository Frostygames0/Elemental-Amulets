package frostygames0.elementalamulets.items.amulets;

import frostygames0.elementalamulets.ElementalAmulets;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeMod;


import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;

// TODO: Finish it
public class WaterAmulet extends AmuletItem {
    public static UUID MODIFIER_UUID = UUID.fromString("88f5e6ea-c12f-4cfd-b3f4-40d9ca8cdfad");
    public WaterAmulet(Properties properties) {
        super(properties, true);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        super.appendHoverText(stack, worldIn, tooltip, flagIn);
        tooltip.add(new TranslationTextComponent("item.elementalamulets.wip").withStyle(TextFormatting.RED));
    }

    @Override
    public void curioTick(String identifier, int index, LivingEntity livingEntity, ItemStack stack) {
        World level = livingEntity.level;
        if(!level.isClientSide()) {
            ModifiableAttributeInstance att = livingEntity.getAttribute(ForgeMod.SWIM_SPEED.get());
            AttributeModifier attMod = new AttributeModifier(MODIFIER_UUID, new ResourceLocation(ElementalAmulets.MOD_ID, "speed").toString(),
                    1, AttributeModifier.Operation.MULTIPLY_BASE);
            livingEntity.setAirSupply(livingEntity.getAirSupply());
            if(livingEntity.isUnderWater()) {

            }
        }
    }
}
