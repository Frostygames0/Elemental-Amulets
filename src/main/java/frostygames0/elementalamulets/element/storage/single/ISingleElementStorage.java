package frostygames0.elementalamulets.element.storage.single;

import frostygames0.elementalamulets.element.Element;
import frostygames0.elementalamulets.element.storage.IElementStorage;
import frostygames0.elementalamulets.element.storage.OperationMode;
import net.minecraft.core.Holder;

import java.util.Set;

public interface ISingleElementStorage extends IElementStorage {
    int add(int amount, OperationMode operationMode);

    int take(int amount, OperationMode operationMode);

    Holder<Element> getStoredElement();

    int getAmount();

    @Override
    default int getElementAmount(Holder<Element> element) {
        if (!containsElement(element)) {
            return 0;
        }

        return getAmount();
    }

    @Override
    default boolean containsElement(Holder<Element> element) {
        return getStoredElement().equals(element);
    }

    @Override
    default Set<Holder<Element>> getAllStoredElementTypes() {
        return Set.of(getStoredElement());
    }

    @Override
    default boolean canAddElement(Holder<Element> element) {
        return containsElement(element);
    }

    @Override
    default boolean canTakeElement(Holder<Element> element) {
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
