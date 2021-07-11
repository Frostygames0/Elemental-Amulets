package frostygames0.elementalamulets.client.models;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class AmuletModel extends EntityModel<Entity> {
	private final ModelRenderer jewel;
	private final ModelRenderer string;

	public AmuletModel() {
		textureWidth = 16;
		textureHeight = 16;

		jewel = new ModelRenderer(this);
		jewel.setRotationPoint(1.0F, 27.75F, -0.25F);
		jewel.setTextureOffset(0, 4).addBox(-2.0F, -23.0F, -3.0F, 2.0F, 2.0F, 1.0F, 0.0F, false);
		jewel.setTextureOffset(7, 0).addBox(-3.0F, -23.0F, -3.0F, 1.0F, 2.0F, 1.0F, 0.0F, false);
		jewel.setTextureOffset(5, 6).addBox(-2.0F, -24.0F, -3.0F, 2.0F, 1.0F, 1.0F, 0.0F, false);
		jewel.setTextureOffset(0, 7).addBox(0.0F, -23.0F, -3.0F, 1.0F, 2.0F, 1.0F, 0.0F, false);
		jewel.setTextureOffset(6, 4).addBox(-2.0F, -21.0F, -3.0F, 2.0F, 1.0F, 1.0F, 0.0F, false);

		string = new ModelRenderer(this);
		string.setRotationPoint(0.0F, -24.0F, 0.0F);
		jewel.addChild(string);
		string.setTextureOffset(8, 8).addBox(0.0F, -1.0F, -2.0F, 1.0F, 1.0F, 0.0F, 0.0F, false);
		string.setTextureOffset(3, 7).addBox(1.0F, -2.0F, -2.0F, 1.0F, 1.0F, 0.0F, 0.0F, false);
		string.setTextureOffset(6, 8).addBox(2.0F, -4.0F, -2.0F, 1.0F, 2.0F, 0.0F, 0.0F, false);
		string.setTextureOffset(2, 0).addBox(2.0F, -4.0F, -2.0F, 1.0F, 0.0F, 3.0F, 0.0F, false);
		string.setTextureOffset(0, 3).addBox(-3.0F, -4.0F, 2.0F, 4.0F, 0.0F, 1.0F, 0.0F, false);
		string.setTextureOffset(0, 1).addBox(1.0F, -4.0F, 1.0F, 1.0F, 0.0F, 1.0F, 0.0F, false);
		string.setTextureOffset(0, 0).addBox(-4.0F, -4.0F, 1.0F, 1.0F, 0.0F, 1.0F, 0.0F, false);
		string.setTextureOffset(0, 0).addBox(-5.0F, -4.0F, -2.0F, 1.0F, 0.0F, 3.0F, 0.0F, false);
		string.setTextureOffset(4, 8).addBox(-5.0F, -4.0F, -2.0F, 1.0F, 2.0F, 0.0F, 0.0F, false);
		string.setTextureOffset(5, 4).addBox(-4.0F, -2.0F, -2.0F, 1.0F, 1.0F, 0.0F, 0.0F, false);
		string.setTextureOffset(0, 2).addBox(-3.0F, -1.0F, -2.0F, 1.0F, 1.0F, 0.0F, 0.0F, false);
	}

	@Override
	public void setRotationAngles(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch){
		//previously the render function, render code was moved to a method below
	}

	@Override
	public void render(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha){
		jewel.render(matrixStack, buffer, packedLight, packedOverlay);
	}

	public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}
}