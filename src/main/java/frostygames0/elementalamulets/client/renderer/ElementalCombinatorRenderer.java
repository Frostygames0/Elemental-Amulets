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

package frostygames0.elementalamulets.client.renderer;

import com.mojang.blaze3d.matrix.MatrixStack;
import frostygames0.elementalamulets.blocks.tiles.ElementalCombinatorTile;
import frostygames0.elementalamulets.config.ModConfig;
import frostygames0.elementalamulets.init.ModTiles;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.items.CapabilityItemHandler;

/**
 * @author Frostygames0
 * @date 13.06.2021 18:11
 */
public class ElementalCombinatorRenderer extends TileEntityRenderer<ElementalCombinatorTile> {
    public ElementalCombinatorRenderer(TileEntityRendererDispatcher rendererDispatcherIn) {
        super(rendererDispatcherIn);
    }

    @Override
    public void render(ElementalCombinatorTile tileEntityIn, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn) {
        World world = tileEntityIn.getLevel();

        ItemStack stack = tileEntityIn.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).map(h -> h.getStackInSlot(0)).orElse(ItemStack.EMPTY);
        ItemRenderer renderer = Minecraft.getInstance().getItemRenderer();

        int upperLight = WorldRenderer.getLightColor(world, tileEntityIn.getBlockPos().above());
        float angle = (world.getGameTime() + partialTicks) * 2.0F;//(System.currentTimeMillis() / ModConfig.CachedValues.COMBINATOR_STACK_ROTATION_SPEED) % 360;

        if(ModConfig.CachedValues.RENDER_COMBINATOR_STACK) {

            matrixStackIn.pushPose();

            matrixStackIn.translate(0.5, 1.4, 0.5);
            matrixStackIn.scale(0.6F, 0.6F, 0.6F);
            matrixStackIn.mulPose(Vector3f.YP.rotationDegrees(angle));

            IBakedModel model = renderer.getModel(stack, world, null);
            renderer.render(stack, ItemCameraTransforms.TransformType.FIXED, true, matrixStackIn, bufferIn, upperLight, combinedOverlayIn, model);
            matrixStackIn.popPose();
        }
    }

    public static void register() {
        ClientRegistry.bindTileEntityRenderer(ModTiles.ELEMENTAL_COMBINATOR_TILE.get(), ElementalCombinatorRenderer::new);
    }
}
