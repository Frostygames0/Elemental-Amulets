package frostygames0.elementalamulets.amuleteffect;

import net.minecraftforge.event.entity.living.*;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;



@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
/*
 * This event handler is for custom amulet effects(not potion effects)
 */
public class EffectEventHandler {

    @SubscribeEvent
    public static void onJump(LivingEvent.LivingJumpEvent event) {
        JumpAmuletEffect.onLivingJump(event);
    }
    @SubscribeEvent
    public static void onHurt(LivingHurtEvent event) {
        JumpAmuletEffect.onLivingHurt(event);
    }
    @SubscribeEvent
    public static void onAttack(LivingAttackEvent event) {
        JumpAmuletEffect.onLivingAttack(event);
    }
 }
