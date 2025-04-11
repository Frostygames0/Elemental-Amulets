package frostygames0.elementalamulets.block.entity.extractor;

import frostygames0.elementalamulets.element.ElementHelper;
import frostygames0.elementalamulets.inventory.ExtractOnlyRangedWrapper;
import frostygames0.elementalamulets.inventory.InsertOnlyRangedWrapper;
import frostygames0.elementalamulets.inventory.menu.extractor.PrimitiveElementalExtractorMenu;
import frostygames0.elementalamulets.inventory.provider.BlockFace;
import frostygames0.elementalamulets.inventory.provider.DynamicItemHandlerProvider;
import frostygames0.elementalamulets.registration.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Containers;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.items.wrapper.CombinedInvWrapper;
import org.jetbrains.annotations.Nullable;

public class PrimitiveElementalExtractorBlockEntity extends AbstractElementalExtractorBlockEntity {
    public static final String TAG_PRIMITIVE_ADDITIONAL_INVENTORY = "primitiveAdditionalInventory";

    public static final int EMPTY_SHARD_SLOT = 0; // 2
    public static final int RESULTS_SLOTS_START = 1;
    public static final int RESULTS_SLOTS_END = 6;

    public static final int ELEMENT_STORAGE_CAPACITY = 50;
    public static final int MAX_DISTINCT_ELEMENTS_STORED = 3;

    public static final int ADDITIONAL_INVENTORY_SIZE = 7;

    private final ItemStackHandler primitiveAdditionalInventory = new ItemStackHandler(ADDITIONAL_INVENTORY_SIZE) {
        @Override
        protected void onContentsChanged(int slot) {
            super.onContentsChanged(slot);
            setChanged();
        }

        @Override
        public boolean isItemValid(int slot, ItemStack stack) {
            if (slot >= RESULTS_SLOTS_START && slot <= RESULTS_SLOTS_END) {
                return false;
            } else if (slot == EMPTY_SHARD_SLOT) {
                return ElementHelper.isStackAnEmptyElementalShard(stack);
            }

            return super.isItemValid(slot, stack);
        }
    };

    private final IItemHandler shardInsertOnlyWrapper = new InsertOnlyRangedWrapper(this.primitiveAdditionalInventory, EMPTY_SHARD_SLOT, EMPTY_SHARD_SLOT + 1);
    private final IItemHandler resultsExtractOnlyWrapper = new ExtractOnlyRangedWrapper(this.primitiveAdditionalInventory, RESULTS_SLOTS_START, RESULTS_SLOTS_END + 1);
    private final IItemHandler mergedItemHandler = new CombinedInvWrapper(this.baseInventory, this.primitiveAdditionalInventory);

    private final DynamicItemHandlerProvider dynamicItemHandlerProvider = DynamicItemHandlerProvider.builder()
            .addItemHandlerForFace(BlockFace.FRONT, this.fuelInsertOnlyWrapper) // From Base class
            .addItemHandlerForFace(BlockFace.BACK, this.fuelInsertOnlyWrapper)
            .addItemHandlerForFace(BlockFace.LEFT, this.shardInsertOnlyWrapper) // From Base class too
            .addItemHandlerForFace(BlockFace.RIGHT, this.shardInsertOnlyWrapper)
            .addItemHandlerForFace(BlockFace.BOTTOM, this.resultsExtractOnlyWrapper)
            .addItemHandlerForFace(BlockFace.ANY, this.mergedItemHandler)
            .build(BlockStateProperties.FACING);

    public PrimitiveElementalExtractorBlockEntity(BlockPos pos, BlockState blockState) {
        super(ModBlockEntities.PRIMITIVE_ELEMENTAL_EXTRACTOR.get(), pos, blockState, ELEMENT_STORAGE_CAPACITY, MAX_DISTINCT_ELEMENTS_STORED);
    }

    @Override
    public IItemHandler getItemHandlerForDirection(@Nullable Direction direction) {
        return this.dynamicItemHandlerProvider.getItemHandlerForDirection(direction);
    }

    @Override
    public void serverTick() {
        super.serverTick();
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);

        tag.put(TAG_PRIMITIVE_ADDITIONAL_INVENTORY, this.primitiveAdditionalInventory.serializeNBT(registries));
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);

        this.primitiveAdditionalInventory.deserializeNBT(registries, tag.getCompound(TAG_PRIMITIVE_ADDITIONAL_INVENTORY));
    }

    @Override
    public void dropContents() {
        var blockPos = this.worldPosition;

        for (int i = 0; i < this.baseInventory.getSlots(); i++) {
            Containers.dropItemStack(this.level, blockPos.getX(), blockPos.getY(), blockPos.getZ(), this.baseInventory.getStackInSlot(i));
        }

        for (int i = 0; i < this.primitiveAdditionalInventory.getSlots(); i++) {
            Containers.dropItemStack(this.level, blockPos.getX(), blockPos.getY(), blockPos.getZ(), this.primitiveAdditionalInventory.getStackInSlot(i));
        }
    }

    @Override
    public Component getDisplayName() {
        return Component.literal("Primitive Elemental Extractor");
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player) {
        return new PrimitiveElementalExtractorMenu(containerId, playerInventory,
                this.baseInventory, this.primitiveAdditionalInventory,
                this.elementStorage, ContainerLevelAccess.create(this.level, this.worldPosition));
    }
}
