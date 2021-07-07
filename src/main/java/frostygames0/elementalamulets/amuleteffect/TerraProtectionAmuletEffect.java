package frostygames0.elementalamulets.amuleteffect;

import frostygames0.elementalamulets.items.amulets.TerraProtectionAmulet;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.DamagingProjectileEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.event.entity.ProjectileImpactEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import top.theillusivec4.curios.api.CuriosApi;

// TODO Finish effect (Make it based on tier, add cooldown(maybe), make so it's not op)
public class TerraProtectionAmuletEffect {

    static void onLivingHurt(LivingHurtEvent event) {
        LivingEntity entity = event.getEntityLiving();
        if(!entity.world.isRemote()) {
            CuriosApi.getCuriosHelper().findEquippedCurio(item -> item.getItem() instanceof TerraProtectionAmulet, entity).ifPresent(triple -> {
                TerraProtectionAmulet amulet = (TerraProtectionAmulet) triple.getRight().getItem();
                float absorb = 1-amulet.getDamageAbsorb(triple.getRight());
                if(event.getSource().getTrueSource() instanceof LivingEntity) {
                    LivingEntity attacker = (LivingEntity) event.getSource().getTrueSource();
                    if(amulet.getTier(triple.getRight()) > 2) {
                        attacker.attackEntityFrom(DamageSource.MAGIC, event.getAmount() / 1.5f); // TODO remake it
                    }
                    if(absorb < 0.999) {
                        if(absorb < 0.001) {
                            event.setCanceled(true);
                        }
                        event.setAmount(absorb * event.getAmount());
                    }
                }
            });
        }
    }

    static void onLivingAttack(LivingAttackEvent event) {
        LivingEntity entity = event.getEntityLiving();
        if(!entity.world.isRemote()) {
            CuriosApi.getCuriosHelper().findEquippedCurio(item -> item.getItem() instanceof TerraProtectionAmulet, entity).ifPresent(triple -> {
                TerraProtectionAmulet amulet = (TerraProtectionAmulet) triple.getRight().getItem();
                float absorb = 1 - amulet.getDamageAbsorb(triple.getRight());
                if (event.getSource().getTrueSource() instanceof LivingEntity) {
                    if (absorb < 0.001) {
                        event.setCanceled(true);
                    }
                }
            });
        }
    }

    static void onProjectileImpact(ProjectileImpactEvent event) {
        if(!(event.getRayTraceResult() instanceof EntityRayTraceResult)) return; // We need only projectiles that hit entity
        Entity projectile = event.getEntity();
        Entity target = ((EntityRayTraceResult) event.getRayTraceResult()).getEntity();
        if(target instanceof PlayerEntity) {
            PlayerEntity entity = (PlayerEntity) target;
            CuriosApi.getCuriosHelper().findEquippedCurio(item -> item.getItem() instanceof TerraProtectionAmulet, entity).ifPresent(triple -> {
                TerraProtectionAmulet amulet = (TerraProtectionAmulet) triple.getRight().getItem(); // For future
                projectile.setMotion(projectile.getMotion().inverse());
                if(projectile instanceof DamagingProjectileEntity) {
                    DamagingProjectileEntity damagingProjectile = (DamagingProjectileEntity) projectile;
                    damagingProjectile.accelerationX *= -1;
                    damagingProjectile.accelerationY *= -1;
                    damagingProjectile.accelerationZ *= -1;
                }
                event.setCanceled(true);
                projectile.velocityChanged = true;
                entity.world.playSound(null, entity.getPosX(), entity.getPosY(), entity.getPosZ(), SoundEvents.ENTITY_WITHER_BREAK_BLOCK, SoundCategory.PLAYERS, 1.0f, 1.0f);
            });
        }

    }
}
