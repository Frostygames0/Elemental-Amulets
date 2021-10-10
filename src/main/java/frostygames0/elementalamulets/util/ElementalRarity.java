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

import net.minecraft.item.Rarity;
import net.minecraft.util.text.TextFormatting;


import static frostygames0.elementalamulets.ElementalAmulets.modPrefix;

public class ElementalRarity {
    // Main elements
    public static final Rarity FIRE = Rarity.create(modPrefix("fire_rarity").toString(), TextFormatting.RED);
    public static final Rarity WATER = Rarity.create(modPrefix("water_rarity").toString(), TextFormatting.BLUE);
    public static final Rarity EARTH = Rarity.create(modPrefix("earth_rarity").toString(), TextFormatting.DARK_GREEN);
    public static final Rarity AIR = Rarity.create(modPrefix("air_rarity").toString(), TextFormatting.WHITE);

    // Sub-elements
    public static final Rarity SPEED = Rarity.create(modPrefix("speed_rarity").toString(), TextFormatting.AQUA);
    public static final Rarity JUMP = Rarity.create(modPrefix("jump_rarity").toString(), TextFormatting.GREEN);
}
