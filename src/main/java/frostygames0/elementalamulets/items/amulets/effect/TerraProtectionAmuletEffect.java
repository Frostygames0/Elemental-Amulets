/*
 *     Copyright (c) 2021
 *
 *     This file is part of Elemental Amulets, a Minecraft Mod.
 *
 *     Elemental Amulets is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Elemental Amulets is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with Elemental Amulets.  If not, see <https://www.gnu.org/licenses/>.
 */

package frostygames0.elementalamulets.items.amulets.effect;

import frostygames0.elementalamulets.init.ModItems;
import frostygames0.elementalamulets.items.amulets.TerraProtectionAmuletItem;
import frostygames0.elementalamulets.util.AmuletHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.DamagingProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraftforge.event.entity.ProjectileImpactEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.LivingKnockBackEvent;

class TerraProtectionAmuletEffect {

    static void onLivingHurt(LivingHurtEvent event) {
        if (event.getEntityLiving() instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) event.getEntityLiving();
            if (!player.level.isClientSide()) {
                AmuletHelper.getAmuletInSlotOrBelt(ModItems.TERRA_PROTECTION_AMULET.get(), player).ifPresent(triple -> {
                    ItemStack stack = triple.getRight();
                    TerraProtectionAmuletItem amulet = (TerraProtectionAmuletItem) stack.getItem();
                    if (amulet.canProtect(stack)) {
                        if (event.getSource().getEntity() instanceof LivingEntity) {
                            LivingEntity attacker = (LivingEntity) event.getSource().getEntity();
                            event.setCanceled(true);
                            amulet.removeOneCharge(stack);
                            attacker.hurt(TerraProtectionAmuletItem.LEAF_CUT, amulet.getReflectedDamageMulti(stack));
                        }
                    }
                });
            }
        }
    }

    static void onProjectileImpact(ProjectileImpactEvent event) {
        if (!(event.getRayTraceResult() instanceof EntityRayTraceResult))
            return; // We need only projectiles that hit entity
        Entity projectile = event.getEntity();
        Entity target = ((EntityRayTraceResult) event.getRayTraceResult()).getEntity();
        if (target instanceof PlayerEntity) {
            PlayerEntity entity = (PlayerEntity) target;
            if (!entity.level.isClientSide()) {
                AmuletHelper.getAmuletInSlotOrBelt(ModItems.TERRA_PROTECTION_AMULET.get(), entity).ifPresent(triple -> {
                    ItemStack stack = triple.getRight();
                    TerraProtectionAmuletItem amulet = (TerraProtectionAmuletItem) stack.getItem(); // For future
                    if (amulet.canProtect(stack)) {
                        projectile.setDeltaMovement(projectile.getDeltaMovement().reverse().scale(0.5)); // I don't want arrows to shoot with the same speed as it looks awful
                        if (projectile instanceof DamagingProjectileEntity) {
                            DamagingProjectileEntity damagingProjectile = (DamagingProjectileEntity) projectile;

                            damagingProjectile.setOwner(entity);

                            damagingProjectile.xPower *= -1;
                            damagingProjectile.yPower *= -1;
                            damagingProjectile.zPower *= -1;
                        }
                        event.setCanceled(true);
                        projectile.hurtMarked = true;
                        entity.level.playSound(null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.GRASS_BREAK, SoundCategory.PLAYERS, 1.0f, 1.0f);
                    }
                });
            }
        }
    }

    // Maybe use attribute instead so other things can affect it too?
    static void onLivingKnockback(LivingKnockBackEvent event) {
        if (event.getEntityLiving() instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) event.getEntityLiving();
            if (!player.level.isClientSide()) {
                AmuletHelper.getAmuletInSlotOrBelt(ModItems.TERRA_PROTECTION_AMULET.get(), player).ifPresent(triple -> {
                    ItemStack stack = triple.getRight();
                    TerraProtectionAmuletItem amulet = (TerraProtectionAmuletItem) stack.getItem();
                    event.setCanceled(amulet.canProtect(stack));
                });
            }
        }
    }
}
