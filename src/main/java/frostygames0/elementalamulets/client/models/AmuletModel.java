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
import frostygames0.elementalamulets.items.amulets.AmuletItem;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;


import static frostygames0.elementalamulets.ElementalAmulets.modPrefix;

public class AmuletModel<T extends Entity> extends EntityModel<T> {
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(modPrefix("amulet"), "amulet");

    private final ModelPart jewel;

    public AmuletModel(ModelPart root) {
        this.jewel = root.getChild("jewel");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition jewel = partdefinition.addOrReplaceChild("jewel", CubeListBuilder.create().texOffs(0, 1).addBox(-3.0F, -24.0F, -3.0F, 4.0F, 4.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(0, 8).addBox(-4.0F, -23.0F, -3.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(5, 6).addBox(-2.0F, -25.0F, -3.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(0, 11).addBox(1.0F, -23.0F, -3.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(5, 9).addBox(-2.0F, -20.0F, -3.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(1.0F, 28.75F, -1.0F));

        PartDefinition string = jewel.addOrReplaceChild("string", CubeListBuilder.create().texOffs(5, 8).addBox(0.0F, -1.0F, -2.0F, 2.0F, 1.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(0, 7).addBox(2.0F, -2.0F, -2.0F, 1.0F, 1.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(3, 7).addBox(3.0F, -4.0F, -2.0F, 1.0F, 2.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(0, 6).addBox(3.0F, -4.0F, -2.0F, 1.0F, 0.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(-4.0F, -4.0F, 2.0F, 6.0F, 0.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(0, 6).addBox(2.0F, -4.0F, 1.0F, 1.0F, 0.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(0, 6).addBox(-5.0F, -4.0F, 1.0F, 1.0F, 0.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(0, 6).addBox(-6.0F, -4.0F, -2.0F, 1.0F, 0.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(3, 7).addBox(-6.0F, -4.0F, -2.0F, 1.0F, 2.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(0, 7).addBox(-5.0F, -2.0F, -2.0F, 1.0F, 1.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(5, 8).addBox(-4.0F, -1.0F, -2.0F, 2.0F, 1.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -25.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 16, 16);
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        jewel.render(poseStack, buffer, packedLight, packedOverlay);
    }

    public static ResourceLocation getTexture(AmuletItem item, ItemStack stack) {
        int tier = Mth.clamp(item.getTier(stack), 0, AmuletItem.MAX_TIER);
        return modPrefix("textures/entity/amulets/tier_" + tier + "/" + item.getRegistryName().getPath() + ".png");
    }
}
