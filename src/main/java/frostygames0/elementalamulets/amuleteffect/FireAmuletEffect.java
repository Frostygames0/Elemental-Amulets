package frostygames0.elementalamulets.amuleteffect;

import frostygames0.elementalamulets.items.interfaces.IFireItem;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import org.apache.commons.lang3.tuple.ImmutableTriple;
import org.apache.commons.lang3.tuple.Triple;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.type.util.ICuriosHelper;

import java.util.Optional;

public class FireAmuletEffect {

    private static float[] calcFireResistance(LivingEntity entity) {
        Optional<ImmutableTriple<String, Integer, ItemStack>> optional = CuriosApi.getCuriosHelper().findEquippedCurio(item -> item.getItem() instanceof IFireItem, entity);
        float fireResistMulti = 1;
        float lavaResistMulti = 1;
        if (optional.isPresent()) {
            ItemStack stack = optional.get().getRight();
            IFireItem item = (IFireItem) stack.getItem();
            fireResistMulti *= 1-item.getFireResist();
            lavaResistMulti *= 1-item.getLavaResist();
        }
        return new float[] {fireResistMulti, lavaResistMulti};
    }

    public static void attackEvent(LivingAttackEvent event) {
        if(event.getSource().isFireDamage()) {

        }

    }

    public static void hurtEvent(LivingHurtEvent event) {
        if(event.getSource().isFireDamage()) {
            LivingEntity entity = event.getEntityLiving();
            float[] multis = calcFireResistance(entity);
            float fire = multis[0];
            float lava = multis[1];
        }
    }

}
