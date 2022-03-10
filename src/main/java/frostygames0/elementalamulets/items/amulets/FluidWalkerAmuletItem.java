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

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import top.theillusivec4.curios.api.SlotContext;


import java.util.function.Supplier;

public class FluidWalkerAmuletItem extends AmuletItem {

    private final Supplier<? extends Block> block;
    private final Supplier<? extends Block> fluid;

    public FluidWalkerAmuletItem(Item.Properties properties, Supplier<? extends Block> block, Supplier<? extends Block> fluid) {
        super(new Properties(properties)
                .hasTier()
                .usesCurioMethods());

        this.block = block;
        this.fluid = fluid;
    }

    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        LivingEntity entity = slotContext.entity();
        Level level = entity.level;

        if (!level.isClientSide()) {
            if (entity.isOnGround()) {
                Block blockToFreeze = block.get();

                BlockState blockstate = blockToFreeze.defaultBlockState();
                BlockPos pos = entity.blockPosition();

                BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos();

                float freeze = Math.min(5, this.getTier(stack) + 1) / 2.0f;

                for (BlockPos blockpos : BlockPos.betweenClosed(pos.offset(-freeze, -1.0f, -freeze), pos.offset(freeze, -1.0f, freeze))) {
                    if (blockpos.closerToCenterThan(entity.position(), freeze)) {

                        mutablePos.set(blockpos.getX(), blockpos.getY() + 1, blockpos.getZ());
                        BlockState blockstate1 = level.getBlockState(mutablePos);

                        if (blockstate1.isAir()) {
                            BlockState blockstate2 = level.getBlockState(blockpos);

                            Block fluidBlock = fluid.get();
                            boolean canBeFrozen = fluidBlock instanceof LiquidBlock
                                    && blockstate2.is(fluidBlock) && blockstate2.getValue(LiquidBlock.LEVEL) == 0;

                            if (canBeFrozen && blockstate.canSurvive(level, blockpos) && level.isUnobstructed(blockstate, blockpos, CollisionContext.empty())) {
                                level.setBlockAndUpdate(blockpos, blockstate);
                                level.scheduleTick(blockpos, blockToFreeze, entity.getRandom().nextInt(60, 120));

                                if (entity.getRandom().nextInt((int) (1.5f * this.getTier(stack))) == 0) {
                                    stack.hurtAndBreak(1, entity, ent -> this.onAmuletBreak(slotContext));
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
