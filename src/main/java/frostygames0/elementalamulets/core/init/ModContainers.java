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

package frostygames0.elementalamulets.core.init;

import frostygames0.elementalamulets.ElementalAmulets;
import frostygames0.elementalamulets.blocks.containers.AmuletBeltContainer;
import frostygames0.elementalamulets.blocks.containers.ElementalCombinatorContainer;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IntArray;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModContainers {
    public static final DeferredRegister<ContainerType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.CONTAINERS, ElementalAmulets.MOD_ID);

    public static final RegistryObject<ContainerType<ElementalCombinatorContainer>> ELEMENTAL_COMBINATOR_CONTAINER = CONTAINERS.register("elemental_combinator", () -> IForgeContainerType.create(
            ((windowId, inv, data) -> {
                BlockPos pos = data.readBlockPos();
                World world = inv.player.getCommandSenderWorld();
                return new ElementalCombinatorContainer(windowId, world, pos, inv, inv.player, new IntArray(2));
            })
    ));

    public static final RegistryObject<ContainerType<AmuletBeltContainer>> AMULET_BELT_CONTAINER = CONTAINERS.register("amulet_belt", () -> IForgeContainerType.create(
            ((windowId, inv, data) -> {
                ItemStack belt = data.readItem();
                return new AmuletBeltContainer(windowId, inv, belt);
            })
    ));

}
