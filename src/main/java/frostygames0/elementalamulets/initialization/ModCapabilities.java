package frostygames0.elementalamulets.initialization;

import frostygames0.elementalamulets.ElementalAmulets;
import frostygames0.elementalamulets.block.entity.extractor.PrimitiveElementalExtractorBlockEntity;
import frostygames0.elementalamulets.element.storage.IElementStorage;
import net.minecraft.core.Direction;
import net.neoforged.neoforge.capabilities.BlockCapability;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.ItemCapability;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;

public final class ModCapabilities {
    private ModCapabilities() {
    }

    public static final BlockCapability<IElementStorage, Direction> ELEMENT_STORAGE_BLOCK =
            BlockCapability.createSided(ElementalAmulets.id("element_storage"), IElementStorage.class);

    public static final ItemCapability<IElementStorage, Void> ELEMENT_STORAGE_ITEM =
            ItemCapability.createVoid(ElementalAmulets.id("element_storage"), IElementStorage.class);

    public static void onRegisterCapabilities(final RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, ModBlockEntities.PRIMITIVE_ELEMENTAL_EXTRACTOR.get(), PrimitiveElementalExtractorBlockEntity::getItemHandlerForDirection);
        event.registerBlockEntity(ELEMENT_STORAGE_BLOCK, ModBlockEntities.PRIMITIVE_ELEMENTAL_EXTRACTOR.get(), (blockEntity, ctx) -> blockEntity.getElementStorage());
        event.registerBlockEntity(ELEMENT_STORAGE_BLOCK, ModBlockEntities.SIMPLE_STORAGE.get(), (blockEntity, direction) -> blockEntity.getElementStorage());
    }
}
