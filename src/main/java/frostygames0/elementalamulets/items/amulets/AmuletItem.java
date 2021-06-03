package frostygames0.elementalamulets.items.amulets;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import frostygames0.elementalamulets.ElementalAmulets;
import frostygames0.elementalamulets.client.models.AmuletModel;
import frostygames0.elementalamulets.core.util.NBTUtil;
import frostygames0.elementalamulets.items.triggers.ModCriteriaTriggers;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
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
    private final boolean hasTier;
    public static final String TIER_TAG = ElementalAmulets.MOD_ID+":tier";
    public static final int MAX_TIER = 4;
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
        if(this.getTier(stack) == 0) return;
        tooltip.add(new TranslationTextComponent("item.elementalamulets.common_amulet.tooltip.tier").mergeStyle(TextFormatting.GOLD)
                .appendSibling(new StringTextComponent("" + this.getTier(stack)).mergeStyle(TextFormatting.YELLOW)));
    }

    @Override
    public void onEquip(SlotContext slotContext, ItemStack prevStack, ItemStack stack) {
        if(prevStack.getItem() != stack.getItem()) {
            if(!slotContext.getWearer().world.isRemote() && slotContext.getWearer() instanceof ServerPlayerEntity) ModCriteriaTriggers.SUCCESS_USE.trigger((ServerPlayerEntity) slotContext.getWearer(), stack);
        }
    }

    @Override
    public ItemStack getDefaultInstance() {
        return getStackWithTier(new ItemStack(this), 1);
    }

    public static ItemStack getStackWithTier(ItemStack stack, int tier) {
        if(stack.getItem() instanceof AmuletItem) {
            if(((AmuletItem) stack.getItem()).hasTier) {
                if(tier > MAX_TIER) throw new IllegalArgumentException("Tier can't be higher than "+MAX_TIER+"! Your tier is "+tier);
                NBTUtil.putInteger(stack, TIER_TAG, tier);
            }
        }
        return stack;
    }

    @Override
    public void fillItemGroup(ItemGroup group, NonNullList<ItemStack> items) {
        if(!this.hasTier) super.fillItemGroup(group, items);
        else {
            if (isInGroup(group)) {
                for (int i = 1; i <= MAX_TIER; ++i) {
                    ItemStack stack = new ItemStack(this);
                    items.add(getStackWithTier(stack, i));
                }
            }
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
            return Math.max(NBTUtil.getInteger(stack, TIER_TAG), 0);
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
