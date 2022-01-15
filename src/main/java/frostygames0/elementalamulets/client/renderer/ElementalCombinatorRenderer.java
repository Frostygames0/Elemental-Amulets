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

package frostygames0.elementalamulets.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import frostygames0.elementalamulets.blocks.tiles.ElementalCombinatorTile;
import frostygames0.elementalamulets.config.ModConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.items.CapabilityItemHandler;

/**
 * @author Frostygames0
 * @date 13.06.2021 18:11
 */
public class ElementalCombinatorRenderer implements BlockEntityRenderer<ElementalCombinatorTile> {
    private BlockRenderDispatcher blockRenderer;

    public ElementalCombinatorRenderer(BlockEntityRendererProvider.Context pContext) {
        this.blockRenderer = pContext.getBlockRenderDispatcher();
    }

    @Override
    public void render(ElementalCombinatorTile tileEntityIn, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int combinedLightIn, int combinedOverlayIn) {
        Level world = tileEntityIn.getLevel();

        ItemStack stack = tileEntityIn.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).map(h -> h.getStackInSlot(0)).orElse(ItemStack.EMPTY);
        ItemRenderer renderer = Minecraft.getInstance().getItemRenderer();

        int upperLight = LevelRenderer.getLightColor(world, tileEntityIn.getBlockPos().above());
        float angle = (world.getGameTime() + partialTicks) * 2.0F;//(System.currentTimeMillis() / ModConfig.CachedValues.COMBINATOR_STACK_ROTATION_SPEED) % 360;

        if (ModConfig.CachedValues.RENDER_COMBINATOR_STACK) {

            matrixStackIn.pushPose();

            matrixStackIn.translate(0.5, 1.4, 0.5);
            matrixStackIn.scale(0.6F, 0.6F, 0.6F);
            matrixStackIn.mulPose(Vector3f.YP.rotationDegrees(angle));

            BakedModel model = renderer.getModel(stack, world, null, upperLight);
            renderer.render(stack, ItemTransforms.TransformType.FIXED, true, matrixStackIn, bufferIn, upperLight, combinedOverlayIn, model);
            matrixStackIn.popPose();
        }
    }
}
