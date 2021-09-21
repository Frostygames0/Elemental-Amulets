package frostygames0.elementalamulets.client;

import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.util.InputMappings;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.fml.client.registry.ClientRegistry;

/**
 * @author Frostygames0
 * @date 21.09.2021 19:32
 */
public class ModKeyBindings {
    public static KeyBinding OPEN_AMULET_BELT = new KeyBinding("key.elementalamulets.open_belt_gui", KeyConflictContext.IN_GAME, InputMappings.UNKNOWN, "key.categories.inventory");

    public static void registerKeyBinds() {
        ClientRegistry.registerKeyBinding(OPEN_AMULET_BELT);
    }
}
