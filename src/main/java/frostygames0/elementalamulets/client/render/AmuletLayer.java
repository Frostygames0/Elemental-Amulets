package frostygames0.elementalamulets.client.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import frostygames0.elementalamulets.items.interfaces.IAmuletItem;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.ElytraLayer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.TextureStitchEvent;
import top.theillusivec4.curios.api.CuriosApi;
/* Realised that Curios API already has render layer for curios but hey at least I know how they workk
public class AmuletLayer<T extends LivingEntity, M extends EntityModel<T>> extends LayerRenderer<T, M> {
    public AmuletLayer(IEntityRenderer<T, M> entityRendererIn) {
        super(entityRendererIn);
    }

    @Override
    public void render(MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn,
                        T entitylivingbaseIn, float limbSwing, float limbSwingAmount,
                        float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        matrixStackIn.push();
        CuriosApi.getCuriosHelper().findEquippedCurio(item -> item.getItem() instanceof IAmuletItem, entitylivingbaseIn).ifPresent(triple -> {
            ItemStack stack = triple.getRight();
            int index = triple.getMiddle();
            String id = triple.getLeft();
            CuriosApi.getCuriosHelper().getCurio(stack).ifPresent(curio -> {
                if(curio.canRender(id, index, entitylivingbaseIn)) {
                    curio.render(id, index, matrixStackIn, bufferIn, packedLightIn, entitylivingbaseIn,
                            limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch);
                    matrixStackIn.pop();
                }
            });
        });
    }
}
*/