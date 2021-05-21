package frostygames0.elementalamulets.items.amulets;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import frostygames0.elementalamulets.ElementalAmulets;
import frostygames0.elementalamulets.client.models.AmuletModel;
import frostygames0.elementalamulets.config.ModConfig;
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
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
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
    private boolean hasTier;
    public static final String TIER_TAG = (ElementalAmulets.MOD_ID+":tier");
    public AmuletItem(Properties properties, boolean hasTier) {
        super(properties);
        this.hasTier = hasTier;
    }

    public AmuletItem(Properties properties) {
        this(properties, true);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);
        if(this.getTier(stack) < 1 || !this.hasTier) return;
        tooltip.add(new TranslationTextComponent("item.elementalamulets.common_amulet.tooltip.tier").mergeStyle(TextFormatting.GOLD)
                .appendSibling(new StringTextComponent("" + this.getTier(stack)).mergeStyle(TextFormatting.YELLOW)));
    }

    @Override
    public ItemStack getDefaultInstance() {
        return getStackWithTier(new ItemStack(this), 1);
    }

    // TODO Maybe move all helper methods to a different class
    public static ItemStack getStackWithTier(ItemStack stack, int tier) {
        if(stack.getItem() instanceof AmuletItem) {
            if(((AmuletItem) stack.getItem()).hasTier) {
                CompoundNBT nbt = new CompoundNBT();
                if (stack.hasTag()) {
                    nbt = stack.getTag();
                }
                nbt.putInt(TIER_TAG, tier);
                stack.setTag(nbt);
            }
        }
        return stack;
    }

    @Override
    public void fillItemGroup(ItemGroup group, NonNullList<ItemStack> items) {
        if(!this.hasTier) {
            super.fillItemGroup(group, items);
        } else {
            if (isInGroup(group)) {
                for (int i = 1; i <= 4; ++i) {
                    ItemStack stack = new ItemStack(this);
                    items.add(getStackWithTier(stack, i));
                }
            }
        }
    }

    @Override
    public void curioBreak(ItemStack stack, LivingEntity livingEntity) {
        World world = livingEntity.getEntityWorld();
            if(livingEntity instanceof PlayerEntity) {
                PlayerEntity player = (PlayerEntity) livingEntity;
                world.playSound(player, livingEntity.getPosition(), SoundEvents.BLOCK_RESPAWN_ANCHOR_DEPLETE, SoundCategory.NEUTRAL, 100, 1);
                player.sendStatusMessage(new TranslationTextComponent("player.elementalamulets.brokenamulet.warn", new TranslationTextComponent(stack.getTranslationKey())), true);
                if(ModConfig.cached.DISPLAY_TOTEM_LIKE_ANIM_ONBREAK) Minecraft.getInstance().gameRenderer.displayItemActivation(stack);
            }
    }

    @Override
    public boolean canRender(String identifier, int index, LivingEntity livingEntity, ItemStack stack) {
        return true;
    }

    @Override
    public void render(String identifier, int index, MatrixStack matrixStack, IRenderTypeBuffer renderTypeBuffer, int light, LivingEntity livingEntity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, ItemStack stack) {
        ResourceLocation texture = new ResourceLocation(ElementalAmulets.MOD_ID, "textures/entity/amulets/tier_"+this.getTier(stack)+"/"+this.getRegistryName().getPath()+"_model.png");
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
        return new ICurio.SoundInfo(SoundEvents.ITEM_ARMOR_EQUIP_GOLD, 1f,1f);
    }

    public int getTier(ItemStack stack) {
        if(this.hasTier()) {
            if (stack.hasTag() && stack.getTag().contains(TIER_TAG)) {
                int tier = stack.getTag().getInt(TIER_TAG);
                return Math.max(tier, 0);
            }
        }
        return 0;
    }

    /**
     * If amulet has tier - true, else false
     * NOTE: It's not individual for every stack of amulet
     * @return hasTier
     */
    public boolean hasTier() {
        return this.hasTier;
    }

    //public abstract int getDamageOnUse(ItemStack stack);

}
