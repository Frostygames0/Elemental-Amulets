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

package frostygames0.elementalamulets.items.amulets;

import frostygames0.elementalamulets.config.ModConfig;
import frostygames0.elementalamulets.init.ModTags;
import frostygames0.elementalamulets.util.WorldUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemCooldowns;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.state.BlockState;
import top.theillusivec4.curios.api.SlotContext;


import java.util.Random;

public class EarthAmuletItem extends AmuletItem {
    public EarthAmuletItem(Item.Properties properties) {
        super(new AmuletItem.Properties(properties)
                .usesCurioMethods()
                .generates()
                .hasTier());
    }

    @Override
    public void curioTick(SlotContext ctx, ItemStack stack) {
        LivingEntity livingEntity = ctx.entity();
        Level world = livingEntity.level;
        if (!world.isClientSide()) {
            if (livingEntity instanceof Player player) {
                ItemCooldowns cooldownTracker = player.getCooldowns();
                if (!cooldownTracker.isOnCooldown(this)) {
                    int boosted = boostLocalPlants(world, stack, player.blockPosition(), world.random);
                    if (boosted > 0 && this.getTier(stack) > 1) {
                        this.regenerate(livingEntity, stack, boosted);
                        cooldownTracker.addCooldown(this, ModConfig.CachedValues.EARTH_AMULET_COOLDOWN);
                    }
                }
            }
        }
    }

    private int boostLocalPlants(Level world, ItemStack amulet, BlockPos centerPos, Random random) {
        int boosted = 0;
        for (BlockPos blockPos : BlockPos.betweenClosed(centerPos.getX() - 4, centerPos.getY() - 1, centerPos.getZ() - 4,
                centerPos.getX() + 4, centerPos.getY() + 3, centerPos.getZ() + 4)) {

            BlockState blockState = world.getBlockState(blockPos);

            if (blockState.getBlock() instanceof BonemealableBlock growable) {
                if (random.nextInt(50) <= this.getTier(amulet)) {
                    if (growable.isValidBonemealTarget(world, blockPos, blockState, false) &&
                            blockState.is(ModTags.Blocks.EARTH_AMULET_BOOSTABLE)) {

                        growable.performBonemeal((ServerLevel) world, random, blockPos, blockState);
                        boosted++;
                    }
                }
            }
        }
        return boosted;
    }

    private void regenerate(LivingEntity living, ItemStack stack, int boosted) {
        Level level = living.level;
        if (getTier(stack) > 1) {
            if (WorldUtil.isNatural(level, living.blockPosition()) || ModConfig.CachedValues.EARTH_AMULET_IGNORE_NATURALITY) {
                living.heal(0.1f * boosted);

                if (living instanceof Player) {
                    ((Player) living).displayClientMessage(new TranslatableComponent("item.elementalamulets.earth_amulet.regeneration").withStyle(ChatFormatting.GREEN), true);
                }
            }
        }
    }
}
