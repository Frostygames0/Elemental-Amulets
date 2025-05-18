package frostygames0.elementalamulets.element.storage.single;

import frostygames0.elementalamulets.element.Element;
import frostygames0.elementalamulets.element.ElementalComposition;
import frostygames0.elementalamulets.element.storage.IElementStorageModifiable;
import frostygames0.elementalamulets.element.storage.OperationMode;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.neoforged.neoforge.common.util.INBTSerializable;

public class SingleElementStorage implements ISingleElementStorage, IElementStorageModifiable, INBTSerializable<CompoundTag> {
    private final int maxCapacity;

    private Holder<Element> element;
    private int amount;

    public SingleElementStorage(int maxCapacity) {
        this.maxCapacity = maxCapacity;
    }

    @Override
    public ElementalComposition getStored() {
        return ElementalComposition.fromSingle(element, amount);
    }

    @Override
    public void setStored(ElementalComposition composition) {
        if (composition.elementAmounts().size() > 1) {
            throw new IllegalArgumentException("Provided composition contains more than one element!");
        }

        var entry = composition.elementAmounts().entrySet().stream().findAny().orElse(null);
        element = entry == null ? null : entry.getKey();
        amount = entry == null ? 0 : entry.getValue();

        onChanged();
    }

    @Override
    public int add(int amount, OperationMode operationMode) {
        if (amount <= 0) {
            return 0;
        }

        int toAdd = Mth.clamp(maxCapacity - this.amount, 0, amount);
        if (operationMode == OperationMode.PERFORM) {
            this.amount += amount;
        }

        return toAdd;
    }

    @Override
    public int take(int amount, OperationMode operationMode) {
        if (amount <= 0) {
            return 0;
        }

        int taken = Math.min(this.amount, amount);

        if (operationMode == OperationMode.PERFORM) {
            this.amount -= taken;
            onChanged();
        }

        return taken;
    }

    @Override
    public Holder<Element> getStoredElement() {
        return element;
    }

    @Override
    public int getAmount() {
        return amount;
    }

    @Override
    public int addElement(Holder<Element> element, int amount, OperationMode operationMode) {
        if (!canAddElement(element)) {
            return 0;
        }

        return add(amount, operationMode);
    }

    @Override
    public int takeElement(Holder<Element> element, int amount, OperationMode operationMode) {
        if (!canTakeElement(element)) {
            return 0;
        }

        return take(amount, operationMode);
    }

    @Override
    public int getMaxCapacity() {
        return maxCapacity;
    }

    @Override
    public int getTotalAmount() {
        return amount;
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
