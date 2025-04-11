package frostygames0.elementalamulets.registration;

import frostygames0.elementalamulets.ElementalAmulets;
import frostygames0.elementalamulets.block.entity.extractor.PrimitiveElementalExtractorBlockEntity;
import frostygames0.elementalamulets.element.storage.IElementStorage;
import net.neoforged.neoforge.capabilities.BlockCapability;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;

public final class ModCapabilities {
    private ModCapabilities() {
    }

    public static final BlockCapability<IElementStorage, Void> ELEMENT_STORAGE_BLOCK =
            BlockCapability.createVoid(ElementalAmulets.id("element_storage"), IElementStorage.class);

    public static void onRegisterCapabilitiesEvent(final RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, ModBlockEntities.PRIMITIVE_ELEMENTAL_EXTRACTOR.get(), PrimitiveElementalExtractorBlockEntity::getItemHandlerForDirection);
        event.registerBlockEntity(ELEMENT_STORAGE_BLOCK, ModBlockEntities.PRIMITIVE_ELEMENTAL_EXTRACTOR.get(), (blockEntity, ctx) -> blockEntity.getElementStorage());
    }
}
