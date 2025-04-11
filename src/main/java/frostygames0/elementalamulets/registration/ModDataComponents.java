package frostygames0.elementalamulets.registration;

import frostygames0.elementalamulets.ElementalAmulets;
import frostygames0.elementalamulets.element.ElementalComposition;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ModDataComponents {
    private ModDataComponents() {
    }

    public static final DeferredRegister.DataComponents DATA_COMPONENTS = DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, ElementalAmulets.MOD_ID);

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<ElementalComposition>> ELEMENTAL_COMPOSITION =
            DATA_COMPONENTS.registerComponentType("elemental_composition", builder -> builder
                    .persistent(ElementalComposition.CODEC)
                    .networkSynchronized(ElementalComposition.STREAM_CODEC)
                    .cacheEncoding());
}
