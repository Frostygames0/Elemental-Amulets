package frostygames0.elementalamulets.items;

import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.SimpleAnimatedParticle;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.Effects;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotContext;

import javax.annotation.Nullable;
import java.util.List;

public class InvisibilityAmulet extends AbstractAmuletItem{
    public InvisibilityAmulet(Properties properties) {
        super(properties);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);
        tooltip.add(new TranslationTextComponent("item.elementalamulets.invisibility_amulet.tooltip", TextFormatting.GRAY));
    }

    @Override
    public void curioTick(String identifier, int index, LivingEntity livingEntity, ItemStack stack) {
        World world = livingEntity.getEntityWorld();
        if(!world.isRemote()) {
            if (livingEntity.isSneaking() && !livingEntity.isPotionActive(Effects.INVISIBILITY)) {
                livingEntity.setInvisible(true);
                stack.damageItem(1, livingEntity, livingEntity1 -> CuriosApi.getCuriosHelper().onBrokenCurio(identifier, index, livingEntity1));
            } else if(livingEntity.isPotionActive(Effects.INVISIBILITY)) {
                livingEntity.setInvisible(true);
            } else {
                livingEntity.setInvisible(false);
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
}
