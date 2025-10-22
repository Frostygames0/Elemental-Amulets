package frostygames0.elementalamulets.element.storage;

import frostygames0.elementalamulets.element.ElementType;
import frostygames0.elementalamulets.element.ElementalComposition;
import net.minecraft.core.Holder;

import java.util.Collection;
import java.util.Set;

public interface IElementStorage {
    ElementalComposition getStored();

    default int addElement(Holder<ElementType> element, int amount, Operation operation) {
        return addComposition(ElementalComposition.fromSingle(element, amount), operation).getAmount(element);
    }

    ElementalComposition addComposition(ElementalComposition composition, Operation operation);

    default int takeElement(Holder<ElementType> element, int amount, Operation operation) {
        return takeComposition(ElementalComposition.fromSingle(element, amount), operation).getAmount(element);
    }

    ElementalComposition takeComposition(ElementalComposition composition, Operation operation);

    boolean canAddElement(Holder<ElementType> element);

    boolean canAddComposition(ElementalComposition composition);

    boolean canTakeElement(Holder<ElementType> element);

    boolean canTakeComposition(ElementalComposition composition);

    Set<Holder<ElementType>> getAllStoredElementTypes();

    int getDistinctElementsAmount();

    int getMaxDistinctElementsStored();

    int getMaxCapacity();

    int getTotalAmount();

    int getElementAmount(Holder<ElementType> element);

    boolean containsElement(Holder<ElementType> element);

    boolean containsElements(Collection<Holder<ElementType>> elements);

    enum Operation {
        SIMULATE,
        PERFORM
    }
}
