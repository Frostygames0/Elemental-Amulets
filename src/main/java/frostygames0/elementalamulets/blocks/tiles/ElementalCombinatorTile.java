package frostygames0.elementalamulets.blocks.tiles;

import frostygames0.elementalamulets.capability.AutomationItemHandler;
import frostygames0.elementalamulets.core.init.ModRecipes;
import frostygames0.elementalamulets.core.init.ModTiles;
import frostygames0.elementalamulets.recipes.ElementalCombination;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.effect.LightningBoltEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.RecipeWrapper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ElementalCombinatorTile extends TileEntity implements ITickableTileEntity {
    private final ItemStackHandler handler = createHandler(10);
    private final LazyOptional<IItemHandler> optional = LazyOptional.of(() -> new AutomationItemHandler(handler));

    private int cooldown;

    public ElementalCombinatorTile() {
        super(ModTiles.ELEMENTAL_COMBINATOR_TILE.get());
    }

    @Override
    public void tick() {
        if(world != null && !world.isRemote()) {
            if (this.cooldown > 0) {
                this.cooldown -= 1;
            }
        }
    }

    /**
     * Used to "combine" ingredients and elemental into result and put it into slot 0
     * @param player player that combined elementals.
     * @return TRUE - if combining was successful, otherwise FALSE;
     */
    public boolean combineElemental(PlayerEntity player) {
        if(world != null && !world.isRemote()) {
                if (this.cooldown <= 0 && this.world.canBlockSeeSky(this.pos.up())) {
                    LightningBoltEntity lightbolt = new LightningBoltEntity(EntityType.LIGHTNING_BOLT, world);
                    lightbolt.moveForced(Vector3d.copyCenteredHorizontally(this.pos.add(0, 1, 0)));
                    lightbolt.setEffectOnly(true);
                    ElementalCombination recipe = this.world.getRecipeManager().getRecipe(ModRecipes.ELEMENTAL_SEPARATION_RECIPE, new RecipeWrapper(handler), this.world).orElse(null);
                    ItemStack result;
                    if (recipe != null) {
                        result = recipe.getCraftingResult(new RecipeWrapper(handler)); // Result
                        NonNullList<ItemStack> remainingItems = recipe.getRemainingItems(new RecipeWrapper(handler)); // A list of item with container item like buckets
                        if (!result.isEmpty()) {
                            if (handler.insertItem(0,result, true).isEmpty()) {
                                handler.insertItem(0,result, false);
                                for (int i = 1; i < handler.getSlots(); ++i) {
                                    handler.extractItem(i, 1, false);
                                    handler.insertItem(i, remainingItems.get(i), false);
                                }
                                world.addEntity(lightbolt);
                                world.playSound(null, pos, SoundEvents.BLOCK_BEACON_ACTIVATE, SoundCategory.BLOCKS, 100, 1);
                                this.cooldown += recipe.getCooldown();
                                return true;
                            }
                        }
                    }
                } else {
                    player.sendStatusMessage(new TranslationTextComponent("block.elementalamulets.elemental_combinator.cooldown", this.cooldown/20).mergeStyle(TextFormatting.RED), true);
                }
        }
        return false;
    }

    public int getCooldown() {
        return this.cooldown;
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        compound.put("Contents", handler.serializeNBT());
        compound.putInt("Cooldown", this.cooldown);
        return super.write(compound);
    }

    @Override
    public void read(BlockState state, CompoundNBT nbt) {
        handler.deserializeNBT(nbt.getCompound("Contents"));
        this.cooldown = nbt.getInt("Cooldown");
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
