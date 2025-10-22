package frostygames0.elementalamulets.initialization;

import com.google.common.collect.ImmutableSet;
import com.mojang.logging.LogUtils;
import frostygames0.elementalamulets.ElementalAmulets;
import frostygames0.elementalamulets.element.ElementType;
import frostygames0.elementalamulets.element.ElementalHelper;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.neoforged.neoforge.registries.DataPackRegistryEvent;
import org.slf4j.Logger;

import java.util.Set;
import java.util.stream.Collectors;

public final class ModElements {
    private ModElements() {
    }

    private static final Logger LOGGER = LogUtils.getLogger();

    public static final ResourceKey<Registry<ElementType>> ELEMENTS = ResourceKey.createRegistryKey(
            ElementalAmulets.id("elements"));

    public static final ResourceKey<ElementType> FIRE = createKey("fire");
    public static final ResourceKey<ElementType> WATER = createKey("water");
    public static final ResourceKey<ElementType> EARTH = createKey("earth");
    public static final ResourceKey<ElementType> AIR = createKey("air");
    public static final ResourceKey<ElementType> AETHER = createKey("aether");

    public static void bootstrap(BootstrapContext<ElementType> context) {
        registerToContext(context, FIRE, 0xffff2a00);
        registerToContext(context, AIR, 0xfffcf2d2);
        registerToContext(context, EARTH, 0xff3fa334);
        registerToContext(context, AETHER, 0xffdb2ecd, ImmutableSet.of(FIRE, WATER, EARTH, AIR), true);
        registerToContext(context, WATER, 0xff4287f5);
    }

    private static ResourceKey<ElementType> createKey(String name) {
        return ResourceKey.create(ELEMENTS, ElementalAmulets.id(name));
    }

    private static void registerToContext(BootstrapContext<ElementType> bootstrapContext, ResourceKey<ElementType> key, int color) {
        registerToContext(bootstrapContext, key, color, false);
    }

    private static void registerToContext(BootstrapContext<ElementType> bootstrapContext, ResourceKey<ElementType> key, int color, boolean hasDescription) {
        registerToContext(bootstrapContext, key, color, ImmutableSet.of(), hasDescription);
    }

    private static void registerToContext(BootstrapContext<ElementType> bootstrapContext, ResourceKey<ElementType> key, int color, Set<ResourceKey<ElementType>> composition, boolean hasDescription) {
        var lookup = bootstrapContext.lookup(ELEMENTS);

        var mappedSet = composition.stream().map(k -> (Holder<ElementType>) lookup.getOrThrow(k)).collect(Collectors.toSet());

        var element = ElementType.createWithDefaultTranslationKeys(key.location(), color, mappedSet, hasDescription);
        bootstrapContext.register(key, element);
    }

    public static void onRegisterDatapackRegistries(DataPackRegistryEvent.NewRegistry event) {
        event.dataPackRegistry(ELEMENTS,
                ElementType.DIRECT_CODEC,
                ElementType.DIRECT_CODEC,
                builder -> builder
                        .onAdd(ModElements::onElementAdded)
                        .onBake(ModElements::onElementRegistryBaked)
        );
    }

    private static void onElementAdded(Registry<ElementType> registry, int id, ResourceKey<ElementType> resourceKey, ElementType value) {
        LOGGER.debug("Registered element: {}", resourceKey.location());
    }

    private static void onElementRegistryBaked(Registry<ElementType> registry) {
        checkElementsForCompositionCycles(registry);
    }

    private static void checkElementsForCompositionCycles(Registry<ElementType> registry) {
        LOGGER.info("Checking Elements registry for composition cycles.");
        var nonPrimordialElements = registry.listElements().filter(holder -> !holder.value().isPrimordial()).toList();

        for (var elementHolder : nonPrimordialElements) {
            if (ElementalHelper.hasCompositionCycle(elementHolder)) {
                throw new IllegalStateException(String.format("A cycle was found while traversing the composition of element %s.", elementHolder.getKey().location()));
            }
        }
    }
}
