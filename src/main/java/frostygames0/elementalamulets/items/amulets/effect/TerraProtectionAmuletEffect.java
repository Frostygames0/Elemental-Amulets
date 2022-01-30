/*
 *  Copyright (c) 2021
 *
 *     This file is part of Elemental Amulets, a Minecraft Mod.
 *
 *     Elemental Amulets is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Lesser General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Elemental Amulets is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Lesser General Public License for more details.
 *
 *     You should have received a copy of the GNU Lesser General Public License
 *     along with Elemental Amulets.  If not, see <https://www.gnu.org/licenses/>.
 */

package frostygames0.elementalamulets.items.amulets.effect;

import frostygames0.elementalamulets.init.ModItems;
import frostygames0.elementalamulets.items.amulets.TerraProtectionAmuletItem;
import frostygames0.elementalamulets.util.AmuletHelper;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractHurtingProjectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraftforge.event.entity.ProjectileImpactEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.LivingKnockBackEvent;

class TerraProtectionAmuletEffect {

    static void onLivingHurt(final LivingHurtEvent event) {
        if (event.getEntityLiving() instanceof Player player) {
            if (!player.level.isClientSide()) {
                AmuletHelper.getAmuletInSlotOrBelt(ModItems.TERRA_PROTECTION_AMULET.get(), player).ifPresent(triple -> {
                    ItemStack stack = triple.stack();
                    TerraProtectionAmuletItem amulet = (TerraProtectionAmuletItem) stack.getItem();
                    if (amulet.canProtect(stack)) {
                        if (event.getSource().getEntity() instanceof LivingEntity attacker) {
                            event.setCanceled(true);
                            amulet.removeOneCharge(stack);
                            attacker.hurt(TerraProtectionAmuletItem.LEAF_CUT, amulet.getReflectedDamageMulti(stack));
                        }
                    }
                });
            }
        }
    }

    static void onProjectileImpact(final ProjectileImpactEvent event) {
        if (!(event.getRayTraceResult() instanceof EntityHitResult))
            return; // We need only projectiles that hit entity
        Entity projectile = event.getEntity();
        Entity target = ((EntityHitResult) event.getRayTraceResult()).getEntity();
        if (target instanceof Player entity) {
            if (!entity.level.isClientSide()) {
                AmuletHelper.getAmuletInSlotOrBelt(ModItems.TERRA_PROTECTION_AMULET.get(), entity).ifPresent(triple -> {
                    ItemStack stack = triple.stack();
                    TerraProtectionAmuletItem amulet = (TerraProtectionAmuletItem) stack.getItem(); // For future
                    if (amulet.canProtect(stack)) {
                        projectile.setDeltaMovement(projectile.getDeltaMovement().reverse().scale(0.5)); // I don't want arrows to shoot with the same speed as it looks awful
                        if (projectile instanceof AbstractHurtingProjectile damagingProjectile) {

                            damagingProjectile.setOwner(entity);

                            damagingProjectile.xPower *= -1;
                            damagingProjectile.yPower *= -1;
                            damagingProjectile.zPower *= -1;
                        }
                        event.setCanceled(true);
                        projectile.hurtMarked = true;
                        entity.level.playSound(null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.GRASS_BREAK, SoundSource.PLAYERS, 1.0f, 1.0f);
                    }
                });
            }
        }
    }

    // Maybe use attribute instead so other things can affect it too?
    static void onLivingKnockback(final LivingKnockBackEvent event) {
        if (event.getEntityLiving() instanceof Player player) {
            if (!player.level.isClientSide()) {
                AmuletHelper.getAmuletInSlotOrBelt(ModItems.TERRA_PROTECTION_AMULET.get(), player).ifPresent(triple -> {
                    ItemStack stack = triple.stack();
                    TerraProtectionAmuletItem amulet = (TerraProtectionAmuletItem) stack.getItem();
                    event.setCanceled(amulet.canProtect(stack));
                });
            }
        }
    }
}
