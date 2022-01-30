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

package frostygames0.elementalamulets.items;

import frostygames0.elementalamulets.ElementalAmulets;
import frostygames0.elementalamulets.items.amulets.AmuletItem;
import frostygames0.elementalamulets.util.NBTUtil;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Direction;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fml.server.ServerLifecycleHooks;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.apache.commons.lang3.tuple.ImmutableTriple;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.SlotTypePreset;
import top.theillusivec4.curios.api.type.capability.ICurio;
import top.theillusivec4.curios.api.type.capability.ICurioItem;
import top.theillusivec4.curios.api.type.util.ICuriosHelper;


import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;


/**
 * @author Frostygames0
 * @date 10.09.2021 23:51
 */
public class AmuletBeltItem extends Item implements ICurioItem {
    private static final String WEARER_UUID_TAG = ElementalAmulets.MOD_ID + ":wearer_UUID";

    public AmuletBeltItem(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable World pLevel, List<ITextComponent> pTooltip, ITooltipFlag pFlag) {
        pStack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(h -> {
            pTooltip.add(new TranslationTextComponent("item.elementalamulets.amulet_belt.contents").withStyle(TextFormatting.GOLD));
            for (int i = 0; i < h.getSlots(); i++) {
                ItemStack stack = h.getStackInSlot(i);
                if (!stack.isEmpty()) {
                    pTooltip.add(new StringTextComponent(i + 1 + ". ").withStyle(TextFormatting.YELLOW).append(stack.getDisplayName().copy().withStyle(TextFormatting.GRAY)));
                }
            }
        });
    }

    @Override
    public void curioTick(String identifier, int index, LivingEntity livingEntity, ItemStack stack) {
        if (livingEntity instanceof PlayerEntity) {
            stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(h -> {
                ICuriosHelper helper = CuriosApi.getCuriosHelper();
                for (int i = 0; i < h.getSlots(); i++) {
                    ItemStack amulet = h.getStackInSlot(i);
                    Item itemAmulet = amulet.getItem();
                    LazyOptional<ICurio> curio = helper.getCurio(amulet);
                    if (curio.isPresent() && itemAmulet instanceof AmuletItem) {
                        if (((AmuletItem) itemAmulet).usesCurioMethods()) {
                            if (!helper.findEquippedCurio(itemAmulet, livingEntity).isPresent()) { // Checks if there is amulet in main slot that is same as one in belt. Rule of priority
                                curio.orElseThrow(NullPointerException::new).curioTick(identifier, index, livingEntity);
                            }
                        }
                    }
                }
            });
            // If for some reason UUID of wearer is not correct, it will correct it
            UUID wearerUUID = NBTUtil.getUUID(stack, WEARER_UUID_TAG);
            UUID livingUUID = livingEntity.getUUID();
            if (!wearerUUID.equals(livingUUID)) {
                NBTUtil.putUUID(stack, WEARER_UUID_TAG, livingUUID);
            }
        }
    }

    @Override
    public void onUnequip(SlotContext slotContext, ItemStack newStack, ItemStack stack) {
        if (slotContext.getWearer() instanceof PlayerEntity) {
            if (!compareBelts(newStack, stack)) {
                stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(h -> {
                    ICuriosHelper helper = CuriosApi.getCuriosHelper();
                    for (int i = 0; i < h.getSlots(); i++) {
                        ItemStack amulet = h.getStackInSlot(i);
                        Item itemAmulet = amulet.getItem();
                        LazyOptional<ICurio> curio = helper.getCurio(amulet);
                        if (curio.isPresent() && itemAmulet instanceof AmuletItem) {
                            if (((AmuletItem) itemAmulet).usesCurioMethods()) {
                                curio.orElseThrow(NullPointerException::new).onUnequip(slotContext, newStack);
                            }
                        }
                    }
                });
            }
        }
    }

    @Override
    public void onEquip(SlotContext slotContext, ItemStack prevStack, ItemStack stack) {
        LivingEntity livingEntity = slotContext.getWearer();
        if (livingEntity instanceof PlayerEntity) {
            if (!compareBelts(prevStack, stack)) {
                stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(h -> {
                    ICuriosHelper helper = CuriosApi.getCuriosHelper();
                    for (int i = 0; i < h.getSlots(); i++) {
                        ItemStack amulet = h.getStackInSlot(i);
                        Item itemAmulet = amulet.getItem();
                        LazyOptional<ICurio> curio = helper.getCurio(amulet);
                        if (curio.isPresent() && itemAmulet instanceof AmuletItem) {
                            curio.orElseThrow(NullPointerException::new).onEquip(slotContext, stack);
                        }
                    }
                });
                // Creates tag with uuid of wearer
                NBTUtil.putUUID(stack, WEARER_UUID_TAG, livingEntity.getUUID());
            }
        }
    }

    @Override
    public boolean canEquipFromUse(SlotContext slotContext, ItemStack stack) {
        return true;
    }

    private static boolean compareBelts(ItemStack stack, ItemStack other) {
        Item amulet = stack.getItem();
        Item secondAmulet = other.getItem();
        if (!(amulet instanceof AmuletBeltItem) || !(secondAmulet instanceof AmuletBeltItem))
            return false;
        return ItemStack.tagMatches(stack, other);
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
                for (int i = 0; i < this.getSlots(); i++) {
                    if (getStackInSlot(i).getItem() == stack.getItem()) {
                        sameAmulet = true;
                        break;
                    }
                }
                return stack.getItem() instanceof AmuletItem && !sameAmulet ? super.insertItem(slot, stack, simulate) : stack;
            }

            @Nonnull
            @Override
            public ItemStack extractItem(int slot, int amount, boolean simulate) {
                ItemStack amulet = getStackInSlot(slot);
                MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
                if (server != null) {
                    UUID UUID = NBTUtil.getUUID(stack, WEARER_UUID_TAG);
                    PlayerEntity wearer = server.getPlayerList().getPlayer(UUID);
                    if (wearer != null) {
                        if (CuriosApi.getCuriosHelper().findEquippedCurio(stack.getItem(), wearer).map(ImmutableTriple::getRight).orElse(ItemStack.EMPTY) == stack) { // Workaround so It won't unEquip when not worn.
                            Item itemAmulet = amulet.getItem();
                            LazyOptional<ICurio> curio = CuriosApi.getCuriosHelper().getCurio(amulet);
                            if (curio.isPresent() && itemAmulet instanceof AmuletItem) {
                                if (((AmuletItem) itemAmulet).usesCurioMethods()) {
                                    curio.orElseThrow(NullPointerException::new).onUnequip(new SlotContext(SlotTypePreset.BELT.getIdentifier(), wearer), ItemStack.EMPTY);
                                }
                            }
                        }
                    } else {
                        return amulet;
                    }
                }
                return super.extractItem(slot, amount, simulate);
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
                if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
                    return optional.cast();
                }
                return LazyOptional.empty();
            }
        };
    }


    @Nullable
    @Override
    public CompoundNBT getShareTag(ItemStack stack) {
        CompoundNBT tag = stack.getOrCreateTag();
        stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(h -> {
            if (h instanceof ItemStackHandler) {
                tag.put("SyncContents", ((ItemStackHandler) h).serializeNBT());
            }
        });
        return tag;
    }

    @Override
    public void readShareTag(ItemStack stack, @Nullable CompoundNBT nbt) {
        super.readShareTag(stack, nbt);

        if (nbt != null) {
            stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(h -> {
                if (h instanceof ItemStackHandler) {
                    ((ItemStackHandler) h).deserializeNBT(nbt.getCompound("SyncContents"));
                }
            });
        }
    }
}
