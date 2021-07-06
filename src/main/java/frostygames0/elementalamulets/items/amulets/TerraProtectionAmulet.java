package frostygames0.elementalamulets.items.amulets;

import com.mojang.blaze3d.matrix.MatrixStack;
import frostygames0.elementalamulets.client.models.LeafShield;
import frostygames0.elementalamulets.config.ModConfig;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class TerraProtectionAmulet extends AmuletItem {
    public TerraProtectionAmulet(Properties properties) {
        super(properties, true);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);
        tooltip.add(new StringTextComponent("This amulet is not finished and may not work properly!").mergeStyle(TextFormatting.GOLD));
        tooltip.add(new StringTextComponent("Use it at your own risk").mergeStyle(TextFormatting.RED, TextFormatting.UNDERLINE));
    }

    @Override
    public void render(String identifier, int index, MatrixStack matrixStack, IRenderTypeBuffer renderTypeBuffer, int light, LivingEntity livingEntity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, ItemStack stack) {
        LeafShield model = new LeafShield();
        float angle = (System.currentTimeMillis() / 20) % 360;

        matrixStack.push();

        matrixStack.scale(2.0f, 2.0f, 2.0f);
        matrixStack.translate(0, -0.7, 0);
        matrixStack.rotate(Vector3f.YP.rotationDegrees(angle));

        model.render(matrixStack, renderTypeBuffer.getBuffer(RenderType.getEntityTranslucent(new ResourceLocation("poop_gang"))), light, OverlayTexture.NO_OVERLAY, 1.0f, 1.0f, 1.0f, 1.0f);

        matrixStack.pop();

        // Calling super is important since super class renders amulet here
        super.render(identifier, index, matrixStack, renderTypeBuffer, light, livingEntity, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, stack);
    }

    public float getDamageAbsorb(ItemStack stack) {
        return (float) (this.getTier(stack) * ModConfig.cached.PROTECTION_AMULET_ABSORPTION);
    }
}
