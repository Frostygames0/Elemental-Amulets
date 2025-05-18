package frostygames0.elementalamulets.initialization;

import frostygames0.elementalamulets.ElementalAmulets;
import frostygames0.elementalamulets.element.ElementHelper;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ModCreativeModeTabs {
    private ModCreativeModeTabs() {
    }

    public static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, ElementalAmulets.MOD_ID);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> MAIN =
            TABS.register("main", () -> CreativeModeTab.builder()
                    .title(Component.translatable("tab.elementalamulets.main"))
                    .icon(ModItems.ELEMENTUM_SHARD::toStack)
                    .displayItems((parameters, output) -> {
                        generateShardsForEachElement(parameters, output);
                        output.accept(ModItems.RING_OF_ELEMENTAL_SENSE);
                        output.accept(ModItems.ELEMENTAL_EXTRACTOR);
                        output.accept(ModItems.ELEMENTAL_PIPE);
                        output.accept(ModItems.ELEMENTUM_CRYSTAL_ORE);
                        output.accept(ModItems.ELEMENTUM_CRYSTAL_DEEPSLATE_ORE);
                    })
                    .build());

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> TESTING =
            TABS.register("testing", () -> CreativeModeTab.builder()
                    .title(Component.literal("Elemental Amulets: Testing"))
                    .icon(ModItems.TEST_BLOCK::toStack)
                    .withLabelColor(0xFFFF0000)
                    .displayItems(((parameters, output) -> {
                        output.accept(ModItems.TEST_BLOCK);
                        output.accept(ModItems.SIMPLE_GENERATOR);
                        output.accept(ModItems.SIMPLE_STORAGE);
                    }))
                    .build());

    private static void generateShardsForEachElement(CreativeModeTab.ItemDisplayParameters parameters, CreativeModeTab.Output output) {
        output.accept(ModItems.ELEMENTUM_SHARD);
        parameters.holders()
                .lookupOrThrow(ModElements.ELEMENTS)
                .listElements().forEach(element -> output.accept(ElementHelper.createShardWithElement(element)));
    }

    public static void register(IEventBus bus) {
        TABS.register(bus);
    }
}
