package frostygames0.elementalamulets.element.storage.single;

import frostygames0.elementalamulets.element.ElementType;
import frostygames0.elementalamulets.element.ElementalComposition;
import frostygames0.elementalamulets.element.storage.ElementStorage;
import net.minecraft.core.Holder;
import net.minecraft.util.Mth;

@Deprecated
public class SingleElementStorage extends ElementStorage implements ISingleElementStorage {
    private Holder<ElementType> element;
    private int amount;

    public SingleElementStorage(int maxCapacity) {
        super(maxCapacity, 1);
    }

    @Override
    public void setStored(ElementalComposition composition) {
        if (composition.size() > 1) {
            throw new IllegalArgumentException("Provided composition contains more than one element!");
        }

        var entry = composition.getEntries().getFirst();
        element = entry == null ? null : entry.getKey();
        amount = entry == null ? 0 : entry.getValue();

        onChanged();
    }

    @Override
    public int add(int amount, Operation operation) {
        if (amount <= 0) {
            return 0;
        }

        int toAdd = Mth.clamp(getMaxCapacity() - this.amount, 0, amount);
        if (operation == Operation.PERFORM) {
            this.amount += amount;
        }

        return toAdd;
    }

    @Override
    public int take(int amount, Operation operation) {
        if (amount <= 0) {
            return 0;
        }

        int taken = Math.min(this.amount, amount);

        if (operation == Operation.PERFORM) {
            this.amount -= taken;
            onChanged();
        }

        return taken;
    }

    @Override
    public Holder<ElementType> getStoredElement() {
        return element;
    }

    @Override
    public int getAmount() {
        return amount;
    }

    @Override
    public int addElement(Holder<ElementType> element, int amount, Operation operation) {
        if (!canAddElement(element)) {
            return 0;
        }

        return add(amount, operation);
    }

    @Override
    public int takeElement(Holder<ElementType> element, int amount, Operation operation) {
        if (!canTakeElement(element)) {
            return 0;
        }

        return take(amount, operation);
    }

    @Override
    public int getTotalAmount() {
        return amount;
    }
}
