package frostygames0.elementalamulets.network.debug;

import frostygames0.elementalamulets.block.entity.pipe.PipeNetwork;
import frostygames0.elementalamulets.network.PacketHelper;
import frostygames0.elementalamulets.util.BlockFace;
import net.minecraft.commands.Commands;
import net.neoforged.fml.loading.FMLEnvironment;

import java.util.List;

public class ModDebugPackets {
    public static void sendPipeNetworkDebugInfo(BlockFace startingFace, List<PipeNetwork.TransferTarget> targets) {
        if (FMLEnvironment.production) {
        }

//        PacketHelper.sendToPlayersIfHavePermissions(Commands.LEVEL_GAMEMASTERS, new DebugPipeNetworkPayload(new DebugPipeNetworkPayload.PipeNetworkInfo(false, startingFace.pos(), targets.stream().map(t -> t.getSecond().pos()).toList())));
    }

    public static void sendPipeNetworkRemoved(BlockFace startingFace) {
        PacketHelper.sendToPlayersIfHavePermissions(Commands.LEVEL_GAMEMASTERS, new DebugPipeNetworkPayload(new DebugPipeNetworkPayload.PipeNetworkInfo(false, startingFace.pos(), List.of())));
    }

    public static void sendPipeNetworkDebugInfo(BlockFace startingFace) {
        sendPipeNetworkDebugInfo(startingFace, List.of());
    }
}
