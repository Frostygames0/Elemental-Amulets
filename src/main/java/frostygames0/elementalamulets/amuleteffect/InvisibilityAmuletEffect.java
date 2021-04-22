package frostygames0.elementalamulets.amuleteffect;

import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

public class InvisibilityAmuletEffect {

    public static void unRender(LivingEvent.LivingVisibilityEvent event) {
        if(event.getEntityLiving() instanceof PlayerEntity) {
            event.modifyVisibility(0);
        }
    }

}
