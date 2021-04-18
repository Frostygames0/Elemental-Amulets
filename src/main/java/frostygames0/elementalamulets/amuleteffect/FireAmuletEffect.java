package frostygames0.elementalamulets.amuleteffect;

import frostygames0.elementalamulets.items.interfaces.IAmuletItem;
import frostygames0.elementalamulets.items.interfaces.IFireItem;
import frostygames0.elementalamulets.items.interfaces.IJumpItem;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Effects;
import net.minecraft.potion.Potions;
import net.minecraft.util.DamageSource;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;
import top.theillusivec4.curios.api.type.inventory.ICurioStacksHandler;

import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

public class FireAmuletEffect {

    // Calculates fire resist multipliers! returns array
    /*
    public static float[] calculateFireResist(LivingEntity entity) {
        LazyOptional<ICuriosItemHandler> opt = CuriosApi.getCuriosHelper().getCuriosHandler(entity);
        float fireResist = 0f;
        float lavaResist = 0f;
        if(opt.isPresent()) {
            ICuriosItemHandler handler = opt.orElse(null);
            Map<String, ICurioStacksHandler> map = handler.getCurios();
            for(ICurioStacksHandler stackHandler : map.values()) {
                int slots = stackHandler.getSlots();
                for(int i = 0; i < slots; i++) {
                    ItemStack stack = stackHandler.getStacks().getStackInSlot(i);
                    Item item = stack.getItem();
                    if(item instanceof IFireItem) {
                        IFireItem amulet = (IFireItem) item;
                        fireResist *= 1-amulet.getFireResist();
                        lavaResist *= 1- amulet.getLavaResist();
                    }
                }
            }

        }
        return new float[] {fireResist, lavaResist};
    }*/

    public static float[] calculateFireResist(LivingEntity entity) {
        AtomicReference<Float> fireResist = new AtomicReference<>(0f);
        AtomicReference<Float> lavaResist = new AtomicReference<>(0f);
        CuriosApi.getCuriosHelper().findEquippedCurio(itemStack -> itemStack.getItem() instanceof IFireItem, entity).map(item -> item.getRight().getItem()).ifPresent(item -> {
            IFireItem amulet = (IFireItem) item;
            fireResist.updateAndGet(v -> (float) (v * 1 - amulet.getFireResist()));
            lavaResist.updateAndGet(v -> (float) (v * 1 - amulet.getLavaResist()));
        });

        return new float[] {Math.min(fireResist.get(), 1), Math.min(lavaResist.get(), 1)};
    }

    public static void onLivingHurt(LivingHurtEvent event) {
        if(event.getSource().isFireDamage()) {
            float[] calcResist = calculateFireResist(event.getEntityLiving());
            float fireResist = calcResist[0];
            float lavaResist = calcResist[1];
            if (event.getSource() == DamageSource.LAVA) {
                fireResist = lavaResist;
            }
            if (fireResist < 0.999) {
                if (fireResist < 0.001F && event.isCancelable()) {
                    event.setCanceled(true);
                }
                event.setAmount(event.getAmount() * fireResist);
                CuriosApi.getCuriosHelper().findEquippedCurio(itemStack -> itemStack.getItem() instanceof IFireItem, event.getEntityLiving()).ifPresent(triple -> {
                    ItemStack itemStack = triple.getRight();
                    Item item = itemStack.getItem();
                    if(item instanceof IAmuletItem) {
                        IAmuletItem amulet = (IAmuletItem) item;
                        itemStack.damageItem(amulet.getDamageOnUse(), event.getEntityLiving(), e -> CuriosApi.getCuriosHelper().onBrokenCurio(triple.getLeft(), triple.getMiddle(), event.getEntityLiving()));
                    }
                });
            }
        }
    }
    public static void onLivingAttack(LivingAttackEvent event) {
        if(event.getSource().isFireDamage()) {
            float[] calcResist = calculateFireResist(event.getEntityLiving());
            float fireResist = calcResist[0];
            float lavaResist = calcResist[1];
            if (event.getSource() == DamageSource.LAVA) {
                fireResist = lavaResist;
            }
            if (fireResist < 0.999) {
                if (fireResist < 0.001F && event.isCancelable()) {
                    event.setCanceled(true);
                }
            }
        }
    }
}
