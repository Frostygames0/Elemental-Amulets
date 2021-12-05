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
import frostygames0.elementalamulets.init.ModTags;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.IGrowable;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.CooldownTracker;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.BiomeDictionary;


import java.util.Random;

/**
 * @author Frostygames0
 * @date 19.09.2021 22:50
 */
public class EarthAmuletItem extends AmuletItem {
    public EarthAmuletItem(Properties properties) {
        super(properties);
    }

    @Override
    public void curioTick(String identifier, int index, LivingEntity livingEntity, ItemStack stack) {
        World world = livingEntity.level;
        if (!world.isClientSide()) {
            if (livingEntity instanceof PlayerEntity) {
                PlayerEntity player = (PlayerEntity) livingEntity;
                CooldownTracker cooldownTracker = player.getCooldowns();
                if (!cooldownTracker.isOnCooldown(this)) {
                    int boosted = boostLocalPlants(world, stack, player.blockPosition(), random);
                    if (boosted > 0 && this.getTier(stack) > 1) {
                        this.regenerate(livingEntity, stack, boosted);
                        cooldownTracker.addCooldown(this, ModConfig.CachedValues.EARTH_AMULET_COOLDOWN);
                    }
                }
            }
        }
    }

    private int boostLocalPlants(World world, ItemStack amulet, BlockPos centerPos, Random random) {
        int boosted = 0;
        for (BlockPos blockPos : BlockPos.betweenClosed(centerPos.getX() - 4, centerPos.getY() - 1, centerPos.getZ() - 4,
                centerPos.getX() + 4, centerPos.getY() + 3, centerPos.getZ() + 4)) {

            BlockState blockState = world.getBlockState(blockPos);
            Block block = blockState.getBlock();

            if (block instanceof IGrowable) {
                IGrowable growable = (IGrowable) block;
                if (random.nextInt(50) <= this.getTier(amulet)) {
                    if (growable.isValidBonemealTarget(world, blockPos, blockState, false) &&
                            ModTags.Blocks.EARTH_AMULET_BOOSTABLE.contains(block)) {

                        growable.performBonemeal((ServerWorld) world, random, blockPos, blockState);
                        boosted++;
                    }
                }
            }
        }
        return boosted;
    }

    private void regenerate(LivingEntity living, ItemStack stack, int boosted) {
        World level = living.level;
        if (getTier(stack) > 1) {
            RegistryKey<Biome> biome = level.getBiomeName(living.blockPosition()).orElseThrow(() -> new NullPointerException("Cannot identify a biome!"));
            if (BiomeDictionary.hasType(biome, BiomeDictionary.Type.PLAINS) || BiomeDictionary.hasType(biome, BiomeDictionary.Type.FOREST)) {
                living.heal(0.1f * boosted);

                if (living instanceof PlayerEntity) {
                    ((PlayerEntity) living).displayClientMessage(new TranslationTextComponent("item.elementalamulets.earth_amulet.regeneration").withStyle(TextFormatting.GREEN), true);
                }
            }
        }
    }
}
