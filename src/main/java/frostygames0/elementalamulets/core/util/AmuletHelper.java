package frostygames0.elementalamulets.core.util;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import top.theillusivec4.curios.api.CuriosApi;

/**
 * @author Frostygames0
 * @date 25.09.2021 15:04
 */
public class AmuletHelper {

    public static void damage(ItemStack stack, LivingEntity entity, String id, int index) {
        stack.hurtAndBreak(2, entity, ent -> CuriosApi.getCuriosHelper().onBrokenCurio(id, index, ent));
    }
}
