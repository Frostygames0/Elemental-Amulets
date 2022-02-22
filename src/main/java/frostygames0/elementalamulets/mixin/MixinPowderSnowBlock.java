package frostygames0.elementalamulets.mixin;

import frostygames0.elementalamulets.config.ModConfig;
import frostygames0.elementalamulets.init.ModItems;
import frostygames0.elementalamulets.items.amulets.AmuletItem;
import frostygames0.elementalamulets.util.AmuletUtil;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.PowderSnowBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * @author Frostygames0
 * @date 19.02.2022 19:07
 */
@Mixin(PowderSnowBlock.class)
public class MixinPowderSnowBlock {

    @Inject(at = @At("RETURN"), method = "canEntityWalkOnPowderSnow(Lnet/minecraft/world/entity/Entity;)Z", cancellable = true)
    private static void elementalamulets_canWalkWithAmulet(Entity pEntity, CallbackInfoReturnable<Boolean> cir) {
        if (pEntity instanceof LivingEntity living) {
            AmuletUtil.getAmuletInSlotOrBelt(ModItems.WATER_WALKER_AMULET.get(), living).ifPresent(triple -> {
                ItemStack stack = triple.stack();
                AmuletItem item = (AmuletItem) stack.getItem();

                if (ModConfig.CachedValues.WATER_WALKER_AMULET_WALK_ON_POWDER_SNOW) {
                    if (item.getTier(stack) > 1)
                        cir.setReturnValue(true);
                }
            });
        }
    }
}
