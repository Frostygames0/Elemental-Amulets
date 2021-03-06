/*
 *  Copyright (c) 2021-2022
 *
 *     This file is part of Elemental Amulets, a Minecraft Mod.
 *
 *     Elemental Amulets is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Lesser General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Elemental Amulets is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Lesser General Public License for more details.
 *
 *     You should have received a copy of the GNU Lesser General Public License
 *     along with Elemental Amulets.  If not, see <https://www.gnu.org/licenses/>.
 */

package frostygames0.elementalamulets.client;

import com.mojang.blaze3d.platform.InputConstants;
import frostygames0.elementalamulets.ElementalAmulets;
import frostygames0.elementalamulets.network.ModNetworkHandler;
import frostygames0.elementalamulets.network.SOpenAmuletBeltGUIPacket;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.ClientRegistry;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.glfw.GLFW;

@Mod.EventBusSubscriber(modid = ElementalAmulets.MOD_ID, value = Dist.CLIENT)
public class ModKeyBindings {
    public static final KeyMapping OPEN_AMULET_BELT = new KeyMapping("key.elementalamulets.open_belt_gui", KeyConflictContext.IN_GAME, InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_I, "key.categories.elementalamulets");

    public static void registerKeyBinds() {
        ClientRegistry.registerKeyBinding(OPEN_AMULET_BELT);
    }

    @SubscribeEvent
    public static void onClientTick(final TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.START) {
            if (OPEN_AMULET_BELT.isDown()) {
                ModNetworkHandler.sendToServer(new SOpenAmuletBeltGUIPacket());
            }
        }
    }
}
