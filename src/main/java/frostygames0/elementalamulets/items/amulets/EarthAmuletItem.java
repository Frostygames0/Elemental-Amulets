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

import frostygames0.elementalamulets.config.ModConfig;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.IGrowable;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.CooldownTracker;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;


import java.util.Random;

/**
 * @author Frostygames0
 * @date 19.09.2021 22:50
 */
public class EarthAmuletItem extends AmuletItem{
    public EarthAmuletItem(Properties properties) {
        super(properties);
    }

    @Override
    public void curioTick(String identifier, int index, LivingEntity livingEntity, ItemStack stack) {
        super.curioTick(identifier, index, livingEntity, stack);
        World world = livingEntity.level;
        if(!world.isClientSide()) {
            if(livingEntity instanceof PlayerEntity) {
                PlayerEntity player = (PlayerEntity) livingEntity;
                CooldownTracker cooldownTracker = player.getCooldowns();
                if (!cooldownTracker.isOnCooldown(this)) {
                    if(boostLocalPlants(world, stack, player.blockPosition(), player.getRandom()))
                        cooldownTracker.addCooldown(this, ModConfig.CachedValues.EARTH_AMULET_COOLDOWN);
                }
            }
        }
    }

    private boolean boostLocalPlants(World world, ItemStack amulet, BlockPos centerPos, Random random) {
        for (BlockPos blockPos : BlockPos.betweenClosed(centerPos.getX() - 4, centerPos.getY() - 1, centerPos.getZ() - 4,
                centerPos.getX() + 4, centerPos.getY() + 3, centerPos.getZ() + 4)) {

            BlockState blockState = world.getBlockState(blockPos);
            Block block = blockState.getBlock();

            if (block instanceof IGrowable) {
                IGrowable growable = (IGrowable) block;
                if (random.nextInt(50) <= this.getTier(amulet)) {
                    if (growable.isValidBonemealTarget(world, blockPos, blockState, false)) {
                        growable.performBonemeal((ServerWorld) world, random, blockPos, blockState);
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
