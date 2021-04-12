package frostygames0.elementalamulets.blocks.tiles;

import frostygames0.elementalamulets.core.init.ModRecipes;
import frostygames0.elementalamulets.core.init.ModTiles;
import net.minecraft.block.BlockState;
import net.minecraft.inventory.container.WorkbenchContainer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.*;
import net.minecraftforge.items.wrapper.RecipeWrapper;

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
        if(!world.isRemote()) {
            craft(2);
        }
    }

    private void craft(int ResultSlot) {
            IRecipe<RecipeWrapper> recipe = this.world.getRecipeManager().getRecipe(ModRecipes.ELEMENTAL_SEPARATION_RECIPE, new RecipeWrapper(handler), this.world).orElse(null);
            if (recipe != null) {
                ItemStack result = recipe.getCraftingResult(new RecipeWrapper(handler));
                if (handler.getStackInSlot(2).isEmpty()){
                    handler.setStackInSlot(2, result);
                }
            }
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
                //removeIngredients(slot);
            }

            // Preventing from inserting anything in slot 2(result slot)
            @Nonnull
            @Override
            public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
                if(slot == 2) {
                    return stack;
                }
                return super.insertItem(slot, stack, simulate);
            }
            /* Preventing from extracting result from slot 2 by hoppers
            @Nonnull
            @Override
            public ItemStack extractItem(int slot, int amount, boolean simulate) {
                if(slot == 2) {
                    return ItemStack.EMPTY;
                }
                return super.extractItem(slot, amount, simulate);
            }*/
        };
    }

}
