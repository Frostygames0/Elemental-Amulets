package frostygames0.elementalamulets.registration;

import frostygames0.elementalamulets.ElementalAmulets;
import frostygames0.elementalamulets.block.extractor.PrimitiveElementalExtractorBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ModBlocks {
    private ModBlocks() {
    }

    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(ElementalAmulets.MOD_ID);

    public static final DeferredBlock<PrimitiveElementalExtractorBlock> PRIMITIVE_ELEMENTAL_EXTRACTOR =
            BLOCKS.registerBlock("primitive_elemental_extractor", PrimitiveElementalExtractorBlock::new,
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.STONE)
                            .instrument(NoteBlockInstrument.BASEDRUM)
                            .requiresCorrectToolForDrops()
                            .strength(2.0F, 6.0F));
}
