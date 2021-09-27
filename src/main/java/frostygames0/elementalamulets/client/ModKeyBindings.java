package frostygames0.elementalamulets.client;

import frostygames0.elementalamulets.ElementalAmulets;
import frostygames0.elementalamulets.network.ModNetworking;
import frostygames0.elementalamulets.network.SOpenAmuletBeltGUIPacket;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.util.InputMappings;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.glfw.GLFW;

/**
 * @author Frostygames0
 * @date 21.09.2021 19:32
 */
@Mod.EventBusSubscriber(modid = ElementalAmulets.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class ModKeyBindings {
    public static KeyBinding OPEN_AMULET_BELT = new KeyBinding("key.elementalamulets.open_belt_gui", KeyConflictContext.IN_GAME, InputMappings.Type.KEYSYM, GLFW.GLFW_KEY_I, "key.categories.inventory");

    public static void registerKeyBinds() {
        ClientRegistry.registerKeyBinding(OPEN_AMULET_BELT);
    }

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if(event.phase == TickEvent.Phase.START) {
            if(OPEN_AMULET_BELT.isDown()) {
                ModNetworking.INSTANCE.sendToServer(new SOpenAmuletBeltGUIPacket());
            }
        }
    }
}
