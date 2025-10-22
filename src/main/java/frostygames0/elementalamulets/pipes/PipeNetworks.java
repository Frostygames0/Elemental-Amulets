package frostygames0.elementalamulets.pipes;

import com.mojang.serialization.Codec;
import frostygames0.elementalamulets.ElementalAmulets;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;
import net.neoforged.neoforge.event.level.LevelEvent;
import net.neoforged.neoforge.event.tick.LevelTickEvent;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PipeNetworks extends SavedData {
    public static final String FILE_PATH = ElementalAmulets.MOD_ID + "/pipe_networks";

    public static final Codec<PipeNetworks> CODEC = PipeNetwork.CODEC.listOf().xmap(PipeNetworks::new, PipeNetworks::getNetworks);

    private final List<PipeNetwork> networks = new ArrayList<>();
    private final Map<BlockPos, PipeNode> unloadedNodes = new HashMap<>();

    public PipeNetworks() {
        this(List.of());
    }

    private PipeNetworks(List<PipeNetwork> networks) {
        networks.forEach(network -> {
            this.networks.add(network);

            for (var node : network.nodes()) {
                unloadedNodes.put(node.getWorldPos(), node);
            }
        });
    }

    private List<PipeNetwork> getNetworks() {
        return networks;
    }

    public static PipeNetworks getOrInit(ServerLevel level) {
        return level.getDataStorage().computeIfAbsent(new Factory<>(PipeNetworks::new, PipeNetworks::load), FILE_PATH);
    }

    public static PipeNode startNewNetwork(ServerLevel serverLevel, BlockPos blockPos) {
        var node = new PipeNode(blockPos);

        var newNetwork = new PipeNetwork(node);
        onNetworkCreated(serverLevel, newNetwork);

        return node;
    }

    public static void unloadNode(ServerLevel level, PipeNode node) {
        getOrInit(level).unloadNode(node);
    }

    public void unloadNode(PipeNode node) {
        node.detach();
        unloadedNodes.put(node.getWorldPos(), node);
    }

    @Nullable
    public static PipeNode retrieveUnloadedNode(ServerLevel level, BlockPos worldPos) {
        return getOrInit(level).retrieveUnloadedNode(worldPos);
    }

    @Nullable
    public PipeNode retrieveUnloadedNode(BlockPos worldPos) {
        if (!unloadedNodes.containsKey(worldPos)) {
            return null;
        }

        return unloadedNodes.remove(worldPos);
    }

    public static void onNetworkDiscarded(ServerLevel serverLevel, PipeNetwork network) {
        getOrInit(serverLevel).onNetworkDiscarded(network);
    }

    public void onNetworkDiscarded(PipeNetwork pipeNetwork) {
        networks.remove(pipeNetwork);
    }

    public static void onNetworkCreated(ServerLevel serverLevel, PipeNetwork network) {
        getOrInit(serverLevel).onNetworkCreated(network);
    }

    public void onNetworkCreated(PipeNetwork pipeNetwork) {
        networks.add(pipeNetwork);
    }

    public static void onLevelTick(LevelTickEvent.Post event) {
        if (event.getLevel() instanceof ServerLevel serverLevel) {
            getOrInit(serverLevel).serverTick(serverLevel);
        }
    }

    public void serverTick(ServerLevel level) {

    }

    @Override
    public CompoundTag save(CompoundTag tag, HolderLookup.Provider registries) {
        tag.put("Networks", CODEC.encodeStart(registries.createSerializationContext(NbtOps.INSTANCE), this).getOrThrow());
        return tag;
    }

    public static PipeNetworks load(CompoundTag tag, HolderLookup.Provider registries) {
        return CODEC.parse(registries.createSerializationContext(NbtOps.INSTANCE), tag.get("Networks")).getPartialOrThrow();
    }

    @Override
    public boolean isDirty() {
        return true;
    }
}
