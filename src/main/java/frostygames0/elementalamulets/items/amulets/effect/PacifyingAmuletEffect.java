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
import frostygames0.elementalamulets.items.amulets.PacifyingAmuletItem;
import frostygames0.elementalamulets.util.AmuletHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.CooldownTracker;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

/**
 * @author Frostygames0
 * @date 20.10.2021 20:10
 */
public class PacifyingAmuletEffect {
    static void onLivingHurt(LivingHurtEvent event) {
        if (event.getEntityLiving() instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) event.getEntityLiving();
            if (!player.level.isClientSide()) {
                AmuletHelper.getAmuletInSlotOrBelt(ModItems.PACIFYING_AMULET.get(), player).ifPresent(triple -> {
                    ItemStack amulet = triple.getRight();
                    PacifyingAmuletItem item = (PacifyingAmuletItem) amulet.getItem();

                    Entity damager = event.getSource().getEntity();

                    if (damager == player)
                        return;

                    CooldownTracker tracker = player.getCooldowns();
                    if (tracker.isOnCooldown(item))
                        return;

                    if (damager instanceof LivingEntity) {
                        LivingEntity livingDamager = (LivingEntity) damager;
                        if (livingDamager.addEffect(new EffectInstance(Effects.MOVEMENT_SLOWDOWN, 100, 2, true, true, true, new EffectInstance(Effects.BLINDNESS)))) {
                            if (livingDamager instanceof PlayerEntity) {
                                PlayerEntity damagingPlayer = (PlayerEntity) livingDamager;
                                damagingPlayer.displayClientMessage(new TranslationTextComponent("item.elementalamulets.pacifying_amulet.tired").withStyle(TextFormatting.RED), true);
                            }
                            player.displayClientMessage(new TranslationTextComponent("item.elementalamulets.pacifying_amulet.tired_success").withStyle(TextFormatting.GREEN), true);
                        }
                    }
                });

            }
        }
    }
}
