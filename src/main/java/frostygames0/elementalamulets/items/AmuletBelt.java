package frostygames0.elementalamulets.items;

import frostygames0.elementalamulets.items.amulets.AmuletItem;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.apache.commons.lang3.tuple.ImmutableTriple;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurio;
import top.theillusivec4.curios.api.type.capability.ICurioItem;
import top.theillusivec4.curios.api.type.util.ICuriosHelper;


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
    public void curioTick(String identifier, int index, LivingEntity livingEntity, ItemStack stack) {
        if(!livingEntity.level.isClientSide()) {
            stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(h -> {
                ICuriosHelper helper = CuriosApi.getCuriosHelper();
                for (int i = 0; i < h.getSlots(); i++) {
                    ItemStack amulet = h.getStackInSlot(i);
                    Item itemAmulet = amulet.getItem();
                    LazyOptional<ICurio> curio = helper.getCurio(amulet);
                    if (curio.isPresent() && itemAmulet instanceof AmuletItem) {
                        if (!((AmuletItem) itemAmulet).hasSpecialEffect()) {
                            if (helper.findEquippedCurio(itemAmulet, livingEntity).map(ImmutableTriple::getRight).orElse(ItemStack.EMPTY).isEmpty()) { // Checks if there is amulet in main slot that is same as one in belt. Rule of priority
                                curio.orElseThrow(NullPointerException::new).curioTick(identifier, index, livingEntity);
                            }
                        }
                    }
                }
            });
        }
    }

    @Override
    public void onUnequip(SlotContext slotContext, ItemStack newStack, ItemStack stack) {
        stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(h -> {
            ICuriosHelper helper = CuriosApi.getCuriosHelper();
            for(int i = 0; i < h.getSlots(); i++) {
                ItemStack amulet = h.getStackInSlot(i);
                Item itemAmulet = amulet.getItem();
                LazyOptional<ICurio> curio = helper.getCurio(amulet);
                if(curio.isPresent() && itemAmulet instanceof AmuletItem) {
                    if(!((AmuletItem)itemAmulet).hasSpecialEffect()) {
                        if(helper.findEquippedCurio(itemAmulet, slotContext.getWearer()).map(ImmutableTriple::getRight).orElse(ItemStack.EMPTY).isEmpty()) { // Checks if there is amulet in main slot that is same as one in belt. Rule of priority
                            curio.orElseThrow(NullPointerException::new).onUnequip(slotContext, newStack);
                        }
                    }
                }
            }
        });
    }

    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundNBT nbt) {
        ItemStackHandler handler = new ItemStackHandler(5) {
            @Nonnull
            @Override
            public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
                boolean sameAmulet = false; // Checks if there is same amulet already in belt
                for(int i = 0; i < this.getSlots(); i++) {
                    if(getStackInSlot(i).getItem() == stack.getItem()) {
                        sameAmulet = true;
                        break;
                    }
                }
                return stack.getItem() instanceof AmuletItem  && !sameAmulet ? super.insertItem(slot, stack, simulate) : stack;
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
