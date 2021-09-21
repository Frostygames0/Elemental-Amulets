package frostygames0.elementalamulets.items;

import frostygames0.elementalamulets.items.amulets.AmuletItem;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import top.theillusivec4.curios.api.type.capability.ICurioItem;


import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author Frostygames0
 * @date 10.09.2021 23:51
 */
public class AmuletBelt extends Item implements ICurioItem {
    public AmuletBelt(Properties properties) {
        super(properties);
    }

    @Override
    public ActionResult<ItemStack> use(World worldIn, PlayerEntity playerIn, Hand handIn) {
        /*if(!worldIn.isClientSide()) {
            ItemStack stack = playerIn.getItemInHand(handIn);
            NetworkHooks.openGui((ServerPlayerEntity) playerIn, new SimpleNamedContainerProvider((id, playerInventory, player) -> new AmuletBeltContainer(id, playerInventory, stack), stack.getDisplayName()), buf -> buf.writeItem(stack));
        }*/
        return ActionResult.success(playerIn.getItemInHand(handIn));
    }

    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundNBT nbt) {
        ItemStackHandler handler = new ItemStackHandler(5) {
            @Nonnull
            @Override
            public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
                return stack.getItem() instanceof AmuletItem ? super.insertItem(slot, stack, simulate) : stack;
            }
        };
        LazyOptional<IItemHandler> optional = LazyOptional.of(() -> handler);

        return new ICapabilitySerializable<CompoundNBT>() {
            @Override
            public CompoundNBT serializeNBT() {
                return handler.serializeNBT();
            }

            @Override
            public void deserializeNBT(CompoundNBT nbt) {
                handler.deserializeNBT(nbt);
            }

            @Nonnull
            @Override
            public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
                if(cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
                    return optional.cast();
                }
                return LazyOptional.empty();
            }
        };
    }
}
