package frostygames0.elementalamulets.blocks.tiles;

import frostygames0.elementalamulets.advancements.triggers.ModCriteriaTriggers;
import frostygames0.elementalamulets.blocks.ElementalCombinator;
import frostygames0.elementalamulets.blocks.containers.ElementalCombinatorContainer;
import frostygames0.elementalamulets.capability.AutomationItemHandler;
import frostygames0.elementalamulets.client.particles.ModParticles;
import frostygames0.elementalamulets.config.ModConfig;
import frostygames0.elementalamulets.core.init.ModRecipes;
import frostygames0.elementalamulets.core.init.ModTiles;
import frostygames0.elementalamulets.recipes.ElementalCombination;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.effect.LightningBoltEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.RecipeWrapper;


import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ElementalCombinatorTile extends TileEntity implements ITickableTileEntity, INamedContainerProvider {
    private final ItemStackHandler handler = createHandler(10);
    private final LazyOptional<IItemHandler> optional = LazyOptional.of(() -> new AutomationItemHandler(handler));

    private int combinationTime;
    private int totalTime;
    private final IIntArray combinatorData = new IIntArray() {
        @Override
        public int get(int index) {
            switch(index) {
                case 0:
                    return ElementalCombinatorTile.this.combinationTime;
                case 1:
                    return ElementalCombinatorTile.this.totalTime;
                default:
                    return 0;
            }
        }

        @Override
        public void set(int index, int value) {
            switch(index) {
                case 0:
                    ElementalCombinatorTile.this.combinationTime = value;
                    break;
                case 1:
                    ElementalCombinatorTile.this.totalTime = value;
                    break;
            }
        }
        @Override
        public int size() {
            return 2;
        }
    };

    public ElementalCombinatorTile() {
        super(ModTiles.ELEMENTAL_COMBINATOR_TILE.get());
    }

    @Override
    public void tick() {
        if(world != null && !world.isRemote()) {
            if(this.isCrafting()) {
                ElementalCombination recipe = this.world.getRecipeManager().getRecipe(ModRecipes.ELEMENTAL_COMBINATION_TYPE, new RecipeWrapper(handler), this.world).orElse(null);
                if(this.canCombine(recipe)) {
                    this.totalTime = recipe.getCombinationTime();
                    this.combinationTime++;
                    if(ModConfig.cached.FANCY_COMBINATION) {
                        if (this.combinationTime % 80 == 0) {
                            this.playSound(SoundEvents.BLOCK_BEACON_AMBIENT);
                            ((ServerWorld) world).spawnParticle(ModParticles.COMBINATION_PARTICLE.get(), pos.getX() + 0.5, pos.up().getY() + 0.4, pos.getZ() + 0.5, 50, 0, 0, 0, 5);
                        }
                    }
                    if(!this.getBlockState().get(ElementalCombinator.COMBINING)) {
                        this.world.setBlockState(pos, this.getBlockState().with(ElementalCombinator.COMBINING, true));
                    }
                    if(this.totalTime == this.combinationTime) {
                        this.stopCombination();
                        this.combine(recipe);

                        if(ModConfig.cached.FANCY_COMBINATION) {
                            this.playSound(SoundEvents.BLOCK_ENCHANTMENT_TABLE_USE);

                            LightningBoltEntity lightbolt = EntityType.LIGHTNING_BOLT.create(world);
                            lightbolt.setEffectOnly(true);
                            lightbolt.moveForced(Vector3d.copyCenteredHorizontally(pos.up()));
                            this.world.addEntity(lightbolt);

                            this.playSound(SoundEvents.ENTITY_LIGHTNING_BOLT_IMPACT); // Since lightning that's effect only has no sound, I need to manually play it
                        }
                    }
                } else {
                    this.stopCombination();
                    this.playSound(SoundEvents.BLOCK_BEACON_DEACTIVATE);
                }
            }
        }
    }

    private void combine(@Nullable ElementalCombination recipe) {
        if(this.canCombine(recipe)) {
            ItemStack result = recipe.getCraftingResult(new RecipeWrapper(handler));
            NonNullList<ItemStack> remainingItems = recipe.getRemainingItems(new RecipeWrapper(handler));
            handler.insertItem(0, result, false);
            for (int i = 1; i < handler.getSlots(); ++i) {
                handler.extractItem(i, 1, false);
                handler.insertItem(i, remainingItems.get(i), false); // inserting remaining items
            }
            // TODO Maybe I should give it only to the closest player or the one who started combination idk
            ((ServerWorld)world).getChunkProvider().chunkManager.getTrackingPlayers(new ChunkPos(pos), false)
                    .filter(player -> player.getDistanceSq(pos.getX(), pos.getY(), pos.getZ()) <= 100)
                    .forEach(player -> ModCriteriaTriggers.ITEM_COMBINED.trigger(player, result, (ServerWorld) world, pos.getX(), pos.getY(), pos.getZ()));
        }
    }

    private boolean canCombine(@Nullable ElementalCombination recipe) {
        if(recipe != null) {
            ItemStack result = recipe.getCraftingResult(new RecipeWrapper(handler));
            return !result.isEmpty() && handler.insertItem(0, result, true).isEmpty() && world.canBlockSeeSky(pos.up());
        }
        return false;
    }


    public void startCombination() {
        if (!this.isCrafting()) {
            this.combinationTime = 1;
        }
    }

    private void stopCombination() {
        if(this.isCrafting()) {
            this.combinationTime = 0;
            this.world.setBlockState(pos, this.getBlockState().with(ElementalCombinator.COMBINING, false));
        }
    }

    public boolean isCrafting() {
        return this.combinationTime > 0;
    }

    private void playSound(SoundEvent sound) {
        this.world.playSound(null, pos, sound, SoundCategory.BLOCKS, 1.0f, 1.0f);
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        compound.putInt("CombinationTime", this.combinationTime);
        compound.putInt("TotalCombinationTime", this.totalTime);
        compound.put("Contents", handler.serializeNBT());
        return super.write(compound);
    }

    @Override
    public void read(BlockState state, CompoundNBT nbt) {
        this.combinationTime = nbt.getInt("CombinationTime");
        this.totalTime = nbt.getInt("TotalCombinationTime");
        handler.deserializeNBT(nbt.getCompound("Contents"));
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
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
        this.handleUpdateTag(this.getBlockState(), pkt.getNbtCompound());
    }

    @Nullable
    @Override
    public SUpdateTileEntityPacket getUpdatePacket() {
        return new SUpdateTileEntityPacket(pos, -1, this.getUpdateTag());
    }

    @Override
    public void handleUpdateTag(BlockState state, CompoundNBT tag) {
        this.read(state, tag);
    }

    @Override
    public CompoundNBT getUpdateTag() {
        return this.write(new CompoundNBT());
    }

    @Override
    public AxisAlignedBB getRenderBoundingBox() {
        return new AxisAlignedBB(pos.add(-1, 0, -1), pos.add(1, 2, 1));
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
                markDirty();
                world.notifyBlockUpdate(pos, getBlockState(), getBlockState(), 2);
            }
        };
    }

    @Override
    public ITextComponent getDisplayName() {
        return new TranslationTextComponent("block.elementalamulets.elemental_combinator.guititle");
    }

    @Nullable
    @Override
    public Container createMenu(int p_createMenu_1_, PlayerInventory p_createMenu_2_, PlayerEntity p_createMenu_3_) {
        return new ElementalCombinatorContainer(p_createMenu_1_, world, pos, p_createMenu_2_, p_createMenu_3_, this.combinatorData);
    }
}
