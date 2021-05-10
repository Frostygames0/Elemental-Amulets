package frostygames0.elementalamulets.core.util;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import frostygames0.elementalamulets.ElementalAmulets;
import frostygames0.elementalamulets.client.models.AmuletModel;
import frostygames0.elementalamulets.items.AmuletItem;
import frostygames0.elementalamulets.items.interfaces.IAmuletItem;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import top.theillusivec4.curios.api.type.capability.ICurio;

import java.util.List;

public final class AmuletUtil {
    private AmuletUtil() {}

    /**
     * Plays a totem-like animation with sound effect and message to a player that broke amulet
     * @param stack Item that will be displayed in animation (Put your amulet's stack here)
     * @param entity LivingEntity that wears amulet (Although this will work only if living entity is player)
     * @param ignoreConfig Should config value "amulet_totem_like_anim" be ignored?
     */
    public static void playAmuletDestructionAnimation(ItemStack stack, LivingEntity entity, boolean ignoreConfig) {
        World world = entity.getEntityWorld();
        if(entity instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) entity;
            world.playSound(player, player.getPosition(), SoundEvents.BLOCK_RESPAWN_ANCHOR_DEPLETE, SoundCategory.NEUTRAL, 100, 1f);
            player.sendStatusMessage(new TranslationTextComponent("player.elementalamulets.brokenamulet.warn", new TranslationTextComponent(stack.getTranslationKey())), true);
            /*if(world.isRemote()) {
                if(ModConfig.cached.DISPLAY_TOTEM_LIKE_ANIM_ONBREAK || ignoreConfig) {
                    Minecraft.getInstance().gameRenderer.displayItemActivation(stack);
                }
            }*/
        }
    }

    /**
     * Use it to add cool tooltip about tier :)
     * @param item amulet
     * @param tooltip tooltip
     */
    public static void getFormattedTierTooltip(IAmuletItem item, List<ITextComponent> tooltip) {
        if(item.getTier() > 0) {
            tooltip.add(new TranslationTextComponent("item.elementalamulets.common_amulet.tooltip.tier").mergeStyle(TextFormatting.GOLD)
                    .appendSibling(new StringTextComponent("" + item.getTier()).mergeStyle(TextFormatting.YELLOW)));
        }
    }

    /**
     * Use to render amulet where you need it
     */
    public static void renderAmuletOnEntity(String identifier, int index, MatrixStack matrixStack, IRenderTypeBuffer renderTypeBuffer, int light, LivingEntity livingEntity,
                                            float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw,
                                            float headPitch, ItemStack stack, ResourceLocation resloc) {
        ICurio.RenderHelper.translateIfSneaking(matrixStack, livingEntity);
        ICurio.RenderHelper.rotateIfSneaking(matrixStack, livingEntity);

        AmuletModel<?> amuletModel = new AmuletModel<>();
        IVertexBuilder vertexBuilder = ItemRenderer.getBuffer(renderTypeBuffer, amuletModel.getRenderType(resloc), false, false);
        amuletModel.render(matrixStack, vertexBuilder, light, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
    }

    /**
     * gets ResourceLocation of texture
     * @param item aboba
     * @return ResourceLocation
     */
    public static ResourceLocation getTextureBasedOnTier(AmuletItem item) {
        return new ResourceLocation(ElementalAmulets.MOD_ID, "textures/entity/amulets/tier_"+item.getTier()+"/"+item.getRegistryName().getPath()+"_model.png");
    }

}
