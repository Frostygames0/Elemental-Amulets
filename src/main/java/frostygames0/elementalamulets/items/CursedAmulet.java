package frostygames0.elementalamulets.items;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.potion.Potions;

public class CursedAmulet extends AbstractAmuletItem {
    public CursedAmulet(Properties properties) {
        super(properties);
    }

    @Override
    public void curioTick(String identifier, int index, LivingEntity livingEntity, ItemStack stack) {
        if(livingEntity.ticksExisted % 40 == 0) {
            livingEntity.addPotionEffect(new EffectInstance(Effects.SLOWNESS, 40, 0, true, false));
            livingEntity.addPotionEffect(new EffectInstance(Effects.HUNGER, 40, 0, true, false));
            livingEntity.addPotionEffect(new EffectInstance(Effects.BLINDNESS, 40, 0, true, false));
        }
    }

    @Override
    public boolean canUnequip(String identifier, LivingEntity livingEntity, ItemStack stack) {
        if(livingEntity instanceof PlayerEntity) {
            PlayerEntity playerEntity = (PlayerEntity) livingEntity;
            return playerEntity.isCreative();
        }
        return false;
    }
}
