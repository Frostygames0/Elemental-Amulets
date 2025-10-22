package frostygames0.elementalamulets.client.renderer;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.Util;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.TriState;

import java.util.function.Function;

public class ModRenderTypes {
    private static final RenderStateShard.LayeringStateShard VIEW_OFFSET_Z_LAYERING_2 = new RenderStateShard.LayeringStateShard(
            "view_offset_z_layering_0_8", () -> {
        var matrix4fstack = RenderSystem.getModelViewStack();
        matrix4fstack.pushMatrix();
        RenderSystem.getProjectionType().applyLayeringTransform(matrix4fstack, 2f);
    }, () -> {
        var matrix4fstack = RenderSystem.getModelViewStack();
        matrix4fstack.popMatrix();
    }
    );

    private static final Function<ResourceLocation, RenderType> ENTITY_SOLID_Z_OFFSET_2 = Util.memoize(
            resourceLocation -> RenderType.create(
                    "elementalamulets_entity_solid_z_offset_0_8",
                    DefaultVertexFormat.NEW_ENTITY,
                    VertexFormat.Mode.QUADS,
                    RenderType.TRANSIENT_BUFFER_SIZE,
                    true,
                    false,
                    RenderType.CompositeState.builder()
                            .setShaderState(RenderStateShard.RENDERTYPE_ENTITY_SOLID_SHADER)
                            .setTextureState(new RenderStateShard.TextureStateShard(resourceLocation, TriState.FALSE, false))
                            .setLightmapState(RenderStateShard.LIGHTMAP)
                            .setOverlayState(RenderStateShard.OVERLAY)
                            .setLayeringState(VIEW_OFFSET_Z_LAYERING_2)
                            .createCompositeState(true)
            )
    );

    private static final Function<ResourceLocation, RenderType> ENTITY_TRANSLUCENT_Z_OFFSET_2 = Util.memoize(
            resourceLocation -> RenderType.create(
                    "elementalamulets_entity_solid_z_offset_0_8",
                    DefaultVertexFormat.NEW_ENTITY,
                    VertexFormat.Mode.QUADS,
                    RenderType.TRANSIENT_BUFFER_SIZE,
                    true,
                    false,
                    RenderType.CompositeState.builder()
                            .setShaderState(RenderStateShard.RENDERTYPE_ENTITY_SOLID_SHADER)
                            .setTextureState(new RenderStateShard.TextureStateShard(resourceLocation, TriState.FALSE, false))
                            .setTransparencyState(RenderStateShard.TRANSLUCENT_TRANSPARENCY)
                            .setLightmapState(RenderStateShard.LIGHTMAP)
                            .setOverlayState(RenderStateShard.OVERLAY)
                            .setLayeringState(VIEW_OFFSET_Z_LAYERING_2)
                            .createCompositeState(true)
            )
    );

    public static RenderType entitySolidZOffset2(ResourceLocation texture) {
        return ENTITY_SOLID_Z_OFFSET_2.apply(texture);
    }

    public static RenderType entityTranslucentZOffset2(ResourceLocation texture) {
        return ENTITY_TRANSLUCENT_Z_OFFSET_2.apply(texture);
    }
}
