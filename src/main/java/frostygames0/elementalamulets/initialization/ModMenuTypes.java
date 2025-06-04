package frostygames0.elementalamulets.initialization;

import frostygames0.elementalamulets.ElementalAmulets;
import frostygames0.elementalamulets.inventory.menu.SimpleStorageMenu;
import frostygames0.elementalamulets.inventory.menu.extractor.PrimitiveElementalExtractorMenu;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ModMenuTypes {
    private ModMenuTypes() {
    }

    public static final DeferredRegister<MenuType<?>> MENU_TYPES = DeferredRegister.create(Registries.MENU, ElementalAmulets.MOD_ID);

    public static final DeferredHolder<MenuType<?>, MenuType<PrimitiveElementalExtractorMenu>> PRIMITIVE_ELEMENTAL_EXTRACTOR =
            MENU_TYPES.register("primitive_elemental_extractor", () -> new MenuType<>(PrimitiveElementalExtractorMenu::forClient, FeatureFlags.DEFAULT_FLAGS));

    public static final DeferredHolder<MenuType<?>, MenuType<SimpleStorageMenu>> SIMPLE_STORAGE_MENU =
            MENU_TYPES.register("simple_storage", () -> new MenuType<>(SimpleStorageMenu::forClient, FeatureFlags.DEFAULT_FLAGS));

    public static void register(IEventBus bus) {
        MENU_TYPES.register(bus);
    }
}
