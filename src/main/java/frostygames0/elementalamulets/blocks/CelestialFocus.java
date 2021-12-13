/*
 *  Copyright (c) 2021
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

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;


import javax.annotation.Nullable;
import java.util.List;

/**
 * @author Frostygames0
 * @date 15.09.2021 18:33
 */
@SuppressWarnings("deprecation")
public class CelestialFocus extends Block {

    private static final VoxelShape STAND = VoxelShapes.or(box(0, 0, 0, 2, 16, 2),
            box(0, 0, 14, 2, 16, 16),
            box(14, 0, 0, 16, 16, 2),
            box(14, 0, 14, 16, 16, 16));

    private static final VoxelShape STICKS = VoxelShapes.or(box(14, 10, 2, 16, 12, 14),
            box(0, 10, 2, 2, 12, 14),
            box(2, 10, 0, 14, 12, 2),
            box(2, 10, 14, 14, 12, 16));

    private static final VoxelShape LENS = box(5, 14.25, 5, 11, 15.25, 11);

    public static final VoxelShape CELESTIAL_FOCUS = VoxelShapes.or(STAND, STICKS, LENS);

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
    public VoxelShape getShape(BlockState pState, IBlockReader pLevel, BlockPos pPos, ISelectionContext pContext) {
        return CELESTIAL_FOCUS;
    }
}
