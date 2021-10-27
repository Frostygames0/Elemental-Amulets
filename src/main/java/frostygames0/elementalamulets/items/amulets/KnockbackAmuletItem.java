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

package frostygames0.elementalamulets.items.amulets;

import net.minecraft.item.ItemStack;

/**
 * @author Frostygames0
 * @date 09.10.2021 11:44
 */
public class KnockbackAmuletItem extends AmuletItem{
    public KnockbackAmuletItem(Properties properties) {
        super(properties);
    }

    public float getKnockback(ItemStack item) {
        return (float) (getTier(item) * 0.5);
    }

    @Override
    public boolean usesCurioMethods() {
        return false;
    }
}
