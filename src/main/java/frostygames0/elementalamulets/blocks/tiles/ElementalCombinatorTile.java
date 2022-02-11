/*
 *  Copyright (c) 2021
 *
 *     This file is part of Elemental Amulets, a Minecraft Mod.
 *
 *     Elemental Amulets is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Lesser General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Elemental Amulets is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Lesser General Public License for more details.
 *
 *     You should have received a copy of the GNU Lesser General Public License
 *     along with Elemental Amulets.  If not, see <https://www.gnu.org/licenses/>.
 */

package frostygames0.elementalamulets.blocks.tiles;

import frostygames0.elementalamulets.advancements.triggers.ModCriteriaTriggers;
import frostygames0.elementalamulets.blocks.ElementalCombinator;
import frostygames0.elementalamulets.blocks.containers.ElementalCombinatorContainer;
import frostygames0.elementalamulets.capability.AutomationItemHandler;
import frostygames0.elementalamulets.client.particles.ModParticles;
import frostygames0.elementalamulets.config.ModConfig;
import frostygames0.elementalamulets.init.ModBlocks;
import frostygames0.elementalamulets.init.ModRecipes;
import frostygames0.elementalamulets.init.ModStats;
import frostygames0.elementalamulets.init.ModTiles;
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
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.RecipeWrapper;


