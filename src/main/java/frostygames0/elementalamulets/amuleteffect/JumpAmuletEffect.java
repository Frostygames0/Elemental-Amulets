package frostygames0.elementalamulets.amuleteffect;

import frostygames0.elementalamulets.items.amulets.interfaces.IJumpItem;
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

    private static float calculateFallResist(LivingEntity entity) {
        AtomicReference<Float> fallResist = new AtomicReference<>(0f);
        CuriosApi.getCuriosHelper().findEquippedCurio(itemStack -> itemStack.getItem() instanceof IJumpItem, entity).map(item -> item.getRight()).ifPresent(item -> {
            IJumpItem amulet = (IJumpItem) item.getItem();
            fallResist.updateAndGet(v -> v + Math.max(amulet.getFallResist(item), 0));
        });
        return fallResist.get();
    }

    static void onLivingHurt(LivingHurtEvent event) {
        if(event.getEntity().getEntityWorld().isRemote()) return;
        if(event.getSource() == DamageSource.FALL) {
            float fallResist = calculateFallResist(event.getEntityLiving());
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
    static void onLivingAttack(LivingAttackEvent event) {
        if(event.getEntity().getEntityWorld().isRemote()) return;
        if(event.getSource() == DamageSource.FALL) {
            float fallResist = calculateFallResist(event.getEntityLiving());
            if (!event.isCanceled() && fallResist > 0) {
                float finalDamage = Math.max(0, (event.getAmount() - fallResist));
                if (finalDamage == 0) {
                    event.setCanceled(true);
                }
            }
        }
    }

    static void onLivingJump(LivingEvent.LivingJumpEvent event) {
        if(event.getEntity().getEntityWorld().isRemote()) return;
        LivingEntity livingEntity = event.getEntityLiving();
        World world = livingEntity.getEntityWorld();
            if (world.getFluidState(livingEntity.getPosition()).isEmpty()) {
                ICuriosHelper helper = CuriosApi.getCuriosHelper();
                helper.findEquippedCurio(itemStack -> itemStack.getItem() instanceof IJumpItem, livingEntity).ifPresent(triple -> {
                    ItemStack stack = triple.getRight();
                    IJumpItem item = (IJumpItem) stack.getItem();
                    livingEntity.addVelocity(0, item.getJump(stack), 0);
                    livingEntity.velocityChanged = true;
                });
            }
    }

}
