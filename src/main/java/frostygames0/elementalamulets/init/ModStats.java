/*
 *  Copyright (c) 2021
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

package frostygames0.elementalamulets.init;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.stats.Stats;


import static frostygames0.elementalamulets.ElementalAmulets.modPrefix;

/**
 * @author Frostygames0
 * @date 26.09.2021 17:22
 */
public class ModStats {
    public static final ResourceLocation AMULET_WORN_STAT = modPrefix("amulets_worn");
    public static final ResourceLocation TIMES_COMBINED = modPrefix("times_combined");
    public static final ResourceLocation GUIDE_OPENED = modPrefix("guide_opened");

    public static void registerStats() {
        registerStat(AMULET_WORN_STAT);
        registerStat(TIMES_COMBINED);
        registerStat(GUIDE_OPENED);
    }

    private static void registerStat(ResourceLocation name) {
        Registry.register(Registry.CUSTOM_STAT, name.getPath(), name);
        Stats.CUSTOM.get(name);
    }
}
