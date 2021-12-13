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

package frostygames0.elementalamulets.network;

import frostygames0.elementalamulets.blocks.containers.AmuletBeltContainer;
import frostygames0.elementalamulets.init.ModItems;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.SimpleNamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.NetworkHooks;
import top.theillusivec4.curios.api.CuriosApi;


import java.util.function.Supplier;

/**
 * @author Frostygames0
 * @date 21.09.2021 22:06
 */
public class SOpenAmuletBeltGUIPacket {
    public static void handle(SOpenAmuletBeltGUIPacket msg, Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context ctx = supplier.get();
        ctx.enqueueWork(() -> {
            ServerPlayerEntity sender = ctx.getSender();
            ItemStack stack = CuriosApi.getCuriosHelper().findEquippedCurio(ModItems.AMULET_BELT.get(), sender).map(triple -> triple.right).orElse(ItemStack.EMPTY);
            if (!stack.isEmpty()) {
                NetworkHooks.openGui(sender, new SimpleNamedContainerProvider((id, playerInventory, player) -> new AmuletBeltContainer(id, playerInventory, stack), new TranslationTextComponent(stack.getDescriptionId())), buf -> buf.writeItem(stack));
            }
        });
        ctx.setPacketHandled(true);
    }
}
