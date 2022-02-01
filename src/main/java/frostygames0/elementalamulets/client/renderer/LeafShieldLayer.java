/*
 *  Copyright (c) 2021
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

package frostygames0.elementalamulets.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import frostygames0.elementalamulets.client.models.LeafShield;
import frostygames0.elementalamulets.config.ModConfig;
import frostygames0.elementalamulets.init.ModItems;
import frostygames0.elementalamulets.items.amulets.TerraProtectionAmuletItem;
import frostygames0.elementalamulets.util.AmuletUtil;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;


import static frostygames0.elementalamulets.ElementalAmulets.modPrefix;

/**
 * @author Frostygames0
 * @date 10.10.2021 11:33
 */
public class LeafShieldLayer<T extends LivingEntity, M extends EntityModel<T>> extends RenderLayer<T, M> {

    private final LeafShield<T> leafShieldModel;

    public LeafShieldLayer(RenderLayerParent<T, M> entityRenderer, EntityModelSet modelSet) {
        super(entityRenderer);
        leafShieldModel = new LeafShield<>(modelSet.bakeLayer(LeafShield.LAYER_LOCATION));
    }

    @Override
    public void render(PoseStack pMatrixStack, MultiBufferSource pBuffer, int pPackedLight, T pLivingEntity, float pLimbSwing, float pLimbSwingAmount, float pPartialTicks, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
        float angle = (pLivingEntity.tickCount + pPartialTicks) * 3.0F;

        AmuletUtil.getAmuletInSlotOrBelt(ModItems.TERRA_PROTECTION_AMULET.get(), pLivingEntity).ifPresent(triple -> {
            ItemStack stack = triple.stack();
            if (!pLivingEntity.isInvisible()) {
                if (ModConfig.CachedValues.RENDER_LEAF_SHIELD && ((TerraProtectionAmuletItem) stack.getItem()).canProtect(stack)) {
                    pMatrixStack.pushPose();

                    pMatrixStack.scale(2.0f, 2.0f, 2.0f);
                    pMatrixStack.translate(0, -0.7, 0);
                    pMatrixStack.mulPose(Vector3f.YP.rotationDegrees(angle));

                    leafShieldModel.renderToBuffer(pMatrixStack, pBuffer.getBuffer(RenderType.entityTranslucent(modPrefix("textures/entity/amulets/leaf_shield.png"))), pPackedLight, OverlayTexture.NO_OVERLAY, 1.0f, 1.0f, 1.0f, 1.0f);

                    pMatrixStack.popPose();
                }
            }
        });
    }
}
