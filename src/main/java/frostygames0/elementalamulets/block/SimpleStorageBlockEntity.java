package frostygames0.elementalamulets.block;

import frostygames0.elementalamulets.element.ElementalComposition;
import frostygames0.elementalamulets.element.storage.ElementStorage;
import frostygames0.elementalamulets.element.storage.IElementStorage;
import frostygames0.elementalamulets.element.storage.IElementStorageProvider;
import frostygames0.elementalamulets.initialization.ModBlockEntities;
import frostygames0.elementalamulets.initialization.ModElements;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.state.BlockState;

public class SimpleStorageBlockEntity extends BlockEntity implements IElementStorageProvider {
    public static final BlockEntityTicker<SimpleStorageBlockEntity> TICKER =
            (level1, pos, state, blockEntity) -> blockEntity.tick();

    private final ElementStorage storage = new ElementStorage(1000, Integer.MAX_VALUE);
    private boolean isInit = false;

    public SimpleStorageBlockEntity(BlockPos pos, BlockState blockState) {
        super(ModBlockEntities.SIMPLE_STORAGE.get(), pos, blockState);
    }

    public void tick() {
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        storage.deserializeNBT(registries, tag);
        isInit = tag.getBoolean("init");
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.put("storage", storage.serializeNBT(registries));
        tag.putBoolean("init", isInit);
    }

    @Override
    public void onLoad() {
        if (hasLevel() && !level.isClientSide()) {
            if (!isInit) {
                var composition = ElementalComposition.builder(level)
                        .addElement(ModElements.FIRE, 100)
                        .addElement(ModElements.AIR, 100)
                        .addElement(ModElements.AETHER, 10).build();

                storage.setStored(composition);
                isInit = true;
            }
        }
    }

    @Override
    public IElementStorage getElementStorage() {
        return storage;
    }
}
