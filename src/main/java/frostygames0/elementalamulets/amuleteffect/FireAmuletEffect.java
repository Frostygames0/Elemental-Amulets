package frostygames0.elementalamulets.amuleteffect;

import frostygames0.elementalamulets.ElementalAmulets;
import frostygames0.elementalamulets.items.amulets.AmuletItem;
import frostygames0.elementalamulets.items.amulets.interfaces.IFireItem;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import org.apache.commons.lang3.tuple.ImmutableTriple;
import top.theillusivec4.curios.api.CuriosApi;

import java.util.Optional;

public class FireAmuletEffect {

    private static boolean stackDamaged;

    private static float[] calcFireResistance(LivingEntity entity) {
        Optional<ImmutableTriple<String, Integer, ItemStack>> optional = CuriosApi.getCuriosHelper().findEquippedCurio(item -> item.getItem() instanceof IFireItem, entity);
        float fireResistMulti = 1;
        float lavaResistMulti = 1;
        if (optional.isPresent()) {
            ItemStack stack = optional.get().getRight();
            IFireItem item = (IFireItem) stack.getItem();
            fireResistMulti *= 1-item.getFireResist();
            lavaResistMulti *= 1-item.getLavaResist();
        }
        return new float[] {fireResistMulti, lavaResistMulti};
    }

    /*
     * Unfortunately cancelling it also cancels LivingHurtEvent
     * and that means that it will not take any effect
     * so i need to find workaround
     */
    public static void onLivingAttack(LivingAttackEvent event) {
        if(event.getEntity().getEntityWorld().isRemote()) return;
        if(event.getSource().isFireDamage()) {
            LivingEntity entity = event.getEntityLiving();
            float[] multis = calcFireResistance(entity);
            float fire = multis[0];
            float lava = multis[1];
            if (event.getSource() == DamageSource.IN_FIRE || event.getSource() == DamageSource.ON_FIRE) {
                if (fire < 0.001f) event.setCanceled(true);
            } else {
                if (lava < 0.001f) event.setCanceled(true);
            }
        }

    }

    public static void onLivingHurt(LivingHurtEvent event) {
        if(event.getEntity().getEntityWorld().isRemote()) return;
        if(event.getSource().isFireDamage()) {
            LivingEntity entity = event.getEntityLiving();
            CuriosApi.getCuriosHelper().findEquippedCurio(item -> item.getItem() instanceof IFireItem, entity).ifPresent((tr) -> {
                ItemStack stack = tr.getRight();
                float[] multis = calcFireResistance(entity);
                float fire = multis[0];
                float lava = multis[1];
                float finalDamage = 1f;
                if(stack.getItem() instanceof AmuletItem) {
                    stack.damageItem(((AmuletItem) stack.getItem()).getDamageOnUse(), entity, (e) -> CuriosApi.getCuriosHelper().onBrokenCurio(tr.getLeft(), tr.getMiddle(), e));
                }
                if (event.getSource() == DamageSource.IN_FIRE || event.getSource() == DamageSource.ON_FIRE) {
                    if (fire < 0.999f) {
                        if (fire < 0.001f) {
                            event.setCanceled(true);
                        }
                        event.setAmount(event.getAmount() * fire);
                        ElementalAmulets.LOGGER.debug("After fire damage is - "+event.getAmount());
                    }
                } else {
                    if (lava < 0.999f) {
                        if (lava < 0.001f) {
                            event.setCanceled(true);
                        }
                        event.setAmount(event.getAmount() * lava);
                        ElementalAmulets.LOGGER.debug("After lava damage is - "+event.getAmount());
                    }
                }
            });
        }
    }

}
