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

import frostygames0.elementalamulets.blocks.entities.ElementalCombinatorBlockEntity;
import frostygames0.elementalamulets.config.ModConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.network.NetworkHooks;


import javax.annotation.Nullable;

@SuppressWarnings("deprecation")
public class ElementalCombinator extends Block implements EntityBlock {
    public static final BooleanProperty COMBINING = BooleanProperty.create("combining");

    public ElementalCombinator(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(COMBINING, false));
    }

    @Override
    public InteractionResult use(BlockState state, Level worldIn, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hit) {
        if (worldIn.isClientSide()) {
            return InteractionResult.SUCCESS;
        }
        BlockEntity te = worldIn.getBlockEntity(pos);
        if (te instanceof ElementalCombinatorBlockEntity elementalCombinatorBlockEntity) {
            if (!player.isShiftKeyDown()) {
                NetworkHooks.openGui((ServerPlayer) player, elementalCombinatorBlockEntity, elementalCombinatorBlockEntity.getBlockPos());
            } else {
                if (ModConfig.CachedValues.OLD_FASHIONED_WAY) elementalCombinatorBlockEntity.startCombination();
            }
        } else {
            throw new IllegalStateException("Block Entity is not correct! Cannot do any action!");
        }
        return InteractionResult.CONSUME;
    }

    @Override
    public PushReaction getPistonPushReaction(BlockState state) {
        return PushReaction.BLOCK;
    }

    @Override
    public void onRemove(BlockState state, Level worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!state.is(newState.getBlock())) {
            BlockEntity tileEntity = worldIn.getBlockEntity(pos);
            if (tileEntity instanceof ElementalCombinatorBlockEntity) {
                tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(h -> {
                    for (int i = 0; i < h.getSlots(); i++) {
                        popResource(worldIn, pos, h.getStackInSlot(i));
                    }
                });
            }
            super.onRemove(state, worldIn, pos, newState, isMoving);
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(COMBINING);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new ElementalCombinatorBlockEntity(pPos, pState);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        if (!pLevel.isClientSide()) {
            return (lvl, pos, stt, te) -> {
                if (te instanceof ElementalCombinatorBlockEntity tile) tile.serverTick();
            };
        }
        return null;
    }
}
