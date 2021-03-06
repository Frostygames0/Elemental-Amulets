/*
 *  Copyright (c) 2021-2022
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
import frostygames0.elementalamulets.items.amulets.FireAmuletItem;
import frostygames0.elementalamulets.util.AmuletUtil;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

class FireAmuletEffect {

    static void onLivingAttack(final LivingAttackEvent event) {
        if (event.getEntityLiving() instanceof Player player) {
            DamageSource source = event.getSource();
            if (!player.level.isClientSide()) {
                if (source.isFire()) {
                    AmuletUtil.getAmuletInSlotOrBelt(ModItems.FIRE_AMULET.get(), player).ifPresent((triple) -> {
                        FireAmuletItem amulet = (FireAmuletItem) triple.stack().getItem();
                        float fire = 1 - amulet.getFireResist(triple.stack());
                        float lava = 1 - amulet.getLavaResist(triple.stack());
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

    static void onLivingHurt(final LivingHurtEvent event) {
        if (event.getEntityLiving() instanceof Player player) {
            DamageSource source = event.getSource();
            if (!player.level.isClientSide()) {
                if (source.isFire()) {
                    AmuletUtil.getAmuletInSlotOrBelt(ModItems.FIRE_AMULET.get(), player).ifPresent((triple) -> {
                        FireAmuletItem amulet = (FireAmuletItem) triple.stack().getItem();
                        float fire = 1 - amulet.getFireResist(triple.stack());
                        float lava = 1 - amulet.getLavaResist(triple.stack());
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
