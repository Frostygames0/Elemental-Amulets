/*
 *     Copyright (c) 2021
 *
 *     This file is part of Elemental Amulets, a Minecraft Mod.
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

package frostygames0.elementalamulets.client.renderer;

import com.mojang.blaze3d.matrix.MatrixStack;
import frostygames0.elementalamulets.client.models.LeafShield;
import frostygames0.elementalamulets.config.ModConfig;
import frostygames0.elementalamulets.init.ModItems;
import frostygames0.elementalamulets.items.amulets.TerraProtectionAmuletItem;
import frostygames0.elementalamulets.util.AmuletHelper;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.vector.Vector3f;


import static frostygames0.elementalamulets.ElementalAmulets.modPrefix;

/**
 * @author Frostygames0
 * @date 10.10.2021 11:33
 */
public class LeafShieldLayer<T extends LivingEntity, M extends EntityModel<T>> extends LayerRenderer<T,M> {

    private final LeafShield<T> leafShieldModel = new LeafShield<>();

    public LeafShieldLayer(IEntityRenderer<T, M> entityRenderer) {
        super(entityRenderer);
    }

    @Override
    public void render(MatrixStack pMatrixStack, IRenderTypeBuffer pBuffer, int pPackedLight, T pLivingEntity, float pLimbSwing, float pLimbSwingAmount, float pPartialTicks, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
        float angle = (pLivingEntity.tickCount + pPartialTicks) * 3.0F;

        AmuletHelper.getAmuletInSlotOrBelt(ModItems.TERRA_PROTECTION_AMULET.get(), pLivingEntity).ifPresent(triple -> {
            ItemStack stack = triple.getRight();
            if(!pLivingEntity.isInvisible()) {
                if (ModConfig.CachedValues.RENDER_LEAF_SHIELD && ((TerraProtectionAmuletItem) stack.getItem()).canProtect(stack)) {
                    pMatrixStack.pushPose();

                    pMatrixStack.scale(2.0f, 2.0f, 2.0f);
                    pMatrixStack.translate(0, -0.7, 0);
                    pMatrixStack.mulPose(Vector3f.YP.rotationDegrees(angle));

                    leafShieldModel.renderToBuffer(pMatrixStack, pBuffer.getBuffer(RenderType.entityTranslucent(modPrefix("textures/entity/amulets/leaf_shield_colored.png"))), pPackedLight, OverlayTexture.NO_OVERLAY, 1.0f, 1.0f, 1.0f, 1.0f);

                    pMatrixStack.popPose();
                }
            }
        });
    }
}
