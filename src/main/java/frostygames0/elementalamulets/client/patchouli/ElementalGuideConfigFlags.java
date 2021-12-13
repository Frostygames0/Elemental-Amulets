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

package frostygames0.elementalamulets.client.patchouli;

import frostygames0.elementalamulets.ElementalAmulets;
import frostygames0.elementalamulets.config.ModConfig;
import net.minecraftforge.fml.ModList;
import vazkii.patchouli.api.PatchouliAPI;

/**
 * @author Frostygames0
 * @date 28.10.2021 0:54
 */
public class ElementalGuideConfigFlags {

    public static void updateConfigFlags() {
        if (ModList.get().isLoaded("patchouli")) {
            PatchouliAPI.get().setConfigFlag(ElementalAmulets.MOD_ID + ":cult_temple", ModConfig.CachedValues.GENERATE_CULT_TEMPLE);
            PatchouliAPI.get().setConfigFlag(ElementalAmulets.MOD_ID + ":jeweller_house", ModConfig.CachedValues.GENERATE_JEWELLER_HOUSE);
        }
    }
}
