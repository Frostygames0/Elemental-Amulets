package frostygames0.elementalamulets.block.entity.extractor;

import frostygames0.elementalamulets.element.ElementHelper;
import frostygames0.elementalamulets.element.ElementalComposition;
import frostygames0.elementalamulets.element.storage.ElementStorage;
import frostygames0.elementalamulets.element.storage.IElementStorage;
import frostygames0.elementalamulets.element.storage.IElementStorageProvider;
import frostygames0.elementalamulets.inventory.InsertOnlyRangedWrapper;
import frostygames0.elementalamulets.inventory.provider.IItemHandlerProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;

public abstract class AbstractElementalExtractorBlockEntity extends BlockEntity implements MenuProvider, IElementStorageProvider, IItemHandlerProvider {
    private static final String TAG_BASE_INVENTORY = "baseInventory";
    private static final String TAG_EXTRACTION_TIME = "extractionTime";
    private static final String TAG_TOTAL_EXTRACTION_TIME = "totalExtractionTime";
    private static final String TAG_LIT_TIME = "litTime";
    private static final String TAG_TOTAL_LIT_TIME = "totalLitTime";
    private static final String TAG_ELEMENT_STORAGE = "elementStorage";

    public static final int INPUT_SLOT = 0;
    public static final int FUEL_SLOT = 1;
    public static final int BASE_INVENTORY_SIZE = 2;

    protected final ItemStackHandler baseInventory = new ItemStackHandler(BASE_INVENTORY_SIZE) {
        @Override
        protected void onContentsChanged(int slot) {
            super.onContentsChanged(slot);
            if (slot == INPUT_SLOT) {
                AbstractElementalExtractorBlockEntity.this.totalExtractionTime = calculateExtractionTime(this.getStackInSlot(slot));
            }

            AbstractElementalExtractorBlockEntity.this.setChanged();
        }

        @Override
        public boolean isItemValid(int slot, ItemStack stack) {
            if (slot == FUEL_SLOT) {
                return isFuel(stack, AbstractElementalExtractorBlockEntity.this.level);
            }

            return super.isItemValid(slot, stack);
        }

        @Override
        public int getSlotLimit(int slot) {
            return super.getSlotLimit(slot);
        }
    };

    protected final IItemHandler inputInsertOnlyWrapper = new InsertOnlyRangedWrapper(this.baseInventory, INPUT_SLOT, INPUT_SLOT + 1);
    protected final IItemHandler fuelInsertOnlyWrapper = new InsertOnlyRangedWrapper(this.baseInventory, FUEL_SLOT, FUEL_SLOT + 1);

    protected AbstractElementalExtractorBlockEntity(BlockEntityType<?> blockEntityType,
                                                    BlockPos pos, BlockState state,
                                                    int storageCapacity, int distinctElementCount) {
        super(blockEntityType, pos, state);

        this.elementStorage = new ElementStorage(storageCapacity, distinctElementCount) {
            @Override
            public void onChanged() {
                super.onChanged();
                AbstractElementalExtractorBlockEntity.this.setChanged();
            }
        };
    }

    protected final ElementStorage elementStorage;

    private int extractionTimer;
    private int totalExtractionTime;

    private int litTimeRemaining;
    private int totalLitTime;

