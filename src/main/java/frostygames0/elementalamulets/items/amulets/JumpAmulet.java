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

import frostygames0.elementalamulets.config.ModConfig;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class JumpAmulet extends AmuletItem {
    public JumpAmulet(Item.Properties properties) {
        super(properties);
    }

    public float getJump(ItemStack stack) {
        return (float) (ModConfig.cached.JUMP_AMULET_BOOST *getTier(stack));
    }

    public float getFallResist(ItemStack stack) {
        return getJump(stack)*10;
    }

    @Override
    public boolean usesCurioMethods() {
        return false;
    }
}
