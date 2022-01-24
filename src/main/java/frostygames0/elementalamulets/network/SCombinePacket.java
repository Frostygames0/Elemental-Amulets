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

import frostygames0.elementalamulets.blocks.entities.ElementalCombinatorBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.network.NetworkEvent;


import java.util.function.Supplier;

/**
 * @author Frostygames0
 * @date 08.10.2021 23:08
 */
public record SCombinePacket(BlockPos pos) {

    public SCombinePacket(FriendlyByteBuf buf) {
        this(buf.readBlockPos());
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeBlockPos(this.pos);
    }

    public static void handle(SCombinePacket packet, Supplier<NetworkEvent.Context> sup) {
        NetworkEvent.Context ctx = sup.get();
        ctx.enqueueWork(() -> {
            Level world = ctx.getSender().level;
            if (world.hasChunkAt(packet.pos)) {
                BlockEntity tile = world.getBlockEntity(packet.pos);
                if (tile instanceof ElementalCombinatorBlockEntity) {
                    ((ElementalCombinatorBlockEntity) tile).startCombination();
                }
            }
        });
        ctx.setPacketHandled(true);
    }
}
