package frostygames0.elementalamulets.amuleteffect;

import frostygames0.elementalamulets.items.amulets.JumpAmulet;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import top.theillusivec4.curios.api.CuriosApi;

public class JumpAmuletEffect {

    static void onLivingHurt(LivingHurtEvent event) {
        LivingEntity entity = event.getEntityLiving();
        if(entity.world.isRemote()) return;
        if(event.getSource() == DamageSource.FALL) {
            CuriosApi.getCuriosHelper().findEquippedCurio(item -> item.getItem() instanceof JumpAmulet, entity).ifPresent((triple) -> {
                float fallResist = ((JumpAmulet) triple.getRight().getItem()).getJump(triple.getRight());
                if (!event.isCanceled() && fallResist > 0) {
                    float finalDamage = Math.max(0, (event.getAmount() - fallResist));
                    if (finalDamage == 0) {
                        event.setCanceled(true);
                    } else {
                        event.setAmount(finalDamage);
                    }
                }
            });
        }
    }
    static void onLivingAttack(LivingAttackEvent event) {
        LivingEntity entity = event.getEntityLiving();
        if(entity.world.isRemote()) return;
        if(event.getSource() == DamageSource.FALL) {
            CuriosApi.getCuriosHelper().findEquippedCurio(item -> item.getItem() instanceof JumpAmulet, entity).ifPresent((triple) -> {
                float fallResist = ((JumpAmulet) triple.getRight().getItem()).getJump(triple.getRight());
                if (!event.isCanceled() && fallResist > 0) {
                    float finalDamage = Math.max(0, (event.getAmount() - fallResist));
                    if (finalDamage == 0) {
                        event.setCanceled(true);
                    }
                }
            });
        }
    }

    static void onLivingJump(LivingEvent.LivingJumpEvent event) {
        LivingEntity livingEntity = event.getEntityLiving();
        World world = livingEntity.getEntityWorld();
        if(world.isRemote) return;
            if (world.getFluidState(livingEntity.getPosition()).isEmpty()) {
                CuriosApi.getCuriosHelper().findEquippedCurio(itemStack -> itemStack.getItem() instanceof JumpAmulet, livingEntity).ifPresent(triple -> {
                    ItemStack stack = triple.getRight();
                    JumpAmulet item = (JumpAmulet) stack.getItem();
                    livingEntity.addVelocity(0, item.getJump(stack), 0);
                    livingEntity.velocityChanged = true;
                });
            }
    }

}
