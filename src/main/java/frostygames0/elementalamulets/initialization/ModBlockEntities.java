package frostygames0.elementalamulets.initialization;

import frostygames0.elementalamulets.ElementalAmulets;
import frostygames0.elementalamulets.block.SimpleGeneratorBlockEntity;
import frostygames0.elementalamulets.block.SimpleStorageBlockEntity;
import frostygames0.elementalamulets.block.entity.TestBlockEntity;
import frostygames0.elementalamulets.block.entity.extractor.PrimitiveElementalExtractorBlockEntity;
import frostygames0.elementalamulets.block.entity.pipe.PressurizerPipeBlockEntity;
import frostygames0.elementalamulets.block.entity.pipe.ElementalPipeBlockEntity;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.Set;

public final class ModBlockEntities {
    private ModBlockEntities() {
    }

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, ElementalAmulets.MOD_ID);

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<PrimitiveElementalExtractorBlockEntity>> PRIMITIVE_ELEMENTAL_EXTRACTOR =
            BLOCK_ENTITY_TYPES.register("primitive_elemental_extractor", () -> new BlockEntityType<>(PrimitiveElementalExtractorBlockEntity::new, ModBlocks.PRIMITIVE_ELEMENTAL_EXTRACTOR.get()));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<ElementalPipeBlockEntity>> ELEMENTAL_PIPE =
            BLOCK_ENTITY_TYPES.register("elemental_pipe", () -> new BlockEntityType<>(ElementalPipeBlockEntity::new, Set.of(ModBlocks.ELEMENTAL_PIPE.get())));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<PressurizerPipeBlockEntity>> PRESSURIZER_PIPE =
            BLOCK_ENTITY_TYPES.register("pressurizer_pipe", () -> new BlockEntityType<>(PressurizerPipeBlockEntity::new, Set.of(ModBlocks.PRESSURIZER_PIPE.get())));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<SimpleStorageBlockEntity>> SIMPLE_STORAGE =
            BLOCK_ENTITY_TYPES.register("simple_storage", () -> new BlockEntityType<>(SimpleStorageBlockEntity::new, Set.of(ModBlocks.SIMPLE_STORAGE.get())));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<SimpleGeneratorBlockEntity>> SIMPLE_GENERATOR =
            BLOCK_ENTITY_TYPES.register("simple_generator", () -> new BlockEntityType<>(SimpleGeneratorBlockEntity::new, Set.of(ModBlocks.SIMPLE_GENERATOR.get())));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<TestBlockEntity>> TEST_BLOCK =
            BLOCK_ENTITY_TYPES.register("test_block", () -> new BlockEntityType<>(TestBlockEntity::new, Set.of(ModBlocks.TEST_BLOCK.get())));

    public static void register(IEventBus bus) {
        BLOCK_ENTITY_TYPES.register(bus);
    }
}
