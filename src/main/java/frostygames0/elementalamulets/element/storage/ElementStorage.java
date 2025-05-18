package frostygames0.elementalamulets.element.storage;

import com.google.common.collect.ImmutableSet;
import frostygames0.elementalamulets.element.Element;
import frostygames0.elementalamulets.element.ElementalComposition;
import it.unimi.dsi.fastutil.objects.Object2IntArrayMap;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.neoforged.neoforge.common.util.INBTSerializable;

import java.util.Set;
import java.util.function.Consumer;

public class ElementStorage implements IElementStorage, IElementStorageModifiable, INBTSerializable<CompoundTag> {
    private final int maxCapacity;
    private final int maxDistinctElements;

    private final Object2IntArrayMap<Holder<Element>> storage = new Object2IntArrayMap<>();

    public ElementStorage(int maxCapacity, int maxDistinctElements) {
        this.maxCapacity = maxCapacity;
        this.maxDistinctElements = maxDistinctElements;
    }

    @Override
    public ElementalComposition getStored() {
        return new ElementalComposition(storage);
    }

    @Override
    public void setStored(ElementalComposition elementalComposition) {
        verifyStorage(elementalComposition);

        update(storage -> {
            storage.clear();
            storage.putAll(elementalComposition.elementAmounts());
        });
    }

    private void update(Consumer<Object2IntArrayMap<Holder<Element>>> storage) {
        storage.accept(this.storage);
        onChanged();
    }

    private void verifyStorage(ElementalComposition composition) {
        if (composition.elementAmounts().size() > maxDistinctElements) {
            throw new IllegalStateException("Provided composition contains more distinct elements than this storage can contain!");
        }
        if (composition.getTotalAmount() > maxCapacity) {
            throw new IllegalStateException("Provided composition's size is bigger than max capacity of this storage!");
        }
    }

    @Override
    public int addElement(Holder<Element> element, int amount, OperationMode operationMode) {
        if (amount <= 0) {
            return 0;
        }

        if (!canAddElement(element)) {
            return 0;
        }

        int elementAdded = Mth.clamp(maxCapacity - getTotalAmount(), 0, amount);
        if (operationMode == OperationMode.PERFORM) {
            //setStored(this.storage.merge(ElementalComposition.fromSingle(element, elementAdded)));

            if (storage.containsKey(element)) {
                update(storage -> storage.put(element, storage.getInt(element) + elementAdded)); // TODO This is broken
            } else {
                update(storage -> storage.putIfAbsent(element, elementAdded));
            }
        }

        return elementAdded;
    }

    @Override
    public int takeElement(Holder<Element> element, int amount, OperationMode operationMode) {
        if (amount <= 0) {
            return 0;
        }

        if (!canTakeElement(element)) {
            return 0;
        }

        var currentAmount = getElementAmount(element);
        int elementTaken = Math.min(currentAmount, amount);

        if (operationMode == OperationMode.PERFORM && elementTaken != 0) {
            if (currentAmount - elementTaken == 0) {
                update(storage -> storage.removeInt(element));
            } else {
                update(storage -> storage.put(element, currentAmount - elementTaken));
            }
        }
        return elementTaken;
    }

    @Override
    public boolean canAddElement(Holder<Element> element) {
        if (storage.containsKey(element)) {
            return true;
        }

        return (getDistinctElementsAmount() + 1) <= maxDistinctElements;
    }

    @Override
    public boolean canTakeElement(Holder<Element> element) {
        return storage.containsKey(element);
    }

    @Override
    public Set<Holder<Element>> getAllStoredElementTypes() {
        return ImmutableSet.copyOf(storage.keySet()); // To avoid concurrent modification error
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
        return storage.values().intStream().reduce(0, Integer::sum);
    }

    @Override
    public int getElementAmount(Holder<Element> element) {
        return storage.getOrDefault(element, 0);
    }

    @Override
    public boolean containsElement(Holder<Element> element) {
        return storage.containsKey(element);
    }

    @Override
    public CompoundTag serializeNBT(HolderLookup.Provider provider) {
        return ElementalComposition.toNbtTag(provider, getStored()).orElse(new CompoundTag());
    }

    @Override
    public void deserializeNBT(HolderLookup.Provider provider, CompoundTag nbt) {
        ElementalComposition.fromNbtTag(provider, nbt)
                .ifPresent(this::setStored);
    }
}
