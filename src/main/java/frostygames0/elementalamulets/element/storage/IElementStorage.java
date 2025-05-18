package frostygames0.elementalamulets.element.storage;

import frostygames0.elementalamulets.element.Element;
import frostygames0.elementalamulets.element.ElementalComposition;
import net.minecraft.core.Holder;

import java.util.Set;

public interface IElementStorage {

    ElementalComposition getStored();

    int addElement(Holder<Element> element, int amount, OperationMode operationMode);

    int takeElement(Holder<Element> element, int amount, OperationMode operationMode);

    boolean canAddElement(Holder<Element> element);

    boolean canTakeElement(Holder<Element> element);

    Set<Holder<Element>> getAllStoredElementTypes();

    int getDistinctElementsAmount();

    int getMaxDistinctElementsStored();

    int getMaxCapacity();

    int getTotalAmount();

    int getElementAmount(Holder<Element> element);

    boolean containsElement(Holder<Element> element);

    default void onChanged() {
    }

}
