package frostygames0.elementalamulets.block.entity.extractor;

import frostygames0.elementalamulets.block.extractor.AbstractElementalExtractorBlock;
import frostygames0.elementalamulets.element.Element;
import frostygames0.elementalamulets.element.ElementHelper;
import frostygames0.elementalamulets.inventory.ExtractOnlyRangedWrapper;
import frostygames0.elementalamulets.inventory.InsertOnlyRangedWrapper;
import frostygames0.elementalamulets.inventory.ItemHandlerHelper;
import frostygames0.elementalamulets.inventory.menu.extractor.PrimitiveElementalExtractorMenu;
import frostygames0.elementalamulets.inventory.provider.BlockFace;
import frostygames0.elementalamulets.inventory.provider.DynamicItemHandlerProvider;
import frostygames0.elementalamulets.registration.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.items.wrapper.CombinedInvWrapper;
import org.jetbrains.annotations.Nullable;

public class PrimitiveElementalExtractorBlockEntity extends AbstractElementalExtractorBlockEntity {
    public static final String TAG_ADDITIONAL_INVENTORY = "additionalInventory";
    //public static final String TAG_TOTAL_CONVERSION_TIME = "totalConversionTime";
    public static final String TAG_CONVERSION_TIMER = "conversionTimer";

    public static final int EMPTY_SHARD_SLOT = 0;
    public static final int RESULTS_SLOTS_START = 1;
    public static final int RESULTS_SLOTS_END = 3;
    public static final int RESULTS_SLOT_MAX_SIZE = 21;

    public static final int ELEMENT_STORAGE_CAPACITY = 50;
    public static final int MAX_DISTINCT_ELEMENTS_STORED = 3;

    public static final int ADDITIONAL_INVENTORY_SIZE = 4;
    public static final int ADDITIONAL_CONTAINER_DATA_SIZE = 2;

    public static final int TOTAL_CONVERSION_TIME = 40;

    private final ItemStackHandler additionalInventory = new ItemStackHandler(ADDITIONAL_INVENTORY_SIZE) {
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

        @Override
        public int getSlotLimit(int slot) {
            if (slot >= RESULTS_SLOTS_START && slot <= RESULTS_SLOTS_END) {
                return RESULTS_SLOT_MAX_SIZE;
            }
            return super.getSlotLimit(slot);
        }
    };

    private final IItemHandler inputInsertOnlyWrapper = new InsertOnlyRangedWrapper(this.baseInventory, INPUT_SLOT, INPUT_SLOT + 1);
    private final IItemHandler fuelInsertOnlyWrapper = new InsertOnlyRangedWrapper(this.baseInventory, FUEL_SLOT, FUEL_SLOT + 1);
    private final IItemHandler shardInsertOnlyWrapper = new InsertOnlyRangedWrapper(this.additionalInventory, EMPTY_SHARD_SLOT, EMPTY_SHARD_SLOT + 1);
    private final IItemHandler resultsExtractOnlyWrapper = new ExtractOnlyRangedWrapper(this.additionalInventory, RESULTS_SLOTS_START, RESULTS_SLOTS_END + 1);

    private final IItemHandler mergedItemHandler = new CombinedInvWrapper(this.baseInventory, this.additionalInventory);

    private final DynamicItemHandlerProvider dynamicItemHandlerProvider = DynamicItemHandlerProvider.builder()
            .addItemHandlerForFace(BlockFace.FRONT, this.fuelInsertOnlyWrapper)
            .addItemHandlerForFace(BlockFace.BACK, this.fuelInsertOnlyWrapper)
            .addItemHandlerForFace(BlockFace.LEFT, this.shardInsertOnlyWrapper)
            .addItemHandlerForFace(BlockFace.RIGHT, this.shardInsertOnlyWrapper)
            .addItemHandlerForFace(BlockFace.TOP, this.inputInsertOnlyWrapper)
            .addItemHandlerForFace(BlockFace.BOTTOM, this.resultsExtractOnlyWrapper)
            .addItemHandlerForFace(BlockFace.ANY, this.mergedItemHandler)
            .build(AbstractElementalExtractorBlock.FACING);

    private final ContainerData additionalData = new ContainerData() {
        @Override
        public int get(int index) {
            return switch (index) {
                case 0 -> PrimitiveElementalExtractorBlockEntity.this.shardConversionTimer;
                case 1 -> TOTAL_CONVERSION_TIME; //PrimitiveElementalExtractorBlockEntity.this.totalShardConversionTime;
                default -> throw new IllegalStateException("Unexpected value: " + index);
            };
        }

        @Override
        public void set(int index, int value) {
            switch (index) {
                case 0 -> PrimitiveElementalExtractorBlockEntity.this.shardConversionTimer = value;
                case 1 -> {
                }
                default -> throw new IllegalStateException("Unexpected value: " + index);
            }
        }

        @Override
        public int getCount() {
            return ADDITIONAL_CONTAINER_DATA_SIZE;
        }
    };

    private int shardConversionTimer;
    //private int totalShardConversionTime;
    private int explosionTimer;

    public PrimitiveElementalExtractorBlockEntity(BlockPos pos, BlockState blockState) {
        super(ModBlockEntities.PRIMITIVE_ELEMENTAL_EXTRACTOR.get(), pos, blockState, ELEMENT_STORAGE_CAPACITY, MAX_DISTINCT_ELEMENTS_STORED);
        this.dynamicItemHandlerProvider.updateSides(this.getBlockState());
    }

