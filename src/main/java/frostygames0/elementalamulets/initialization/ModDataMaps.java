package frostygames0.elementalamulets.initialization;

import frostygames0.elementalamulets.ElementalAmulets;
import frostygames0.elementalamulets.element.ElementalComposition;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.datamaps.AdvancedDataMapType;
import net.neoforged.neoforge.registries.datamaps.DataMapType;
import net.neoforged.neoforge.registries.datamaps.RegisterDataMapTypesEvent;

public final class ModDataMaps {
    private ModDataMaps() {
    }

    public static final DataMapType<Item, ElementalComposition> ELEMENTAL_COMPOSITION = AdvancedDataMapType.builder(
                    ElementalAmulets.id("elemental_composition"),
                    Registries.ITEM,
                    ElementalComposition.CODEC)
            .merger((registry, key1,
                     value1, key2, value2)
                    -> value1.merge(value2))
            .synced(ElementalComposition.CODEC, true)
            .build();

    public static void onRegisterDataMaps(RegisterDataMapTypesEvent event) {
        event.register(ELEMENTAL_COMPOSITION);
    }
}
