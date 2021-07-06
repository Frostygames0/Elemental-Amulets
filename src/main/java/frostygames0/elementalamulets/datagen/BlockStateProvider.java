package frostygames0.elementalamulets.datagen;

import frostygames0.elementalamulets.ElementalAmulets;
import frostygames0.elementalamulets.blocks.ElementalCombinator;
import frostygames0.elementalamulets.core.init.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.fml.RegistryObject;

import java.util.function.Function;
import java.util.stream.Collectors;

public class BlockStateProvider extends net.minecraftforge.client.model.generators.BlockStateProvider {
    public BlockStateProvider(DataGenerator gen, ExistingFileHelper exFileHelper) {
        super(gen, ElementalAmulets.MOD_ID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {

        // Elemental Combinator
        /*simpleBlock(ModBlocks.ELEMENTAL_COMBINATOR.get(), models().cubeBottomTop(name(ModBlocks.ELEMENTAL_COMBINATOR.get()),
                modLoc( "block/"+name(ModBlocks.ELEMENTAL_COMBINATOR.get())+"_side"),
                mcLoc("block/furnace_top"),
                modLoc("block/"+name(ModBlocks.ELEMENTAL_COMBINATOR.get())+"_up_2.0")));*/

        blockstateBlock(ModBlocks.ELEMENTAL_COMBINATOR.get(), state -> {
            if(state.get(ElementalCombinator.COMBINING)) {
                return models().cubeBottomTop(name(ModBlocks.ELEMENTAL_COMBINATOR.get())+"_on",
                        modLoc( "block/"+name(ModBlocks.ELEMENTAL_COMBINATOR.get())+"_side"),
                        mcLoc("block/furnace_top"),
                        modLoc("block/"+name(ModBlocks.ELEMENTAL_COMBINATOR.get())+"_up_on"));
            } else {
                return models().cubeBottomTop(name(ModBlocks.ELEMENTAL_COMBINATOR.get()),
                        modLoc( "block/"+name(ModBlocks.ELEMENTAL_COMBINATOR.get())+"_side"),
                        mcLoc("block/furnace_top"),
                        modLoc("block/"+name(ModBlocks.ELEMENTAL_COMBINATOR.get())+"_up_off"));
            }
        });

        // Elemental Stone
        simpleBlock(ModBlocks.ELEMENTAL_STONE.get());

        for(Block block : ModBlocks.BLOCKS.getEntries().stream().map(RegistryObject::get).collect(Collectors.toList())) {
            simpleBlockItem(block, models().getExistingFile(modLoc("block/"+name(block))));
        }
    }

    private void blockstateBlock(Block block, Function<BlockState, ModelFile> func) {
        getVariantBuilder(block).forAllStates(state -> ConfiguredModel.builder().modelFile(func.apply(state)).build());
    }

    private String name(Block block) {
        return block.getRegistryName().getPath();
    }
}
