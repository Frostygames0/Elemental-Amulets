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
import frostygames0.elementalamulets.items.amulets.JumpAmuletItem;
import frostygames0.elementalamulets.network.CUpdatePlayerVelocityPacket;
import frostygames0.elementalamulets.network.ModNetworkHandler;
import frostygames0.elementalamulets.util.AmuletUtil;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

class JumpAmuletEffect {

    static void onLivingHurt(final LivingHurtEvent event) {
        if (event.getEntityLiving() instanceof Player player) {
            if (!player.level.isClientSide()) {
                if (event.getSource() == DamageSource.FALL) {
                    AmuletUtil.getAmuletInSlotOrBelt(ModItems.JUMP_AMULET.get(), player).ifPresent((triple) -> {
                        float fallResist = ((JumpAmuletItem) triple.stack().getItem()).getFallResist(triple.stack());
                        if (!event.isCanceled() && fallResist > 0) {
                            float finalDamage = Math.max(0, (event.getAmount() - fallResist));
                            if (finalDamage == 0) {
                                event.setCanceled(true);
                            } else {
                                event.setAmount(finalDamage);
                            }
                        }
                    });
                }
            }
        }
    }

    static void onLivingAttack(final LivingAttackEvent event) {
        if (event.getEntityLiving() instanceof Player player) {
            if (!player.level.isClientSide()) {
                if (event.getSource() == DamageSource.FALL) {
                    AmuletUtil.getAmuletInSlotOrBelt(ModItems.JUMP_AMULET.get(), player).ifPresent((triple) -> {
                        float fallResist = ((JumpAmuletItem) triple.stack().getItem()).getFallResist(triple.stack());
                        if (!event.isCanceled() && fallResist > 0) {
                            float finalDamage = Math.max(0, (event.getAmount() - fallResist));
                            if (finalDamage == 0) {
                                event.setCanceled(true);
                            }
                        }
                    });
                }
            }
        }
    }

    static void onLivingJump(LivingEvent.LivingJumpEvent event) {
        if (event.getEntityLiving() instanceof Player player) {
            Level world = player.getCommandSenderWorld();
            if (!world.isClientSide) {
                if (world.getFluidState(player.blockPosition()).isEmpty()) {
                    AmuletUtil.getAmuletInSlotOrBelt(ModItems.JUMP_AMULET.get(), player).ifPresent(triple -> {
                        ItemStack stack = triple.stack();
                        JumpAmuletItem item = (JumpAmuletItem) stack.getItem();

                        Vec3 vector = player.getDeltaMovement().add(0, item.getJump(stack), 0);
                        ModNetworkHandler.sendToClient(new CUpdatePlayerVelocityPacket(vector.x, vector.y, vector.z), (ServerPlayer) player);
                    });
                }
            }
        }
    }

}
