package frostygames0.elementalamulets.item;

import frostygames0.elementalamulets.element.ElementalHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public final class ElementumShardItem extends Item {
    public ElementumShardItem(Properties properties) {
        super(properties);
    }

    @Override
    public Component getName(ItemStack stack) {
        var compositionOptional = ElementalHelper.getStackElementalComposition(stack);

        var suffix = Component.empty();

        if (compositionOptional.isPresent()) {
            var composition = compositionOptional.get();
            if (!composition.isEmpty()) {
                var inside = composition.size() > 1
                        ? Component.translatable("generic.elementalamulets.mixed")
                        : composition.getElements().getFirst().value().colorizedName();

                suffix.append(Component.translatable("generic.elementalamulets.round_brackets", inside));
            }
        }

        return super.getName(stack).copy().append(" ").append(suffix);
    }
}
