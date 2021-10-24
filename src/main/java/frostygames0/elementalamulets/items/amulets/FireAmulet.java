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

import frostygames0.elementalamulets.config.ModConfig;
import net.minecraft.item.ItemStack;

public class FireAmulet extends AmuletItem {
    public FireAmulet(Properties p_i48487_1_) {
        super(p_i48487_1_);
    }

    public float getFireResist(ItemStack stack) {
        return (float) (ModConfig.CachedValues.FIRE_AMULET_FIRE_RESISTANCE * this.getTier(stack));
    }

    public float getLavaResist(ItemStack stack) {
        return (float) (ModConfig.CachedValues.FIRE_AMULET_LAVA_RESISTANCE * this.getTier(stack));
    }

    @Override
    public boolean usesCurioMethods() {
        return false;
    }
}
