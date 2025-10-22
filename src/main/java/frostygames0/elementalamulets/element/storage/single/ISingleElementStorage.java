package frostygames0.elementalamulets.element.storage.single;

import frostygames0.elementalamulets.element.ElementType;
import frostygames0.elementalamulets.element.storage.IElementStorage;
import net.minecraft.core.Holder;

import java.util.Set;

@Deprecated
public interface ISingleElementStorage extends IElementStorage {
    int add(int amount, Operation operation);

    int take(int amount, Operation operation);

    Holder<ElementType> getStoredElement();

    int getAmount();

    @Override
    default int getElementAmount(Holder<ElementType> element) {
        if (!containsElement(element)) {
            return 0;
        }

        return getAmount();
    }

    @Override
    default boolean containsElement(Holder<ElementType> element) {
        return getStoredElement().equals(element);
    }

    @Override
    default Set<Holder<ElementType>> getAllStoredElementTypes() {
        return Set.of(getStoredElement());
    }

    @Override
    default boolean canAddElement(Holder<ElementType> element) {
        return containsElement(element);
    }

    @Override
    default boolean canTakeElement(Holder<ElementType> element) {
        return containsElement(element);
    }

    @Override
    default int getDistinctElementsAmount() {
        return 1;
    }

    @Override
    default int getMaxDistinctElementsStored() {
        return 1;
    }
}
