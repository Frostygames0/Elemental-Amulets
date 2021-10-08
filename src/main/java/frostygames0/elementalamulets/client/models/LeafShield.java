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

package frostygames0.elementalamulets.client.models;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.LivingEntity;

public class LeafShield<T extends LivingEntity> extends EntityModel<T> {
	private final ModelRenderer leafPart1;
	private final ModelRenderer cube_r1_r1;
	private final ModelRenderer leafPart2;
	private final ModelRenderer cube_r2_r1;

	public LeafShield() {
		texWidth = 64;
		texHeight = 64;

		leafPart1 = new ModelRenderer(this);
		leafPart1.setPos(0.0F, 24.0F, 0.0F);
		leafPart1.texOffs(10, 6).addBox(-8.0F, -12.0F, -4.0F, 1.0F, 6.0F, 8.0F, 0.0F, false);
		leafPart1.texOffs(20, 0).addBox(-4.0F, -12.0F, -8.0F, 8.0F, 6.0F, 1.0F, 0.0F, false);

		cube_r1_r1 = new ModelRenderer(this);
		cube_r1_r1.setPos(0.0F, 0.0F, 0.0F);
		leafPart1.addChild(cube_r1_r1);
		setRotationAngle(cube_r1_r1, 0.0F, 0.7854F, 0.0F);
		cube_r1_r1.texOffs(20, 7).addBox(-3.0F, -12.0F, -8.35F, 6.0F, 6.0F, 1.0F, 0.0F, false);

		leafPart2 = new ModelRenderer(this);
		leafPart2.setPos(0.0F, 24.0F, 0.0F);
		leafPart2.texOffs(0, 0).addBox(7.0F, -12.0F, -4.0F, 1.0F, 6.0F, 8.0F, 0.0F, false);
		leafPart2.texOffs(0, 20).addBox(-4.0F, -12.0F, 7.0F, 8.0F, 6.0F, 1.0F, 0.0F, false);

		cube_r2_r1 = new ModelRenderer(this);
		cube_r2_r1.setPos(0.0F, 0.0F, 0.0F);
		leafPart2.addChild(cube_r2_r1);
		setRotationAngle(cube_r2_r1, 0.0F, 0.7854F, 0.0F);
		cube_r2_r1.texOffs(18, 20).addBox(-3.0F, -12.0F, 7.35F, 6.0F, 6.0F, 1.0F, 0.0F, false);
	}

	@Override
	public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch){
		//previously the render function, render code was moved to a method below
	}

	@Override
	public void renderToBuffer(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha){
		leafPart1.render(matrixStack, buffer, packedLight, packedOverlay);
		leafPart2.render(matrixStack, buffer, packedLight, packedOverlay);
	}

	public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.xRot = x;
		modelRenderer.yRot = y;
		modelRenderer.zRot = z;
	}
}