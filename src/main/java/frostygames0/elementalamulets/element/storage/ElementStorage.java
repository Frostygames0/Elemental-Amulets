package frostygames0.elementalamulets.element.storage;

import frostygames0.elementalamulets.element.Element;
import frostygames0.elementalamulets.element.ElementalComposition;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.util.Mth;
import net.neoforged.neoforge.common.util.INBTSerializable;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ElementStorage implements IElementStorage, INBTSerializable<CompoundTag> {
    private final int maxCapacity;
    private final int maxDistinctElements;

    private Map<Holder<Element>, Integer> storage = new HashMap<>();

    public ElementStorage(int maxCapacity, int maxDistinctElements) {
        this.maxCapacity = maxCapacity;
        this.maxDistinctElements = maxDistinctElements;
    }

    @Override
    public ElementalComposition getStored() {
        return new ElementalComposition(this.storage);
    }

    @Override
    public void setStored(ElementalComposition elementalComposition) {
        this.verifyStorage(elementalComposition);

        this.storage = new HashMap<>(elementalComposition.elementAmounts());

        this.onChanged();
    }

    private void verifyStorage(ElementalComposition composition) {
        if (composition.elementAmounts().size() > this.maxDistinctElements) {
            throw new IllegalStateException("Provided composition contains more distinct elements than this storage can contain!");
        }
        if (composition.getTotalAmount() > this.maxCapacity) {
            throw new IllegalStateException("Provided composition's size is bigger than max capacity of this storage!");
        }
    }

    @Override
    public int addElement(Holder<Element> element, int amount, boolean simulate) {
        if (amount <= 0) {
            return 0;
        }

        if (!canAddElement(element)) {
            return 0;
        }

        int elementAdded = Mth.clamp(this.maxCapacity - this.getTotalAmount(), 0, amount);
        if (!simulate) {
            //setStored(this.storage.merge(ElementalComposition.fromSingle(element, elementAdded)));

            if (this.storage.containsKey(element)) {
                this.storage.put(element, this.storage.get(element) + elementAdded); // TODO This is broken
            } else {
                this.storage.putIfAbsent(element, elementAdded);
            }

            this.onChanged();
        }

        return elementAdded;
    }

    @Override
    public int takeElement(Holder<Element> element, int amount, boolean simulate) {
        if (amount <= 0) {
            return 0;
        }

        if (!canTakeElement(element)) {
            return 0;
        }

        var currentAmount = getElementAmount(element);
        int elementTaken = Math.min(currentAmount, amount);

        if (!simulate && elementTaken != 0) {
            //this.setStored(this.storage.merge(ElementalComposition.fromSingle(element, -elementTaken)));

            if (currentAmount - elementTaken == 0) {
                this.storage.remove(element);
            } else {
                this.storage.put(element, currentAmount - elementTaken);
            }

            this.onChanged();
        }
        return elementTaken;
    }

    @Override
    public boolean canAddElement(Holder<Element> element) {
        if (this.storage.containsKey(element)) {
            return true;
        }

        return (this.getDistinctElementsAmount() + 1) <= this.maxDistinctElements;
    }

    @Override
    public boolean canTakeElement(Holder<Element> element) {
        return this.storage.containsKey(element);
    }

    @Override
    public Set<Holder<Element>> getAllStoredElementTypes() {
        return Set.copyOf(this.storage.keySet()); // TODO I don't think that creating a copy of all stored element types is a good idea, but it works for now
    }

    @Override
    public int getDistinctElementsAmount() {
        return this.storage.size();
    }

    @Override
    public int getMaxDistinctElementsStored() {
        return this.maxDistinctElements;
    }

    @Override
    public int getMaxCapacity() {
        return this.maxCapacity;
    }

    @Override
    public int getTotalAmount() {
        return this.storage.values().stream().reduce(0, Integer::sum);
    }

    @Override
    public int getElementAmount(Holder<Element> element) {
        return this.storage.getOrDefault(element, 0);
    }

    @Override
    public boolean containsElement(Holder<Element> element) {
        return this.storage.containsKey(element);
    }

    @Override
    public CompoundTag serializeNBT(HolderLookup.Provider provider) {
        if (!this.storage.isEmpty()) {
            return (CompoundTag) ElementalComposition.CODEC.encodeStart(
                    provider.createSerializationContext(NbtOps.INSTANCE), this.getStored()).getOrThrow();
        }
        return new CompoundTag();
    }

    @Override
    public void deserializeNBT(HolderLookup.Provider provider, CompoundTag nbt) {
        ElementalComposition.CODEC.parse(
                        provider.createSerializationContext(NbtOps.INSTANCE), nbt)
                .ifSuccess(this::setStored);
    }
}
