package frostygames0.elementalamulets.client.color.item;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import frostygames0.elementalamulets.ElementalAmulets;
import frostygames0.elementalamulets.element.ElementalComposition;
import frostygames0.elementalamulets.initialization.ModDataComponents;
import net.minecraft.client.color.item.ItemTintSource;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public record ElementalCompositionTintSource(int defaultColor) implements ItemTintSource {
    public static final ResourceLocation ID = ElementalAmulets.id("elemental_composition");
    public static final int DEFAULT_COLOR = 0xFF6CD0D0;

    public static final MapCodec<ElementalCompositionTintSource> MAP_CODEC = RecordCodecBuilder.mapCodec(
            p_387230_ ->
                    p_387230_.group(
                            ExtraCodecs.RGB_COLOR_CODEC.fieldOf("default").forGetter(ElementalCompositionTintSource::defaultColor)
                    ).apply(p_387230_, ElementalCompositionTintSource::new)
    );

    public ElementalCompositionTintSource() {
        this(DEFAULT_COLOR);
    }

    @Override
    public int calculate(ItemStack stack, @Nullable ClientLevel level, @Nullable LivingEntity entity) {
        var composition = stack.getOrDefault(ModDataComponents.ELEMENTAL_COMPOSITION, ElementalComposition.EMPTY);

        if (composition.isEmpty()) {
            return defaultColor;
        }

        return composition.getColor();
    }

    @Override
    public MapCodec<? extends ItemTintSource> type() {
        return MAP_CODEC;
    }
}
