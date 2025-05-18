package frostygames0.elementalamulets.client.model;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import frostygames0.elementalamulets.ElementalAmulets;
import net.minecraft.Util;
import net.minecraft.client.data.models.model.TextureSlot;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.block.model.TextureSlots;
import net.minecraft.client.resources.model.*;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.context.ContextMap;
import net.neoforged.neoforge.client.model.ExtendedUnbakedModel;
import net.neoforged.neoforge.client.model.UnbakedModelLoader;

import java.util.HashMap;
import java.util.Map;

public class PipeConnectionsModelLoader implements UnbakedModelLoader<PipeConnectionsModelLoader.Unbaked> {
    public static final ResourceLocation ID = ElementalAmulets.id("pipe");

    private static final Map<Direction, ModelState> DIRECTION_TO_MODEL_STATE = Util.make(new HashMap<>(), map -> {
        map.put(Direction.NORTH, BlockModelRotation.X0_Y0);
        map.put(Direction.EAST, BlockModelRotation.X0_Y90);
        map.put(Direction.SOUTH, BlockModelRotation.X0_Y180);
        map.put(Direction.WEST, BlockModelRotation.X0_Y270);
        map.put(Direction.UP, BlockModelRotation.X270_Y0);
        map.put(Direction.DOWN, BlockModelRotation.X90_Y0);
    });

    @Override
    public Unbaked read(JsonObject jsonObject, JsonDeserializationContext deserializationContext) throws JsonParseException {
        var connectorModel = (UnbakedModel) deserializationContext.deserialize(GsonHelper.getAsJsonObject(jsonObject, "connector"), BlockModel.class);
        var shortenedConnectorModel = (UnbakedModel) deserializationContext.deserialize(GsonHelper.getAsJsonObject(jsonObject, "connector_short"), BlockModel.class);
        var rimModel = (UnbakedModel) deserializationContext.deserialize(GsonHelper.getAsJsonObject(jsonObject, "rim"), BlockModel.class);

        return new Unbaked(rimModel, connectorModel, shortenedConnectorModel);
    }

    public record Unbaked(UnbakedModel rimModel, UnbakedModel connectorModel,
                          UnbakedModel shortConnectorModel) implements ExtendedUnbakedModel {
        @Override
        public BakedModel bake(TextureSlots textures, ModelBaker baker, ModelState modelState, boolean useAmbientOcclusion, boolean usesBlockLight, ItemTransforms itemTransforms, ContextMap additionalProperties) {
            var directions = Direction.values();
            var size = directions.length;

            BakedModel[] rims = new BakedModel[size];
            BakedModel[] connectors = new BakedModel[size];
            BakedModel[] shortenedConnectors = new BakedModel[size];

            for (var direction : directions) {
                rims[direction.ordinal()] = UnbakedModel.bakeWithTopModelValues(rimModel, baker, DIRECTION_TO_MODEL_STATE.get(direction));
                connectors[direction.ordinal()] = UnbakedModel.bakeWithTopModelValues(connectorModel, baker, DIRECTION_TO_MODEL_STATE.get(direction));
                shortenedConnectors[direction.ordinal()] = UnbakedModel.bakeWithTopModelValues(shortConnectorModel, baker, DIRECTION_TO_MODEL_STATE.get(direction));
            }

            var particleSprite = baker.findSprite(UnbakedModel.getTopTextureSlots(connectorModel, baker.rootName()), TextureSlot.PARTICLE.getId());

            return new PipeConnectionsBakedModel(
                    useAmbientOcclusion,
                    usesBlockLight,
                    particleSprite,
                    rims,
                    shortenedConnectors,
                    connectors);
        }

        @Override
        public void resolveDependencies(Resolver resolver) {
            connectorModel.resolveDependencies(resolver);
            rimModel.resolveDependencies(resolver);
            shortConnectorModel.resolveDependencies(resolver);
        }
    }

}
