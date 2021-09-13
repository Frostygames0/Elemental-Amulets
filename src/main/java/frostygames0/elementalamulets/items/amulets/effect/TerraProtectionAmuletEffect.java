package frostygames0.elementalamulets.items.amulets.effect;

import frostygames0.elementalamulets.items.amulets.TerraProtectionAmulet;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.DamagingProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraftforge.event.entity.ProjectileImpactEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import top.theillusivec4.curios.api.CuriosApi;

public class TerraProtectionAmuletEffect {

    static void onLivingHurt(LivingHurtEvent event) {
        if(event.getEntityLiving() instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) event.getEntityLiving();
            if (!player.level.isClientSide()) {
                CuriosApi.getCuriosHelper().findEquippedCurio(item -> item.getItem() instanceof TerraProtectionAmulet, player).ifPresent(triple -> {
                    ItemStack stack = triple.getRight();
                    TerraProtectionAmulet amulet = (TerraProtectionAmulet) stack.getItem();
                    if (amulet.canProtect(stack)) {
                        if (event.getSource().getEntity() instanceof LivingEntity) {
                            LivingEntity attacker = (LivingEntity) event.getSource().getEntity();
                            event.setCanceled(true);
                            amulet.removeOneCharge(stack);
                            attacker.hurt(DamageSource.MAGIC, amulet.getReflectedDamageMulti(stack));
                        }
                    }
                });
            }
        }
    }

    static void onProjectileImpact(ProjectileImpactEvent event) {
        if(!(event.getRayTraceResult() instanceof EntityRayTraceResult)) return; // We need only projectiles that hit entity
        Entity projectile = event.getEntity();
        Entity target = ((EntityRayTraceResult) event.getRayTraceResult()).getEntity();
        if(target instanceof PlayerEntity) {
            PlayerEntity entity = (PlayerEntity) target;
            CuriosApi.getCuriosHelper().findEquippedCurio(item -> item.getItem() instanceof TerraProtectionAmulet, entity).ifPresent(triple -> {
                ItemStack stack = triple.getRight();
                TerraProtectionAmulet amulet = (TerraProtectionAmulet) stack.getItem(); // For future
                if(amulet.canProtect(stack)) {
                    projectile.setDeltaMovement(projectile.getDeltaMovement().reverse().scale(0.5)); // I don't want arrows to shoot with the same speed as it looks awful
                    if (projectile instanceof DamagingProjectileEntity) {
                        DamagingProjectileEntity damagingProjectile = (DamagingProjectileEntity) projectile;
                        damagingProjectile.xPower *= -0.5;
                        damagingProjectile.yPower *= -0.5;
                        damagingProjectile.zPower *= -0.5;
                    }
                    event.setCanceled(true);
                    projectile.hurtMarked = true;
                    entity.level.playSound(null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.GRASS_BREAK, SoundCategory.PLAYERS, 1.0f, 1.0f);
                }
            });
        }

    }
}
