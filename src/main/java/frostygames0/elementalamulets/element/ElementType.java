package frostygames0.elementalamulets.element;

import com.google.common.collect.ImmutableSet;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import frostygames0.elementalamulets.initialization.ModElements;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.RegistryFixedCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ARGB;
import net.minecraft.util.ExtraCodecs;
import net.neoforged.neoforge.common.util.NeoForgeExtraCodecs;

import java.util.Optional;
import java.util.Set;

public record ElementType(Component name, Optional<Component> description, int color, Set<Holder<ElementType>> composition) {
    public static final Codec<Holder<ElementType>> CODEC = RegistryFixedCodec.create(ModElements.ELEMENTS);
    public static final StreamCodec<RegistryFriendlyByteBuf, Holder<ElementType>> STREAM_CODEC = ByteBufCodecs.holderRegistry(ModElements.ELEMENTS);

    public static final Codec<ElementType> DIRECT_CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    ComponentSerialization.CODEC.fieldOf("name").forGetter(ElementType::name),
                    ComponentSerialization.CODEC.optionalFieldOf("description").forGetter(ElementType::description),
                    ExtraCodecs.RGB_COLOR_CODEC.fieldOf("color").forGetter(ElementType::color),
                    NeoForgeExtraCodecs.setOf(CODEC).optionalFieldOf("composition", ImmutableSet.of()).forGetter(ElementType::composition)
            ).apply(instance, ElementType::new));

    public ElementType {
        color = ARGB.opaque(color);
        composition = ImmutableSet.copyOf(composition);
    }

    public boolean isPrimordial() {
        return composition.isEmpty();
    }

    public MutableComponent colorizedName() {
        return name.plainCopy().withColor(color);
    }

    public static ElementType createWithDefaultTranslationKeys(ResourceLocation resourceLocation, int color, Set<Holder<ElementType>> composition, boolean hasDescription) {
        return new ElementType(
                Component.translatable(getNameTranslationKey(resourceLocation)),
                hasDescription ? Optional.of(Component.translatable(getDescriptionTranslationKey(resourceLocation))) : Optional.empty(),
                color, composition);
    }

    public static String getNameTranslationKey(ResourceLocation resourceLocation) {
        return resourceLocation.toLanguageKey(ModElements.ELEMENTS.location().getPath(), "name");
    }

    public static String getDescriptionTranslationKey(ResourceLocation resourceLocation) {
        return resourceLocation.toLanguageKey(ModElements.ELEMENTS.location().getPath(), "description");
    }

    public static Optional<Holder<ElementType>> deserializeFromNbt(Tag tag, HolderLookup.Provider provider) {
        return CODEC.parse(provider.createSerializationContext(NbtOps.INSTANCE), tag).resultOrPartial();
    }

    public static Optional<Tag> serializeToNbt(Holder<ElementType> element, HolderLookup.Provider provider) {
        return CODEC.encodeStart(provider.createSerializationContext(NbtOps.INSTANCE), element).resultOrPartial();
    }
}