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
import frostygames0.elementalamulets.items.amulets.KnockbackAmuletItem;
import frostygames0.elementalamulets.util.AmuletHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

/**
 * @author Frostygames0
 * @date 09.10.2021 11:51
 */
class KnockbackAmuletEffect {
    static void onLivingHurt(LivingHurtEvent event) {
        if (event.getEntityLiving() instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) event.getEntityLiving();
            if (!player.level.isClientSide()) {
                AmuletHelper.getAmuletInSlotOrBelt(ModItems.KNOCKBACK_AMULET.get(), player).ifPresent(triple -> {
                    if (event.getSource().getEntity() instanceof LivingEntity) {
                        LivingEntity attacker = (LivingEntity) event.getSource().getEntity();

                        ItemStack stack = triple.getRight();
                        KnockbackAmuletItem amulet = (KnockbackAmuletItem) stack.getItem();
                        attacker.knockback(amulet.getKnockback(stack), MathHelper.sin(player.yRot * ((float) Math.PI / 180F)), -MathHelper.cos(player.yRot * ((float) Math.PI / 180F)));
                    }
                });
            }
        }
    }
}
