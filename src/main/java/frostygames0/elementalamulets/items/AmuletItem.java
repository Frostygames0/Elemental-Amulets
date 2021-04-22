package frostygames0.elementalamulets.items;

import frostygames0.elementalamulets.config.ModConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
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

public abstract class AmuletItem extends Item implements ICurioItem{
    private final int tier;

    public AmuletItem(Properties properties, int tier) {
        super(properties);
        this.tier = tier;
    }

    public AmuletItem(Properties properties) {
        this(properties, 0);
    }

    @Override
    public boolean canEquipFromUse(SlotContext slotContext, ItemStack stack) {
        return true;
    }

    @Override
    public void curioBreak(ItemStack stack, LivingEntity livingEntity) {
        World world = livingEntity.getEntityWorld();
        if(livingEntity instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) livingEntity;
            world.playSound(player, player.getPosition(), SoundEvents.BLOCK_RESPAWN_ANCHOR_DEPLETE, SoundCategory.NEUTRAL, 100, 1f);
            player.sendStatusMessage(new TranslationTextComponent("player.elementalamulets.brokenamulet.warn", new TranslationTextComponent(this.getTranslationKey())), true);
            if(world.isRemote() && ModConfig.cached.DISPLAY_TOTEM_LIKE_ANIM_ONBREAK) {
                Minecraft.getInstance().gameRenderer.displayItemActivation(stack);
            }
        }
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);
         if(this.hasTier()) tooltip.add(new TranslationTextComponent("item.adventuretomagium.common_amulet.tooltip.tier").mergeStyle(TextFormatting.GOLD)
                .appendSibling(new StringTextComponent(""+this.getTier()).mergeStyle(TextFormatting.YELLOW)));
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

    public int getDamageOnUse() {
        return this.tier;
    }

    public int getTier() {
        return this.tier;
    }

    public boolean hasTier() {
        return this.getTier() > 0;
    }


}
