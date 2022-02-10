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

import frostygames0.elementalamulets.util.AmuletUtil;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import top.theillusivec4.curios.api.SlotContext;

public class InvisibilityAmuletItem extends AmuletItem {
    public InvisibilityAmuletItem(Item.Properties properties) {
        super(new AmuletItem.Properties(properties)
                .usesCurioMethods()
                .generates());
    }

    @Override
    public void curioTick(SlotContext ctx, ItemStack stack) {
        LivingEntity livingEntity = ctx.entity();
        Level world = livingEntity.getCommandSenderWorld();
        if (!world.isClientSide()) {
            if (livingEntity.isShiftKeyDown()) {
                if (!livingEntity.isInvisible()) livingEntity.setInvisible(true);
            } else {
                if (!livingEntity.hasEffect(MobEffects.INVISIBILITY)) livingEntity.setInvisible(false);
            }
        }
    }

    @Override
    public void onUnequip(SlotContext slotContext, ItemStack newStack, ItemStack stack) {
        LivingEntity entity = slotContext.entity();
        if (AmuletUtil.compareAmulets(stack, newStack)) {
            if (entity.isInvisible() && !entity.hasEffect(MobEffects.INVISIBILITY)) {
                entity.setInvisible(false);
            }
        }
    }
}
