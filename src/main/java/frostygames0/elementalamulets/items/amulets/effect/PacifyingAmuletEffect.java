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

import frostygames0.elementalamulets.config.ModConfig;
import frostygames0.elementalamulets.init.ModItems;
import frostygames0.elementalamulets.items.amulets.PacifyingAmuletItem;
import frostygames0.elementalamulets.util.AmuletUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemCooldowns;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

class PacifyingAmuletEffect {
    static void onLivingHurt(final LivingHurtEvent event) {
        if (event.getEntityLiving() instanceof Player player) {
            if (!player.level.isClientSide()) {
                AmuletUtil.getAmuletInSlotOrBelt(ModItems.PACIFYING_AMULET.get(), player).ifPresent(triple -> {
                    ItemStack amulet = triple.stack();
                    PacifyingAmuletItem item = (PacifyingAmuletItem) amulet.getItem();

                    Entity damager = event.getSource().getEntity();

                    if (damager == player)
                        return;

                    ItemCooldowns tracker = player.getCooldowns();
                    if (tracker.isOnCooldown(item))
                        return;

                    if (damager instanceof LivingEntity livingDamager) {

                        int disorientationTime = ModConfig.CachedValues.PACIFYING_AMULET_DISORIENTATION_TIME;

                        if (livingDamager.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, disorientationTime, 2, true, true, true))) {
                            if (livingDamager instanceof Player damagingPlayer) {
                                damagingPlayer.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, (disorientationTime / 2), 2));
                                damagingPlayer.addEffect(new MobEffectInstance(MobEffects.CONFUSION, disorientationTime, 2));
                                damagingPlayer.displayClientMessage(new TranslatableComponent("item.elementalamulets.pacifying_amulet.tired").withStyle(ChatFormatting.RED), true);
                            }
                            player.displayClientMessage(new TranslatableComponent("item.elementalamulets.pacifying_amulet.tired_success").withStyle(ChatFormatting.GREEN), true);
                        }
                    }
                });

            }
        }
    }
}
