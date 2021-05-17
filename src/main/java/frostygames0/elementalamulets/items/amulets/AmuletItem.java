package frostygames0.elementalamulets.items.amulets;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import frostygames0.elementalamulets.ElementalAmulets;
import frostygames0.elementalamulets.client.models.AmuletModel;
import frostygames0.elementalamulets.core.util.AmuletUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.TippedArrowItem;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.potion.Potion;
import net.minecraft.util.*;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurio;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public abstract class AmuletItem extends Item implements ICurioItem {
    public static final String TIER_TAG = (ElementalAmulets.MOD_ID+":tier");
    private final int tier;
    public AmuletItem(Properties properties, int tier) {
        super(properties);
        this.tier = tier;
    }

    public AmuletItem(Properties properties) {
        this(properties, 0);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);
        if(this.getTier() > 0) {
            tooltip.add(new TranslationTextComponent("item.elementalamulets.common_amulet.tooltip.tier").mergeStyle(TextFormatting.GOLD)
                    .appendSibling(new StringTextComponent("" + this.getTier()).mergeStyle(TextFormatting.YELLOW)));
        }
    }

    @Override
    public void curioBreak(ItemStack stack, LivingEntity livingEntity) {
        World world = livingEntity.getEntityWorld();
            if(livingEntity instanceof PlayerEntity) {
                PlayerEntity player = (PlayerEntity) livingEntity;
                world.playSound(player, livingEntity.getPosition(), SoundEvents.BLOCK_RESPAWN_ANCHOR_DEPLETE, SoundCategory.NEUTRAL, 100, 1);
                player.sendStatusMessage(new TranslationTextComponent("player.elementalamulets.brokenamulet.warn", new TranslationTextComponent(stack.getTranslationKey())), true);
                Minecraft.getInstance().gameRenderer.displayItemActivation(stack);
            }
    }

    @Override
    public boolean canRender(String identifier, int index, LivingEntity livingEntity, ItemStack stack) {
        return true;
    }

    @Override
    public void render(String identifier, int index, MatrixStack matrixStack, IRenderTypeBuffer renderTypeBuffer, int light, LivingEntity livingEntity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, ItemStack stack) {
        ResourceLocation texture = new ResourceLocation(ElementalAmulets.MOD_ID, "textures/entity/amulets/tier_"+this.getTier()+"/"+this.getRegistryName().getPath()+"_model.png");
        ICurio.RenderHelper.translateIfSneaking(matrixStack, livingEntity);
        ICurio.RenderHelper.rotateIfSneaking(matrixStack, livingEntity);

        AmuletModel<?> amuletModel = new AmuletModel<>();
        IVertexBuilder vertexBuilder = ItemRenderer.getBuffer(renderTypeBuffer, amuletModel.getRenderType(texture), false, stack.hasEffect());
        amuletModel.render(matrixStack, vertexBuilder, light, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
    }

    @Override
    public boolean canEquipFromUse(SlotContext slotContext, ItemStack stack) {
        return true;
    }

    @Nonnull
    @Override
    public ICurio.DropRule getDropRule(LivingEntity livingEntity, ItemStack stack) {
        return ICurio.DropRule.DEFAULT;
    }

    @Nonnull
    @Override
    public ICurio.SoundInfo getEquipSound(SlotContext slotContext, ItemStack stack) {
        return new ICurio.SoundInfo(SoundEvents.ITEM_ARMOR_EQUIP_IRON, 1f,1f);
    }

    public abstract int getDamageOnUse();

    public int getTier() {
        return tier;
    }

}
