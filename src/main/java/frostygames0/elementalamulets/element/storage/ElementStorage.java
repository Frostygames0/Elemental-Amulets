package frostygames0.elementalamulets.element.storage;

import com.google.common.base.Preconditions;
import frostygames0.elementalamulets.element.ElementType;
import frostygames0.elementalamulets.element.ElementalComposition;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.ListTag;
import net.minecraft.util.Mth;
import net.neoforged.neoforge.common.util.INBTSerializable;

import java.util.Collection;
import java.util.Set;
import java.util.function.Consumer;

public class ElementStorage implements IElementStorage, IElementStorageModifiable, INBTSerializable<ListTag> {
    private final int maxCapacity;
    private final int maxDistinctElements;

    private ElementalComposition.Mutable storage = ElementalComposition.mutable();

    public ElementStorage(int maxCapacity) {
        this(maxCapacity, Integer.MAX_VALUE);
    }

    public ElementStorage(int maxCapacity, int maxDistinctElements) {
        this.maxCapacity = maxCapacity;
        this.maxDistinctElements = maxDistinctElements;
    }

    @Override
    public ElementalComposition getStored() {
        return storage.toImmutable();
    }

    @Override
    public void setStored(ElementalComposition elementalComposition) {
        Preconditions.checkArgument(elementalComposition.size() <= maxDistinctElements,
                "Provided composition contains more distinct elements than this storage can contain!");
        Preconditions.checkArgument(elementalComposition.getTotalAmount() <= maxCapacity,
                "Provided composition's size is bigger than max capacity of this storage!");

        storage = elementalComposition.toMutableCopy();
        onChanged();
    }

    private void updateStorageAndNotify(Consumer<ElementalComposition.Mutable> consumer) {
        consumer.accept(storage);
        onChanged();
    }

    protected void onChanged() {
    }

    private int clampAddAmount(Holder<ElementType> element, int amount) {
        return Mth.clamp(maxCapacity - getTotalAmount(), 0, amount);
    }

    @Override
    public int addElement(Holder<ElementType> element, int amount, Operation operation) {
        if (amount <= 0) {
            return 0;
        }

        if (!canAddElement(element)) {
            return 0;
        }

        int elementAdded = clampAddAmount(element, amount);

        if (operation == Operation.PERFORM) {
            updateStorageAndNotify(storage -> storage.add(element, elementAdded));
        }

        return elementAdded;
    }

    @Override
    public ElementalComposition addComposition(ElementalComposition composition, Operation operation) {
        if (!canAddComposition(composition)) {
            return ElementalComposition.EMPTY;
        }

        var toAdd = composition.toImmutable().applyToAmounts(this::clampAddAmount);

        if (toAdd.isEmpty()) {
            return ElementalComposition.EMPTY;
        }

        if (operation == Operation.PERFORM) {
            updateStorageAndNotify(storage -> storage.merge(toAdd));
        }

        return toAdd;
    }

    private int clampTakeAmount(Holder<ElementType> element, int amount) {
        var currentAmount = getElementAmount(element);
        return Math.min(currentAmount, amount);
    }

    @Override
    public int takeElement(Holder<ElementType> element, int amount, Operation operation) {
        if (amount <= 0) {
            return 0;
        }

        if (!canTakeElement(element)) {
            return 0;
        }

        int elementTaken = clampTakeAmount(element, amount);
        if (elementTaken == 0) {
            return 0;
        }

        if (operation == Operation.PERFORM) {
            updateStorageAndNotify(storage -> storage.reduce(element, elementTaken));
        }

        return elementTaken;
    }

    @Override
    public ElementalComposition takeComposition(ElementalComposition composition, Operation operation) {
        if (!canTakeComposition(composition)) {
            return ElementalComposition.EMPTY;
        }

        var toSubtract = composition.toImmutable().applyToAmounts(this::clampTakeAmount);

        if (toSubtract.isEmpty()) {
            return ElementalComposition.EMPTY;
        }

        if (operation == Operation.PERFORM) {
            updateStorageAndNotify(storage -> storage.subtract(toSubtract));
        }

        return toSubtract;
    }

    @Override
    public boolean canAddElement(Holder<ElementType> element) {
        return storage.contains(element) || ((getDistinctElementsAmount() + 1) <= maxDistinctElements);
    }

    @Override
    public boolean canAddComposition(ElementalComposition composition) {
        return composition.toImmutable().merge(storage).size() <= maxDistinctElements;
    }

    @Override
    public boolean canTakeElement(Holder<ElementType> element) {
        return storage.contains(element);
    }

    // TODO This all logic with canTake canAdd is honestly pretty flawed
    @Override
    public boolean canTakeComposition(ElementalComposition composition) {
        return storage.containsElements(composition.getElements());
    }

    @Override
    public Set<Holder<ElementType>> getAllStoredElementTypes() {
        return storage.getElements();
    }

    @Override
    public int getDistinctElementsAmount() {
        return storage.size();
    }

    @Override
    public int getMaxDistinctElementsStored() {
        return maxDistinctElements;
    }

    @Override
    public int getMaxCapacity() {
        return maxCapacity;
    }

    @Override
    public int getTotalAmount() {
        return storage.getTotalAmount();
    }

    @Override
    public int getElementAmount(Holder<ElementType> element) {
        return storage.getAmount(element);
    }

    @Override
    public boolean containsElement(Holder<ElementType> element) {
        return storage.contains(element);
    }

    @Override
    public boolean containsElements(Collection<Holder<ElementType>> elements) {
        return storage.containsElements(elements);
    }

    @Override
    public ListTag serializeNBT(HolderLookup.Provider provider) {
        return ElementalComposition.serializeToNbt(provider, getStored()).orElse(new ListTag());
    }

    @Override
    public void deserializeNBT(HolderLookup.Provider provider, ListTag nbt) {
        ElementalComposition.deserializeFromNbt(provider, nbt)
                .ifPresent(this::setStored);
    }
}
