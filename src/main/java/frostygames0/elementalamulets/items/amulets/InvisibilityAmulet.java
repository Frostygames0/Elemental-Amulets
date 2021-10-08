/*
 *    This file is part of Elemental Amulets.
 *
 *     Elemental Amulets is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Elemental Amulets is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with Elemental Amulets.  If not, see <https://www.gnu.org/licenses/>.
 */

package frostygames0.elementalamulets.items.amulets;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Effects;
import net.minecraft.world.World;
import top.theillusivec4.curios.api.SlotContext;

public class InvisibilityAmulet extends AmuletItem {
    public InvisibilityAmulet(Properties properties) {
        super(properties, false);
    }

    @Override
    public void curioTick(String identifier, int index, LivingEntity livingEntity, ItemStack stack) {
        World world = livingEntity.getCommandSenderWorld();
        if(!world.isClientSide()) {
            if(livingEntity.isShiftKeyDown()) {
                if(!livingEntity.isInvisible()) livingEntity.setInvisible(true);
                //if(livingEntity.tickCount % 20 == 0) AmuletHelper.damage(stack, livingEntity, identifier, index);
            } else {
                if(!livingEntity.hasEffect(Effects.INVISIBILITY)) livingEntity.setInvisible(false);
            }
        }
    }

    @Override
    public boolean canRender(String identifier, int index, LivingEntity livingEntity, ItemStack stack) {
        return !livingEntity.isInvisible();
    }

    @Override
    public void onUnequip(SlotContext slotContext, ItemStack newStack, ItemStack stack) {
        LivingEntity entity = slotContext.getWearer();
        if(stack.getItem() != newStack.getItem()) {
            if (entity.isInvisible() && !entity.hasEffect(Effects.INVISIBILITY)) {
                entity.setInvisible(false);
            }
        }
    }
}
