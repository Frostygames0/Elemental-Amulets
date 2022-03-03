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

package frostygames0.elementalamulets.items.amulets;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import frostygames0.elementalamulets.ElementalAmulets;
import frostygames0.elementalamulets.advancements.triggers.ModCriteriaTriggers;
import frostygames0.elementalamulets.client.models.AmuletModel;
import frostygames0.elementalamulets.init.ModStats;
import frostygames0.elementalamulets.util.AmuletUtil;
import frostygames0.elementalamulets.util.NBTUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.client.ICurioRenderer;
import top.theillusivec4.curios.api.type.capability.ICurio;
import top.theillusivec4.curios.api.type.capability.ICurioItem;


import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;

public abstract class AmuletItem extends Item implements ICurioItem, ICurioRenderer {
    public static final String TIER_TAG = ElementalAmulets.MOD_ID + ":tier";
    public static final int MAX_TIER = 4;

    private static final List<AmuletItem> AMULETS = new ArrayList<>();

    private final boolean hasTier;
    private final boolean usesCurioMethods;
    private final boolean canBeGenerated;

    private AmuletModel<Player> model;

    public AmuletItem(Properties builder) {
        super(builder.properties);
        this.hasTier = builder.hasTier;
        this.usesCurioMethods = builder.usesCurioMethods;
        this.canBeGenerated = builder.canBeGenerated;

        AMULETS.add(this);
    }

    public static List<AmuletItem> getAmulets() {
        return Collections.unmodifiableList(AMULETS);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        // Tier
        if (this.getTier(stack) > 0)
            tooltip.add(new TranslatableComponent("item.elementalamulets.common_amulet.tooltip.tier", new TextComponent(String.valueOf(this.getTier(stack))).withStyle(ChatFormatting.YELLOW)).withStyle(ChatFormatting.GOLD));

        this.addAdditionalValues(stack, worldIn, tooltip, flagIn);

        // Elemental Power (Cool looking durability)
        if (stack.isDamaged()) {
            tooltip.add(new TranslatableComponent("item.elementalamulets.common_amulet.elemental_power",
                    new TextComponent(
                            stack.getMaxDamage() - stack.getDamageValue() + " / " + stack.getMaxDamage()).withStyle(ChatFormatting.YELLOW)
            ).withStyle(ChatFormatting.GOLD));
        }
        // Tooltip
        tooltip.add(new TranslatableComponent(getOrCreateDescriptionId() + ".tooltip").withStyle(ChatFormatting.GRAY));
    }

    @Override
    public int getMaxDamage(ItemStack stack) {
        return super.getMaxDamage(stack) * Math.max(this.getTier(stack), 1);
    }

    // I had to override them, as they used maxDamage and not getMaxDamage()
    @Override
    public int getBarWidth(ItemStack pStack) {
        return Math.round(13.0F - (float) pStack.getDamageValue() * 13.0F / (float) this.getMaxDamage(pStack));
    }

    @Override
    public int getBarColor(ItemStack pStack) {
        float f = Math.max(0.0F, ((float) this.getMaxDamage(pStack) - (float) pStack.getDamageValue()) / (float) this.getMaxDamage(pStack));
        return Mth.hsvToRgb(f / 3.0F, 1.0F, 1.0F);
    }

    @Override
    public boolean canBeDepleted() {
        return true;
    }
    // End of 'fixing'

    @Override
    public boolean isEnchantable(ItemStack pStack) {
        return false;
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        return false;
    }

