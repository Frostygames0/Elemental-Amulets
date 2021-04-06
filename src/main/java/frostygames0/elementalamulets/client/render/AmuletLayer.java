package frostygames0.elementalamulets.client.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.entity.LivingEntity;
import top.theillusivec4.curios.api.CuriosApi;
/*
public class AmuletLayer<T extends LivingEntity, M extends EntityModel<T>> extends LayerRenderer<T, M> {
    public AmuletLayer(IEntityRenderer<T, M> entityRendererIn) {
        super(entityRendererIn);
    }

    @Override
    public void render(MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn, T entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        matrixStackIn.push();
        CuriosApi.getCuriosHelper().getCuriosHandler(entitylivingbaseIn).ifPresent();
    }
}
*/