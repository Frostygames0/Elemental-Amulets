package frostygames0.elementalamulets.items.amulets;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Effects;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import top.theillusivec4.curios.api.SlotContext;

public class InvisibilityAmulet extends AmuletItem {
    public InvisibilityAmulet(Properties properties) {
        super(properties, false);
    }

    @Override
    protected IFormattableTextComponent getDescription(ItemStack stack, World worldIn) {
        return new TranslationTextComponent("item.elementalamulets.invisibility_amulet.tooltip");
    }

    @Override
    public void curioTick(String identifier, int index, LivingEntity livingEntity, ItemStack stack) {
        World world = livingEntity.getEntityWorld();
        if(!world.isRemote()) {
            if(livingEntity.isSneaking()) {
                if(!livingEntity.isInvisible()) {
                    livingEntity.setInvisible(true);
                }
                //if(livingEntity.ticksExisted % 20 == 0) stack.damageItem(10, livingEntity, livingEntity1 -> livingEntity1.sendBreakAnimation(EquipmentSlotType.CHEST));
            } else {
                if(!livingEntity.isPotionActive(Effects.INVISIBILITY)) {
                    livingEntity.setInvisible(false);
                }
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
            if (entity.isInvisible() && !entity.isPotionActive(Effects.INVISIBILITY)) {
                entity.setInvisible(false);
            }
        }
    }
}