    public void serverTick() {
        boolean wasLitAtStartOfTick = this.isLit();

        if (this.isLit()) {
            this.litTimeRemaining--;
        }

        var extractableStack = this.baseInventory.getStackInSlot(INPUT_SLOT);
        var fuelStack = this.baseInventory.getStackInSlot(FUEL_SLOT);

        var stacksArePresent = !extractableStack.isEmpty() && !fuelStack.isEmpty();
        var isActiveOrCanBe = this.isLit() || stacksArePresent;
        if (isActiveOrCanBe) {
            var canBeLit = !this.isLit() && this.canBeFullyExtractedFrom(extractableStack);
            if (canBeLit) {
                this.litTimeRemaining = getBurnTime(fuelStack, this.level);
                this.totalLitTime = this.litTimeRemaining;

                if (this.isLit()) { // If was succesfully lit, consume fuel
                    var remainder = fuelStack.getCraftingRemainder();
                    if (!remainder.isEmpty()) {
                        this.baseInventory.setStackInSlot(FUEL_SLOT, remainder);
                    } else if (!fuelStack.isEmpty()) {
                        fuelStack.shrink(1);
                    }
                }
            }

            var canPerformExtraction = this.isLit() && this.canBeFullyExtractedFrom(extractableStack);
            if (canPerformExtraction) {
                this.extractionTimer++;
                if (this.extractionTimer == this.totalExtractionTime) {
                    this.extractionTimer = 0;
                    this.totalExtractionTime = this.calculateExtractionTime(extractableStack);

                    if (this.canBeFullyExtractedFrom(extractableStack)) {
                        this.insertCompositionOfStackIntoStorage(extractableStack);
                        extractableStack.shrink(1);
                    }
                }
            } else {
                this.extractionTimer = 0;
            }
        } else if (extractionTimer > 0) {
            this.extractionTimer = Mth.clamp(this.extractionTimer - 2, 0, this.extractionTimer);
        }

        if (wasLitAtStartOfTick != this.isLit()) {
            this.setChanged();

            var newState = getBlockState().setValue(BlockStateProperties.LIT, this.isLit());
            this.level.setBlockAndUpdate(this.worldPosition, newState);
        }
    }

    private boolean isLit() {
        return this.litTimeRemaining > 0;
    }

    public static boolean isFuel(ItemStack stack, Level level) {
        return getBurnTime(stack, level) > 0 || stack.is(Items.BUCKET);
    }

    public static int getBurnTime(ItemStack stack, Level level) {
        return stack.getBurnTime(RecipeType.SMELTING, level.fuelValues());
    }

    protected int calculateExtractionTime(ItemStack stack) {
        var composition = ElementHelper.getItemComposition(stack);
        return composition.map(elementalComposition -> elementalComposition.getTotalAmount() * 20).orElse(0);
    }

    private boolean canBeFullyExtractedFrom(ItemStack stack) {
        var composition = ElementHelper.getItemComposition(stack);
        return composition.filter(this::canInsertAllElementsOfStack).isPresent();
    }

    private void insertCompositionOfStackIntoStorage(ItemStack stack) {
        var composition = ElementHelper.getItemComposition(stack);
        if (composition.isEmpty()) {
            return;
        }

        for (var entry : composition.get().elementAmounts().entrySet()) {
            var element = entry.getKey();
            var amount = entry.getValue();
            this.elementStorage.addElement(element, amount, false);
        }
    }

    private boolean canInsertAllElementsOfStack(ElementalComposition composition) {
        for (var entry : composition.elementAmounts().entrySet()) {
            var element = entry.getKey();
            var amount = entry.getValue();
            if (!this.elementStorage.canAddElement(element)) {
                return false;
            }

            int added = this.elementStorage.addElement(entry.getKey(), entry.getValue(), true);
            if (added != amount) {
                return false;
            }
        }

        return true;
    }

    @Override
    public IElementStorage getElementStorage() {
        return this.elementStorage;
    }

    public abstract void dropContents();

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);

        this.elementStorage.deserializeNBT(registries, tag.getCompound(TAG_ELEMENT_STORAGE));
        this.baseInventory.deserializeNBT(registries, tag.getCompound(TAG_BASE_INVENTORY));
        this.extractionTimer = tag.getInt(TAG_EXTRACTION_TIME);
        this.totalExtractionTime = tag.getInt(TAG_TOTAL_EXTRACTION_TIME);
        this.litTimeRemaining = tag.getInt(TAG_TOTAL_LIT_TIME);
        this.totalLitTime = tag.getInt(TAG_TOTAL_EXTRACTION_TIME);
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);

        tag.put(TAG_ELEMENT_STORAGE, this.elementStorage.serializeNBT(registries));
        tag.put(TAG_BASE_INVENTORY, this.baseInventory.serializeNBT(registries));
        tag.putInt(TAG_EXTRACTION_TIME, this.extractionTimer);
        tag.putInt(TAG_TOTAL_EXTRACTION_TIME, this.totalExtractionTime);
        tag.putInt(TAG_LIT_TIME, this.litTimeRemaining);
        tag.putInt(TAG_TOTAL_LIT_TIME, this.totalLitTime);
    }
}
