package frostygames0.elementalamulets.element;

import com.google.common.base.Preconditions;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import frostygames0.elementalamulets.initialization.ModElements;
import frostygames0.elementalamulets.util.MapUtils;
import net.minecraft.Util;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.ARGB;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.inventory.tooltip.TooltipComponent;

import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Consumer;

@Immutable
public sealed class ElementalComposition implements TooltipComponent {
    public static final ElementalComposition EMPTY = new ElementalComposition(Collections.unmodifiableSequencedMap(createEmptyInnerMap()));

    private static final Codec<ElementalComposition> FROM_MAP_CODEC =
            ExtraCodecs.nonEmptyMap(ExtraCodecs.strictUnboundedMap(ElementType.CODEC, Codec.intRange(1, Integer.MAX_VALUE)))
                    .xmap(ElementalComposition::createInnerMapFromBase, seq -> seq)
                    .xmap(ElementalComposition::createImmutableFromMap, comp -> comp.composition)
                    .validate(ElementalComposition::validateImmutability);

    private static final Codec<ElementalComposition> FROM_LIST_CODEC =
            Codec.list(Codec.pair(ElementType.CODEC.fieldOf("element").codec(), Codec.intRange(1, Integer.MAX_VALUE).fieldOf("amount").codec()))
                    .xmap(ElementalComposition::fromListOfPairs, ElementalComposition::toListOfPairs)
                    .validate(ElementalComposition::validateImmutability);

    public static Codec<ElementalComposition> JSON_CODEC = Codec.withAlternative(FROM_MAP_CODEC, FROM_LIST_CODEC);

    public static Codec<ElementalComposition> NBT_CODEC = Codec.withAlternative(FROM_LIST_CODEC, FROM_MAP_CODEC);

    public static final StreamCodec<RegistryFriendlyByteBuf, ElementalComposition> STREAM_CODEC =
            ByteBufCodecs.fromCodecWithRegistries(NBT_CODEC);

    protected final SequencedMap<Holder<ElementType>, Integer> composition;

    private ElementalComposition(SequencedMap<Holder<ElementType>, Integer> composition) {
        this.composition = composition;
    }

    private static DataResult<ElementalComposition> validateImmutability(ElementalComposition toValidate) {
        if (toValidate instanceof Mutable) {
            return DataResult.error(() -> "A mutable elemental composition can't be encoded: " + toValidate);
        }

        return DataResult.success(toValidate);
    }

    private static SequencedMap<Holder<ElementType>, Integer> createInnerMapFromBase(@Nullable Map<Holder<ElementType>, Integer> base) {
        return base == null || base.isEmpty() ? new LinkedHashMap<>() : new LinkedHashMap<>(base);
    }

    private static SequencedMap<Holder<ElementType>, Integer> createEmptyInnerMap() {
        return createInnerMapFromBase(null);
    }

    private static ElementalComposition createImmutableFromMap(SequencedMap<Holder<ElementType>, Integer> composition) {
        if (composition.isEmpty()) {
            return EMPTY;
        }

        return new ElementalComposition(Collections.unmodifiableSequencedMap(composition));
    }

    private static ElementalComposition fromListOfPairs(List<Pair<Holder<ElementType>, Integer>> listOfPairs) {
        if (listOfPairs.isEmpty()) {
            return EMPTY;
        }

        var map = MapUtils.createFromListOfPairs(ElementalComposition::createEmptyInnerMap, listOfPairs, Integer::sum);
        return createImmutableFromMap(map);
    }

    private static List<Pair<Holder<ElementType>, Integer>> toListOfPairs(ElementalComposition composition) {
        return MapUtils.convertToListOfPairs(composition.composition);
    }

    public static ElementalComposition fromSingle(Holder<ElementType> elementHolder, int amount) {
        return ElementalComposition.EMPTY.add(elementHolder, amount);
    }

    public boolean contains(Holder<ElementType> elementHolder) {
        return composition.containsKey(elementHolder);
    }

    public boolean contains(ResourceKey<ElementType> resourceKey) {
        return composition.entrySet().stream().anyMatch((entry) -> entry.getKey().is(resourceKey));
    }

    public boolean containsElements(Collection<Holder<ElementType>> elements) {
        return elements.stream().allMatch(this::contains);
    }

    public boolean contains(ElementalComposition other) {
        for (var entry : composition.entrySet()) {
            var element = entry.getKey();
            var amount = entry.getValue();

            if (!other.contains(element)) {
                return false;
            }

            if (amount < other.getAmount(element)) {
                return false;
            }
        }

        return true;
    }

    public SequencedSet<Holder<ElementType>> getElements() {
        return composition.sequencedKeySet();
    }

    public int getAmount(Holder<ElementType> elementHolder) {
        return composition.getOrDefault(elementHolder, 0);
    }

    public int getTotalAmount() {
        return composition.values().stream().reduce(Integer::sum).orElse(0);
    }

    public int size() {
        return composition.size();
    }

    public SequencedSet<Map.Entry<Holder<ElementType>, Integer>> getEntries() {
        return composition.sequencedEntrySet();
    }

    public int getColor() {
        var size = size();

        if (isEmpty()) {
            return 0;
        }

        int r = 0;
        int g = 0;
        int b = 0;

        for (var holder : getElements()) {
            var element = holder.value();
            var color = element.color();

            r += ARGB.red(color);
            g += ARGB.green(color);
            b += ARGB.blue(color);
        }

        return ARGB.color(r / size, g / size, b / size);
    }

    public boolean isEmpty() {
        return this == EMPTY || composition.isEmpty();
    }

