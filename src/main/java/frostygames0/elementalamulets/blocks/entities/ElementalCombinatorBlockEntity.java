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

package frostygames0.elementalamulets.blocks.entities;

import frostygames0.elementalamulets.advancements.triggers.ModCriteriaTriggers;
import frostygames0.elementalamulets.blocks.ElementalCombinator;
import frostygames0.elementalamulets.blocks.menu.ElementalCombinatorMenu;
import frostygames0.elementalamulets.capability.RestrictiveItemHandler;
import frostygames0.elementalamulets.client.particles.ModParticles;
import frostygames0.elementalamulets.config.ModConfig;
import frostygames0.elementalamulets.init.ModBEs;
import frostygames0.elementalamulets.init.ModBlocks;
import frostygames0.elementalamulets.init.ModRecipes;
import frostygames0.elementalamulets.init.ModStats;
import frostygames0.elementalamulets.recipes.ElementalCombination;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.RecipeWrapper;


import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ElementalCombinatorBlockEntity extends BlockEntity implements MenuProvider {
    private static final String TAG_CONTENTS = "Contents";
    private static final String TAG_COMBINATION_TIME = "CombinationTime";
    private static final String TAG_TOTAL_COMBINATION_TIME = "TotalCombinationTime";

    public static final int HANDLER_SIZE = 10;
    public static final int DATA_SIZE = 2;

    // Internal handler with no restrictions
    private final ItemStackHandler internalHandler = new ItemStackHandler(HANDLER_SIZE) {
        @Override
        protected void onContentsChanged(int slot) {
            ElementalCombinatorBlockEntity.this.setChanged();
            if (slot == 0)
                level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Block.UPDATE_CLIENTS);
        }
    };

    // Restricted handler which restricts inserting into slot 0 (output)
    private final IItemHandler restrictedHandler = new RestrictiveItemHandler(internalHandler);
    private final LazyOptional<IItemHandler> optional = LazyOptional.of(() -> restrictedHandler);

    @Nullable
    private ElementalCombination recipe;
    private final RecipeWrapper wrappedHandler = new RecipeWrapper(internalHandler);

    private int combinationTime;
    private int totalTime;

    private final ContainerData combinatorData = new ContainerData() {
        @Override
        public int get(int index) {
            return switch (index) {
                case 0 -> ElementalCombinatorBlockEntity.this.combinationTime;
                case 1 -> ElementalCombinatorBlockEntity.this.totalTime;
                default -> 0;
            };
        }

        @Override
        public void set(int index, int value) {
            switch (index) {
                case 0 -> ElementalCombinatorBlockEntity.this.combinationTime = value;
                case 1 -> ElementalCombinatorBlockEntity.this.totalTime = value;
            }
        }

        @Override
        public int getCount() {
            return DATA_SIZE;
        }
    };

    public ElementalCombinatorBlockEntity(BlockPos pWorldPosition, BlockState pBlockState) {
        super(ModBEs.ELEMENTAL_COMBINATOR_BE.get(), pWorldPosition, pBlockState);
    }

    public void serverTick() {
        if (level != null) {
            if (this.isCombining()) {
                ElementalCombination recipe = this.getCombinationRecipe();
                if (this.canCombine(recipe)) {
                    this.totalTime = recipe.getCombinationTime();
                    this.combinationTime += this.getFocusedLevel();
                    if (ModConfig.CachedValues.FANCY_COMBINATION) {
                        if (this.level.getGameTime() % 80 == 0) {
                            this.playSound(SoundEvents.BEACON_AMBIENT);
                            ((ServerLevel) level).sendParticles(ModParticles.COMBINATION_PARTICLE.get(), worldPosition.getX() + 0.5, worldPosition.above().getY() + 0.4, worldPosition.getZ() + 0.5, 50, 0, 0, 0, 5);
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
                                LightningBolt lightbolt = EntityType.LIGHTNING_BOLT.create(level);
                                lightbolt.setVisualOnly(true);
                                lightbolt.moveTo(Vec3.atBottomCenterOf(worldPosition.above()));
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
            ItemStack result = recipe.assemble(new RecipeWrapper(internalHandler));
            NonNullList<ItemStack> remainingItems = recipe.getRemainingItems(new RecipeWrapper(internalHandler));

            internalHandler.insertItem(0, result, false);
            for (int i = 1; i < internalHandler.getSlots(); ++i) {
                internalHandler.extractItem(i, 1, false);
                internalHandler.insertItem(i, remainingItems.get(i), false); // inserting remaining items
            }

            this.setChanged();

            Vec3 vector = Vec3.atBottomCenterOf(worldPosition);
            if (level instanceof ServerLevel) {
                ((ServerLevel) level).getPlayers(player -> vector.distanceToSqr(player.position()) <= 16)
                        .forEach(player -> {
                            ModCriteriaTriggers.ITEM_COMBINED.trigger(player, result, (ServerLevel) level, worldPosition.getX(), worldPosition.getY(), worldPosition.getZ());
                            player.awardStat(ModStats.TIMES_COMBINED);
                        });
            }
        }
    }

    @Nullable
    private ElementalCombination getCombinationRecipe() {
        if (this.recipe != null && recipe.matches(wrappedHandler, level))
            return this.recipe;

        return this.recipe = level.getRecipeManager().getRecipeFor(ModRecipes.ELEMENTAL_COMBINATION_TYPE, wrappedHandler, level).orElse(null);
    }

    private boolean canCombine(@Nullable ElementalCombination recipe) {
        if (recipe != null) {
            ItemStack result = recipe.assemble(new RecipeWrapper(internalHandler));
            return !result.isEmpty() && internalHandler.insertItem(0, result, true).isEmpty() && level.canSeeSky(worldPosition.above());
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
        this.level.playSound(null, worldPosition, sound, SoundSource.BLOCKS, 1.0f, 1.0f);
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
    protected void saveAdditional(CompoundTag compound) {
        compound.putInt(TAG_COMBINATION_TIME, this.combinationTime);
        compound.putInt(TAG_TOTAL_COMBINATION_TIME, this.totalTime);
        this.writeInventory(compound);
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        this.combinationTime = nbt.getInt(TAG_COMBINATION_TIME);
        this.totalTime = nbt.getInt(TAG_TOTAL_COMBINATION_TIME);
        this.readInventory(nbt);
    }

    private void writeInventory(CompoundTag compound) {
        compound.put(TAG_CONTENTS, internalHandler.serializeNBT());
    }

    private void readInventory(CompoundTag nbt) {
        internalHandler.deserializeNBT(nbt.getCompound(TAG_CONTENTS));
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
    public void invalidateCaps() {
        super.invalidateCaps();
        optional.invalidate();
    }

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag tag = new CompoundTag();
        this.writeInventory(tag);
        return tag;
    }

    @Nullable
    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
        CompoundTag tag = pkt.getTag();
        if (tag != null) {
            this.readInventory(tag);
        }
    }

    @Override
    public AABB getRenderBoundingBox() {
        return new AABB(worldPosition.offset(-1, 0, -1), worldPosition.offset(1, 2, 1));
    }

    @Override
    public Component getDisplayName() {
        return new TranslatableComponent("block.elementalamulets.elemental_combinator.guititle");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int windowId, Inventory playerInventory, Player player) {
        return new ElementalCombinatorMenu(windowId, this.worldPosition, playerInventory, this.restrictedHandler, this.combinatorData);
    }
}
