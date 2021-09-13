package frostygames0.elementalamulets.items.amulets;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Effects;
import net.minecraft.world.World;
import top.theillusivec4.curios.api.SlotContext;

public class InvisibilityAmulet extends AmuletItem {
    public InvisibilityAmulet(Properties properties) {
        super(properties, false);
    }

    @Override
    public void curioTick(String identifier, int index, LivingEntity livingEntity, ItemStack stack) {
        World world = livingEntity.getCommandSenderWorld();
        if(!world.isClientSide()) {
            if(livingEntity.isShiftKeyDown()) {
                if(!livingEntity.isInvisible()) livingEntity.setInvisible(true);
            } else {
                if(!livingEntity.hasEffect(Effects.INVISIBILITY)) livingEntity.setInvisible(false);
            }
        }
    }

    @Override
    public boolean canRender(String identifier, int index, LivingEntity livingEntity, ItemStack stack) {
        return !livingEntity.isInvisible();
    }

    @Override
    public void onUnequip(SlotContext slotContext, ItemStack newStack, ItemStack stack) {
        LivingEntity entity = slotContext.getWearer();
        if(stack.getItem() != newStack.getItem()) {
            if (entity.isInvisible() && !entity.hasEffect(Effects.INVISIBILITY)) {
                entity.setInvisible(false);
            }
        }
    }
}