    public ElementalComposition add(Holder<ElementType> elementHolder, int amount) {
        Preconditions.checkArgument(amount >= 0, "Amount can't be a negative value!");
        return createImmutableCopy(map -> MapUtils.mergeAndRemoveNegativeValues(map, elementHolder, amount));
    }

    public ElementalComposition merge(ElementalComposition other) {
        if (other.isEmpty()) {
            return this;
        }

        return createImmutableCopy(map -> other.composition.forEach((element, amount) -> {
            Preconditions.checkArgument(amount >= 0, "Amount can't be a negative value!");
            MapUtils.mergeAndRemoveNegativeValues(map, element, amount);
        }));
    }

    public ElementalComposition reduce(Holder<ElementType> elementHolder, int amount) {
        Preconditions.checkArgument(amount >= 0, "Amount can't be a negative value!");
        return createImmutableCopy(map -> MapUtils.mergeAndRemoveNegativeValues(map, elementHolder, -amount));
    }

    public ElementalComposition subtract(ElementalComposition other) {
        if (other.isEmpty()) {
            return this;
        }

        return createImmutableCopy(map -> other.composition.forEach((element, amount) -> {
            Preconditions.checkArgument(amount >= 0, "Amount can't be a negative value!");
            MapUtils.mergeAndRemoveNegativeValues(map, element, -amount);
        }));
    }

    public ElementalComposition applyToAmounts(BiFunction<Holder<ElementType>, Integer, Integer> transformer) {
        return createImmutableCopy((map) -> MapUtils.transformMapAndRemoveNegative(map, transformer));
    }

    protected ElementalComposition createImmutableCopy() {
        return ElementalComposition.createImmutableFromMap(copyInnerMap());
    }

    protected ElementalComposition createImmutableCopy(Consumer<SequencedMap<Holder<ElementType>, Integer>> action) {
        return ElementalComposition.createImmutableFromMap(Util.make(copyInnerMap(), action));
    }

    protected SequencedMap<Holder<ElementType>, Integer> copyInnerMap() {
        return createInnerMapFromBase(composition);
    }

    /**
     * @return Itself if the composition is already immutable, otherwise it makes an immutable copy.
     */
    public ElementalComposition toImmutable() {
        return this;
    }

    public ElementalComposition.Mutable toMutableCopy() {
        return new ElementalComposition.Mutable(this);
    }

    public static ElementalComposition.Mutable mutable() {
        return new ElementalComposition.Mutable();
    }

    public static Optional<ListTag> serializeToNbt(HolderLookup.Provider provider, ElementalComposition elementalComposition) {
        return NBT_CODEC.encodeStart(provider.createSerializationContext(NbtOps.INSTANCE), elementalComposition).resultOrPartial().map(tag -> (ListTag) tag);
    }

    public static Optional<ElementalComposition> deserializeFromNbt(HolderLookup.Provider provider, ListTag tag) {
        return NBT_CODEC.parse(provider.createSerializationContext(NbtOps.INSTANCE), tag).resultOrPartial();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (!(obj instanceof ElementalComposition other)) {
            return false;
        }

        return composition.equals(other.composition);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(composition);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        builder.append(getClass().getSimpleName());
        var iterator = composition.entrySet().iterator();
        if (!iterator.hasNext()) {
            builder.append("{}");
            return builder.toString();
        }

        builder.append("{");

        while (true) {
            var entry = iterator.next();

            builder.append('"').append(entry.getKey().getRegisteredName()).append('"');
            builder.append(": ");
            builder.append(entry.getValue());

            if (!iterator.hasNext()) {
                return builder.append('}').toString();
            }

            builder.append(',').append(' ');
        }
    }

    public static final class Mutable extends ElementalComposition {
        public Mutable(ElementalComposition base) {
            super(base.copyInnerMap());
        }

        public Mutable() {
            this(ElementalComposition.createEmptyInnerMap());
        }

        private Mutable(SequencedMap<Holder<ElementType>, Integer> composition) {
            super(composition);
        }

        private Mutable merge(Holder<ElementType> elementHolder, int amount) {
            MapUtils.mergeAndRemoveNegativeValues(composition, elementHolder, amount);
            return this;
        }

        @Override
        public Mutable add(Holder<ElementType> elementHolder, int amount) {
            Preconditions.checkArgument(amount >= 0, "Amount can't be a negative value!");
            return merge(elementHolder, amount);
        }

        @Override
        public Mutable merge(ElementalComposition other) {
            other.composition.forEach(this::add);
            return this;
        }

        @Override
        public Mutable reduce(Holder<ElementType> elementHolder, int amount) {
            Preconditions.checkArgument(amount >= 0, "Amount can't be a negative value!");
            return merge(elementHolder, -amount);
        }

        @Override
        public Mutable subtract(ElementalComposition other) {
            other.composition.forEach(this::reduce);
            return this;
        }

        @Override
        public Mutable applyToAmounts(BiFunction<Holder<ElementType>, Integer, Integer> transformer) {
            MapUtils.transformMapAndRemoveNegative(composition, transformer);
            return this;
        }

        @Override
        public ElementalComposition toImmutable() {
            return createImmutableCopy();
        }
    }

    public static Builder builder(HolderLookup.Provider registries) {
        return new Builder(registries);
    }

    public static class Builder {
        private final ElementalComposition.Mutable mutable = ElementalComposition.mutable();

        private final HolderLookup.RegistryLookup<ElementType> holderGetter;

        public Builder(HolderLookup.Provider registries) {
            holderGetter = registries.lookupOrThrow(ModElements.ELEMENTS);
        }

        public Builder add(ResourceKey<ElementType> element, int amount) {
            mutable.add(holderGetter.getOrThrow(element), amount);
            return this;
        }

        public ElementalComposition build() {
            return mutable.toImmutable();
        }
    }
}
