package frostygames0.elementalamulets.element.storage;

import frostygames0.elementalamulets.element.Element;
import frostygames0.elementalamulets.element.ElementalComposition;
import net.minecraft.core.Holder;

import java.util.Set;

public interface IElementStorage {

    ElementalComposition getStored();

    void setStored(ElementalComposition composition);

    int addElement(Holder<Element> element, int amount, boolean simulate);

    int takeElement(Holder<Element> element, int amount, boolean simulate);

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
