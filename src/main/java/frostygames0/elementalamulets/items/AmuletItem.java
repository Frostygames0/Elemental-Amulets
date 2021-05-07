package frostygames0.elementalamulets.items;

import com.mojang.blaze3d.matrix.MatrixStack;
import frostygames0.elementalamulets.core.util.AmuletUtil;
import frostygames0.elementalamulets.items.interfaces.IAmuletItem;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurio;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public abstract class AmuletItem extends Item implements ICurioItem, IAmuletItem {
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
        tooltip.add(AmuletUtil.getFormattedTierTooltip(this));
    }

    @Override
    public void curioBreak(ItemStack stack, LivingEntity livingEntity) {
        AmuletUtil.playAmuletDestructionAnimation(stack, livingEntity, false);
    }

    @Override
    public boolean canRender(String identifier, int index, LivingEntity livingEntity, ItemStack stack) {
        return true;
    }

    @Override
    public void render(String identifier, int index, MatrixStack matrixStack, IRenderTypeBuffer renderTypeBuffer, int light, LivingEntity livingEntity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, ItemStack stack) {
        AmuletUtil.renderAmuletOnEntity(identifier, index, matrixStack, renderTypeBuffer, light, livingEntity,
                limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, stack, AmuletUtil.getTextureBasedOnTier(this));
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

    @Override
    public int getTier() {
        return tier;
    }
}
