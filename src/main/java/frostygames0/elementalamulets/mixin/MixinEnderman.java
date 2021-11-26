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
import frostygames0.elementalamulets.util.AmuletHelper;
import net.minecraft.entity.monster.EndermanEntity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * @author Frostygames0
 * @date 20.10.2021 20:02
 */
@Mixin(EndermanEntity.class)
public class MixinEnderman {

    @Inject(at = @At("HEAD"), method = "isLookingAtMe", cancellable = true)
    public void elementalamulets_ignorePlayerWithAmulet(PlayerEntity player, CallbackInfoReturnable<Boolean> ci) {
        if (AmuletHelper.getAmuletInSlotOrBelt(ModItems.PACIFYING_AMULET.get(), player).isPresent())
            ci.setReturnValue(false);
    }
}
