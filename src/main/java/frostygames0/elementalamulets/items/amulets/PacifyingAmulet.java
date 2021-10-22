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

import net.minecraft.entity.IAngerable;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3i;
import net.minecraft.world.World;

/**
 * @author Frostygames0
 * @date 14.10.2021 22:15
 */
// TODO: Debug it
public class PacifyingAmulet extends AmuletItem {
    public PacifyingAmulet(Properties properties) {
        super(properties, false);
    }

    @Override
    public void curioTick(String identifier, int index, LivingEntity livingEntity, ItemStack stack) {
        World world = livingEntity.level;
        if(!world.isClientSide()) {
            BlockPos pos = livingEntity.blockPosition();

            if (livingEntity.tickCount % 20 == 0) { // For some reasons I need to delay it or it will cause strange behavior
                for (MobEntity mob : world.getEntitiesOfClass(MobEntity.class, new AxisAlignedBB(pos.subtract(new Vector3i(6, 5, 6)), pos.offset(new Vector3i(6, 5, 6))), entity -> entity instanceof IAngerable)) {
                    IAngerable angerable = (IAngerable) mob;
                    if (angerable.getPersistentAngerTarget() == null || !angerable.getPersistentAngerTarget().equals(livingEntity.getUUID()))
                        continue;

                    angerable.stopBeingAngry();
                }
            }
        }
    }
}
