package frostygames0.elementalamulets.client;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.settings.KeyConflictContext;
import net.neoforged.neoforge.common.util.Lazy;
import org.lwjgl.glfw.GLFW;

import java.util.function.Supplier;

public final class ModKeyMappings {
    private ModKeyMappings() {
    }

    public static final Lazy<KeyMapping> SHOW_COMPOSITION =
            Lazy.of(() -> new KeyMapping(
                    "key.elementalamulets.show_composition",
                    KeyConflictContext.UNIVERSAL,
                    InputConstants.Type.KEYSYM,
                    InputConstants.KEY_LSHIFT,
                    "key.categories.inventory"));

    public static boolean isKeyDown(Supplier<KeyMapping> keyMapping) {
        return isKeyDown(keyMapping.get());
    }

    public static boolean isKeyDown(KeyMapping keyMapping) {
        if (keyMapping.isUnbound()) {
            return false;
        }

        boolean isDown = switch (keyMapping.getKey().getType()) {
            case KEYSYM ->
                    InputConstants.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), keyMapping.getKey().getValue());
            case MOUSE ->
                    GLFW.glfwGetMouseButton(Minecraft.getInstance().getWindow().getWindow(), keyMapping.getKey().getValue()) == GLFW.GLFW_PRESS;
            default -> false;
        };

        return isDown && keyMapping.getKeyConflictContext().isActive() && keyMapping.getKeyModifier().isActive(keyMapping.getKeyConflictContext());
    }

    @SubscribeEvent
    public static void onRegisterKeyMappings(RegisterKeyMappingsEvent event) {
        event.register(ModKeyMappings.SHOW_COMPOSITION.get());
    }
}
