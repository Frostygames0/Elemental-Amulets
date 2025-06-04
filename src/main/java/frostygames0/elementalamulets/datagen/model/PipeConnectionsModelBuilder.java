package frostygames0.elementalamulets.datagen.model;

import com.google.gson.JsonObject;
import frostygames0.elementalamulets.client.model.PipeConnectionsModelLoader;
import net.minecraft.client.data.models.model.ModelTemplate;
import net.minecraft.client.data.models.model.TextureMapping;
import net.neoforged.neoforge.client.model.generators.template.CustomLoaderBuilder;

public class PipeConnectionsModelBuilder extends CustomLoaderBuilder {
    private Inlined connector;
    private Inlined shortConnector;
    private Inlined rim;

    public PipeConnectionsModelBuilder() {
        super(PipeConnectionsModelLoader.ID, false);
    }

    public PipeConnectionsModelBuilder withConnectorModel(ModelTemplate template, TextureMapping textures) {
        connector = new Inlined(template, textures);
        return this;
    }

    public PipeConnectionsModelBuilder withShortConnectorModel(ModelTemplate template, TextureMapping textures) {
        shortConnector = new Inlined(template, textures);
        return this;
    }

    public PipeConnectionsModelBuilder withRimModel(ModelTemplate template, TextureMapping textures) {
        rim = new Inlined(template, textures);
        return this;
    }

    @Override
    protected CustomLoaderBuilder copyInternal() {
        var newBuilder = new PipeConnectionsModelBuilder();
        newBuilder.connector = connector;
        newBuilder.shortConnector = shortConnector;
        newBuilder.rim = rim;

        return newBuilder;
    }

    @Override
    public JsonObject toJson(JsonObject json) {
        serializeModelTemplate(connector, "connector", json);
        serializeModelTemplate(shortConnector, "connector_short", json);
        serializeModelTemplate(rim, "rim", json);
        return super.toJson(json);
    }

    private static void serializeModelTemplate(Inlined inlined, String fieldName, JsonObject jsonObject) {
        serializeNestedTemplate(inlined.template(), inlined.textures(), consumer -> jsonObject.add(fieldName, consumer));
    }

    private record Inlined(ModelTemplate template, TextureMapping textures) {
    }
}
