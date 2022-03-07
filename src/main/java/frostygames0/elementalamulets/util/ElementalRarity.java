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

package frostygames0.elementalamulets.util;

import net.minecraft.ChatFormatting;
import net.minecraft.world.item.Rarity;


import static frostygames0.elementalamulets.ElementalAmulets.modPrefix;

public final class ElementalRarity {
    // Main elements
    public static final Rarity FIRE = Rarity.create(modPrefix("fire_rarity").toString(), ChatFormatting.RED);
    public static final Rarity WATER = Rarity.create(modPrefix("water_rarity").toString(), ChatFormatting.BLUE);
    public static final Rarity EARTH = Rarity.create(modPrefix("earth_rarity").toString(), ChatFormatting.DARK_GREEN);
    public static final Rarity AIR = Rarity.create(modPrefix("air_rarity").toString(), ChatFormatting.WHITE);

    // Sub-elements
    public static final Rarity SPEED = Rarity.create(modPrefix("speed_rarity").toString(), ChatFormatting.AQUA);
    public static final Rarity JUMP = Rarity.create(modPrefix("jump_rarity").toString(), ChatFormatting.GREEN);
}
