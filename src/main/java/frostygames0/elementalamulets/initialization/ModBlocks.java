package frostygames0.elementalamulets.initialization;

import frostygames0.elementalamulets.ElementalAmulets;
import frostygames0.elementalamulets.block.TestBlock;
import frostygames0.elementalamulets.block.entity.SimpleGeneratorBlock;
import frostygames0.elementalamulets.block.entity.SimpleStorageBlock;
import frostygames0.elementalamulets.block.extractor.PrimitiveElementalExtractorBlock;
import frostygames0.elementalamulets.block.pipe.ElementalPipeBlock;
import frostygames0.elementalamulets.block.pipe.PipePressurizerBlock;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DropExperienceBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ModBlocks {
    private ModBlocks() {
    }

    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(ElementalAmulets.MOD_ID);

    public static final DeferredBlock<PrimitiveElementalExtractorBlock> PRIMITIVE_ELEMENTAL_EXTRACTOR =
            BLOCKS.registerBlock("primitive_elemental_extractor",
                    PrimitiveElementalExtractorBlock::new,
                    BlockBehaviour.Properties.ofFullCopy(Blocks.FURNACE));

    public static final DeferredBlock<DropExperienceBlock> ELEMENTUM_CRYSTAL_ORE =
            BLOCKS.registerBlock("elementum_crystal_ore",
                    (props) -> new DropExperienceBlock(UniformInt.of(1, 3), props),
                    BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_ORE));

    public static final DeferredBlock<DropExperienceBlock> ELEMENTUM_CRYSTAL_DEEPSLATE_ORE =
            BLOCKS.registerBlock("elementum_crystal_deepslate_ore",
                    (props) -> new DropExperienceBlock(UniformInt.of(1, 3), props),
                    BlockBehaviour.Properties.ofFullCopy(Blocks.DEEPSLATE_IRON_ORE));

    public static final DeferredBlock<ElementalPipeBlock> ELEMENTAL_PIPE =
            BLOCKS.registerBlock("elemental_pipe",
                    ElementalPipeBlock::new,
                    BlockBehaviour.Properties.ofFullCopy(Blocks.CHAIN)
                            .mapColor(DyeColor.GRAY)
                            .sound(SoundType.COPPER_GRATE));
    public static final DeferredBlock<PipePressurizerBlock> PRESSURIZER_PIPE =
            BLOCKS.registerBlock("pressurizer_pipe",
                    PipePressurizerBlock::new,
                    BlockBehaviour.Properties.ofFullCopy(Blocks.CHAIN)
                            .mapColor(DyeColor.GRAY)
                            .sound(SoundType.COPPER_GRATE));


    public static final DeferredBlock<Block> TEST_BLOCK = BLOCKS.registerBlock("test_block", TestBlock::new);

    public static final DeferredBlock<SimpleStorageBlock> SIMPLE_STORAGE = BLOCKS.registerBlock("simple_storage", SimpleStorageBlock::new);
    public static final DeferredBlock<SimpleGeneratorBlock> SIMPLE_GENERATOR = BLOCKS.registerBlock("simple_generator", SimpleGeneratorBlock::new);

    public static void register(IEventBus bus) {
        BLOCKS.register(bus);
    }
}
