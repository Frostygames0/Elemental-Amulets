package frostygames0.elementalamulets.items.amulets.effect;

import frostygames0.elementalamulets.items.amulets.FireAmulet;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.DamageSource;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import top.theillusivec4.curios.api.CuriosApi;

public class FireAmuletEffect {

    static void onLivingAttack(LivingAttackEvent event) {
        if(event.getEntityLiving() instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) event.getEntityLiving();
            DamageSource source = event.getSource();
            if (!player.world.isRemote()) {
                if (source.isFireDamage()) {
                    CuriosApi.getCuriosHelper().findEquippedCurio(item -> item.getItem() instanceof FireAmulet, player).ifPresent((triple) -> {
                        FireAmulet amulet = (FireAmulet) triple.getRight().getItem();
                        float fire = 1 - amulet.getFireResist(triple.getRight());
                        float lava = 1 - amulet.getLavaResist(triple.getRight());
                        if (source == DamageSource.IN_FIRE || source == DamageSource.ON_FIRE) {
                            if (fire < 0.001f) event.setCanceled(true);
                        } else {
                            if (lava < 0.001f) event.setCanceled(true);
                        }
                    });
                }
            }
        }
    }

    static void onLivingHurt(LivingHurtEvent event) {
        if (event.getEntityLiving() instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) event.getEntityLiving();
            DamageSource source = event.getSource();
            if (!player.world.isRemote()) {
                if (source.isFireDamage()) {
                    CuriosApi.getCuriosHelper().findEquippedCurio(item -> item.getItem() instanceof FireAmulet, player).ifPresent((triple) -> {
                        FireAmulet amulet = (FireAmulet) triple.getRight().getItem();
                        float fire = 1 - amulet.getFireResist(triple.getRight());
                        float lava = 1 - amulet.getLavaResist(triple.getRight());
                        if (source == DamageSource.IN_FIRE || source == DamageSource.ON_FIRE) {
                            if (fire < 0.999f) {
                                if (fire < 0.001f) {
                                    event.setCanceled(true);
                                }
                                event.setAmount(event.getAmount() * fire);
                            }
                        } else {
                            if (lava < 0.999f) {
                                if (lava < 0.001f) {
                                    event.setCanceled(true);
                                }
                                event.setAmount(event.getAmount() * lava);
                            }
                        }
                    });
                }
            }
        }
    }
}
