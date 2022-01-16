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
import frostygames0.elementalamulets.items.amulets.KnockbackAmuletItem;
import frostygames0.elementalamulets.util.AmuletHelper;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

/**
 * @author Frostygames0
 * @date 09.10.2021 11:51
 */
class KnockbackAmuletEffect {
    static void onLivingHurt(LivingHurtEvent event) {
        if (event.getEntityLiving() instanceof Player player) {
            if (!player.level.isClientSide()) {
                AmuletHelper.getAmuletInSlotOrBelt(ModItems.KNOCKBACK_AMULET.get(), player).ifPresent(triple -> {
                    if (event.getSource().getEntity() instanceof LivingEntity attacker) {

                        ItemStack stack = triple.stack();
                        KnockbackAmuletItem amulet = (KnockbackAmuletItem) stack.getItem();
                        attacker.knockback(amulet.getKnockback(stack), Mth.sin(player.getYRot() * ((float) Math.PI / 180F)), -Mth.cos(player.getYRot() * ((float) Math.PI / 180F)));
                    }
                });
            }
        }
    }
}
