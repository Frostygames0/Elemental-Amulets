package frostygames0.elementalamulets.client.models;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import frostygames0.elementalamulets.items.amulets.AmuletItem;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;


import static frostygames0.elementalamulets.ElementalAmulets.modPrefix;

public class AmuletModel<T extends LivingEntity> extends EntityModel<T> {
	private final ModelRenderer amulet;
	private final ModelRenderer string;

	public AmuletModel() {
		textureWidth = 16;
		textureHeight = 16;

		amulet = new ModelRenderer(this);
		amulet.setRotationPoint(0.0F, -1.0F, 0.0F);
		amulet.setTextureOffset(6, 2).addBox(-1.0F, 3.0F, -3.0F, 2.0F, 2.0F, 1.0F, 0.0F, false);

		string = new ModelRenderer(this);
		string.setRotationPoint(0.0F, 0.0F, 0.0F);
		amulet.addChild(string);
		string.setTextureOffset(0, 2).addBox(1.0F, 2.0F, -3.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
		string.setTextureOffset(0, 2).addBox(-2.0F, 2.0F, -3.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
		string.setTextureOffset(0, 2).addBox(-3.0F, 1.0F, -3.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
		string.setTextureOffset(0, 2).addBox(2.0F, 1.0F, -3.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
		string.setTextureOffset(0, 2).addBox(3.0F, 0.0F, -2.0F, 1.0F, 1.0F, 4.0F, 0.0F, false);
		string.setTextureOffset(0, 2).addBox(-4.0F, 0.0F, -2.0F, 1.0F, 1.0F, 4.0F, 0.0F, false);
		string.setTextureOffset(0, 0).addBox(-3.0F, 0.0F, 1.0F, 6.0F, 1.0F, 1.0F, 0.0F, false);
	}

	@Override
	public void setRotationAngles(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch){
		//previously the render function, render code was moved to a method below
	}

	@Override
	public void render(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha){
		amulet.render(matrixStack, buffer, packedLight, packedOverlay);
	}

	public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}

	public static ResourceLocation getTexture(AmuletItem item, ItemStack stack) {
		int tier = MathHelper.clamp(item.getTier(stack), 0, AmuletItem.MAX_TIER);
		return modPrefix("textures/entity/amulets/tier_"+tier+"/"+item.getRegistryName().getPath()+".png");
	}
}