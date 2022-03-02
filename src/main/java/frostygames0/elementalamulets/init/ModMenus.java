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

import frostygames0.elementalamulets.ElementalAmulets;
import frostygames0.elementalamulets.blocks.menu.AmuletBeltMenu;
import frostygames0.elementalamulets.blocks.menu.ElementalCombinatorMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModMenus {
    public static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(ForgeRegistries.CONTAINERS, ElementalAmulets.MOD_ID);

    public static final RegistryObject<MenuType<ElementalCombinatorMenu>> ELEMENTAL_COMBINATOR_MENU = MENUS.register("elemental_combinator", () -> IForgeMenuType.create(
            ((windowId, inv, data) -> new ElementalCombinatorMenu(windowId, data.readBlockPos(), inv))));

    public static final RegistryObject<MenuType<AmuletBeltMenu>> AMULET_BELT_MENU = MENUS.register("amulet_belt", () -> IForgeMenuType.create(
            ((windowId, inv, data) -> new AmuletBeltMenu(windowId, inv, data.readItem()))));

}
