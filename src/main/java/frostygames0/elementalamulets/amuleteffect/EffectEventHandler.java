package frostygames0.elementalamulets.amuleteffect;

import frostygames0.elementalamulets.ElementalAmulets;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = ElementalAmulets.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class EffectEventHandler {

    @SubscribeEvent
    public static void onJump(final LivingEvent.LivingJumpEvent event) {
        JumpAmuletEffect.onLivingJump(event);
    }
    @SubscribeEvent
    public static void onHurt(final LivingHurtEvent event) {
        JumpAmuletEffect.onLivingHurt(event);
        FireAmuletEffect.onLivingHurt(event);
    }
    @SubscribeEvent
    public static void onAttack(final LivingAttackEvent event) {
        JumpAmuletEffect.onLivingAttack(event);
        FireAmuletEffect.onLivingAttack(event);
    }

 }
