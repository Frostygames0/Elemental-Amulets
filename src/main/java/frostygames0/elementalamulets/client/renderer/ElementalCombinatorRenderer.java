package frostygames0.elementalamulets.client.renderer;

import com.mojang.blaze3d.matrix.MatrixStack;
import frostygames0.elementalamulets.blocks.tiles.ElementalCombinatorTile;
import frostygames0.elementalamulets.config.ModConfig;
import frostygames0.elementalamulets.core.init.ModTiles;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.ItemRenderer;
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
        ItemStack stack = tileEntityIn.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).map(h -> h.getStackInSlot(0)).orElse(ItemStack.EMPTY);
        ItemRenderer renderer = Minecraft.getInstance().getItemRenderer();

        long time = System.currentTimeMillis();
        float angle = (time / ModConfig.cached.COMBINATOR_STACK_ROTATION_SPEED) % 360;
        World world = tileEntityIn.getWorld();

        if(ModConfig.cached.RENDER_COMBINATOR_STACK) {
            matrixStackIn.push();

            matrixStackIn.translate(0.5, 1.4, 0.5);
            matrixStackIn.scale(0.6F, 0.6F, 0.6F);
            matrixStackIn.rotate(Vector3f.YP.rotationDegrees(angle));

            IBakedModel model = renderer.getItemModelWithOverrides(stack, world, null);
            renderer.renderItem(stack, ItemCameraTransforms.TransformType.FIXED, true, matrixStackIn, bufferIn, combinedLightIn, combinedOverlayIn, model);

            matrixStackIn.pop();
        }
    }

    public static void register() {
        ClientRegistry.bindTileEntityRenderer(ModTiles.ELEMENTAL_COMBINATOR_TILE.get(), ElementalCombinatorRenderer::new);
    }
}
