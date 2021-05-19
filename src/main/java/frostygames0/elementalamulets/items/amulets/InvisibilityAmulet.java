package frostygames0.elementalamulets.items.amulets;

import frostygames0.elementalamulets.config.ModConfig;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Effects;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotContext;

import javax.annotation.Nullable;
import java.util.List;

public class InvisibilityAmulet extends AmuletItem {
    public InvisibilityAmulet(Properties properties) {
        super(properties);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);
        tooltip.add(new TranslationTextComponent("item.elementalamulets.invisibility_amulet.tooltip").mergeStyle(TextFormatting.GRAY));
        tooltip.add(new StringTextComponent("NOT WORKING!").mergeStyle(TextFormatting.RED));
    }

    @Override
    public void curioTick(String identifier, int index, LivingEntity livingEntity, ItemStack stack) {
        World world = livingEntity.getEntityWorld();
        if(!world.isRemote()) {
            if(livingEntity.isSneaking()) {
                if(!livingEntity.isInvisible()) {
                    livingEntity.setInvisible(true);
                }
                stack.damageItem(getDamageOnUse(),livingEntity, ent -> CuriosApi.getCuriosHelper().onBrokenCurio(identifier, index, ent));
            } else {
                if(!livingEntity.isPotionActive(Effects.INVISIBILITY)) {
                    livingEntity.setInvisible(false);
                }
            }
        }
    }


    @Override
    public void onUnequip(SlotContext slotContext, ItemStack newStack, ItemStack stack) {
        LivingEntity entity = slotContext.getWearer();
        if(entity.isInvisible() && !entity.isPotionActive(Effects.INVISIBILITY)) {
            entity.setInvisible(false);
        }
    }

    @Override
    public int getDamageOnUse() {
        return ModConfig.cached.INVISIBILITY_AMULET_USAGE_DMG;
    }
}
