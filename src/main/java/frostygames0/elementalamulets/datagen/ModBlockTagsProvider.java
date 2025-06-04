package frostygames0.elementalamulets.datagen;

import frostygames0.elementalamulets.ElementalAmulets;
import frostygames0.elementalamulets.initialization.ModBlocks;
import frostygames0.elementalamulets.initialization.tags.ModBlockTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.neoforged.neoforge.common.data.BlockTagsProvider;

import java.util.concurrent.CompletableFuture;

public class ModBlockTagsProvider extends BlockTagsProvider {
    public ModBlockTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider, ElementalAmulets.MOD_ID);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(ModBlockTags.ELEMENTUM_CRYSTAL_ORES)
                .add(ModBlocks.ELEMENTUM_CRYSTAL_ORE.get())
                .add(ModBlocks.ELEMENTUM_CRYSTAL_DEEPSLATE_ORE.get());

        tag(ModBlockTags.ELEMENTAL_PIPES)
                .add(ModBlocks.ELEMENTAL_PIPE.get())
                .add(ModBlocks.PRESSURIZER_PIPE.get());

        tag(BlockTags.MINEABLE_WITH_PICKAXE)
                .add(ModBlocks.PRIMITIVE_ELEMENTAL_EXTRACTOR.get())
                .add(ModBlocks.ELEMENTAL_PIPE.get())
                .addTag(ModBlockTags.ELEMENTUM_CRYSTAL_ORES);

        tag(BlockTags.NEEDS_STONE_TOOL)
                .addTag(ModBlockTags.ELEMENTUM_CRYSTAL_ORES);

        tag(BlockTags.NEEDS_IRON_TOOL)
                .addTag(ModBlockTags.ELEMENTAL_PIPES);
    }
}
