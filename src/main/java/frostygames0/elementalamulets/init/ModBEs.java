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
import frostygames0.elementalamulets.blocks.entities.ElementalCombinatorBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBEs {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITIES, ElementalAmulets.MOD_ID);

    public static final RegistryObject<BlockEntityType<ElementalCombinatorBlockEntity>> ELEMENTAL_COMBINATOR_BE = BLOCK_ENTITIES.register("elemental_combinator",
            () -> BlockEntityType.Builder.of(ElementalCombinatorBlockEntity::new, ModBlocks.ELEMENTAL_COMBINATOR.get()).build(null));
}
