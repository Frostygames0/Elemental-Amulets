package frostygames0.elementalamulets.datagen.loottables;

import frostygames0.elementalamulets.core.init.ModBlocks;
import frostygames0.elementalamulets.core.init.ModItems;
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
        this.lootTables.put(ModBlocks.ELEMENTAL_COMBINATOR.get(), this.createTableNBT("elemental_combinator", ModBlocks.ELEMENTAL_COMBINATOR.get()));
        this.lootTables.put(ModBlocks.ELEMENTAL_STONE.get(), this.createOreTable("elemental_ore", ModBlocks.ELEMENTAL_STONE.get(), ModItems.ELEMENTAL_SHARDS.get()));
    }
}
