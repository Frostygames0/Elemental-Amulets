package frostygames0.elementalamulets.amuleteffect;

import frostygames0.elementalamulets.items.interfaces.IFireItem;
import frostygames0.elementalamulets.items.interfaces.IJumpItem;
import net.minecraft.entity.LivingEntity;
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

public class FireAmuletEffect {

    // Calculates fire resist multipliers! returns array
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
    }

    public static void onLivingHurt(LivingHurtEvent event) {
        float[] calcResist = calculateFireResist(event.getEntityLiving());
        float fireResist = calcResist[0];
        float lavaResist = calcResist[1];
    }
    public static void onLivingAttack(LivingAttackEvent event) {
        float[] calcResist = calculateFireResist(event.getEntityLiving());
        float fireResist = calcResist[0];
        float lavaResist = calcResist[1];
    }
}
