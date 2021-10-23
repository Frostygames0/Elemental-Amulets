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
import frostygames0.elementalamulets.util.NBTUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.IGrowable;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;


import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

/**
 * @author Frostygames0
 * @date 19.09.2021 22:50
 */
// TODO: Add more effects, maybe saturation when in forest idk
public class EarthAmulet extends AmuletItem{
    private static final String COOLDOWN_TAG = ElementalAmulets.MOD_ID+":cooldown";
    public EarthAmulet(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        super.appendHoverText(stack, worldIn, tooltip, flagIn);
        tooltip.add(new TranslationTextComponent("item.elementalamulets.wip"));
    }


    @Override
    public void curioTick(String identifier, int index, LivingEntity livingEntity, ItemStack stack) {
        super.curioTick(identifier, index, livingEntity, stack);
        World world = livingEntity.level;
        if(!world.isClientSide()) {
            int cooldown = NBTUtil.getInteger(stack, COOLDOWN_TAG);
            if(cooldown < 1) {
                boostLocalPlants(world, stack, livingEntity.blockPosition(), livingEntity.getRandom());
            }
            if(cooldown > 0) NBTUtil.putInteger(stack, COOLDOWN_TAG, cooldown-1);
            ElementalAmulets.LOGGER.debug(cooldown);
        }
    }

    // TODO: Make it grow plants randomly and not in order
    private void boostLocalPlants(World world, ItemStack amulet, BlockPos centerPos, Random random) {
        for (BlockPos blockPos : BlockPos.betweenClosed(centerPos.getX() - 4, centerPos.getY() - 1, centerPos.getZ() - 4,
                centerPos.getX() + 4, centerPos.getY() + 3, centerPos.getZ() + 4)) {

            BlockState blockState = world.getBlockState(blockPos);
            Block block = blockState.getBlock();

            if (block instanceof IGrowable) {
                IGrowable growable = (IGrowable) block;
                if (random.nextInt(20) <= this.getTier(amulet)) {
                    if (growable.isValidBonemealTarget(world, blockPos, blockState, false)) {
                        growable.performBonemeal((ServerWorld) world, random, blockPos, blockState);

                        ElementalAmulets.LOGGER.debug("Boosted " + block.getName() + " at " + blockPos.toShortString());

                        NBTUtil.putInteger(amulet, COOLDOWN_TAG, 100);
                    }
                }
            }
        }
    }
}
