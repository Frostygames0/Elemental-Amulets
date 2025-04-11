package frostygames0.elementalamulets.registration;

import com.google.common.collect.ImmutableSet;
import com.mojang.logging.LogUtils;
import frostygames0.elementalamulets.ElementalAmulets;
import frostygames0.elementalamulets.element.Element;
import frostygames0.elementalamulets.element.ElementHelper;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.neoforged.neoforge.registries.DataPackRegistryEvent;
import org.slf4j.Logger;

import java.util.Set;
import java.util.stream.Collectors;

public final class Elements {
    private static final Logger LOGGER = LogUtils.getLogger();

    private Elements() {
    }

    public static final ResourceKey<Registry<Element>> ELEMENTS_REGISTRY_KEY = ResourceKey.createRegistryKey(
            ElementalAmulets.id("elements"));

    public static final ResourceKey<Element> FIRE = createKey("fire");
    public static final ResourceKey<Element> WATER = createKey("water");
    public static final ResourceKey<Element> EARTH = createKey("earth");
    public static final ResourceKey<Element> AIR = createKey("air");
    public static final ResourceKey<Element> AETHER = createKey("aether");

    public static void bootstrap(BootstrapContext<Element> context) {
        registerToContext(context, FIRE, 0xffff2a00, false);
        registerToContext(context, AIR, 0xfffcf2d2, false);
        registerToContext(context, EARTH, 0xff3fa334, false);
        registerToContext(context, AETHER, 0xffcd3eed, ImmutableSet.of(FIRE, WATER, EARTH, AIR), true);
        registerToContext(context, WATER, 0xff4287f5, false);
    }

    private static ResourceKey<Element> createKey(String name) {
        return ResourceKey.create(ELEMENTS_REGISTRY_KEY, ElementalAmulets.id(name));
    }

    private static void registerToContext(BootstrapContext<Element> bootstrapContext, ResourceKey<Element> key, int color, boolean hasDescription) {
        registerToContext(bootstrapContext, key, color, ImmutableSet.of(), hasDescription);
    }

    private static void registerToContext(BootstrapContext<Element> bootstrapContext, ResourceKey<Element> key, int color, Set<ResourceKey<Element>> composition, boolean hasDescription) {
        var lookup = bootstrapContext.lookup(ELEMENTS_REGISTRY_KEY);

        var mappedSet = composition.stream().map(k -> (Holder<Element>) lookup.getOrThrow(k)).collect(Collectors.toSet());

        var element = Element.createWithDefaultTranslationKeys(key.location(), color, mappedSet, hasDescription);
        bootstrapContext.register(key, element);
    }

    public static void onRegisterDatapackRegistries(DataPackRegistryEvent.NewRegistry event) {
        event.dataPackRegistry(ELEMENTS_REGISTRY_KEY,
                Element.DIRECT_CODEC,
                Element.DIRECT_CODEC,
                builder -> builder
                        .onAdd(Elements::onElementAdded)
                        .onBake(Elements::onElementRegistryBaked)
        );
    }

    private static void onElementAdded(Registry<Element> registry, int id, ResourceKey<Element> resourceKey, Element value) {
        LOGGER.debug("Registered element: {}", resourceKey.toString());
    }

    private static void onElementRegistryBaked(Registry<Element> registry) {
        checkElementsForCompositionCycles(registry);
    }

    private static void checkElementsForCompositionCycles(Registry<Element> registry) {
        LOGGER.info("Checking Elements registry for composition cycles.");
        var nonPrimordialElements = registry.listElements().filter(holder -> !holder.value().isPrimordial()).toList();

        for (var elementHolder : nonPrimordialElements) {
            if (ElementHelper.hasCycle(elementHolder)) {
                ;
                var presentableName = String.format("%s [%s]", elementHolder.value().name().getString(), elementHolder.getKey().location());
                throw new IllegalStateException(
                        String.format("A cycle has been found while traversing the composition of %s.", presentableName));
            }
        }
        LOGGER.info("The check of Elements registry for composition loops was successfully completed! :)");
    }
}