    @Override
    public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
        return false;
    }

    @Override
    public void onEquip(SlotContext slotContext, ItemStack prevStack, ItemStack stack) {
        if (prevStack.getItem() != stack.getItem()) {
            LivingEntity entity = slotContext.entity();
            if (!entity.level.isClientSide() && entity instanceof ServerPlayer player) {

                ModCriteriaTriggers.SUCCESS_USE.trigger(player, stack);
                player.awardStat(ModStats.AMULET_WORN_STAT);
            }
        }
    }

    @Override
    public ItemStack getDefaultInstance() {
        return AmuletUtil.setStackTier(this, 1);
    }

    @Override
    public void fillItemCategory(CreativeModeTab group, NonNullList<ItemStack> items) {
        if (!this.hasTier) super.fillItemCategory(group, items);
        else {
            if (allowdedIn(group)) {
                for (int i = 1; i <= MAX_TIER; ++i) {
                    items.add(AmuletUtil.setStackTier(this, i));
                }
            }
        }
    }

    @Override
    public <T extends LivingEntity, M extends EntityModel<T>> void render(ItemStack stack, SlotContext slotContext, PoseStack matrixStack, RenderLayerParent<T, M> renderLayerParent, MultiBufferSource renderTypeBuffer, int light, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        if (model == null) {
            model = new AmuletModel<>(Minecraft.getInstance().getEntityModels().bakeLayer(AmuletModel.LAYER_LOCATION));
        } else {
            if (slotContext.entity().isInvisible())
                return;

            matrixStack.pushPose();
            ICurioRenderer.translateIfSneaking(matrixStack, slotContext.entity());
            ICurioRenderer.rotateIfSneaking(matrixStack, slotContext.entity());

            matrixStack.scale(0.7f, 0.7f, 0.7f); // Makes amulet smaller gugugaga

            VertexConsumer vertexBuilder = ItemRenderer.getArmorFoilBuffer(renderTypeBuffer, RenderType.entityTranslucent(AmuletModel.getTexture(this, stack)), false, stack.hasFoil());
            model.renderToBuffer(matrixStack, vertexBuilder, light, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
            matrixStack.popPose();
        }
    }

    @Override
    public boolean canEquipFromUse(SlotContext slotContext, ItemStack stack) {
        return true;
    }

    @Nonnull
    @Override
    public ICurio.SoundInfo getEquipSound(SlotContext slotContext, ItemStack stack) {
        return new ICurio.SoundInfo(SoundEvents.ARMOR_EQUIP_GOLD, 1f, 1f);
    }

    //---------------------//
    // AMULET STUFF START  //
    //---------------------//

    /**
     * Call this when amulet breaks, by default calls onBrokenCurio api
     */
    public void onAmuletBreak(SlotContext ctx) {
        CuriosApi.getCuriosHelper().onBrokenCurio(ctx);
    }

    /**
     * Place to add additional values like nature charge and any other thingy, without overriding {@link Item#appendHoverText(ItemStack, Level, List, TooltipFlag)}
     */
    protected void addAdditionalValues(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
    }

    public final int getTier(ItemStack stack) {
        if (this.hasTier()) {
            return NBTUtil.getInteger(stack, TIER_TAG);
        }
        return 0;
    }

    public final boolean hasTier() {
        return this.hasTier;
    }

    public final boolean usesCurioMethods() {
        return this.usesCurioMethods;
    }

    public final boolean canBeGenerated() {
        return this.canBeGenerated;
    }

    public static class Properties {
        private final Item.Properties properties;
        private boolean usesCurioMethods;
        private boolean canBeGenerated;
        private boolean hasTier;

        public Properties(Item.Properties properties) {
            this.properties = properties;
        }

        public Properties usesCurioMethods() {
            this.usesCurioMethods = true;
            return this;
        }

        public Properties hasTier() {
            this.hasTier = true;
            return this;
        }

        public Properties generates() {
            this.canBeGenerated = true;
            return this;
        }
    }

    // Removes vanilla durability tooltip from the tooltip list
    @Mod.EventBusSubscriber(modid = ElementalAmulets.MOD_ID, value = Dist.CLIENT)
    private static class TooltipEventHandler {
        @SubscribeEvent
        static void onTooltipEvent(final ItemTooltipEvent event) {
            ItemStack stack = event.getItemStack();
            if (stack.getItem() instanceof AmuletItem) {
                for (ListIterator<Component> iterator = event.getToolTip().listIterator(); iterator.hasNext(); ) {
                    Component tooltip = iterator.next();
                    if (tooltip instanceof TranslatableComponent) {
                        if (((TranslatableComponent) tooltip).getKey().equals("item.durability")) {
                            iterator.remove();
                        }
                    }
                }
            }
        }
    }
}
