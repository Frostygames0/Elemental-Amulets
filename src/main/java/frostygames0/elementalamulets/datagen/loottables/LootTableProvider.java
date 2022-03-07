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

package frostygames0.elementalamulets.datagen.loottables;

import frostygames0.elementalamulets.init.ModBlocks;
import frostygames0.elementalamulets.init.ModItems;
import net.minecraft.data.DataGenerator;

/**
 * @author Frostygames0
 * @date 11.06.2021 18:56
 */
public class LootTableProvider extends BaseLootTableProvider {
    public LootTableProvider(DataGenerator dataGeneratorIn) {
        super(dataGeneratorIn);
    }

    @Override
    protected void addTables() {
        this.lootTables.put(ModBlocks.ELEMENTAL_COMBINATOR.get(), this.createTableNBT(ModBlocks.ELEMENTAL_COMBINATOR.get()));
        this.lootTables.put(ModBlocks.ELEMENTAL_ORE.get(), this.createOreTable(ModBlocks.ELEMENTAL_ORE.get(), ModItems.ELEMENTAL_SHARDS.get()));
        this.lootTables.put(ModBlocks.DEEPSLATE_ELEMENTAL_ORE.get(), this.createOreTable(ModBlocks.DEEPSLATE_ELEMENTAL_ORE.get(), ModItems.ELEMENTAL_SHARDS.get()));
        this.lootTables.put(ModBlocks.CELESTIAL_FOCUS.get(), this.createDefaultTable(ModBlocks.CELESTIAL_FOCUS.get()));

        this.lootTables.put(ModBlocks.ELEMENTAL_SHARDS_BLOCK.get(), this.createShardsTable(ModBlocks.ELEMENTAL_SHARDS_BLOCK.get(), ModItems.ELEMENTAL_SHARDS.get()));
        this.lootTables.put(ModBlocks.FIRE_SHARDS_BLOCK.get(), this.createShardsTable(ModBlocks.FIRE_SHARDS_BLOCK.get(), ModItems.FIRE_ELEMENT.get()));
        this.lootTables.put(ModBlocks.WATER_SHARDS_BLOCK.get(), this.createShardsTable(ModBlocks.WATER_SHARDS_BLOCK.get(), ModItems.WATER_ELEMENT.get()));
        this.lootTables.put(ModBlocks.AIR_SHARDS_BLOCK.get(), this.createShardsTable(ModBlocks.AIR_SHARDS_BLOCK.get(), ModItems.AIR_ELEMENT.get()));
        this.lootTables.put(ModBlocks.EARTH_SHARDS_BLOCK.get(), this.createShardsTable(ModBlocks.EARTH_SHARDS_BLOCK.get(), ModItems.EARTH_ELEMENT.get()));
    }
}
