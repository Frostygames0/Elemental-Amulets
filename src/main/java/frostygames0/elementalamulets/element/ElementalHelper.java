package frostygames0.elementalamulets.element;

import frostygames0.elementalamulets.element.storage.IElementStorage;
import frostygames0.elementalamulets.initialization.ModCapabilities;
import frostygames0.elementalamulets.initialization.ModDataComponents;
import frostygames0.elementalamulets.initialization.ModDataMaps;
import frostygames0.elementalamulets.initialization.ModItems;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public final class ElementalHelper {
    private ElementalHelper() {
    }

    public static ElementalComposition simplifyToPrimordials(Holder<ElementType> elementHolder) {
        return simplifyToPrimordialsMutable(elementHolder).toImmutable();
    }

    private static ElementalComposition.Mutable simplifyToPrimordialsMutable(Holder<ElementType> elementHolder) {
        var mutable = ElementalComposition.mutable();

        var element = elementHolder.value();

        if (element.isPrimordial()) {
            mutable.add(elementHolder, 1);
        } else {
            var composition = element.composition();

            for (var compositionElementHolder : composition) {
                var mutable2 = simplifyToPrimordialsMutable(compositionElementHolder);
                mutable.merge(mutable2);
            }
        }

        return mutable;
    }

    public static boolean hasElementalComposition(ItemStack stack) {
        return getStackElementalComposition(stack).isPresent();
    }

    public static Optional<ElementalComposition> getStackElementalComposition(ItemStack stack) {
        var dataMapComposition = stack.getItemHolder().getData(ModDataMaps.ELEMENTAL_COMPOSITION);
        var dataComponentComposition = stack.get(ModDataComponents.ELEMENTAL_COMPOSITION);

        if (dataMapComposition != null) {
            if (dataComponentComposition != null) {
                return Optional.of(dataMapComposition.merge(dataComponentComposition));
            }

            return Optional.of(dataMapComposition);
        }

        return dataComponentComposition != null && !dataComponentComposition.isEmpty() ? Optional.of(dataComponentComposition) : Optional.empty();
    }

    public static boolean isStackAnEmptyElementumShard(ItemStack stack) {
        return isStackAnElementumShard(stack) && stack.getOrDefault(ModDataComponents.ELEMENTAL_COMPOSITION, ElementalComposition.EMPTY).isEmpty();
    }

    public static boolean isStackAnElementumShard(ItemStack stack) {
        return stack.is(ModItems.ELEMENTUM_SHARD);
    }

    public static ItemStack createShardWithElement(Holder<ElementType> elementHolder) {
        var stack = new ItemStack(ModItems.ELEMENTUM_SHARD.get());
        stack.set(ModDataComponents.ELEMENTAL_COMPOSITION, ElementalComposition.fromSingle(elementHolder, 1));
        return stack;
    }

    public static boolean canSenseElements(Player player) {
        return player.isCreative() || player.getOffhandItem().is(ModItems.RING_OF_ELEMENTAL_SENSE);
    }

    public static boolean hasCompositionCycle(Holder<ElementType> element) {
        return hasCompositionCycle(element, new HashSet<>(), new HashSet<>());
    }

    private static boolean hasCompositionCycle(Holder<ElementType> current, Set<Holder<ElementType>> visited, Set<Holder<ElementType>> recursionStack) {
        visited.add(current);
        recursionStack.add(current);

        for (var element : current.value().composition()) {
            if (!visited.contains(element)) {
                if (hasCompositionCycle(element, visited, recursionStack)) {
                    return true;
                }
            } else if (recursionStack.contains(element)) {
                return true;
            }
        }

        recursionStack.remove(current);
        return false;
    }

    public static Optional<IElementStorage> getElementStorage(@Nullable Level level, @Nullable BlockPos blockPos, @Nullable Direction side) {
        if (level == null || blockPos == null) {
            return Optional.empty();
        }

        var cap = level.getCapability(ModCapabilities.ELEMENT_STORAGE_BLOCK, blockPos, side);
        return cap == null ? Optional.empty() : Optional.of(cap);
    }

    public static Optional<IElementStorage> getElementStorage(@Nullable BlockGetter level, @Nullable BlockPos blockPos, @Nullable Direction side) {
        if (level == null || blockPos == null) {
            return Optional.empty();
        }

        var blockEntity = level.getBlockEntity(blockPos);
        if (blockEntity == null || blockEntity.getLevel() == null) {
            return Optional.empty();
        }

        return getElementStorage(blockEntity.getLevel(), blockPos, side);
    }

    public static boolean hasElementStorage(@Nullable BlockGetter level, @Nullable BlockPos blockPos, @Nullable Direction side) {
        return getElementStorage(level, blockPos, side).isPresent();
    }

    @Contract()
    public static boolean hasElementStorage(@Nullable Level level, @Nullable BlockPos blockPos, @Nullable Direction side) {
        return getElementStorage(level, blockPos, side).isPresent();
    }

    public static MutableComponent createFancyElementComponent(Holder<ElementType> holderElement) {
        return Component.empty().append(holderElement.value().colorizedName()).withStyle(style -> style.withClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, String.format("/elementalamulets elements get %s", holderElement.getKey().location()))))
                .append(" ")
                .append(ComponentUtils.wrapInSquareBrackets(Component.literal(holderElement.getKey().location().toString()))
                        .withStyle(ChatFormatting.GRAY));
    }

    public static Component createFancyElementComponentForCommand(Holder<ElementType> element) {
        return createFancyElementComponent(element)
                .withStyle(style ->
                        style.withClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, String.format("/elementalamulets elements get %s", element.getKey().location())))
                                .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Component.translatable("command.elementalamulets.click_to_see_element"))));
    }
}
