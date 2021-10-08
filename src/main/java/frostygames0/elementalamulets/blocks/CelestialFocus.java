/*
 *    This file is part of Elemental Amulets.
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
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;


import javax.annotation.Nullable;
import java.util.List;

/**
 * @author Frostygames0
 * @date 15.09.2021 18:33
 */
public class CelestialFocus extends Block {
    public CelestialFocus(Properties p_i48440_1_) {
        super(p_i48440_1_);
    }

    @Override
    public void appendHoverText(ItemStack p_190948_1_, @Nullable IBlockReader p_190948_2_, List<ITextComponent> tooltip, ITooltipFlag p_190948_4_) {
        super.appendHoverText(p_190948_1_, p_190948_2_, tooltip, p_190948_4_);
        tooltip.add(new TranslationTextComponent("block.elementalamulets.celestial_focus.tooltip").withStyle(TextFormatting.GRAY));
    }

    @Override
    public boolean propagatesSkylightDown(BlockState p_200123_1_, IBlockReader p_200123_2_, BlockPos p_200123_3_) {
        return true;
    }

    @Override
    public ActionResultType use(BlockState pState, World pLevel, BlockPos pPos, PlayerEntity pPlayer, Hand pHand, BlockRayTraceResult pHit) {
        if(pLevel.isClientSide()) {
            return ActionResultType.SUCCESS;
        }
        TileEntity te = pLevel.getBlockEntity(pPos.below());
        if(te instanceof ElementalCombinatorTile) {
            ElementalCombinatorTile elementalCombinatorTile = (ElementalCombinatorTile) te;
            if(!pPlayer.isShiftKeyDown()) {
                NetworkHooks.openGui((ServerPlayerEntity) pPlayer, elementalCombinatorTile, elementalCombinatorTile.getBlockPos());
            } else {
                elementalCombinatorTile.startCombination();
            }
        }
        return ActionResultType.CONSUME;
    }
}
