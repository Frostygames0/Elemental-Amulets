package frostygames0.elementalamulets.blocks.tiles;

import frostygames0.elementalamulets.ElementalAmulets;
import frostygames0.elementalamulets.core.init.ModRecipes;
import frostygames0.elementalamulets.core.init.ModTiles;
import frostygames0.elementalamulets.recipes.ElementalSeparation;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.effect.LightningBoltEntity;
import net.minecraft.entity.merchant.villager.VillagerProfession;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.WorkbenchContainer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.items.*;
import net.minecraftforge.items.wrapper.RecipeWrapper;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ElementalCombinatorTile extends TileEntity implements ITickableTileEntity {
    private ItemStackHandler handler = createHandler(10);
    private LazyOptional<IItemHandler> optional = LazyOptional.of(() -> handler);

    public ElementalCombinatorTile() {
        super(ModTiles.ELEMENTAL_CRAFTER_TILE.get());
    }

    @Override
    public void tick() {
    }

    public void combineElemental() {
        if(!world.isRemote()) {
            LightningBoltEntity lightbolt = new LightningBoltEntity(EntityType.LIGHTNING_BOLT, world);
            lightbolt.moveForced(Vector3d.copyCenteredHorizontally(this.pos.add(0, 1, 0)));
            lightbolt.setEffectOnly(true);
            IRecipe<RecipeWrapper> recipe = this.world.getRecipeManager().getRecipe(ModRecipes.ELEMENTAL_SEPARATION_RECIPE, new RecipeWrapper(handler), this.world).orElse(null);
            ItemStack result;
            if (recipe != null) {
                result = recipe.getCraftingResult(new RecipeWrapper(handler));
                ElementalAmulets.LOGGER.debug(result);
                if (!result.isEmpty()) {
                    if(handler.insertItem(0, result, true).isEmpty()) {
                        handler.insertItem(0, result, false);
                        for (int i = 1; i < handler.getSlots(); ++i) {
                            handler.extractItem(i, 1, false);
                        }
                        world.addEntity(lightbolt);
                        world.playSound(null, pos, SoundEvents.BLOCK_BEACON_ACTIVATE, SoundCategory.BLOCKS, 100, 1);
                    }
                }
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
            }
        };
    }

}
