package frostygames0.elementalamulets.initialization;

import frostygames0.elementalamulets.ElementalAmulets;
import frostygames0.elementalamulets.block.SimpleStorageBlock;
import frostygames0.elementalamulets.block.extractor.PrimitiveElementalExtractorBlock;
import frostygames0.elementalamulets.block.pipe.ElementalPipeBlock;
import frostygames0.elementalamulets.block.pipe.PressurizerPipeBlock;
import frostygames0.elementalamulets.pipes.NewPipeBlock;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.item.DyeColor;
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
                    BlockBehaviour.Properties.of()
                            .requiresCorrectToolForDrops()
                            .strength(5.0F, 6.0F)
                            .sound(SoundType.CHAIN)
                            .noOcclusion()
                            .mapColor(DyeColor.GRAY)
                            .sound(SoundType.COPPER_GRATE));
    public static final DeferredBlock<PressurizerPipeBlock> PRESSURIZER_PIPE =
            BLOCKS.registerBlock("pressurizer_pipe",
                    PressurizerPipeBlock::new,
                    BlockBehaviour.Properties.of()
                            .requiresCorrectToolForDrops()
                            .strength(5.0F, 6.0F)
                            .sound(SoundType.CHAIN)
                            .noOcclusion()
                            .mapColor(DyeColor.GRAY)
                            .sound(SoundType.COPPER_GRATE));

    public static final DeferredBlock<SimpleStorageBlock> SIMPLE_STORAGE =
            BLOCKS.registerBlock("simple_storage",
                    SimpleStorageBlock::new,
                    BlockBehaviour.Properties.of().destroyTime(2f));

    public static final DeferredBlock<NewPipeBlock> NEW_PIPE =
            BLOCKS.registerBlock("new_pipe", NewPipeBlock::new);

    public static void register(IEventBus bus) {
        BLOCKS.register(bus);
    }
}
