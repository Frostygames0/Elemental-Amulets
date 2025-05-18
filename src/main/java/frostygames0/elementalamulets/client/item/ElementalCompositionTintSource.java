package frostygames0.elementalamulets.client.item;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import frostygames0.elementalamulets.element.Element;
import frostygames0.elementalamulets.element.ElementalComposition;
import frostygames0.elementalamulets.initialization.ModDataComponents;
import net.minecraft.client.color.item.ItemTintSource;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.Holder;
import net.minecraft.util.ARGB;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public record ElementalCompositionTintSource(int defaultColor) implements ItemTintSource {
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
        var data = stack.getOrDefault(ModDataComponents.ELEMENTAL_COMPOSITION, ElementalComposition.EMPTY);
        var elementAmounts = data.elementAmounts();

        if (elementAmounts.isEmpty()) {
            return defaultColor;
        } else if (elementAmounts.size() == 1) {
            return elementAmounts
                    .entrySet()
                    .stream()
                    .findAny()
                    .orElseThrow()
                    .getKey()
                    .value().color();
        }

        return mixElementColors(elementAmounts);
    }

    @Override
    public MapCodec<? extends ItemTintSource> type() {
        return MAP_CODEC;
    }

    private static int mixElementColors(Map<Holder<Element>, Integer> elementAmounts) {
        var size = elementAmounts.size();

        int r = 0;
        int g = 0;
        int b = 0;

        for (var holder : elementAmounts.keySet()) {
            var element = holder.value();

            var color = element.color();

            r += ARGB.red(color);
            g += ARGB.green(color);
            b += ARGB.blue(color);
        }

        return ARGB.color(r / size, g / size, b / size);
    }
}
