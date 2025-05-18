package frostygames0.elementalamulets.client.model;

import frostygames0.elementalamulets.block.entity.pipe.ElementalPipeBlockEntity;
import frostygames0.elementalamulets.block.pipe.ElementalPipeBlock;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.ChunkRenderTypeSet;
import net.neoforged.neoforge.client.model.IDynamicBakedModel;
import net.neoforged.neoforge.client.model.data.ModelData;
import net.neoforged.neoforge.client.model.data.ModelProperty;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class PipeConnectionsBakedModel implements IDynamicBakedModel {
    private final boolean usesAO;
    private final boolean usesBlockLight;
    private final TextureAtlasSprite particle;

    private final BakedModel[] rims;
    private final BakedModel[] shortenedConnections;
    private final BakedModel[] connections;

    public PipeConnectionsBakedModel(boolean usesAO, boolean usesBlockLight, TextureAtlasSprite particle, BakedModel[] rims, BakedModel[] shortenedConnections, BakedModel[] connections) {
        this.usesAO = usesAO;
        this.usesBlockLight = usesBlockLight;
        this.particle = particle;

        this.rims = rims;
        this.shortenedConnections = shortenedConnections;
        this.connections = connections;
    }

    @Override
    public ModelData getModelData(BlockAndTintGetter level, BlockPos pos, BlockState state, ModelData modelData1) {
        var modelData = new PipeConnectionsBakedModel.PipeModelData();
        for (var direction : Direction.values()) {
            var property = state.getValue(ElementalPipeBlock.PROPERTY_BY_DIRECTION.get(direction));
            if (property) {
                modelData.addConnection(direction, ElementalPipeBlockEntity.ConnectionType.NORMAL);
            }
        }

        return ModelData.builder().with(PipeModelData.PROPERTY, modelData).build();
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState blockState, @Nullable Direction direction, RandomSource randomSource, ModelData modelData, @Nullable RenderType renderType) {
        var quads = new ArrayList<BakedQuad>();

        if (modelData.has(PipeModelData.PROPERTY)) {
            addRims(quads, modelData.get(PipeModelData.PROPERTY), blockState, direction, randomSource, modelData, renderType);
        }

        return quads;
    }

    @Override
    public ChunkRenderTypeSet getRenderTypes(BlockState state, RandomSource rand, ModelData data) {
        var renderTypeSets = new LinkedList<ChunkRenderTypeSet>();
        for (int i = 0; i < 6; i++) {
            renderTypeSets.add(connections[i].getRenderTypes(state, rand, data));
            renderTypeSets.add(shortenedConnections[i].getRenderTypes(state, rand, data));
            renderTypeSets.add(rims[i].getRenderTypes(state, rand, data));
        }

        return ChunkRenderTypeSet.union(renderTypeSets);
    }

    private void addRims(List<BakedQuad> quads, PipeModelData pipeModelData, @Nullable BlockState blockState, @Nullable Direction direction, RandomSource randomSource, ModelData modelData, @Nullable RenderType renderType) {
        for (int i = 0; i < 6; i++) {
            var type = pipeModelData.getConnection(Direction.from3DDataValue(i));
            if (type == ElementalPipeBlockEntity.ConnectionType.NONE) {
                continue;
            }

            if (type == ElementalPipeBlockEntity.ConnectionType.RIM) {
                quads.addAll(rims[i].getQuads(blockState, direction, randomSource, modelData, renderType));
                quads.addAll(shortenedConnections[i].getQuads(blockState, direction, randomSource, modelData, renderType));
            } else {
                quads.addAll(connections[i].getQuads(blockState, direction, randomSource, modelData, renderType));
            }
        }
    }

    @Override
    public boolean isGui3d() {
        return true;
    }

    @Override
    public boolean useAmbientOcclusion() {
        return usesAO;
    }

    @Override
    public boolean usesBlockLight() {
        return usesBlockLight;
    }

    @Override
    public TextureAtlasSprite getParticleIcon() {
        return particle;
    }

    @Override
    public ItemTransforms getTransforms() {
        return ItemTransforms.NO_TRANSFORMS;
    }

    public final static class PipeModelData {
        public static final ModelProperty<PipeModelData> PROPERTY = new ModelProperty<>();

        private final ElementalPipeBlockEntity.ConnectionType[] connections;

        public PipeModelData() {
            connections = new ElementalPipeBlockEntity.ConnectionType[6];
            Arrays.fill(connections, ElementalPipeBlockEntity.ConnectionType.NONE);
        }

        public ElementalPipeBlockEntity.ConnectionType getConnection(Direction direction) {
            return connections[direction.get3DDataValue()];
        }

        public void addConnection(Direction direction, ElementalPipeBlockEntity.ConnectionType type) {
            connections[direction.get3DDataValue()] = type;
        }
    }

}
