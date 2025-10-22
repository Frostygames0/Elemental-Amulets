package frostygames0.elementalamulets.initialization;

import frostygames0.elementalamulets.ElementalAmulets;
import frostygames0.elementalamulets.block.entity.SimpleStorageBlockEntity;
import frostygames0.elementalamulets.block.entity.extractor.AbstractElementalExtractorBlockEntity;
import frostygames0.elementalamulets.block.entity.extractor.PrimitiveElementalExtractorBlockEntity;
import frostygames0.elementalamulets.element.storage.IElementStorage;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.capabilities.BlockCapability;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.ItemCapability;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;

public final class ModCapabilities {
    private ModCapabilities() {
    }

    public static final ResourceLocation ELEMENT_STORAGE_ID = ElementalAmulets.id("element_storage");

    public static final BlockCapability<IElementStorage, Direction> ELEMENT_STORAGE_BLOCK =
            BlockCapability.createSided(ELEMENT_STORAGE_ID, IElementStorage.class);

    public static final ItemCapability<IElementStorage, Void> ELEMENT_STORAGE_ITEM =
            ItemCapability.createVoid(ELEMENT_STORAGE_ID, IElementStorage.class);

    public static void onRegisterCapabilities(final RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, ModBlockEntities.PRIMITIVE_ELEMENTAL_EXTRACTOR.get(), PrimitiveElementalExtractorBlockEntity::getItemHandler);
        event.registerBlockEntity(ELEMENT_STORAGE_BLOCK, ModBlockEntities.PRIMITIVE_ELEMENTAL_EXTRACTOR.get(), AbstractElementalExtractorBlockEntity::getElementStorage);
        event.registerBlockEntity(ELEMENT_STORAGE_BLOCK, ModBlockEntities.SIMPLE_STORAGE.get(), SimpleStorageBlockEntity::getElementStorage);
    }
}
