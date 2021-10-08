/*
 *    This file is part of Elemental Amulets.
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

import frostygames0.elementalamulets.core.init.ModItems;
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
            if (!player.level.isClientSide()) {
                if (source.isFire()) {
                    CuriosApi.getCuriosHelper().findEquippedCurio(ModItems.FIRE_AMULET.get(), player).ifPresent((triple) -> {
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
            if (!player.level.isClientSide()) {
                if (source.isFire()) {
                    CuriosApi.getCuriosHelper().findEquippedCurio(ModItems.FIRE_AMULET.get(), player).ifPresent((triple) -> {
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
