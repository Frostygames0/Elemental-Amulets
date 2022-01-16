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

import frostygames0.elementalamulets.util.AmuletHelper;
import frostygames0.elementalamulets.util.AttributeUtil;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.ForgeMod;
import top.theillusivec4.curios.api.SlotContext;


import java.util.UUID;

import static frostygames0.elementalamulets.ElementalAmulets.modPrefix;

public class AirAmuletItem extends AmuletItem {
    public static UUID MODIFIER_UUID = UUID.fromString("2589aeb9-2b6a-44dc-8fab-97c9743dacdf");

    public AirAmuletItem(Properties properties) {
        super(properties, true);
    }

    public float getFloating(ItemStack stack) {
        return -0.01f * (this.getTier(stack) * 1.75f);
    }

    @Override
    public void curioTick(SlotContext ctx, ItemStack stack) {
        LivingEntity livingEntity = ctx.entity();
        AttributeInstance gravity = livingEntity.getAttribute(ForgeMod.ENTITY_GRAVITY.get());
        AttributeModifier attMod = new AttributeModifier(AirAmuletItem.MODIFIER_UUID, modPrefix("air_speed").toString(),
                this.getFloating(stack), AttributeModifier.Operation.ADDITION);

        if (livingEntity.getDeltaMovement().y <= 0 && !livingEntity.isShiftKeyDown()) {
            AttributeUtil.applyModifier(gravity, attMod);
            livingEntity.fallDistance = 0;
        } else
            AttributeUtil.removeModifier(gravity, attMod);
    }


    @Override
    public void onUnequip(SlotContext slotContext, ItemStack newStack, ItemStack stack) {
        AttributeInstance att = slotContext.entity().getAttribute(ForgeMod.ENTITY_GRAVITY.get());
        if (!AmuletHelper.compareAmulets(stack, newStack)) {
            AttributeUtil.removeModifierByUUID(att, MODIFIER_UUID);
        }
    }
}
