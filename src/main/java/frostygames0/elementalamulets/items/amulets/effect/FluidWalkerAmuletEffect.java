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
import frostygames0.elementalamulets.util.AmuletUtil;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingAttackEvent;

class FluidWalkerAmuletEffect {
    static class Lava {
        static void onLivingAttack(LivingAttackEvent event) {
            if (event.getEntityLiving() instanceof Player player) {
                if (!player.level.isClientSide()) {
                    if (event.getSource() == DamageSource.HOT_FLOOR) {
                        if (AmuletUtil.getAmuletInSlotOrBelt(ModItems.LAVA_WALKER_AMULET.get(), player).isPresent()) {
                            event.setCanceled(true);
                        }
                    }
                }
            }
        }
    }

    static class Water {
        // EMPTY BODY
    }
}
