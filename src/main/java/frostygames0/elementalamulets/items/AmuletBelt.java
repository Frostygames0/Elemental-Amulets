package frostygames0.elementalamulets.items;

import frostygames0.elementalamulets.blocks.containers.AmuletBeltContainer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fml.network.NetworkHooks;
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
public class AmuletBelt extends Item implements ICurioItem{
    public AmuletBelt(Properties properties) {
        super(properties);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
        if(!worldIn.isRemote()) {
            ItemStack stack = playerIn.getHeldItem(handIn);
            INamedContainerProvider provider = new INamedContainerProvider() {
                @Override
                public ITextComponent getDisplayName() {
                    return new TranslationTextComponent(AmuletBelt.this.getDefaultTranslationKey());
                }

                @Nullable
                @Override
                public Container createMenu(int p_createMenu_1_, PlayerInventory p_createMenu_2_, PlayerEntity p_createMenu_3_) {
                    return new AmuletBeltContainer(p_createMenu_1_, worldIn, stack, p_createMenu_2_, p_createMenu_3_);
                }
            };
            NetworkHooks.openGui((ServerPlayerEntity) playerIn, provider, buf -> buf.writeItemStack(stack));
        }
        return ActionResult.resultSuccess(playerIn.getHeldItem(handIn));
    }

    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundNBT nbt) {
        ItemStackHandler handler = new ItemStackHandler(5);
        LazyOptional<IItemHandler> optional = LazyOptional.of(() -> handler);
        return new ICapabilityProvider() {
            @Nonnull
            @Override
            public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
                return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.orEmpty(cap, optional.cast());
            }
        };
    }
}
