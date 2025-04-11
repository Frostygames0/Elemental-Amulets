package frostygames0.elementalamulets.registration;

import frostygames0.elementalamulets.ElementalAmulets;
import frostygames0.elementalamulets.element.ElementHelper;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ModCreativeModeTabs {
    private ModCreativeModeTabs() {
    }

    public static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, ElementalAmulets.MOD_ID);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> TAB =
            TABS.register("tab", () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.elementalamulets"))
                    .icon(() -> new ItemStack(ModItems.ELEMENT_SHARD.get()))
                    .displayItems((parameters, output) -> {
                        generateShardsForEachElement(ModItems.ELEMENT_SHARD, parameters, output);
                        output.accept(ModItems.ELEMENTAL_EXTRACTOR);
                        output.accept(ModItems.RING_OF_ELEMENTAL_SENSE);
                    })
                    .build());

    private static void generateShardsForEachElement(ItemLike item, CreativeModeTab.ItemDisplayParameters parameters, CreativeModeTab.Output output) {
        output.accept(item);

        parameters.holders()
                .lookupOrThrow(Elements.ELEMENTS_REGISTRY_KEY)
                .listElements().forEach(element -> output.accept(ElementHelper.createStackForElement(item, element, 1)));
    }
}
