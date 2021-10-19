/*
 *     Copyright (c) 2021
 *
 *     This file is part of Elemental Amulets, a Minecraft Mod.
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

package frostygames0.elementalamulets.mixin;

import frostygames0.elementalamulets.init.ModItems;
import frostygames0.elementalamulets.items.amulets.WaterAmulet;
import frostygames0.elementalamulets.util.AmuletHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * @author Frostygames0
 * @date 12.10.2021 16:45
 */
@Mixin(LivingEntity.class)
public class MixinLivingEntity {

    @Inject(at = @At("RETURN"), method = "decreaseAirSupply", cancellable = true)
    protected void decreaseAirWithWaterAmulet(int air, CallbackInfoReturnable<Integer> ci) {
        LivingEntity entity = (LivingEntity) (Object) this;
        AmuletHelper.getAmuletInSlotOrBelt(ModItems.WATER_AMULET.get(), entity).ifPresent(triple -> {
            ItemStack stack = triple.getRight();
            WaterAmulet amulet = (WaterAmulet) stack.getItem();

            if(amulet.getTier(stack) >= 2) {
                ci.setReturnValue(air);
            }
        });
    }
}
