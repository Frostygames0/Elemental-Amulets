package frostygames0.elementalamulets.block.entity.extractor;

import frostygames0.elementalamulets.element.ElementHelper;
import frostygames0.elementalamulets.element.ElementalComposition;
import frostygames0.elementalamulets.element.storage.ElementStorage;
import frostygames0.elementalamulets.element.storage.IElementStorage;
import frostygames0.elementalamulets.element.storage.IElementStorageProvider;
import frostygames0.elementalamulets.element.storage.OperationMode;
import frostygames0.elementalamulets.inventory.provider.IItemHandlerProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.neoforged.neoforge.items.ItemStackHandler;

// An elemental extractor with default logic, but it doesn't expose item handler capability or drops contents
public abstract class AbstractElementalExtractorBlockEntity extends BlockEntity implements MenuProvider, IElementStorageProvider, IItemHandlerProvider {
    private static final String TAG_BASE_INVENTORY = "BaseInventory";
    private static final String TAG_EXTRACTION_TIME = "ExtractionTime";
    private static final String TAG_TOTAL_EXTRACTION_TIME = "TotalExtractionTime";
    private static final String TAG_LIT_TIME_REMAINING = "LitTimeRemaining";
    private static final String TAG_TOTAL_LIT_TIME = "TotalLitTime";
    private static final String TAG_ELEMENT_STORAGE = "ElementStorage";

    public static final int INPUT_SLOT = 0;
    public static final int FUEL_SLOT = 1;
    public static final int BASE_INVENTORY_SIZE = 2;

    public static final int BASE_CONTAINER_DATA_SIZE = 4;

    protected final ItemStackHandler baseInventory = new ItemStackHandler(BASE_INVENTORY_SIZE) {
        @Override
        protected void onContentsChanged(int slot) {
            super.onContentsChanged(slot);
            if (slot == INPUT_SLOT) {
                totalExtractionTime = calculateExtractionTime(getStackInSlot(slot));
            }

            setChanged();
        }

        @Override
        public boolean isItemValid(int slot, ItemStack stack) {
            if (slot == FUEL_SLOT) {
                return isFuel(stack, level);
            }

            return super.isItemValid(slot, stack);
        }

        @Override
        public int getSlotLimit(int slot) {
            return super.getSlotLimit(slot);
        }
    };

    protected final ElementStorage elementStorage;

    private int extractionTimer;
    private int totalExtractionTime;

    private int litTimeRemaining;
    private int totalLitTime;

    protected final ContainerData baseContainerData = new ContainerData() {
        @Override
        public int get(int index) {
            return switch (index) {
                case 0 -> extractionTimer;
                case 1 -> totalExtractionTime;
                case 2 -> litTimeRemaining;
                case 3 -> totalLitTime;
                default -> throw new IllegalStateException("Unexpected value: " + index);
            };
        }

        @Override
        public void set(int index, int value) {
            switch (index) {
                case 0 -> extractionTimer = value;
                case 1 -> totalExtractionTime = value;
                case 2 -> litTimeRemaining = value;
                case 3 -> totalLitTime = value;
                default -> throw new IllegalStateException("Unexpected value: " + index);
            }
        }

        @Override
        public int getCount() {
            return BASE_CONTAINER_DATA_SIZE;
        }
    };

    protected AbstractElementalExtractorBlockEntity(BlockEntityType<?> blockEntityType,
                                                    BlockPos pos, BlockState state,
                                                    int storageCapacity, int distinctElementCount) {
        super(blockEntityType, pos, state);

        elementStorage = new ElementStorage(storageCapacity, distinctElementCount) {
            @Override
            public void onChanged() {
                super.onChanged();
                setChanged();
            }
        };
    }

    public static final BlockEntityTicker<AbstractElementalExtractorBlockEntity> TICKER =
            ((level1, pos, state, blockEntity) -> blockEntity.serverTick());

