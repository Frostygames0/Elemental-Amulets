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

package frostygames0.elementalamulets.datagen.loottables;

import frostygames0.elementalamulets.init.ModBlocks;
import frostygames0.elementalamulets.init.ModItems;
import net.minecraft.data.DataGenerator;
import net.minecraft.loot.ConstantRange;
import net.minecraft.loot.ItemLootEntry;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.functions.ExplosionDecay;
import net.minecraft.loot.functions.SetCount;

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
        this.lootTables.put(ModBlocks.ELEMENTAL_COMBINATOR.get(), this.createTableNBT("elemental_combinator", ModBlocks.ELEMENTAL_COMBINATOR.get()));
        this.lootTables.put(ModBlocks.ELEMENTAL_ORE.get(), this.createOreTable("elemental_ore", ModBlocks.ELEMENTAL_ORE.get(), ModItems.ELEMENTAL_SHARDS.get()));
        this.lootTables.put(ModBlocks.CELESTIAL_FOCUS.get(), this.createDefaultTable("celestial_focus", ModBlocks.CELESTIAL_FOCUS.get()));
        this.lootTables.put(ModBlocks.ELEMENTAL_SHARDS_BLOCK.get(), LootTable.lootTable().withPool(LootPool.lootPool()
                .name("elemental_shards_block")
                .setRolls(ConstantRange.exactly(1))
                .add(ItemLootEntry.lootTableItem(ModBlocks.ELEMENTAL_SHARDS_BLOCK.get()).when(SILK_TOUCH)
                        .otherwise(ItemLootEntry.lootTableItem(ModItems.ELEMENTAL_SHARDS.get())
                                .apply(SetCount.setCount(ConstantRange.exactly(9)))
                                .apply(ExplosionDecay.explosionDecay())))));
    }
}
