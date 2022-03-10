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

package frostygames0.elementalamulets.advancements.triggers;

import net.minecraft.advancements.CriteriaTriggers;

/**
 * @author Frostygames0
 * @date 02.06.2021 10:15
 */
public class ModCriteriaTriggers {
    public static final ElementalGuideSuccessUse ELEMENTAL_GUIDE_USE = new ElementalGuideSuccessUse();
    public static final ItemCombinedTrigger ITEM_COMBINED = new ItemCombinedTrigger();

    public static void register() {
        CriteriaTriggers.register(ELEMENTAL_GUIDE_USE);
        CriteriaTriggers.register(ITEM_COMBINED);
    }
}
