package frostygames0.elementalamulets.items;

import frostygames0.elementalamulets.ElementalAmulets;
import frostygames0.elementalamulets.config.ModConfig;
import frostygames0.elementalamulets.items.interfaces.IAmuletItem;
import io.netty.util.SuppressForbidden;
import net.minecraft.block.AbstractBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.renderer.entity.layers.BipedArmorLayer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistries;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurio;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

import javax.annotation.Nonnull;

/**
 * Basic Implementation of amulet
 */
public class AbstractAmuletItem extends Item implements ICurioItem, IAmuletItem {
    private int tier;

    /**
     * AbstractAmuletItem is basic implementation of all amulets that shares all common things.
     * @param properties Item Properties
     * @param tier Tier of amulet (used in ability's power calculation)
     */
    public AbstractAmuletItem(Properties properties, int tier) {
        super(properties);
        this.tier = tier;
    }

    /**
     * Overload of {@link AbstractAmuletItem} when amulet has no tiers
     * @param properties Item Properties
     */
    public AbstractAmuletItem(Properties properties) {
        this(properties, 0);
    }

    @Override
    public boolean canEquipFromUse(SlotContext slotContext, ItemStack stack) {
        return true;
    }

    /**
     * super this if you want breaking animation and sound effect to play
     * @param stack curio stack
     * @param livingEntity wearer
     */
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
    public int getDamageOnUse() {
        return this.tier;
    }

    @Override
    public int getTier() {
        return this.tier;
    }
}
