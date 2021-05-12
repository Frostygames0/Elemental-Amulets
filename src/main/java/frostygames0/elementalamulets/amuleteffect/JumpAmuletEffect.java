package frostygames0.elementalamulets.amuleteffect;

import frostygames0.elementalamulets.items.AmuletItem;
import frostygames0.elementalamulets.items.interfaces.IJumpItem;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.type.util.ICuriosHelper;

import java.util.concurrent.atomic.AtomicReference;

public class JumpAmuletEffect {

    /* Calculates fall resist(Chooses bigger fall resistance of all amulets that are instances of IJumpItem)
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
    }*/
    /*
    public static float calculateFallResist(LivingEvent event, LivingEntity entity) {
        AtomicReference<Float> fallResist = new AtomicReference<>(0f);
        CuriosApi.getCuriosHelper().getCuriosHandler(entity).ifPresent(handler -> {
            handler.getCurios().forEach((id, stackHandler) -> {
                int slots = stackHandler.getSlots();
                for(int i = 0; i < slots; i++) {
                    ItemStack stack = stackHandler.getStacks().getStackInSlot(i);
                    Item item = stack.getItem();
                    if(item instanceof IJumpItem) {
                        IJumpItem amulet = (IJumpItem) item;
                        fallResist.updateAndGet(v -> (float) (v + Math.max(amulet.getFallResist(), 0)));
                    }
                }
            });
        });
        return fallResist.get();
    }*/

    public static float calculateFallResist(LivingEvent event, LivingEntity entity) {
        AtomicReference<Float> fallResist = new AtomicReference<>(0f);
        CuriosApi.getCuriosHelper().findEquippedCurio(itemStack -> itemStack.getItem() instanceof IJumpItem, entity).map(item -> item.getRight().getItem()).ifPresent(item -> {
            IJumpItem amulet = (IJumpItem) item;
            fallResist.updateAndGet(v -> (float) (v + Math.max(amulet.getFallResist(), 0)));
        });
        return fallResist.get();
    }

    // This is to decrease(or cancel) fall damage
    public static void onLivingHurt(LivingHurtEvent event) {
        if(event.getSource() == DamageSource.FALL) {
            float fallResist = calculateFallResist(event, event.getEntityLiving());
            if (!event.isCanceled() && fallResist > 0) {
                float finalDamage = Math.max(0, (event.getAmount() - fallResist));
                if (finalDamage == 0) {
                    event.setCanceled(true);
                } else {
                    event.setAmount(finalDamage);
                }
            }
        }
    }
    // This is to cancel damage animation
    public static void onLivingAttack(LivingAttackEvent event) {
        if(event.getSource() == DamageSource.FALL) {
            float fallResist = calculateFallResist(event, event.getEntityLiving());
            if (!event.isCanceled() && fallResist > 0) {
                float finalDamage = Math.max(0, (event.getAmount() - fallResist));
                if (finalDamage == 0) {
                    event.setCanceled(true);
                }
            }
        }
    }
    // This is make player jump higher
    /*public static void onLivingJump(LivingEvent.LivingJumpEvent event) {
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
    public static void onLivingJump(LivingEvent.LivingJumpEvent event) {
        LivingEntity livingEntity = event.getEntityLiving();
        ICuriosHelper helper = CuriosApi.getCuriosHelper();
        helper.getCuriosHandler(livingEntity).ifPresent(handler -> {
            handler.getCurios().forEach((id, stacksHandler) -> {
                int slots = stacksHandler.getSlots();
                for (int i = 0; i < slots; i++) {
                    ItemStack stack = stacksHandler.getStacks().getStackInSlot(i);
                    Item item = stack.getItem();
                    if (item instanceof IJumpItem) {
                        IJumpItem amulet = (IJumpItem) item;
                        livingEntity.setMotion(livingEntity.getMotion().add(0, amulet.getJump(), 0));
                        if(item instanceof IAmuletItem) {
                            IAmuletItem amulet1 = (IAmuletItem) item;
                            int finalI = i;
                            stack.damageItem(amulet1.getDamageOnUse(), livingEntity, ent -> helper.onBrokenCurio(id, finalI, ent));
                        }
                    }
                }
            });
        });
    }*/

    public static void onLivingJump(LivingEvent.LivingJumpEvent event) {
        LivingEntity livingEntity = event.getEntityLiving();
        World world = livingEntity.getEntityWorld();
            if (world.getFluidState(livingEntity.getPosition()).isEmpty()) {
                ICuriosHelper helper = CuriosApi.getCuriosHelper();
                helper.findEquippedCurio(itemStack -> itemStack.getItem() instanceof IJumpItem, livingEntity).ifPresent(triple -> {
                    ItemStack stack = triple.getRight();
                    IJumpItem item = (IJumpItem) stack.getItem();
                    livingEntity.setMotion(livingEntity.getMotion().add(0, item.getJump(), 0));
                    if (item instanceof AmuletItem) {
                        AmuletItem amulet1 = (AmuletItem) item;
                        stack.damageItem(amulet1.getDamageOnUse(), livingEntity, ent -> CuriosApi.getCuriosHelper().onBrokenCurio(triple.getLeft(), triple.getMiddle(), ent));
                    }
                });
            }
    }

}
