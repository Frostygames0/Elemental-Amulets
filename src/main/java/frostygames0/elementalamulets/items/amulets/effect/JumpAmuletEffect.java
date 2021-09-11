package frostygames0.elementalamulets.items.amulets.effect;

import frostygames0.elementalamulets.items.amulets.JumpAmulet;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import top.theillusivec4.curios.api.CuriosApi;

public class JumpAmuletEffect {

    static void onLivingHurt(LivingHurtEvent event) {
        if(event.getEntityLiving() instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) event.getEntityLiving();
            if (!player.world.isRemote()) {
                if (event.getSource() == DamageSource.FALL) {
                    CuriosApi.getCuriosHelper().findEquippedCurio(item -> item.getItem() instanceof JumpAmulet, player).ifPresent((triple) -> {
                        float fallResist = ((JumpAmulet) triple.getRight().getItem()).getFallResist(triple.getRight());
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
        }
    }
    static void onLivingAttack(LivingAttackEvent event) {
        if(event.getEntityLiving() instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) event.getEntityLiving();
            if (!player.world.isRemote()) {
                if (event.getSource() == DamageSource.FALL) {
                    CuriosApi.getCuriosHelper().findEquippedCurio(item -> item.getItem() instanceof JumpAmulet, player).ifPresent((triple) -> {
                        float fallResist = ((JumpAmulet) triple.getRight().getItem()).getFallResist(triple.getRight());
                        if (!event.isCanceled() && fallResist > 0) {
                            float finalDamage = Math.max(0, (event.getAmount() - fallResist));
                            if (finalDamage == 0) {
                                event.setCanceled(true);
                            }
                        }
                    });
                }
            }
        }
    }

    static void onLivingJump(LivingEvent.LivingJumpEvent event) {
        if(event.getEntityLiving() instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) event.getEntityLiving();
            World world = player.getEntityWorld();
            if (!world.isRemote) {
                if (world.getFluidState(player.getPosition()).isEmpty()) {
                    CuriosApi.getCuriosHelper().findEquippedCurio(itemStack -> itemStack.getItem() instanceof JumpAmulet, player).ifPresent(triple -> {
                        ItemStack stack = triple.getRight();
                        JumpAmulet item = (JumpAmulet) stack.getItem();
                        player.addVelocity(0, item.getJump(stack), 0);
                        player.velocityChanged = true;
                    });
                }
            }
        }
    }

}
