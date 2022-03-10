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

package frostygames0.elementalamulets.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.Nullable;


import java.util.Random;

@SuppressWarnings("deprecation")
public class MeltingMagmaBlock extends Block {
    public static final IntegerProperty AGE = BlockStateProperties.AGE_3;

    public MeltingMagmaBlock(BlockBehaviour.Properties p_53564_) {
        super(p_53564_);
        this.registerDefaultState(this.stateDefinition.any().setValue(AGE, 0));
    }

    @Override
    public void playerDestroy(Level pLevel, Player pPlayer, BlockPos pPos, BlockState pState, @Nullable BlockEntity pBlockEntity, ItemStack pTool) {
        super.playerDestroy(pLevel, pPlayer, pPos, pState, pBlockEntity, pTool);

        Material material = pLevel.getBlockState(pPos.below()).getMaterial();
        if (material.blocksMotion() || material.isLiquid()) {
            pLevel.setBlockAndUpdate(pPos, Blocks.LAVA.defaultBlockState());
        }
    }

    @Override
    public void stepOn(Level pLevel, BlockPos pPos, BlockState pState, Entity pEntity) {
        if (!pEntity.fireImmune() && pEntity instanceof LivingEntity && !EnchantmentHelper.hasFrostWalker((LivingEntity) pEntity)) {
            pEntity.hurt(DamageSource.HOT_FLOOR, 1.0F);
        }

        super.stepOn(pLevel, pPos, pState, pEntity);
    }

    @Override
    public void tick(BlockState pState, ServerLevel pLevel, BlockPos pPos, Random pRand) {
        if ((pRand.nextInt(3) == 0 || this.fewerNeigboursThan(pLevel, pPos, 4)) && this.slightlyMelt(pState, pLevel, pPos)) {
            BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos();

            for (Direction direction : Direction.values()) {
                mutablePos.setWithOffset(pPos, direction);
                BlockState blockstate = pLevel.getBlockState(mutablePos);
                if (blockstate.is(this) && !this.slightlyMelt(blockstate, pLevel, mutablePos)) {
                    pLevel.scheduleTick(mutablePos, this, Mth.nextInt(pRand, 20, 40));
                }
            }
        } else {
            pLevel.scheduleTick(pPos, this, Mth.nextInt(pRand, 20, 40));
        }
    }

    @Override
    public void neighborChanged(BlockState pState, Level pLevel, BlockPos pPos, Block pBlock, BlockPos pFromPos, boolean pIsMoving) {
        if (pBlock.defaultBlockState().is(this) && this.fewerNeigboursThan(pLevel, pPos, 2)) {
            this.melt(pLevel, pPos);
        }

        super.neighborChanged(pState, pLevel, pPos, pBlock, pFromPos, pIsMoving);
    }

    private boolean slightlyMelt(BlockState pState, Level pLevel, BlockPos pPos) {
        int i = pState.getValue(AGE);
        if (i < 3) {
            pLevel.setBlock(pPos, pState.setValue(AGE, i + 1), 2);
            return false;
        } else {
            this.melt(pLevel, pPos);
            return true;
        }
    }


    private boolean fewerNeigboursThan(BlockGetter pLevel, BlockPos pPos, int pNeighborsRequired) {
        int i = 0;
        BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos();

        for (Direction direction : Direction.values()) {
            mutablePos.setWithOffset(pPos, direction);
            if (pLevel.getBlockState(mutablePos).is(this)) {
                ++i;
                if (i >= pNeighborsRequired) {
                    return false;
                }
            }
        }

        return true;
    }

    private void melt(Level pLevel, BlockPos pPos) {
        pLevel.setBlockAndUpdate(pPos, Blocks.LAVA.defaultBlockState());
        pLevel.neighborChanged(pPos, Blocks.LAVA, pPos);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(AGE);
    }

    @Override
    public ItemStack getCloneItemStack(BlockState state, HitResult target, BlockGetter world, BlockPos pos, Player player) {
        return ItemStack.EMPTY;
    }
}
