package frostygames0.elementalamulets.datagen.loottables;

import frostygames0.elementalamulets.core.init.ModBlocks;
import frostygames0.elementalamulets.core.init.ModItems;
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
