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

package frostygames0.elementalamulets.items.amulets;

import frostygames0.elementalamulets.config.ModConfig;
import frostygames0.elementalamulets.mixin.accessors.AccessorTargetGoal;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.NeutralMob;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.GoalSelector;
import net.minecraft.world.entity.ai.goal.WrappedGoal;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.item.ItemCooldowns;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.phys.AABB;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.level.Level;
import net.minecraft.server.level.ServerLevel;
import top.theillusivec4.curios.api.CuriosApi;


import java.util.stream.Collectors;

import net.minecraft.world.item.Item.Properties;

/**
 * @author Frostygames0
 * @date 14.10.2021 22:15
 */
public class PacifyingAmuletItem extends AmuletItem {

    public PacifyingAmuletItem(Properties properties) {
        super(properties, false);
    }

    @Override
    public void curioTick(String identifier, int index, LivingEntity livingEntity, ItemStack stack) {
        Level world = livingEntity.level;
        if (!world.isClientSide()) {
            if (livingEntity instanceof Player) {
                Player player = (Player) livingEntity;
                ItemCooldowns tracker = player.getCooldowns();

                if (tracker.isOnCooldown(this))
                    return;

                if (player.tickCount % 5 == 0) {// A little optimization so it won't call it every tick
                    for (Mob mob : this.getAngerablesAround(player)) {
                        if (tracker.isOnCooldown(this))
                            break;

                        NeutralMob angerable = (NeutralMob) mob;

                        GoalSelector selector = mob.targetSelector;
                        for (WrappedGoal priGoal : selector.getRunningGoals().toList()) {
                            if (priGoal.getGoal() instanceof TargetGoal) {
                                TargetGoal goal = (TargetGoal) priGoal.getGoal();

                                if (((AccessorTargetGoal) goal).getTargetMob() == player)
                                    // This should stop mobs that use TargetGoal to be angry after they stop being angry
                                    priGoal.stop();
                            }
                        }
                        angerable.stopBeingAngry();
                        stack.hurtAndBreak(1, player, playerEnt -> this.onAmuletBreak(identifier, index, playerEnt));

                        // Makes heart particles around entity :>
                        ((ServerLevel) world).sendParticles(ParticleTypes.HEART, mob.getX(), mob.getY(), mob.getZ(), 20, 0, 0, 0, 1);
                    }
                }
            }
        }
    }

    @Override
    public void curioBreak(ItemStack stack, LivingEntity livingEntity) {
        Minecraft mc = Minecraft.getInstance();

        mc.gameRenderer.displayItemActivation(stack);

        LocalPlayer player = mc.player;
        if (player != null) {
            player.playSound(SoundEvents.WITHER_DEATH, 1f, 1f);
            player.displayClientMessage(new TranslatableComponent("item.elementalamulets.pacifying_amulet.onbreak", this.getName(stack)).withStyle(ChatFormatting.RED), true);
        }
    }

    // Call this in ItemStack#hurtAndBreak's consumer
    public void onAmuletBreak(String identifier, int index, LivingEntity entity) {
        if (!ModConfig.CachedValues.PACIFYING_AMULET_ANGER_ONBREAK)
            return;

        for (Mob mob : getAngerablesAround(entity)) {
            NeutralMob angerable = (NeutralMob) mob;
            angerable.setTarget(entity);
            if (entity instanceof Player) {
                ((Player) entity).getCooldowns().addCooldown(this, ModConfig.CachedValues.PACIFYING_AMULET_BREAK_COOLDOWN);
            }

            CuriosApi.getCuriosHelper().onBrokenCurio(identifier, index, entity);
        }
    }

    private Iterable<Mob> getAngerablesAround(LivingEntity target) {
        BlockPos position = target.blockPosition();
        return target.level.getEntitiesOfClass(Mob.class, new AABB(position.subtract(new Vec3i(6, 5, 6)), position.offset(new Vec3i(6, 5, 6))), entity -> {
            if (entity instanceof NeutralMob angerable) {
                return angerable.getPersistentAngerTarget() != null && angerable.getPersistentAngerTarget().equals(target.getUUID());
            }
            return false;
        });
    }
}
