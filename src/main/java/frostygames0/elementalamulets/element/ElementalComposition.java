package frostygames0.elementalamulets.element;

import com.google.common.collect.ImmutableMap;
import com.mojang.serialization.Codec;
import frostygames0.elementalamulets.initialization.ModElements;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.level.Level;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

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
        return elementAmounts.isEmpty();
    }

    public int getTotalAmount() {
        int total = 0;
        for (var amount : elementAmounts.values()) {
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
            var amountToMerge = copy.get(holder) + amount;
            if (amountToMerge == 0) {
                copy.remove(holder);
            } else {
                copy.put(holder, amountToMerge);
            }
        }

        return new ElementalComposition(copy);
    }

    public static ElementalComposition fromSingle(Holder<Element> elementHolder, int amount) {
        return new ElementalComposition(ImmutableMap.of(elementHolder, amount));
    }

    public static Optional<CompoundTag> toNbtTag(HolderLookup.Provider provider, ElementalComposition elementalComposition) {
        return CODEC.encodeStart(provider.createSerializationContext(NbtOps.INSTANCE), elementalComposition).resultOrPartial().map(tag -> (CompoundTag) tag);
    }

    public static Optional<ElementalComposition> fromNbtTag(HolderLookup.Provider provider, CompoundTag tag) {
        return CODEC.parse(provider.createSerializationContext(NbtOps.INSTANCE), tag).resultOrPartial();
    }

    public static Builder builder(HolderLookup.Provider lookupProvider) {
        return new Builder(lookupProvider);
    }

    public static Builder builder(Level level) {
        return new Builder(level.registryAccess());
    }

    public static class Builder {
        private final HolderLookup<Element> holderLookup;
        private final ImmutableMap.Builder<Holder<Element>, Integer> builder;

        public Builder(HolderLookup.Provider lookupProvider) {
            holderLookup = lookupProvider.lookupOrThrow(ModElements.ELEMENTS);
            builder = ImmutableMap.builder();
        }

        public Builder addElement(ResourceKey<Element> elementKey, int amount) {
            var elementHolder = holderLookup.getOrThrow(elementKey);

            builder.put(elementHolder, amount);

            return this;
        }

        public ElementalComposition build() {
            return new ElementalComposition(builder.buildOrThrow());
        }
    }
}
