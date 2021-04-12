package frostygames0.elementalamulets.datagen;

import frostygames0.elementalamulets.ElementalAmulets;
import frostygames0.elementalamulets.core.init.ModBlocks;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;

public class BlockStateProvider extends net.minecraftforge.client.model.generators.BlockStateProvider {
    public BlockStateProvider(DataGenerator gen, ExistingFileHelper exFileHelper) {
        super(gen, ElementalAmulets.MOD_ID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        simpleBlock(ModBlocks.ELEMENTAL_CRAFTER.get(), cubeAll(ModBlocks.ELEMENTAL_CRAFTER.get()));
    }
}