    @Override
    public void serverTick() {
        super.serverTick();

        var emptyShardStack = this.additionalInventory.getStackInSlot(EMPTY_SHARD_SLOT);
        var canBeConverted = this.elementStorage.getTotalAmount() > 0 && emptyShardStack.getCount() > 0;

        if (canBeConverted) {
            var canAtleastOneBeConverted = false;
            for (var element : this.elementStorage.getAllStoredElementTypes()) {
                if (!this.elementStorage.canTakeElement(element)) {
                    continue;
                }

                var canConvert = tryAddShardToResult(element, true);
                var canTake = this.elementStorage.takeElement(element, 1, true) == 1;
                var hasEnoughEmptyShards = this.additionalInventory.getStackInSlot(EMPTY_SHARD_SLOT).getCount() > 0;

                if (!canConvert || !canTake || !hasEnoughEmptyShards) {
                    continue;
                }

                canAtleastOneBeConverted = true;
                if (this.shardConversionTimer == TOTAL_CONVERSION_TIME) {
                    this.tryAddShardToResult(element, false);
                    this.elementStorage.takeElement(element, 1, false);

                    emptyShardStack.shrink(1);

                    shardConversionTimer = 0;
                } else {
                    this.shardConversionTimer++;
                }

                break;
            }

            if (!canAtleastOneBeConverted && this.shardConversionTimer > 0) {
                this.shardConversionTimer = 0;
            }
        } else if (this.shardConversionTimer > 0) {
            this.shardConversionTimer = 0;
        }

        if (this.elementStorage.getTotalAmount() == this.elementStorage.getMaxCapacity()) {
            this.explosionTimer++;

            if (this.explosionTimer == 1) {
                this.level.playSound(null, this.worldPosition, SoundEvents.TNT_PRIMED, SoundSource.BLOCKS, 1f, 1f);
            }

            ((ServerLevel) this.level).sendParticles(ParticleTypes.SMOKE, this.worldPosition.getX() + 0.5, this.worldPosition.getY() + 1, this.worldPosition.getZ() + 0.5, 1, 0.1, 0.1, 0.1, 0);

            if (this.explosionTimer == 100) {
                this.level.removeBlock(this.worldPosition, false);
                this.level.explode(null, this.worldPosition.getX(), this.worldPosition.getY(), this.worldPosition.getZ(), 3f, Level.ExplosionInteraction.BLOCK);
            }
        } else if (this.explosionTimer > 0) {
            this.explosionTimer = 0;
        }
    }

    private boolean tryAddShardToResult(Holder<Element> elementHolder, boolean simulate) {
        var shard = ElementHelper.createShardWithElement(elementHolder);

        for (int i = RESULTS_SLOTS_START; i <= RESULTS_SLOTS_END; i++) {
            var stackInSlot = this.additionalInventory.getStackInSlot(i);

            if (stackInSlot.isEmpty()) {
                if (!simulate) {
                    this.additionalInventory.setStackInSlot(i, shard);
                }
                return true;
            }

            var stackSize = stackInSlot.getCount();
            var slotLimit = this.additionalInventory.getSlotLimit(i);

            if (stackSize == slotLimit) {
                continue;
            }

            var isSameItem = ItemStack.isSameItem(stackInSlot, shard);
            var hasSameDataComponents = ItemStack.isSameItemSameComponents(stackInSlot, shard);

            if (!isSameItem || !hasSameDataComponents) {
                continue;
            }

            if (!simulate) {
                stackInSlot.grow(1);
            }

            return true;
        }

        return false;
    }

    @Override
    public IItemHandler getItemHandlerForDirection(@Nullable Direction direction) {
        return this.dynamicItemHandlerProvider.getItemHandlerForDirection(direction);
    }

    @Override
    @SuppressWarnings("deprecation")
    public void setBlockState(BlockState blockState) {
        super.setBlockState(blockState);
        this.dynamicItemHandlerProvider.updateSides(this.getBlockState());
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);

        tag.put(TAG_ADDITIONAL_INVENTORY, this.additionalInventory.serializeNBT(registries));
        tag.putInt(TAG_CONVERSION_TIMER, this.shardConversionTimer);
        //tag.putInt(TAG_TOTAL_CONVERSION_TIME, this.totalShardConversionTime);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);

        this.additionalInventory.deserializeNBT(registries, tag.getCompound(TAG_ADDITIONAL_INVENTORY));
        this.shardConversionTimer = tag.getInt(TAG_CONVERSION_TIMER);
        //this.totalShardConversionTime = tag.getInt(TAG_TOTAL_CONVERSION_TIME);
    }

    @Override
    public void dropContents() {
        ItemHandlerHelper.dropItemHandlerContents(this.level, this.worldPosition, this.mergedItemHandler);
    }

    @Override
    public Component getDisplayName() {
        return Component.empty();
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player) {
        return new PrimitiveElementalExtractorMenu(containerId, playerInventory,
                this.baseInventory, this.additionalInventory,
                this.baseContainerData, this.additionalData,
                this.elementStorage, ContainerLevelAccess.create(this.level, this.worldPosition));
    }
}
