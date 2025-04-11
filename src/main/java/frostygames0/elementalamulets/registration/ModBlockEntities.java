package frostygames0.elementalamulets.registration;

import frostygames0.elementalamulets.ElementalAmulets;
import frostygames0.elementalamulets.block.entity.extractor.PrimitiveElementalExtractorBlockEntity;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ModBlockEntities {
    private ModBlockEntities() {
    }

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, ElementalAmulets.MOD_ID);

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<PrimitiveElementalExtractorBlockEntity>> PRIMITIVE_ELEMENTAL_EXTRACTOR =
            BLOCK_ENTITY_TYPES.register("primitive_elemental_extractor", () -> new BlockEntityType<>(PrimitiveElementalExtractorBlockEntity::new, ModBlocks.PRIMITIVE_ELEMENTAL_EXTRACTOR.get()));
}
