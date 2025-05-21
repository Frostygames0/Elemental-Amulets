package frostygames0.elementalamulets.element;

import frostygames0.elementalamulets.initialization.ModDataComponents;
import frostygames0.elementalamulets.initialization.ModDataMaps;
import frostygames0.elementalamulets.initialization.ModItems;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public final class ElementHelper {
    private ElementHelper() {
    }

    public static ElementalComposition simplifyToPrimordials(Holder<Element> elementHolder) {
        var map = new HashMap<Holder<Element>, Integer>();

        var element = elementHolder.value();

        if (element.isPrimordial()) {
            map.putIfAbsent(elementHolder, 0);
            map.put(elementHolder, map.get(elementHolder) + 1);
        } else {
            var composition = element.composition();

            for (var compositionElementHolder : composition) {
                var map2 = simplifyToPrimordials(compositionElementHolder).elementAmounts();

                for (var entry : map2.entrySet()) {
                    var holder = entry.getKey();
                    var amount = entry.getValue();

                    map.putIfAbsent(holder, 0);
                    map.put(holder, map.get(holder) + amount);
                }
            }
        }

        return new ElementalComposition(map);
    }

    public static boolean hasElementalComposition(ItemStack stack) {
        return getStackElementalComposition(stack).isPresent();
    }

    public static Optional<ElementalComposition> getStackElementalComposition(ItemStack stack) {
        var dataMap = stack.getItemHolder().getData(ModDataMaps.ELEMENTAL_COMPOSITION);
        var dataComponent = stack.get(ModDataComponents.ELEMENTAL_COMPOSITION);

        if (dataMap != null) {
            if (dataComponent != null) {
                return Optional.of(dataMap.merge(dataComponent));
            }

            return Optional.of(dataMap);
        }

        return dataComponent != null && !dataComponent.isEmpty() ? Optional.of(dataComponent) : Optional.empty();
    }

    public static boolean isStackAnEmptyElementumShard(ItemStack stack) {
        return isStackAnElementumShard(stack) && stack.getOrDefault(ModDataComponents.ELEMENTAL_COMPOSITION, ElementalComposition.EMPTY).isEmpty();
    }

    public static boolean isStackAnElementumShard(ItemStack stack) {
        return stack.is(ModItems.ELEMENTUM_SHARD);
    }

    public static ItemStack createShardWithElement(Holder<Element> elementHolder) {
        var stack = new ItemStack(ModItems.ELEMENTUM_SHARD.get());
        stack.set(ModDataComponents.ELEMENTAL_COMPOSITION, ElementalComposition.fromSingle(elementHolder, 1));
        return stack;
    }

    public static boolean canSenseElements(Player player) {
        return player.isCreative() || player.getOffhandItem().is(ModItems.RING_OF_ELEMENTAL_SENSE);
    }

    public static boolean hasCompositionCycle(Holder<Element> element) {
        return hasCompositionCycle(element, new HashSet<>(), new HashSet<>());
    }

    private static boolean hasCompositionCycle(Holder<Element> current, Set<Holder<Element>> visited, Set<Holder<Element>> recursionStack) {
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

    public static Optional<Holder<Element>> deserializeFromNbt(Tag tag, HolderLookup.Provider provider) {
        return Element.CODEC.parse(provider.createSerializationContext(NbtOps.INSTANCE), tag).resultOrPartial();
    }

    public static Optional<Tag> serializeToNbt(Holder<Element> element, HolderLookup.Provider provider) {
        return Element.CODEC.encodeStart(provider.createSerializationContext(NbtOps.INSTANCE), element).resultOrPartial();
    }
}