    public void serverTick() {
        boolean wasLitAtStartOfTick = isLit();

        if (isLit()) {
            litTimeRemaining--;
        }

        var extractableStack = baseInventory.getStackInSlot(INPUT_SLOT);
        var fuelStack = baseInventory.getStackInSlot(FUEL_SLOT);

        var stacksArePresent = !extractableStack.isEmpty() && !fuelStack.isEmpty();
        var isActiveOrCanBe = isLit() || stacksArePresent || canBeAdditionallyLit();
        if (isActiveOrCanBe) {
            var canBeLit = !isLit() && (canBeFullyExtractedFrom(extractableStack) || canBeAdditionallyLit());
            if (canBeLit) {
                litTimeRemaining = getBurnTime(fuelStack, level);
                totalLitTime = litTimeRemaining;

                if (isLit()) { // If was succesfully lit, consume fuel
                    var remainder = fuelStack.getCraftingRemainder();
                    if (!remainder.isEmpty()) {
                        baseInventory.setStackInSlot(FUEL_SLOT, remainder);
                    } else if (!fuelStack.isEmpty()) {
                        fuelStack.shrink(1);
                    }
                }
            }

            var canPerformExtraction = isLit() && canBeFullyExtractedFrom(extractableStack);
            if (canPerformExtraction) {
                extractionTimer++;
                if (extractionTimer == totalExtractionTime) {
                    extractionTimer = 0;
                    totalExtractionTime = calculateExtractionTime(extractableStack);

                    if (canBeFullyExtractedFrom(extractableStack)) {
                        insertCompositionOfStackIntoStorage(extractableStack);
                        extractableStack.shrink(1);
                    }
                }
            } else {
                extractionTimer = 0;
            }
        } else if (extractionTimer > 0) {
            extractionTimer = Mth.clamp(extractionTimer - 2, 0, extractionTimer);
        }

        if (wasLitAtStartOfTick != isLit()) {
            setChanged();

            var newState = getBlockState().setValue(BlockStateProperties.LIT, isLit());
            level.setBlockAndUpdate(worldPosition, newState);
        }
    }

    protected boolean canBeAdditionallyLit() {
        return false;
    }

    protected boolean isLit() {
        return litTimeRemaining > 0;
    }

    public static boolean isFuel(ItemStack stack, Level level) {
        return getBurnTime(stack, level) > 0 || stack.is(Items.BUCKET);
    }

    public static int getBurnTime(ItemStack stack, Level level) {
        return stack.getBurnTime(RecipeType.SMELTING, level.fuelValues());
    }

    protected int calculateExtractionTime(ItemStack stack) {
        var composition = ElementHelper.getStackElementalComposition(stack);
        return composition.map(elementalComposition -> elementalComposition.getTotalAmount() * 20).orElse(0);
    }

    private boolean canBeFullyExtractedFrom(ItemStack stack) {
        if (!ElementHelper.hasElementalComposition(stack)) {
            return false;
        }

        var composition = ElementHelper.getStackElementalComposition(stack);
        return composition.filter(this::canInsertAllElementsOfStack).isPresent();
    }

    private void insertCompositionOfStackIntoStorage(ItemStack stack) {
        var composition = ElementHelper.getStackElementalComposition(stack);
        if (composition.isEmpty()) {
            return;
        }

        for (var entry : composition.get().elementAmounts().entrySet()) {
            var element = entry.getKey();
            var amount = entry.getValue();
            elementStorage.addElement(element, amount, OperationMode.PERFORM);
        }
    }

    private boolean canInsertAllElementsOfStack(ElementalComposition composition) {
        for (var entry : composition.elementAmounts().entrySet()) {
            var element = entry.getKey();
            var amount = entry.getValue();
            if (!elementStorage.canAddElement(element)) {
                return false;
            }

            int added = elementStorage.addElement(entry.getKey(), entry.getValue(), OperationMode.SIMULATE);
            if (added != amount) {
                return false;
            }
        }

        return true;
    }

    @Override
    public IElementStorage getElementStorage() {
        return elementStorage;
    }

    public abstract void dropContents();

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);

        elementStorage.deserializeNBT(registries, tag.getCompound(TAG_ELEMENT_STORAGE));
        baseInventory.deserializeNBT(registries, tag.getCompound(TAG_BASE_INVENTORY));
        extractionTimer = tag.getInt(TAG_EXTRACTION_TIME);
        totalExtractionTime = tag.getInt(TAG_TOTAL_EXTRACTION_TIME);
        litTimeRemaining = tag.getInt(TAG_LIT_TIME_REMAINING);
        totalLitTime = tag.getInt(TAG_TOTAL_LIT_TIME);
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);

        tag.put(TAG_ELEMENT_STORAGE, elementStorage.serializeNBT(registries));
        tag.put(TAG_BASE_INVENTORY, baseInventory.serializeNBT(registries));
        tag.putInt(TAG_EXTRACTION_TIME, extractionTimer);
        tag.putInt(TAG_TOTAL_EXTRACTION_TIME, totalExtractionTime);
        tag.putInt(TAG_LIT_TIME_REMAINING, litTimeRemaining);
        tag.putInt(TAG_TOTAL_LIT_TIME, totalLitTime);
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("container.elementalamulets.elemental_extractor");
    }
}
