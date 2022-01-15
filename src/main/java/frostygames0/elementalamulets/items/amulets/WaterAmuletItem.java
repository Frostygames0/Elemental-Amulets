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

import frostygames0.elementalamulets.config.ModConfig;
import frostygames0.elementalamulets.util.AmuletHelper;
import frostygames0.elementalamulets.util.AttributeUtil;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.ForgeMod;
import top.theillusivec4.curios.api.SlotContext;


import java.util.UUID;

import static frostygames0.elementalamulets.ElementalAmulets.modPrefix;

import net.minecraft.world.item.Item.Properties;

public class WaterAmuletItem extends AmuletItem {
    public static UUID MODIFIER_UUID = UUID.fromString("88f5e6ea-c12f-4cfd-b3f4-40d9ca8cdfad");

    public WaterAmuletItem(Properties properties) {
        super(properties, true);
    }

    @Override
    public void curioTick(String identifier, int index, LivingEntity livingEntity, ItemStack stack) {
        if (!livingEntity.level.isClientSide()) {
            AttributeInstance att = livingEntity.getAttribute(ForgeMod.SWIM_SPEED.get());
            AttributeModifier attMod = new AttributeModifier(MODIFIER_UUID, modPrefix("swim_speed_boost").toString(),
                    this.getSwimSpeed(stack), AttributeModifier.Operation.MULTIPLY_TOTAL);
            if (livingEntity.isUnderWater()) {
                if (livingEntity.isSwimming()) {
                    AttributeUtil.applyModifier(att, attMod);
                } else {
                    AttributeUtil.removeModifier(att, attMod);
                }
            }
        }
    }

    @Override
    public void onUnequip(SlotContext slotContext, ItemStack newStack, ItemStack stack) {
        AttributeInstance att = slotContext.getWearer().getAttribute(ForgeMod.SWIM_SPEED.get());
        if (AmuletHelper.compareAmulets(stack, newStack)) {
            AttributeUtil.removeModifierByUUID(att, MODIFIER_UUID);
        }
    }

    public float getSwimSpeed(ItemStack stack) {
        return (float) ModConfig.CachedValues.WATER_AMULET_SPEED_BOOST * this.getTier(stack);
    }
}
