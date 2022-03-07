/*
 *  Copyright (c) 2021-2022
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

import frostygames0.elementalamulets.ElementalAmulets;
import frostygames0.elementalamulets.config.ModConfig;
import frostygames0.elementalamulets.util.NBTUtil;
import frostygames0.elementalamulets.util.WorldUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import top.theillusivec4.curios.api.SlotContext;


import javax.annotation.Nullable;
import java.util.List;

import static frostygames0.elementalamulets.ElementalAmulets.modPrefix;

public class TerraProtectionAmuletItem extends AmuletItem {
    public static final DamageSource LEAF_CUT = new DamageSource(ElementalAmulets.MOD_ID + ".leaf_cut");
    public static final String CHARGES_TAG = modPrefix("charge").toString();

    public TerraProtectionAmuletItem(Item.Properties properties) {
        super(new AmuletItem.Properties(properties)
                .usesCurioMethods()
                .hasTier());
    }

    @Override
    protected void addAdditionalValues(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        int maxCharge = this.getMaxCharge(stack);
        if (maxCharge > 0) {
            tooltip.add(new TranslatableComponent("item.elementalamulets.protection_amulet.charges",
                    new TextComponent(
                            this.getCharges(stack) + " / " + maxCharge).withStyle(ChatFormatting.YELLOW)
            ).withStyle(ChatFormatting.GOLD));
        }
    }

    @Override
    public void curioTick(SlotContext ctx, ItemStack stack) {
        LivingEntity livingEntity = ctx.entity();
        Level level = livingEntity.level;
        if (!level.isClientSide()) {
            boolean natural = WorldUtil.isNatural(level, livingEntity.blockPosition()) || ModConfig.CachedValues.PROTECTION_AMULET_IGNORE_NATURALITY;
            if (natural) {
                if (livingEntity.tickCount % ModConfig.CachedValues.PROTECTION_AMULET_CHARGE_TIME == 0) {
                    if (getCharges(stack) < getMaxCharge(stack)) {
                        NBTUtil.putInteger(stack, CHARGES_TAG, getCharges(stack) + 1);
                    }
                }
            }
        }
    }

    public float getReflectedDamageMulti(ItemStack stack) {
        return (float) (this.getTier(stack) * ModConfig.CachedValues.PROTECTION_AMULET_REFLECT_DAMAGE_MULT);
    }

    public int getCharges(ItemStack stack) {
        return NBTUtil.getInteger(stack, CHARGES_TAG);
    }

    public void removeOneCharge(ItemStack stack) {
        NBTUtil.putInteger(stack, CHARGES_TAG, getCharges(stack) - 1);
    }

    public boolean canProtect(ItemStack stack) {
        return getCharges(stack) > 0;
    }

    public int getMaxCharge(ItemStack stack) {
        return 4 * getTier(stack);
    }
}