import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ElementalCombinatorTile extends TileEntity implements ITickableTileEntity, INamedContainerProvider {
    private static final String TAG_CONTENTS = "Contents";
    private static final String TAG_COMBINATION_TIME = "CombinationTime";
    private static final String TAG_TOTAL_COMBINATION_TIME = "TotalCombinationTime";

    private final ItemStackHandler handler = new ItemStackHandler(10) {
        @Override
        protected void onContentsChanged(int slot) {
            ElementalCombinatorTile.this.setChanged();
            if (slot == 0)
                level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Constants.BlockFlags.BLOCK_UPDATE);
        }
    };
    private final LazyOptional<IItemHandler> optional = LazyOptional.of(() -> new AutomationItemHandler(handler));

    private int combinationTime;
    private int totalTime;
    private final IIntArray combinatorData = new IIntArray() {
        @Override
        public int get(int index) {
            switch (index) {
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
            switch (index) {
                case 0:
                    ElementalCombinatorTile.this.combinationTime = value;
                    break;
                case 1:
                    ElementalCombinatorTile.this.totalTime = value;
                    break;
            }
        }

        @Override
        public int getCount() {
            return 2;
        }
    };

    public ElementalCombinatorTile() {
        super(ModTiles.ELEMENTAL_COMBINATOR_TILE.get());
    }

    @Override
    public void tick() {
        if (level != null && !level.isClientSide()) {
            if (this.isCombining()) {
                ElementalCombination recipe = this.level.getRecipeManager().getRecipeFor(ModRecipes.ELEMENTAL_COMBINATION_TYPE, new RecipeWrapper(handler), this.level).orElse(null);
                if (this.canCombine(recipe)) {
                    this.totalTime = recipe.getCombinationTime();
                    this.combinationTime += this.getFocusedLevel();
                    if (ModConfig.CachedValues.FANCY_COMBINATION) {
                        if (this.level.getGameTime() % 80 == 0) {
                            this.playSound(SoundEvents.BEACON_AMBIENT);
                            ((ServerWorld) level).sendParticles(ModParticles.COMBINATION_PARTICLE.get(), worldPosition.getX() + 0.5, worldPosition.above().getY() + 0.4, worldPosition.getZ() + 0.5, 50, 0, 0, 0, 5);
                        }
                    }
                    if (!this.getBlockState().getValue(ElementalCombinator.COMBINING)) {
                        this.level.setBlockAndUpdate(worldPosition, this.getBlockState().setValue(ElementalCombinator.COMBINING, true));
                    }
                    if (this.totalTime <= this.combinationTime) {
                        this.stopCombination();
                        this.combine(recipe);

                        if (ModConfig.CachedValues.FANCY_COMBINATION) {
                            this.playSound(SoundEvents.ENCHANTMENT_TABLE_USE);

                            if (this.totalTime >= 100) { // Strike only when combining for 5 or more seconds
                                LightningBoltEntity lightbolt = EntityType.LIGHTNING_BOLT.create(level);
                                lightbolt.setVisualOnly(true);
                                lightbolt.moveTo(Vector3d.atBottomCenterOf(worldPosition.above()));
                                this.level.addFreshEntity(lightbolt);
                            }
                        }
                    }
                } else {
                    this.stopCombination();
                    this.playSound(SoundEvents.BEACON_DEACTIVATE);
                }
            }
        }
    }

    private void combine(@Nullable ElementalCombination recipe) {
        if (this.canCombine(recipe)) {
            ItemStack result = recipe.assemble(new RecipeWrapper(handler));
            NonNullList<ItemStack> remainingItems = recipe.getRemainingItems(new RecipeWrapper(handler));
            handler.insertItem(0, result, false);
            for (int i = 1; i < handler.getSlots(); ++i) {
                handler.extractItem(i, 1, false);
                handler.insertItem(i, remainingItems.get(i), false); // inserting remaining items
            }

            this.setChanged();

            Vector3d vector = Vector3d.atBottomCenterOf(worldPosition);
            if (level instanceof ServerWorld) {
                ((ServerWorld) level).getPlayers(player -> vector.distanceToSqr(player.position()) <= 16)
                        .forEach(player -> {
                            ModCriteriaTriggers.ITEM_COMBINED.trigger(player, result, (ServerWorld) level, worldPosition.getX(), worldPosition.getY(), worldPosition.getZ());
                            player.awardStat(ModStats.TIMES_COMBINED);
                        });
            }
        }
    }

    private boolean canCombine(@Nullable ElementalCombination recipe) {
        if (recipe != null) {
            ItemStack result = recipe.assemble(new RecipeWrapper(handler));
            return !result.isEmpty() && handler.insertItem(0, result, true).isEmpty() && level.canSeeSky(worldPosition.above());
        }
        return false;
    }


    public void startCombination() {
        if (!this.isCombining()) {
            this.combinationTime = 1;
            this.setChanged();
        }
    }

    private void stopCombination() {
        if (this.isCombining()) {
            this.combinationTime = 0;
            this.level.setBlockAndUpdate(worldPosition, this.getBlockState().setValue(ElementalCombinator.COMBINING, false));
            this.setChanged();
        }
    }

    public boolean isCombining() {
        return this.combinationTime > 0;
    }

    private void playSound(SoundEvent sound) {
        this.level.playSound(null, worldPosition, sound, SoundCategory.BLOCKS, 1.0f, 1.0f);
    }

    private int getFocusedLevel() {
        int lv = 1;

        if (this.level.getBlockState(this.getBlockPos().above()).getBlock() == ModBlocks.CELESTIAL_FOCUS.get()) {
            lv++;
            if (level.getMoonBrightness() == 1 && level.isNight()) {
                lv++;
            }
        }

        return lv;
    }

    @Override
    public CompoundNBT save(CompoundNBT compound) {
        compound.putInt("CombinationTime", this.combinationTime);
        compound.putInt("TotalCombinationTime", this.totalTime);
        this.writeInventory(compound);
        return super.save(compound);
    }

    @Override
    public void load(BlockState state, CompoundNBT nbt) {
        this.combinationTime = nbt.getInt(TAG_COMBINATION_TIME);
        this.totalTime = nbt.getInt(TAG_TOTAL_COMBINATION_TIME);
        this.readInventory(nbt);
        super.load(state, nbt);
    }

    private CompoundNBT writeInventory(CompoundNBT compound) {
        compound.put(TAG_CONTENTS, handler.serializeNBT());
        return compound;
    }

    private void readInventory(CompoundNBT nbt) {
        handler.deserializeNBT(nbt.getCompound(TAG_CONTENTS));
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return optional.cast();
        }
        return super.getCapability(cap, side);
    }

    @Override
    protected void invalidateCaps() {
        super.invalidateCaps();
        optional.invalidate();
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
        this.readInventory(pkt.getTag());
    }

    @Nullable
    @Override
    public SUpdateTileEntityPacket getUpdatePacket() {
        return new SUpdateTileEntityPacket(worldPosition, -1, this.writeInventory(new CompoundNBT()));
    }

    @Override
    public ITextComponent getDisplayName() {
        return new TranslationTextComponent("block.elementalamulets.elemental_combinator.guititle");
    }

    @Nullable
    @Override
    public Container createMenu(int p_createMenu_1_, PlayerInventory p_createMenu_2_, PlayerEntity p_createMenu_3_) {
        return new ElementalCombinatorContainer(p_createMenu_1_, level, worldPosition, p_createMenu_2_, p_createMenu_3_, this.combinatorData);
    }

    @Override
    public AxisAlignedBB getRenderBoundingBox() {
        return new AxisAlignedBB(worldPosition.offset(-1, 0, -1), worldPosition.offset(1, 2, 1));
    }
}
