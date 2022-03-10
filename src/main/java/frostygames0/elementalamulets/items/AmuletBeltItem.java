/*
 *  Copyright (c) 2021-2022
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
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.server.ServerLifecycleHooks;
import org.jetbrains.annotations.NotNull;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.SlotResult;
import top.theillusivec4.curios.api.SlotTypePreset;
import top.theillusivec4.curios.api.type.capability.ICurio;
import top.theillusivec4.curios.api.type.capability.ICurioItem;
import top.theillusivec4.curios.api.type.util.ICuriosHelper;


import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;

public class AmuletBeltItem extends Item implements ICurioItem {
    private static final String WEARER_UUID_TAG = ElementalAmulets.MOD_ID + ":wearer_UUID";

    public static final int HANDLER_SIZE = 5;

    public AmuletBeltItem(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltip, TooltipFlag pFlag) {
        pStack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(h -> {
            pTooltip.add(new TranslatableComponent("item.elementalamulets.amulet_belt.contents").withStyle(ChatFormatting.GOLD));
            for (int i = 0; i < h.getSlots(); i++) {
                ItemStack stack = h.getStackInSlot(i);
                if (!stack.isEmpty()) {
                    pTooltip.add(new TextComponent(i + 1 + ". ").withStyle(ChatFormatting.YELLOW).append(stack.getDisplayName().copy().withStyle(ChatFormatting.GRAY)));
                }
            }
        });
        pTooltip.add(new TranslatableComponent("item.elementalamulets.amulet_belt.tooltip").withStyle(ChatFormatting.GRAY));
        pTooltip.add(new TranslatableComponent("item.elementalamulets.amulet_belt." + (Screen.hasShiftDown() ? "instability" : "collapsed_warn")).withStyle(ChatFormatting.RED));
    }

    @Override
    public void curioTick(SlotContext ctx, ItemStack stack) {
        if (ctx.entity() instanceof Player player) {
            stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(h -> {
                ICuriosHelper helper = CuriosApi.getCuriosHelper();
                for (int i = 0; i < h.getSlots(); i++) {
                    ItemStack amulet = h.getStackInSlot(i);
                    Item itemAmulet = amulet.getItem();
                    LazyOptional<ICurio> curio = helper.getCurio(amulet);
                    if (curio.isPresent() && itemAmulet instanceof AmuletItem) {
                        if (((AmuletItem) itemAmulet).usesCurioMethods()) {
                            if (helper.findFirstCurio(player, itemAmulet).isEmpty()) { // Checks if there is amulet in main slot that is same as one in belt. Rule of priority
                                curio.orElseThrow(NullPointerException::new).curioTick(new SlotContext(ctx.identifier(), player, ctx.index(), ctx.cosmetic(), false));
                            }
                        }
                    }
                }
            });
            // If for some reason UUID of wearer is not correct, it will correct it
            UUID wearerUUID = NBTUtil.getUUID(stack, WEARER_UUID_TAG);
            UUID livingUUID = player.getUUID();
            if (!wearerUUID.equals(livingUUID)) {
                NBTUtil.putUUID(stack, WEARER_UUID_TAG, livingUUID);
            }
        }
    }

    @Override
    public void onUnequip(SlotContext slotContext, ItemStack newStack, ItemStack stack) {
        if (slotContext.entity() instanceof Player) {
            if (notSame(newStack, stack)) {
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
        if (slotContext.entity() instanceof Player player) {
            if (notSame(prevStack, stack)) {
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
                NBTUtil.putUUID(stack, WEARER_UUID_TAG, player.getUUID());
            }
        }
    }

    @Override
    public boolean canEquipFromUse(SlotContext slotContext, ItemStack stack) {
        return this.canEquip(slotContext, stack);
    }

    @Override
    public boolean canEquip(SlotContext slotContext, ItemStack stack) {
        return CuriosApi.getCuriosHelper().findFirstCurio(slotContext.entity(), this).isEmpty();
    }

    public static boolean notSame(ItemStack stack, ItemStack other) {
        Item amulet = stack.getItem();
        Item secondAmulet = other.getItem();
        if (!(amulet instanceof AmuletBeltItem) || !(secondAmulet instanceof AmuletBeltItem))
            return true;
        return !ItemStack.tagMatches(stack, other);
    }

    /*------------------------------------------------------*/
    /*  Capability init                                    */
    /*-----------------------------------------------------*/

    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag nbt) {
        ItemStackHandler handler = new ItemStackHandler(5) {
            @Override
            public boolean isItemValid(int slot, @NotNull ItemStack stack) {
                boolean sameAmulet = false; // Checks if there is same amulet already in belt
                for (int i = 0; i < this.getSlots(); i++) {
                    if (getStackInSlot(i).getItem() == stack.getItem()) {
                        sameAmulet = true;
                        break;
                    }
                }
                return stack.getItem() instanceof AmuletItem && !sameAmulet;
            }

            @Nonnull
            @Override
            public ItemStack extractItem(int slot, int amount, boolean simulate) {
                if (!simulate) {
                    ItemStack amulet = getStackInSlot(slot);
                    MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
                    if (server != null) {
                        UUID UUID = NBTUtil.getUUID(stack, WEARER_UUID_TAG);
                        Player wearer = server.getPlayerList().getPlayer(UUID);
                        if (wearer != null) {
                            if (CuriosApi.getCuriosHelper().findFirstCurio(wearer, stack.getItem()).map(SlotResult::stack).orElse(ItemStack.EMPTY) == stack) { // Workaround so It won't unEquip when not worn.
                                Item itemAmulet = amulet.getItem();
                                LazyOptional<ICurio> curio = CuriosApi.getCuriosHelper().getCurio(amulet);
                                if (curio.isPresent() && itemAmulet instanceof AmuletItem) {
                                    if (((AmuletItem) itemAmulet).usesCurioMethods()) {
                                        curio.orElseThrow(NullPointerException::new).onUnequip(new SlotContext(SlotTypePreset.BELT.getIdentifier(), wearer, 0, false, false), ItemStack.EMPTY);
                                    }
                                }
                            }
                        } else {
                            return amulet;
                        }
                    }
                }
                return super.extractItem(slot, amount, simulate);
            }

            @Override
            public int getSlotLimit(int slot) {
                return 1;
            }
        };
        LazyOptional<IItemHandler> optional = LazyOptional.of(() -> handler);

        return new ICapabilitySerializable<CompoundTag>() {
            @Override
            public CompoundTag serializeNBT() {
                return handler.serializeNBT();
            }

            @Override
            public void deserializeNBT(CompoundTag nbt) {
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
    public CompoundTag getShareTag(ItemStack stack) {
        CompoundTag tag = stack.getOrCreateTag();
        stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(h -> {
            if (h instanceof ItemStackHandler) {
                tag.put("SyncContents", ((ItemStackHandler) h).serializeNBT());
            }
        });
        return tag;
    }

    @Override
    public void readShareTag(ItemStack stack, @Nullable CompoundTag nbt) {
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
