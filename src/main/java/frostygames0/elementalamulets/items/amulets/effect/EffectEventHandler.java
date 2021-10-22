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

import frostygames0.elementalamulets.ElementalAmulets;
import net.minecraftforge.event.entity.ProjectileImpactEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.LivingKnockBackEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = ElementalAmulets.MOD_ID)
public class EffectEventHandler {

    @SubscribeEvent
    public static void onJump(final LivingEvent.LivingJumpEvent event) {
        JumpAmuletEffect.onLivingJump(event);
    }
    @SubscribeEvent
    public static void onHurt(final LivingHurtEvent event) {
        JumpAmuletEffect.onLivingHurt(event);
        FireAmuletEffect.onLivingHurt(event);
        TerraProtectionAmuletEffect.onLivingHurt(event);
        PacifyingAmuletEffect.onLivingHurt(event);
    }
    @SubscribeEvent
    public static void onAttack(final LivingAttackEvent event) {
        JumpAmuletEffect.onLivingAttack(event);
        FireAmuletEffect.onLivingAttack(event);
    }
    @SubscribeEvent
    public static void onProjectileImpact(final ProjectileImpactEvent event) {
        TerraProtectionAmuletEffect.onProjectileImpact(event);
    }
    @SubscribeEvent
    public static void onLivingKnockback(final LivingKnockBackEvent event) {
        TerraProtectionAmuletEffect.onLivingKnockback(event);
    }
 }
