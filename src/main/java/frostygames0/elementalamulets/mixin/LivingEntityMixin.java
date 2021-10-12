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

import frostygames0.elementalamulets.ElementalAmulets;
import frostygames0.elementalamulets.init.ModItems;
import frostygames0.elementalamulets.items.amulets.AirAmulet;
import frostygames0.elementalamulets.util.AmuletHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3d;
import org.apache.commons.lang3.tuple.ImmutableTriple;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;


import java.util.Optional;

/**
 * @author Frostygames0
 * @date 12.10.2021 16:45
 */
@Mixin(LivingEntity.class)
public class LivingEntityMixin  {
    @Inject(at = @At(value = "INVOKE",
            target = "Lnet/minecraft/entity/LivingEntity;getDeltaMovement()Lnet/minecraft/util/math/vector/Vector3d;", ordinal = 0), method = "travel", locals = LocalCapture.CAPTURE_FAILSOFT)
    public void travel(Vector3d travelVector, CallbackInfo ci, double d0, ModifiableAttributeInstance gravity) {
        LivingEntity entity = (LivingEntity) (Object) this;

        boolean amuletFlag = entity.getDeltaMovement().y <= 0.0D;
        Optional<ImmutableTriple<String, Integer, ItemStack>> optionalTriple = AmuletHelper.getAmuletInSlotOrBelt(ModItems.AIR_AMULET.get(), entity);

        if(optionalTriple.isPresent()) {
            ImmutableTriple<String, Integer, ItemStack> triple = optionalTriple.get();
            ItemStack stack = triple.getRight();
            AirAmulet amulet = (AirAmulet) stack.getItem();

            AttributeModifier attMod = new AttributeModifier(AirAmulet.MODIFIER_UUID, new ResourceLocation(ElementalAmulets.MOD_ID, "speed").toString(),
                    amulet.getFloating(stack), AttributeModifier.Operation.ADDITION);

            if(amuletFlag && !entity.isShiftKeyDown()) {
                if (!gravity.hasModifier(attMod)) gravity.addTransientModifier(attMod);
            } else if (gravity.hasModifier(attMod)) {
                gravity.removeModifier(attMod);
            }
        } else {
            if (gravity.getModifier(AirAmulet.MODIFIER_UUID) != null) {
                gravity.removeModifier(AirAmulet.MODIFIER_UUID);
            }
        }
    }
}
