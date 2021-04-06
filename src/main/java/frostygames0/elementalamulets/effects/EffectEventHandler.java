package frostygames0.elementalamulets.effects;

import frostygames0.elementalamulets.items.IJumpItem;
import frostygames0.elementalamulets.items.JumpAmulet;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.DamageSource;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;
import top.theillusivec4.curios.api.type.inventory.ICurioStacksHandler;

import java.util.Map;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
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
    /*
    @SubscribeEvent
    public static void onJump(LivingEvent.LivingJumpEvent event) {
        LivingEntity livingEntity = event.getEntityLiving();
        LazyOptional<ICuriosItemHandler> opt = CuriosApi.getCuriosHelper().getCuriosHandler(livingEntity);
        if(opt.isPresent()) {
            ICuriosItemHandler handler = opt.orElse(null);
            Map<String, ICurioStacksHandler> map = handler.getCurios();
            for(ICurioStacksHandler stackHandler : map.values()) {
                int slots = stackHandler.getSlots();
                for(int i = 0; i < slots; i++) {
                    ItemStack stack = stackHandler.getStacks().getStackInSlot(i);
                    Item item = stack.getItem();
                    if(item instanceof IJumpItem) {
                        IJumpItem amulet = (IJumpItem) item;
                        livingEntity.setMotion(livingEntity.getMotion().add(0, amulet.getJump(), 0));
                        stack.damageItem(5, livingEntity, livingEntity1 -> livingEntity1.sendBreakAnimation(EquipmentSlotType.CHEST));
                    }
                }
            }
        }
    }*/
    /*
    @SubscribeEvent
    public static void onDamage(LivingHurtEvent event) {
        if(event.getSource() == DamageSource.FALL) {
            LivingEntity livingEntity = event.getEntityLiving();
            LazyOptional<ICuriosItemHandler> opt = CuriosApi.getCuriosHelper().getCuriosHandler(livingEntity);
            if(opt.isPresent()) {
                ICuriosItemHandler handler = opt.orElse(null);
                Map<String, ICurioStacksHandler> map = handler.getCurios();
                for(ICurioStacksHandler stackHandler : map.values()) {
                    int slots = stackHandler.getSlots();
                    for(int i = 0; i < slots; i++) {
                        ItemStack stack = stackHandler.getStacks().getStackInSlot(i);
                        Item item = stack.getItem();
                        if(item instanceof IJumpItem) {
                            IJumpItem amulet = (IJumpItem) item;
                            float decrease = Math.max(0, amulet.getFallResist());
                            //If event is not canceled and fall resistance is more than 0 then
                            if(!event.isCanceled() && decrease > 0) {
                                float finalDamage = Math.max(0, (event.getAmount()-decrease)); // Final damage (Fall damage - fall resist of amulet)
                                /* If final damage is 0 then I can freely just cancel event
                                else just sets new fall damage
                                if(finalDamage == 0) {
                                    event.setCanceled(true);
                                } else {
                                    event.setAmount(finalDamage);
                                }
                            }
                        }
                    }
                }
            }
        }
    }*/
}
