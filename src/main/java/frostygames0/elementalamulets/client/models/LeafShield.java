package frostygames0.elementalamulets.client.models;// Made with Blockbench 3.9.2
// Exported for Minecraft version 1.15 - 1.16 with MCP mappings
// Paste this class into your mod and generate all required imports


import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.LivingEntity;

public class LeafShield extends EntityModel<LivingEntity> {
	private final ModelRenderer leafPart1;
	private final ModelRenderer cube_r1;
	private final ModelRenderer leafPart2;
	private final ModelRenderer cube_r2;

	public LeafShield() {
		textureWidth = 64;
		textureHeight = 64;

		leafPart1 = new ModelRenderer(this);
		leafPart1.setRotationPoint(0.0F, 24.0F, 0.0F);
		leafPart1.setTextureOffset(0, 0).addBox(-8.0F, -12.0F, -4.0F, 1.0F, 6.0F, 8.0F, 0.0F, false);
		leafPart1.setTextureOffset(20, 0).addBox(-4.0F, -12.0F, -8.0F, 8.0F, 6.0F, 1.0F, 0.0F, false);

		cube_r1 = new ModelRenderer(this);
		cube_r1.setRotationPoint(0.0F, 0.0F, 0.0F);
		leafPart1.addChild(cube_r1);
		setRotationAngle(cube_r1, 0.0F, 0.7854F, 0.0F);
		cube_r1.setTextureOffset(0, 27).addBox(-3.0F, -12.0F, -8.35F, 6.0F, 6.0F, 1.0F, 0.0F, false);

		leafPart2 = new ModelRenderer(this);
		leafPart2.setRotationPoint(0.0F, 24.0F, 0.0F);
		leafPart2.setTextureOffset(10, 6).addBox(7.0F, -12.0F, -4.0F, 1.0F, 6.0F, 8.0F, 0.0F, false);
		leafPart2.setTextureOffset(0, 20).addBox(-4.0F, -12.0F, 7.0F, 8.0F, 6.0F, 1.0F, 0.0F, false);

		cube_r2 = new ModelRenderer(this);
		cube_r2.setRotationPoint(0.0F, 0.0F, 0.0F);
		leafPart2.addChild(cube_r2);
		setRotationAngle(cube_r2, 0.0F, 0.7854F, 0.0F);
		cube_r2.setTextureOffset(20, 7).addBox(-3.0F, -12.0F, 7.35F, 6.0F, 6.0F, 1.0F, 0.0F, false);
		cube_r2.setTextureOffset(18, 20).addBox(-3.0F, -12.0F, 7.35F, 6.0F, 6.0F, 1.0F, 0.0F, false);
	}

	@Override
	public void setRotationAngles(LivingEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch){
		//previously the render function, render code was moved to a method below
	}

	@Override
	public void render(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha){
		leafPart1.render(matrixStack, buffer, packedLight, packedOverlay);
		leafPart2.render(matrixStack, buffer, packedLight, packedOverlay);
	}

	public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}
}