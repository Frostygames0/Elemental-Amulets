package frostygames0.elementalamulets;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = ElementalAmulets.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientEvents {
    @SubscribeEvent
    public static void textureStitch(final TextureStitchEvent.Pre event) {
        event.addSprite(new ResourceLocation(ElementalAmulets.MOD_ID, "item/necklace_slot"));
    }
}
