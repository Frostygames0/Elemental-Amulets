package frostygames0.elementalamulets.inventory.menu.extractor;

import com.google.common.collect.ImmutableList;
import frostygames0.elementalamulets.block.entity.extractor.AbstractElementalExtractorBlockEntity;
import frostygames0.elementalamulets.element.storage.IElementStorage;
import frostygames0.elementalamulets.inventory.menu.SyncedElementalStorageMenu;
import frostygames0.elementalamulets.inventory.menu.slots.ElementalExtractorFuelSlot;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.SlotItemHandler;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Predicate;

public abstract class AbstractElementalExtractorMenu extends SyncedElementalStorageMenu {
    protected static final int INPUT_SLOT = AbstractElementalExtractorBlockEntity.INPUT_SLOT;
    protected static final int FUEL_SLOT = AbstractElementalExtractorBlockEntity.FUEL_SLOT;

    protected static final int BASE_INVENTORY_SIZE = AbstractElementalExtractorBlockEntity.BASE_INVENTORY_SIZE;
    protected static final int LAST_SLOT_INDEX = BASE_INVENTORY_SIZE - 1;

    protected final IItemHandler baseInventory;

    protected final ContainerLevelAccess access;
    protected final Level level;

    protected AdditionalSlots additionalSlots;

    protected AbstractElementalExtractorMenu(MenuType<?> menuType, int containerId,
                                             Inventory playerInventory, IItemHandler baseInventory,
                                             IElementStorage storage, ContainerLevelAccess access,
                                             @Nullable AdditionalSlots additionalSlots) {
        super(menuType, containerId, playerInventory.player, storage);

        this.baseInventory = baseInventory;
        this.access = access;
        this.level = this.player.level();

        this.addSlot(new SlotItemHandler(this.baseInventory, INPUT_SLOT, 44, 11));
        this.addSlot(new ElementalExtractorFuelSlot(this.baseInventory, FUEL_SLOT, 44, 47, this.level));

        this.additionalSlots = additionalSlots;
        this.addAdditionalSlots(this.additionalSlots);

        this.addStandardInventorySlots(playerInventory, 8, 84);
    }

    private void addAdditionalSlots(AdditionalSlots additionalSlots) {
        if (additionalSlots == null) {
            return;
        }

        for (var slot : additionalSlots.getSlots()) {
            this.addSlot(slot);
        }
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        return null;
    }

    public static class AdditionalSlots {
        private final ImmutableList<SlotItemHandler> slots;

        private AdditionalSlots(ImmutableList<SlotItemHandler> slots) {
            this.slots = slots;
        }

        public int getLastSlotIndex() {
            return slots.size();
        }

        public List<SlotItemHandler> getSlots() {
            return this.slots;
        }

        public ItemStack quickMoveStack(Player player, int slot) {
            return null;
        }

        public static AdditionalSlots.Builder builder() {
            return new AdditionalSlots.Builder();
        }

        public static class Builder {
            private final ImmutableList.Builder<SlotItemHandler> slots = new ImmutableList.Builder<>();
            private BiFunction<Player, Integer, ItemStack> quickMove;

            public Builder addInputSlot(int slot, int x, int y) {
                return this;
            }

            public Builder addFuelSlot(int slot, int x, int y) {
                return this;
            }

            public Builder addAdditionalSlot(IItemHandler inventory, int slot, int x, int y, int maxStackSize, Predicate<ItemStack> mayPlace) {
                this.slots.add(new SlotItemHandler(null, 1, 1, 1));
                return this;
            }

            public Builder addAdditionalOnQuickMoveBehaviour(BiFunction<Player, Integer, ItemStack> quickMove) {
                this.quickMove = quickMove;
                return this;
            }

            public AdditionalSlots build() {
                return new AdditionalSlots(slots.build());
            }
        }

        @FunctionalInterface
        public interface SlotFactory {
            SlotItemHandler factory(int slotIndex, int x, int y);
        }
    }
}
