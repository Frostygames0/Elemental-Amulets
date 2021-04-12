package frostygames0.elementalamulets.amuleteffect;

import frostygames0.elementalamulets.items.interfaces.IJumpItem;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;
import top.theillusivec4.curios.api.type.inventory.ICurioStacksHandler;

import java.util.Map;

public class JumpAmuletEffect {

    // Calculates fall resist(Chooses bigger fall resistance of all amulets that are instances of IJumpItem)
    public static float calculateFallResist(LivingEvent event, LivingEntity entity) {
        LazyOptional<ICuriosItemHandler> opt = CuriosApi.getCuriosHelper().getCuriosHandler(entity);
        float fallResist = 0f;
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
                        float oldFallResist = fallResist;
                        fallResist += Math.max(0, Math.max(amulet.getFallResist(), oldFallResist));
                    }
                }
            }
        }
        return fallResist;
    }
    // This is to decrease(or cancel) fall damage
    public static void onLivingHurt(LivingHurtEvent event) {
        float fallResist = calculateFallResist(event, event.getEntityLiving());
        if(!event.isCanceled() && fallResist > 0) {
            float finalDamage = Math.max(0, (event.getAmount()-fallResist));
            if(finalDamage == 0) {
                event.setCanceled(true);
            } else {
                event.setAmount(finalDamage);
            }
        }
    }
    // This is to cancel damage animation
    public static void onLivingAttack(LivingAttackEvent event) {
        float fallResist = calculateFallResist(event, event.getEntityLiving());
        if(!event.isCanceled() && fallResist > 0) {
            float finalDamage = Math.max(0, (event.getAmount()-fallResist));
            if(finalDamage == 0) {
                event.setCanceled(true);
            }
        }
    }
    // This is make player jump higher
    public static void onLivingJump(LivingEvent.LivingJumpEvent event) {
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
    }
}
