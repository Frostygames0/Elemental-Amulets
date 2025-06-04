package frostygames0.elementalamulets.network.debug;

import frostygames0.elementalamulets.block.entity.pipe.source.FlowSource;
import frostygames0.elementalamulets.network.PacketHelper;
import frostygames0.elementalamulets.util.BlockFace;
import frostygames0.elementalamulets.util.MutableTriple;
import net.minecraft.commands.Commands;
import net.neoforged.fml.loading.FMLEnvironment;

import java.util.List;

public class ModDebugPackets {
    public static void sendPipeNetworkDebugInfo(BlockFace startingFace, List<MutableTriple<Integer, BlockFace, FlowSource>> targets) {
        if (FMLEnvironment.production) {
            return;
        }

        PacketHelper.sendToPlayersIfHavePermissions(Commands.LEVEL_GAMEMASTERS, new DebugPipeNetworkPayload(new DebugPipeNetworkPayload.PipeNetworkInfo(false, startingFace.pos(), targets.stream().map(t -> t.getSecond().pos()).toList())));
    }

    public static void sendPipeNetworkDebugInfo(BlockFace startingFace) {
        sendPipeNetworkDebugInfo(startingFace, List.of());
    }
}
