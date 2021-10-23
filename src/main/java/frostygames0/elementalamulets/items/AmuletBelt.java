/*
 *     Copyright (c) 2021
 *
 *     This file is part of Elemental Amulets, a Minecraft Mod.
 *
 *     Elemental Amulets is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Elemental Amulets is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with Elemental Amulets.  If not, see <https://www.gnu.org/licenses/>.
 */

package frostygames0.elementalamulets.items;

import com.google.common.collect.ImmutableList;
import frostygames0.elementalamulets.init.ModItems;
import frostygames0.elementalamulets.items.amulets.AmuletItem;
import frostygames0.elementalamulets.util.AmuletHelper;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.Lazy;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurio;
import top.theillusivec4.curios.api.type.capability.ICurioItem;
import top.theillusivec4.curios.api.type.util.ICuriosHelper;


import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;


/**
 * @author Frostygames0
 * @date 10.09.2021 23:51
 */
public class AmuletBelt extends Item implements ICurioItem {

    public static final Lazy<ImmutableList<Item>> INCOMPATIBLE_AMULETS = Lazy.of(() -> ImmutableList.of(ModItems.AIR_AMULET.get(), ModItems.EARTH_AMULET.get(), ModItems.SPEED_AMULET.get(), ModItems.WATER_AMULET.get(), ModItems.INVISIBILITY_AMULET.get()));

    public AmuletBelt(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable World pLevel, List<ITextComponent> pTooltip, ITooltipFlag pFlag) {
        super.appendHoverText(pStack, pLevel, pTooltip, pFlag);

        if(Screen.hasShiftDown()) {
            pTooltip.add(new StringTextComponent("Can cause bugs(extremely rare):").withStyle(TextFormatting.GOLD));
            INCOMPATIBLE_AMULETS.get().forEach(amulet -> pTooltip.add(new StringTextComponent(" * ").append(amulet.getDescription()).withStyle(TextFormatting.YELLOW)));
        } else {
            pTooltip.add(new TranslationTextComponent("item.elementalamulets.amulet_belt.incompat").withStyle(TextFormatting.GRAY));
        }
    }

    @Override
    public void curioTick(String identifier, int index, LivingEntity livingEntity, ItemStack stack) {
        stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(h -> {
            ICuriosHelper helper = CuriosApi.getCuriosHelper();
            for (int i = 0; i < h.getSlots(); i++) {
                ItemStack amulet = h.getStackInSlot(i);
                Item itemAmulet = amulet.getItem();
                LazyOptional<ICurio> curio = helper.getCurio(amulet);
                if (curio.isPresent() && itemAmulet instanceof AmuletItem) {
                    if (((AmuletItem) itemAmulet).usesCurioMethods()) {
                        if (!AmuletHelper.isAmuletPresent(itemAmulet, livingEntity)) { // Checks if there is amulet in main slot that is same as one in belt. Rule of priority
                            curio.orElseThrow(NullPointerException::new).curioTick(identifier, index, livingEntity);
                        }
                    }
                }
            }
        });
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
                    if(((AmuletItem) itemAmulet).usesCurioMethods()) {
                        curio.orElseThrow(NullPointerException::new).onUnequip(slotContext, newStack);
                    }
                }
            }
        });
    }

    @Override
    public boolean canEquipFromUse(SlotContext slotContext, ItemStack stack) {
        return true;
    }

    /*------------------------------------------------------*/
    /*  Capability init                                    */
    /*-----------------------------------------------------*/

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
                return stack.getItem() instanceof AmuletItem  && !sameAmulet  /*&& !INCOMPATIBLE_AMULETS.get().contains(stack.getItem())*/ ? super.insertItem(slot, stack, simulate) : stack;
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


    @Override
    public boolean canSync(String identifier, int index, LivingEntity livingEntity, ItemStack stack) {
        return true;
    }

    @Nullable
    @Override
    public CompoundNBT writeSyncData(ItemStack stack) {
        IItemHandler handler = stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).orElseThrow(NullPointerException::new);
        if(handler instanceof ItemStackHandler) {
            return ((ItemStackHandler)handler).serializeNBT();
        }
        return new CompoundNBT();
    }

    @Override
    public void readSyncData(CompoundNBT compound, ItemStack stack) {
        stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(h -> {
            if(h instanceof ItemStackHandler) {
                ((ItemStackHandler)h).deserializeNBT(compound);
            }
        });
    }
    
    

}
