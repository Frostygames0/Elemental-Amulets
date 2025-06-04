package frostygames0.elementalamulets.item;

import frostygames0.elementalamulets.element.ElementalHelper;
import frostygames0.elementalamulets.initialization.ModElements;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public final class ElementumShardItem extends Item {
    public ElementumShardItem(Properties properties) {
        super(properties);
    }

    @Override
    public Component getName(ItemStack stack) {
        var composition = ElementalHelper.getStackElementalComposition(stack);

        var suffix = Component.empty();

        if (composition.isPresent() && !composition.orElseThrow().isEmpty()) {
            var elementAmounts = composition.orElseThrow().elementAmounts();
            var inside = elementAmounts.size() > 1
                    ? Component.translatable("generic.elementalamulets.mixed")
                    : elementAmounts.keySet().stream().findFirst().orElseThrow().value().colorizeNameMutable();

            suffix.append(Component.translatable("generic.elementalamulets.round_brackets", inside));
        }

        return super.getName(stack).copy().append(" ").append(suffix);
    }

    @Override
    public boolean canBeHurtBy(ItemStack stack, DamageSource source) {
        if (!source.is(DamageTypeTags.IS_FIRE)) {
            return super.canBeHurtBy(stack, source);
        }

        var optionalComposition = ElementalHelper.getStackElementalComposition(stack);
        if (optionalComposition.isEmpty()) {
            return super.canBeHurtBy(stack, source);
        }

        var composition = optionalComposition.get();
        return composition.elementAmounts()
                .keySet()
                .stream()
                .noneMatch(element -> element.getKey() == ModElements.FIRE);
    }
}
