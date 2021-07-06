package frostygames0.elementalamulets.blocks;

import frostygames0.elementalamulets.blocks.tiles.ElementalCombinatorTile;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.PushReaction;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraftforge.items.CapabilityItemHandler;

import javax.annotation.Nullable;

@SuppressWarnings("deprecation")
public class ElementalCombinator extends Block {
    public static final BooleanProperty COMBINING = BooleanProperty.create("combining");
    public ElementalCombinator(Properties properties) {
        super(properties);
        this.setDefaultState(this.getDefaultState().with(COMBINING, false));
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
                NetworkHooks.openGui((ServerPlayerEntity) player, elementalCombinatorTile, elementalCombinatorTile.getPos());
            } else {
                elementalCombinatorTile.startCombination();
            }
        } else {
            throw new IllegalStateException("Tile Entity is not correct! Cannot do any action!");
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

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(COMBINING);
    }
}
