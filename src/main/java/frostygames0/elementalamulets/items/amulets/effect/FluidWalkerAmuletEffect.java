package frostygames0.elementalamulets.items.amulets.effect;

import frostygames0.elementalamulets.init.ModItems;
import frostygames0.elementalamulets.util.AmuletUtil;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingAttackEvent;

/**
 * @author Frostygames0
 * @date 19.02.2022 19:01
 */
public class FluidWalkerAmuletEffect {
    static class Lava {
        static void onLivingAttack(LivingAttackEvent event) {
            if (event.getEntityLiving() instanceof Player player) {
                if (!player.level.isClientSide()) {
                    if (event.getSource() == DamageSource.HOT_FLOOR) {
                        if (AmuletUtil.getAmuletInSlotOrBelt(ModItems.LAVA_WALKER_AMULET.get(), player).isPresent()) {
                            event.setCanceled(true);
                        }
                    }
                }
            }
        }
    }

    static class Water {
        // EMPTY BODY
    }
}
