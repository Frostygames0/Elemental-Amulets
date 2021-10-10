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

package frostygames0.elementalamulets.blocks;

import frostygames0.elementalamulets.blocks.tiles.ElementalCombinatorTile;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.PushReaction;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraftforge.items.CapabilityItemHandler;


import javax.annotation.Nullable;

@SuppressWarnings("deprecation")
public class ElementalCombinator extends Block {
    public static final BooleanProperty COMBINING = BooleanProperty.create("combining");
    public ElementalCombinator(Properties properties) {
        super(properties);
        this.registerDefaultState(this.defaultBlockState().setValue(COMBINING, false));
    }

    @Override
    public ActionResultType use(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        if(worldIn.isClientSide()) {
            return ActionResultType.SUCCESS;
        }
        TileEntity te = worldIn.getBlockEntity(pos);
        if(te instanceof ElementalCombinatorTile) {
            ElementalCombinatorTile elementalCombinatorTile = (ElementalCombinatorTile) te;
            if(!player.isShiftKeyDown()) {
                NetworkHooks.openGui((ServerPlayerEntity) player, elementalCombinatorTile, elementalCombinatorTile.getBlockPos());
            } else {
                elementalCombinatorTile.startCombination();
            }
        } else {
            throw new IllegalStateException("Tile Entity is not correct! Cannot do any action!");
        }
        return ActionResultType.CONSUME;
    }

    @Override
    public PushReaction getPistonPushReaction(BlockState state) {
        return PushReaction.BLOCK;
    }

    @Override
    public void onRemove(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
        if(!state.is(newState.getBlock())) {
            TileEntity tileEntity = worldIn.getBlockEntity(pos);
            if(tileEntity instanceof ElementalCombinatorTile) {
                tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(h -> {
                    for (int i = 0; i < h.getSlots(); i++) {
                        popResource(worldIn, pos, h.getStackInSlot(i));
                    }
                });
            }
            super.onRemove(state,worldIn,pos, newState, isMoving);
        }
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new ElementalCombinatorTile();
    }

    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(COMBINING);
    }
}
