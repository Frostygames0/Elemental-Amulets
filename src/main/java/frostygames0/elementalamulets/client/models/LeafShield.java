/*
 *  Copyright (c) 2021-2022
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

package frostygames0.elementalamulets.client.models;


import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

public class LeafShield<T extends Entity> extends EntityModel<T> {
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("player"), "leaf_shield");
    private final ModelPart leafPart1;
    private final ModelPart leafPart2;

    public LeafShield(ModelPart root) {
        this.leafPart1 = root.getChild("leafPart1");
        this.leafPart2 = root.getChild("leafPart2");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition leafPart1 = partdefinition.addOrReplaceChild("leafPart1", CubeListBuilder.create().texOffs(10, 6).addBox(-8.0F, -12.0F, -4.0F, 1.0F, 6.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(20, 0).addBox(-4.0F, -12.0F, -8.0F, 8.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition cube_r1_r1 = leafPart1.addOrReplaceChild("cube_r1_r1", CubeListBuilder.create().texOffs(20, 7).addBox(-3.0F, -12.0F, -8.35F, 6.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.7854F, 0.0F));

        PartDefinition leafPart2 = partdefinition.addOrReplaceChild("leafPart2", CubeListBuilder.create().texOffs(0, 0).addBox(7.0F, -12.0F, -4.0F, 1.0F, 6.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(0, 20).addBox(-4.0F, -12.0F, 7.0F, 8.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition cube_r2_r1 = leafPart2.addOrReplaceChild("cube_r2_r1", CubeListBuilder.create().texOffs(18, 20).addBox(-3.0F, -12.0F, 7.35F, 6.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.7854F, 0.0F));

        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        leafPart1.render(poseStack, buffer, packedLight, packedOverlay);
        leafPart2.render(poseStack, buffer, packedLight, packedOverlay);
    }
}