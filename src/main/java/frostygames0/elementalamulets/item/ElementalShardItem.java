package frostygames0.elementalamulets.item;

import frostygames0.elementalamulets.element.ElementHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public final class ElementalShardItem extends Item {
    public ElementalShardItem(Properties properties) {
        super(properties);
    }

    @Override
    public Component getName(ItemStack stack) {
        var composition = ElementHelper.getStackElementalComposition(stack);

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
}
