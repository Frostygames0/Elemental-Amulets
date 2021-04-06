package frostygames0.elementalamulets.blocks.tiles;

import frostygames0.elementalamulets.core.init.ModTiles;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ElementalCrafterTile extends TileEntity implements ITickableTileEntity {
    private ItemStackHandler handler = createHandler(3);
    private LazyOptional<IItemHandler> optional = LazyOptional.of(() -> handler);

    public ElementalCrafterTile() {
        super(ModTiles.ELEMENTAL_CRAFTER_TILE.get());
    }

    @Override
    public void tick() {
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        compound.put("contents", handler.serializeNBT());
        return super.write(compound);
    }

    @Override
    public void read(BlockState state, CompoundNBT nbt) {
        handler.deserializeNBT(nbt.getCompound("contents"));
        super.read(state, nbt);
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if(cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return optional.cast();
        }
        return super.getCapability(cap, side);
    }

    @Override
    protected void invalidateCaps() {
        super.invalidateCaps();
        optional.invalidate();
    }

    private ItemStackHandler createHandler(int size) {
        return new ItemStackHandler(size) {
            @Override
            protected void onContentsChanged(int slot) {
                super.onContentsChanged(slot);
                markDirty();
            }
        };
    }

}
