package frostygames0.elementalamulets.inventory.menu.extractor;

import com.google.common.collect.ImmutableList;
import net.neoforged.neoforge.items.SlotItemHandler;

import java.util.List;
import java.util.Optional;

public class ElementalExtractorSlotDefinitions {
    private final SlotDefinition inputSlot;
    private final SlotDefinition fuelSlot;

    private final List<SlotDefinition> additionalSlots;

    private ElementalExtractorSlotDefinitions(SlotDefinition inputSlot, SlotDefinition fuelSlot, List<SlotDefinition> additionalSlots) {
        this.inputSlot = inputSlot;
        this.fuelSlot = fuelSlot;
        this.additionalSlots = additionalSlots;
    }

    public SlotDefinition getInputSlot() {
        return inputSlot;
    }

    public SlotDefinition getFuelSlot() {
        return fuelSlot;
    }

    public List<SlotDefinition> getAdditionalSlots() {
        return additionalSlots;
    }

    public int getLastSlotIndex() {
        return 2 + (additionalSlots.size() - 1); // input + fuel + last index of additional slot
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private SlotDefinition inputSlot;
        private SlotDefinition fuelSlot;

        private final ImmutableList.Builder<SlotDefinition> additionalSlots = ImmutableList.builder();

        public Builder addInputSlot(int slot, int x, int y) {
            inputSlot = new SlotDefinition(slot, x, y, Optional.empty());
            return this;
        }

        public Builder addFuelSlot(int slot, int x, int y) {
            fuelSlot = new SlotDefinition(slot, x, y, Optional.empty());
            return this;
        }

        public Builder addAdditionalSlot(int slot, int x, int y, SlotFactory slotFactory) {
            additionalSlots.add(new SlotDefinition(slot, x, y, Optional.of(slotFactory)));
            return this;
        }

        public ElementalExtractorSlotDefinitions build() {
            if (inputSlot == null) {
                throw new NullPointerException("Input slot must be added!");
            }

            if (fuelSlot == null) {
                throw new NullPointerException("Fuel slot must be added!");
            }

            return new ElementalExtractorSlotDefinitions(inputSlot, fuelSlot, additionalSlots.build());
        }
    }

    @FunctionalInterface
    public interface SlotFactory {
        SlotItemHandler create(int slot, int x, int y);
    }

    public record SlotDefinition(int slot, int x, int y, Optional<SlotFactory> factory) {
    }
}
