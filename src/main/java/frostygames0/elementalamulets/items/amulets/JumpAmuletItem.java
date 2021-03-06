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

package frostygames0.elementalamulets.items.amulets;

import frostygames0.elementalamulets.config.ModConfig;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class JumpAmuletItem extends AmuletItem {
    public JumpAmuletItem(Item.Properties properties) {
        super(new AmuletItem.Properties(properties)
                .generates()
                .hasTier());
    }

    public float getJump(ItemStack stack) {
        return (float) (ModConfig.CachedValues.JUMP_AMULET_BOOST * getTier(stack));
    }

    public float getFallResist(ItemStack stack) {
        return getJump(stack) * 10;
    }
}
