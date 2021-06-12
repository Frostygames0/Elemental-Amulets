package frostygames0.elementalamulets.blocks.tiles;

/*
public class Old extends TileEntity implements ITickableTileEntity {
    private final ItemStackHandler handler = createHandler(10);
    private final LazyOptional<IItemHandler> optional = LazyOptional.of(() -> new AutomationItemHandler(handler));

    private int cooldown;
    private int totalTime;
    private boolean isCrafting;

    public Old() {
        super(ModTiles.ELEMENTAL_COMBINATOR_TILE.get());
    }

    @Override
    public void tick() {
        if(world != null && !world.isRemote()) {
            if(this.cooldown > 0) {
                this.cooldown--;
            }
        }
    }
    // TODO: I think it can be smaller and more optimized
    public boolean combineElemental(PlayerEntity player) {
        if(world != null && !world.isRemote()) {
            if(this.world.canBlockSeeSky(this.pos.up())) {
                if (this.cooldown <= 0) {
                    LightningBoltEntity lightbolt = new LightningBoltEntity(EntityType.LIGHTNING_BOLT, world);
                    lightbolt.moveForced(Vector3d.copyCenteredHorizontally(this.pos.up()));
                    lightbolt.setEffectOnly(true);
                    ElementalCombination recipe = this.world.getRecipeManager().getRecipe(ModRecipes.ELEMENTAL_COMBINATION_TYPE, new RecipeWrapper(handler), this.world).orElse(null);
                    ItemStack result;
                    if (recipe != null) {
                        result = recipe.getCraftingResult(new RecipeWrapper(handler)); // Result
                        NonNullList<ItemStack> remainingItems = recipe.getRemainingItems(new RecipeWrapper(handler)); // A list of items with container item like buckets
                        if (!result.isEmpty()) {
                            if (handler.insertItem(0, result, true).isEmpty()) {
                                handler.insertItem(0, result, false);
                                for (int i = 1; i < handler.getSlots(); ++i) {
                                    handler.extractItem(i, 1, false);
                                    handler.insertItem(i, remainingItems.get(i), false); // inserting remaining items
                                }
                                // Trigger stuff
                                ModCriteriaTriggers.ITEM_COMBINED.trigger((ServerPlayerEntity) player, result, (ServerWorld) world, this.pos.getX(), this.pos.getY(), this.pos.getZ()); // Triggers advancement trigger
                                // Decoration stuff
                                world.addEntity(lightbolt);
                                world.playSound(null, pos, SoundEvents.BLOCK_BEACON_ACTIVATE, SoundCategory.BLOCKS, 100, 1);
                                ((ServerWorld) world).spawnParticle(ModParticles.COMBINATION_PARTICLE.get(), pos.getX() + 0.5, pos.up().getY(), pos.getZ() + 0.5, 200, 0, 0, 0, 6);
                                // Cooldown
                                this.cooldown += 30;
                                return true;
                            }
                        }
                    }
                } else player.sendStatusMessage(new TranslationTextComponent("block.elementalamulets.elemental_combinator.cooldown", this.getBlockState().getBlock().getTranslatedName(), this.cooldown / 20).mergeStyle(TextFormatting.RED), true);
            } else player.sendStatusMessage(new TranslationTextComponent("block.elementalamulets.elemental_combinator.no_sky", this.getBlockState().getBlock().getTranslatedName()).mergeStyle(TextFormatting.RED), true);
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
        compound.putInt("TotalTime", this.totalTime);
        compound.putBoolean("isCrafting", this.isCrafting);
        return super.write(compound);
    }

    @Override
    public void read(BlockState state, CompoundNBT nbt) {
        handler.deserializeNBT(nbt.getCompound("Contents"));
        this.cooldown = nbt.getInt("Cooldown");
        this.totalTime = nbt.getInt("TotalTime");
        this.isCrafting = nbt.getBoolean("isCrafting");
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
                markDirty();
            }
        };
    }

}*/
