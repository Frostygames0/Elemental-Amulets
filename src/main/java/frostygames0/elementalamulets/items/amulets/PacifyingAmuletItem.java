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
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.entity.IAngerable;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.goal.GoalSelector;
import net.minecraft.entity.ai.goal.PrioritizedGoal;
import net.minecraft.entity.ai.goal.TargetGoal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.CooldownTracker;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3i;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import top.theillusivec4.curios.api.CuriosApi;


import java.util.stream.Collectors;

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
        World world = livingEntity.level;
        if (!world.isClientSide()) {
            if (livingEntity instanceof PlayerEntity) {
                PlayerEntity player = (PlayerEntity) livingEntity;
                CooldownTracker tracker = player.getCooldowns();

                if (tracker.isOnCooldown(this))
                    return;

                if (player.tickCount % 5 == 0) {// A little optimization so it won't call it every tick
                    for (MobEntity mob : this.getAngerablesAround(player)) {
                        if (tracker.isOnCooldown(this))
                            break;

                        IAngerable angerable = (IAngerable) mob;

                        GoalSelector selector = mob.targetSelector;
                        for (PrioritizedGoal priGoal : selector.getRunningGoals().collect(Collectors.toList())) {
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
                        ((ServerWorld) world).sendParticles(ParticleTypes.HEART, mob.getX(), mob.getY(), mob.getZ(), 20, 0, 0, 0, 1);
                    }
                }
            }
        }
    }

    @Override
    public void curioBreak(ItemStack stack, LivingEntity livingEntity) {
        Minecraft mc = Minecraft.getInstance();

        mc.gameRenderer.displayItemActivation(stack);

        ClientPlayerEntity player = mc.player;
        if (player != null) {
            player.playSound(SoundEvents.WITHER_DEATH, 1f, 1f);
            player.displayClientMessage(new TranslationTextComponent("item.elementalamulets.pacifying_amulet.onbreak", this.getName(stack)).withStyle(TextFormatting.RED), true);
        }
    }

    // Call this in ItemStack#hurtAndBreak's consumer
    public void onAmuletBreak(String identifier, int index, LivingEntity entity) {
        if (!ModConfig.CachedValues.PACIFYING_AMULET_ANGER_ONBREAK)
            return;

        for (MobEntity mob : getAngerablesAround(entity)) {
            IAngerable angerable = (IAngerable) mob;
            angerable.setTarget(entity);
            if (entity instanceof PlayerEntity) {
                ((PlayerEntity) entity).getCooldowns().addCooldown(this, ModConfig.CachedValues.PACIFYING_AMULET_BREAK_COOLDOWN);
            }

            CuriosApi.getCuriosHelper().onBrokenCurio(identifier, index, entity);
        }
    }

    private Iterable<MobEntity> getAngerablesAround(LivingEntity target) {
        BlockPos position = target.blockPosition();
        return target.level.getLoadedEntitiesOfClass(MobEntity.class, new AxisAlignedBB(position.subtract(new Vector3i(6, 5, 6)), position.offset(new Vector3i(6, 5, 6))), entity -> {
            if (entity instanceof IAngerable) {
                IAngerable angerable = (IAngerable) entity;
                return entity.canSee(target) && angerable.getPersistentAngerTarget() != null && angerable.getPersistentAngerTarget().equals(target.getUUID());
            }
            return false;
        });
    }
}
