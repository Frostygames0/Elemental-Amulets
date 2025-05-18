package frostygames0.elementalamulets.element;

import com.google.common.collect.ImmutableSet;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import frostygames0.elementalamulets.initialization.ModElements;
import net.minecraft.core.Holder;
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

public record Element(Component name, Optional<Component> description, int color, Set<Holder<Element>> composition) {
    public static final Codec<Holder<Element>> CODEC = RegistryFixedCodec.create(ModElements.ELEMENTS);
    public static final StreamCodec<RegistryFriendlyByteBuf, Holder<Element>> STREAM_CODEC = ByteBufCodecs.holderRegistry(ModElements.ELEMENTS);

    public static final Codec<Element> DIRECT_CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    ComponentSerialization.CODEC.fieldOf("name").forGetter(Element::name),
                    ComponentSerialization.CODEC.optionalFieldOf("description").forGetter(Element::description),
                    ExtraCodecs.RGB_COLOR_CODEC.fieldOf("color").forGetter(Element::color),
                    NeoForgeExtraCodecs.setOf(CODEC).optionalFieldOf("composition", ImmutableSet.of()).forGetter(Element::composition)
            ).apply(instance, Element::new));

    public Element {
        color = ARGB.opaque(color);
        composition = ImmutableSet.copyOf(composition);
    }

    public boolean isPrimordial() {
        return composition.isEmpty();
    }

    public MutableComponent colorizeNameMutable() {
        return name.plainCopy().withColor(color);
    }

    public static Element createWithDefaultTranslationKeys(ResourceLocation resourceLocation, int color, Set<Holder<Element>> composition, boolean hasDescription) {
        return new Element(
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
}