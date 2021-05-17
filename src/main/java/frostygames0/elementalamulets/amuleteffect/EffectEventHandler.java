package frostygames0.elementalamulets.amuleteffect;

import frostygames0.elementalamulets.ElementalAmulets;
import net.minecraft.client.gui.toasts.AdvancementToast;
import net.minecraft.client.gui.toasts.IToast;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.event.entity.living.*;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;



@Mod.EventBusSubscriber(modid = ElementalAmulets.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class EffectEventHandler {

    @SubscribeEvent
    public static void onJump(LivingEvent.LivingJumpEvent event) {
        JumpAmuletEffect.onLivingJump(event);
    }
    @SubscribeEvent
    public static void onHurt(LivingHurtEvent event) {
        JumpAmuletEffect.onLivingHurt(event);
        FireAmuletEffect.onLivingHurt(event);
    }
    @SubscribeEvent
    public static void onAttack(LivingAttackEvent event) {
        JumpAmuletEffect.onLivingAttack(event);
        //FireAmuletEffect.onLivingAttack(event);
    }
 }
