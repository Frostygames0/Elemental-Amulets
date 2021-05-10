package frostygames0.elementalamulets.blocks;

import frostygames0.elementalamulets.blocks.containers.ElementalCombinatorContainer;
import frostygames0.elementalamulets.blocks.tiles.ElementalCombinatorTile;
import net.minecraft.block.*;
import net.minecraft.block.material.PushReaction;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraftforge.items.CapabilityItemHandler;

import javax.annotation.Nullable;

@SuppressWarnings("deprecation")
public class ElementalCombinator extends Block {
    public ElementalCombinator(Properties properties) {
        super(properties);
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        if(worldIn.isRemote()) {
            return ActionResultType.SUCCESS;
        }
        TileEntity te = worldIn.getTileEntity(pos);
        if(te instanceof ElementalCombinatorTile) {
            ElementalCombinatorTile elementalCombinatorTile = (ElementalCombinatorTile) te;
            if(!player.isSneaking()) {
                INamedContainerProvider provider = new INamedContainerProvider() {

                    @Override
                    public ITextComponent getDisplayName() {
                        return new TranslationTextComponent("block.elementalamulets.elemental_combinator.guititle");
                    }

                    @Nullable
                    @Override
                    public Container createMenu(int p_createMenu_1_, PlayerInventory p_createMenu_2_, PlayerEntity p_createMenu_3_) {
                        return new ElementalCombinatorContainer(p_createMenu_1_, worldIn, pos, p_createMenu_2_, p_createMenu_3_);
                    }
                };
                NetworkHooks.openGui((ServerPlayerEntity) player, provider, elementalCombinatorTile.getPos());
            } else {
                elementalCombinatorTile.combineElemental(player);
            }
        } else {
            throw new IllegalStateException("TileEntity is not correct! Cannot open block's GUI!");
        }
        return ActionResultType.CONSUME;
    }

    @Override
    public PushReaction getPushReaction(BlockState state) {
        return PushReaction.BLOCK;
    }

    @Override
    public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
        if(!state.matchesBlock(newState.getBlock())) {
            TileEntity tileEntity = worldIn.getTileEntity(pos);
            if(tileEntity instanceof ElementalCombinatorTile) {
                tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(h -> {
                    for (int i = 0; i < h.getSlots(); i++) {
                        spawnAsEntity(worldIn, pos, h.getStackInSlot(i));
                    }
                });
            }
            super.onReplaced(state,worldIn,pos, newState, isMoving);
        }
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new ElementalCombinatorTile();
    }
}
