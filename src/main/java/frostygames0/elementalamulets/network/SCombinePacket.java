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

package frostygames0.elementalamulets.network;

import frostygames0.elementalamulets.blocks.tiles.ElementalCombinatorTile;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkEvent;


import java.util.function.Supplier;

/**
 * @author Frostygames0
 * @date 08.10.2021 23:08
 */
public class SCombinePacket {
    private final BlockPos pos;

    public SCombinePacket(BlockPos pos) {
        this.pos = pos;
    }

    public SCombinePacket(PacketBuffer buf) {
        this.pos = buf.readBlockPos();
    }

    public void toBytes(PacketBuffer buf) {
        buf.writeBlockPos(this.pos);
    }

    public void handle(Supplier<NetworkEvent.Context> sup) {
        NetworkEvent.Context ctx = sup.get();
        ctx.enqueueWork(() -> {
            World world = ctx.getSender().level;
            if(world != null) {
                if (world.isLoaded(pos)) {
                    TileEntity tile = world.getBlockEntity(pos);
                    if (tile instanceof ElementalCombinatorTile) {
                        ((ElementalCombinatorTile) tile).startCombination();
                    }
                }
            }
        });
        ctx.setPacketHandled(true);
    }
}
