package frostygames0.elementalamulets.element.storage;

import frostygames0.elementalamulets.element.Element;
import frostygames0.elementalamulets.element.ElementalComposition;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.util.Mth;
import net.neoforged.neoforge.common.util.INBTSerializable;

public class ElementStorage implements IElementStorage, INBTSerializable<CompoundTag> {
    private final int maxCapacity;
    private final int maxDistinctElements;

    private ElementalComposition storage = ElementalComposition.EMPTY;
    private int totalAmount = 0;
    private int currentDistinctElements = 0;

    public ElementStorage(int maxCapacity, int maxDistinctElements) {
        this.maxCapacity = maxCapacity;
        this.maxDistinctElements = maxDistinctElements;
    }

    public ElementStorage(int maxCapacity) {
        this(maxCapacity, -1);
    }

    @Override
    public ElementalComposition getStored() {
        return this.storage;
    }

    @Override
    public void setStored(ElementalComposition composition) {
        this.verifyStorageBoundaries(composition);

        this.storage = composition;
        this.totalAmount = this.storage.getTotalAmount();
        this.currentDistinctElements = this.storage.elementAmounts().size();

        onChanged();
    }

    private void verifyStorageBoundaries(ElementalComposition composition) {
        if (composition.getTotalAmount() > this.maxCapacity) {
            throw new IndexOutOfBoundsException("Provided composition's size is bigger than max capacity of this storage!");
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

        int elementAdded = Mth.clamp(this.maxCapacity - this.totalAmount, 0, amount);
        if (!simulate && elementAdded != 0) {
            setStored(this.storage.merge(ElementalComposition.fromSingle(element, elementAdded)));
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

        int elementTaken = Math.min(getElementAmount(element), amount);
        if (!simulate && elementTaken != 0) {
            this.setStored(this.storage.merge(ElementalComposition.fromSingle(element, -elementTaken)));
        }
        return elementTaken;
    }

    @Override
    public boolean canAddElement(Holder<Element> element) {
        if (this.getStored().elementAmounts().containsKey(element)) {
            return true;
        }

        return (this.currentDistinctElements + 1) <= this.maxDistinctElements;
    }

    @Override
    public boolean canTakeElement(Holder<Element> element) {
        return this.getStored().elementAmounts().containsKey(element);
    }

    @Override
    public int getDistinctElementsAmount() {
        return this.currentDistinctElements;
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
        return this.totalAmount;
    }

    @Override
    public int getElementAmount(Holder<Element> element) {
        return this.storage.elementAmounts().getOrDefault(element, 0);
    }

    @Override
    public boolean containsElement(Holder<Element> element) {
        return this.storage.elementAmounts().containsKey(element);
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
