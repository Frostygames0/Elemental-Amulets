package frostygames0.elementalamulets.inventory.menu.extractor;

import com.google.common.collect.ImmutableList;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.SlotItemHandler;

import java.util.List;
import java.util.Optional;

public class ElementalExtractorSlotDefinitions {
    private final SlotDefinition inputSlot;
    private final SlotDefinition fuelSlot;

    private final List<SlotDefinition> additionalSlots;

    private final AdditionalQuickMoveHandler additionalQuickMove;

    private ElementalExtractorSlotDefinitions(SlotDefinition inputSlot, SlotDefinition fuelSlot,
                                              List<SlotDefinition> additionalSlots, AdditionalQuickMoveHandler additionalQuickMove) {
        this.inputSlot = inputSlot;
        this.fuelSlot = fuelSlot;
        this.additionalSlots = additionalSlots;
        this.additionalQuickMove = additionalQuickMove;
    }

    public SlotDefinition getInputSlot() {
        return this.inputSlot;
    }

    public SlotDefinition getFuelSlot() {
        return this.fuelSlot;
    }

    public List<SlotDefinition> getAdditionalSlots() {
        return this.additionalSlots;
    }

    public int getFirstAdditionalSlotIndex() {
        return 2;
    }

    public int getLastSlotIndex() {
        return 2 + (additionalSlots.size() - 1); // input + fuel + last index of additional slot
    }

    public AdditionalQuickMoveHandler getAdditionalQuickMoveHandler() {
        return this.additionalQuickMove;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private SlotDefinition inputSlot;
        private SlotDefinition fuelSlot;

        private final ImmutableList.Builder<SlotDefinition> additionalSlots = ImmutableList.builder();

        private AdditionalQuickMoveHandler additionalQuickMove;

        public Builder addInputSlot(int slot, int x, int y) {
            this.inputSlot = new SlotDefinition(slot, x, y, Optional.empty());
            return this;
        }

        public Builder addFuelSlot(int slot, int x, int y) {
            this.fuelSlot = new SlotDefinition(slot, x, y, Optional.empty());
            return this;
        }

        public Builder addAdditionalSlot(int slot, int x, int y, SlotFactory slotFactory) {
            additionalSlots.add(new SlotDefinition(slot, x, y, Optional.of(slotFactory)));
            return this;
        }

        public Builder addAdditionalSlotsQuickMoveHandler(AdditionalQuickMoveHandler quickMove) {
            this.additionalQuickMove = quickMove;
            return this;
        }

        public ElementalExtractorSlotDefinitions build() {
            if (this.inputSlot == null) {
                throw new NullPointerException("Input slot must be added!");
            }

            if (this.fuelSlot == null) {
                throw new NullPointerException("Fuel slot must be added!");
            }

            var additionalSlotsBuilt = this.additionalSlots.build();
            if (this.additionalQuickMove != null && additionalSlotsBuilt.isEmpty()) {
                throw new IllegalStateException("There is no need to add additional quick move if there are no additional slots");
            }

            return new ElementalExtractorSlotDefinitions(this.inputSlot, this.fuelSlot, this.additionalSlots.build(), this.additionalQuickMove);
        }
    }

    @FunctionalInterface
    public interface SlotFactory {
        SlotItemHandler create(int slot, int x, int y);
    }

    @FunctionalInterface
    public interface AdditionalQuickMoveHandler {
        ItemStack handle(AbstractElementalExtractorMenu menu, Player player, int slot);
    }

    public record SlotDefinition(int slot, int x, int y, Optional<SlotFactory> factory) {
    }
}
