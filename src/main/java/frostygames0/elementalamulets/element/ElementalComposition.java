package frostygames0.elementalamulets.element;

import com.google.common.collect.ImmutableMap;
import com.mojang.serialization.Codec;
import frostygames0.elementalamulets.registration.Elements;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.inventory.tooltip.TooltipComponent;

import java.util.HashMap;
import java.util.Map;

public record ElementalComposition(Map<Holder<Element>, Integer> elementAmounts) implements TooltipComponent {
    public static final ElementalComposition EMPTY = new ElementalComposition(Map.of());

    public static final Codec<ElementalComposition> CODEC =
            ExtraCodecs.nonEmptyMap(ExtraCodecs.strictUnboundedMap(Element.CODEC, Codec.intRange(1, Integer.MAX_VALUE)))
                    .xmap(ElementalComposition::new, ElementalComposition::elementAmounts);

    public static final StreamCodec<RegistryFriendlyByteBuf, ElementalComposition> STREAM_CODEC =
            StreamCodec.composite(
                    ByteBufCodecs.map(HashMap::new, Element.STREAM_CODEC, ByteBufCodecs.VAR_INT), ElementalComposition::elementAmounts,
                    ElementalComposition::new);

    public ElementalComposition {
        elementAmounts = ImmutableMap.copyOf(elementAmounts);
    }

    public boolean isEmpty() {
        return this.elementAmounts.isEmpty();
    }

    public int getTotalAmount() {
        int total = 0;
        for (var amount : this.elementAmounts.values()) {
            total += amount;
        }

        return total;
    }

    public ElementalComposition merge(ElementalComposition other) {
        return merge(other.elementAmounts());
    }

    public ElementalComposition merge(Map<Holder<Element>, Integer> other) {
        var copy = new HashMap<>(elementAmounts);

        for (var entry : other.entrySet()) {
            var holder = entry.getKey();
            var amount = entry.getValue();

            copy.putIfAbsent(holder, 0);
            copy.put(holder, copy.get(holder) + amount);
        }

        return new ElementalComposition(copy);
    }

    public static ElementalComposition fromSingle(Holder<Element> elementHolder, int amount) {
        return new ElementalComposition(ImmutableMap.of(elementHolder, amount));
    }

    public static Builder builder(HolderLookup.Provider lookupProvider) {
        return new Builder(lookupProvider);
    }

    public static class Builder {
        private final HolderLookup<Element> holderLookup;
        private final ImmutableMap.Builder<Holder<Element>, Integer> builder;

        public Builder(HolderLookup.Provider lookupProvider) {
            holderLookup = lookupProvider.lookupOrThrow(Elements.ELEMENTS_REGISTRY_KEY);
            builder = ImmutableMap.builder();
        }

        public Builder addElement(ResourceKey<Element> elementKey, int amount) {
            var elementHolder = holderLookup.getOrThrow(elementKey);

            builder.put(elementHolder, amount);

            return this;
        }

        public Builder addElement(Holder<Element> element, int amount) {
            builder.put(element, amount);
            return this;
        }

        public ElementalComposition build() {
            return new ElementalComposition(builder.buildOrThrow());
        }
    }
}
