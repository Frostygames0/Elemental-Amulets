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

package frostygames0.elementalamulets.items.amulets;

import frostygames0.elementalamulets.ElementalAmulets;
import frostygames0.elementalamulets.mixin.accessors.AccessorTargetGoal;
import frostygames0.elementalamulets.util.NBTUtil;
import net.minecraft.entity.IAngerable;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.goal.GoalSelector;
import net.minecraft.entity.ai.goal.PrioritizedGoal;
import net.minecraft.entity.ai.goal.TargetGoal;
import net.minecraft.item.ItemStack;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3i;
import net.minecraft.world.World;


import java.util.stream.Collectors;

/**
 * @author Frostygames0
 * @date 14.10.2021 22:15
 */
public class PacifyingAmuletItem extends AmuletItem {
    public static final String CHARGE_TAG = ElementalAmulets.MOD_ID + ":calming_charge";

    public PacifyingAmuletItem(Properties properties) {
        super(properties, false);
    }

    @Override
    public void curioTick(String identifier, int index, LivingEntity livingEntity, ItemStack stack) {
        World world = livingEntity.level;
        if(!world.isClientSide()) {
            BlockPos pos = livingEntity.blockPosition();
            if (livingEntity.tickCount % 5 == 0) { // A little optimization so it wont call it every tick
                for (MobEntity mob : world.getLoadedEntitiesOfClass(MobEntity.class, new AxisAlignedBB(pos.subtract(new Vector3i(6, 5, 6)), pos.offset(new Vector3i(6, 5, 6))), entity -> entity instanceof IAngerable)) {
                    IAngerable angerable = (IAngerable) mob;

                    if(!mob.canSee(livingEntity)) continue; // Prevents pacifying of mobs that are behind walls etc(basically that can't see user)

                    if (angerable.getPersistentAngerTarget() == null || !angerable.getPersistentAngerTarget().equals(livingEntity.getUUID()))
                        continue;

                    GoalSelector selector = mob.targetSelector;
                    for(PrioritizedGoal priGoal : selector.getRunningGoals().collect(Collectors.toList())) {
                           if(priGoal.getGoal() instanceof TargetGoal) {
                               TargetGoal goal = (TargetGoal) priGoal.getGoal();

                               if(((AccessorTargetGoal)goal).getTargetMob() == livingEntity)
                                   // This should stop mobs that use TargetGoal to be angry after they stop being angry, TODO maybe there is a better way?
                                   //ElementalAmulets.LOGGER.warn("Goal {} was stopped in entity {}", goal, mob);
                                   priGoal.stop();
                           }
                    }
                    //ElementalAmulets.LOGGER.warn("Fully stopping {} from being angry", mob);
                    angerable.stopBeingAngry();

                    // Makes heart particles :)
                    world.addParticle(ParticleTypes.HEART, mob.getX(), mob.getY(), mob.getZ(), 0.1, 0.1, 0.1);
                }
            }
        }
    }

    public int getCharges(ItemStack stack) {
        return NBTUtil.getInteger(stack, CHARGE_TAG);
    }

    public boolean canPacify(ItemStack stack) {
        return getCharges(stack) > 0;
    }
}
