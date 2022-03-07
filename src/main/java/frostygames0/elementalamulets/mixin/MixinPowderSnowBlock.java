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

package frostygames0.elementalamulets.mixin;

import frostygames0.elementalamulets.config.ModConfig;
import frostygames0.elementalamulets.init.ModItems;
import frostygames0.elementalamulets.items.amulets.AmuletItem;
import frostygames0.elementalamulets.util.AmuletUtil;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.PowderSnowBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * @author Frostygames0
 * @date 19.02.2022 19:07
 */
@Mixin(PowderSnowBlock.class)
public class MixinPowderSnowBlock {

    @Inject(at = @At("RETURN"), method = "canEntityWalkOnPowderSnow(Lnet/minecraft/world/entity/Entity;)Z", cancellable = true)
    private static void elementalamulets_canWalkWithAmulet(Entity pEntity, CallbackInfoReturnable<Boolean> cir) {
        if (pEntity instanceof LivingEntity living) {
            AmuletUtil.getAmuletInSlotOrBelt(ModItems.WATER_WALKER_AMULET.get(), living).ifPresent(triple -> {
                ItemStack stack = triple.stack();
                AmuletItem item = (AmuletItem) stack.getItem();

                if (ModConfig.CachedValues.WATER_WALKER_AMULET_WALK_ON_POWDER_SNOW) {
                    if (item.getTier(stack) > 1)
                        cir.setReturnValue(true);
                }
            });
        }
    }
}
