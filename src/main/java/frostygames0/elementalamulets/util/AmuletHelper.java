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

package frostygames0.elementalamulets.util;

import frostygames0.elementalamulets.init.ModItems;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import org.apache.commons.lang3.tuple.ImmutableTriple;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.type.util.ICuriosHelper;


import java.util.Optional;

/**
 * @author Frostygames0
 * @date 25.09.2021 15:04
 */
public final class AmuletHelper {

    // This is a wrapper method for Amulet Belt to work
    public static Optional<ImmutableTriple<String, Integer, ItemStack>> getAmuletInSlotOrBelt(Item item, LivingEntity entity) {
        ICuriosHelper helper = CuriosApi.getCuriosHelper();
        if(helper.findEquippedCurio(item, entity).isPresent()) {
            return helper.findEquippedCurio(item, entity);
        }
        Optional<ImmutableTriple<String, Integer, ItemStack>> optional = helper.findEquippedCurio(ModItems.AMULET_BELT.get(), entity);
        if(optional.isPresent()) {
            ImmutableTriple<String, Integer, ItemStack> triple = optional.get();
            IItemHandler handler = triple.getRight().getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).orElseThrow(NullPointerException::new);
            for(int i = 0; i < handler.getSlots(); i++) {
                ItemStack amulet = handler.getStackInSlot(i);
                if(amulet.getItem() == item) {
                    return Optional.of(ImmutableTriple.of(triple.getLeft(), triple.getMiddle(), amulet));
                }
            }
        }

        return Optional.empty();
    }
}
