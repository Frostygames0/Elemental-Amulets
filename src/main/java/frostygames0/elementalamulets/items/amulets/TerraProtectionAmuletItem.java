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

import frostygames0.elementalamulets.ElementalAmulets;
import frostygames0.elementalamulets.config.ModConfig;
import frostygames0.elementalamulets.util.NBTUtil;
import frostygames0.elementalamulets.util.WorldUtil;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;


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
    protected void addAdditionalValues(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        int maxCharge = this.getMaxCharge(stack);
        if (maxCharge > 0) {
            tooltip.add(new TranslationTextComponent("item.elementalamulets.protection_amulet.charges",
                    new StringTextComponent(
                            this.getCharges(stack) + " / " + maxCharge).withStyle(TextFormatting.YELLOW)
            ).withStyle(TextFormatting.GOLD));
        }
    }

    @Override
    public void curioTick(String identifier, int index, LivingEntity livingEntity, ItemStack stack) {
        super.curioTick(identifier, index, livingEntity, stack);
        World level = livingEntity.level;
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
