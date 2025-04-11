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
        var composition = ElementHelper.getItemComposition(stack);

        var suffix = Component.empty();

        if (composition.isPresent() && !composition.get().isEmpty()) {
            suffix.append(" (");

            var elementAmounts = composition.get().elementAmounts();
            var elementName = elementAmounts.size() > 1 ? Component.literal("Mixed") : elementAmounts.keySet().stream().findFirst().orElseThrow().value().colorizeNameMutable();
            suffix.append(elementName);

            suffix.append(")");
        }

        return super.getName(stack).copy().append(suffix);
    }
}
