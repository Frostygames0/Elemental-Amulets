package frostygames0.elementalamulets.items.amulets;

import frostygames0.elementalamulets.ElementalAmulets;
import frostygames0.elementalamulets.config.ModConfig;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import top.theillusivec4.curios.api.SlotContext;

import java.util.UUID;

public class SpeedAmulet extends AmuletItem {
    public static UUID MODIFIER_UUID = UUID.fromString("06c06b38-3779-4ca2-b678-7c111c77faef");
    public SpeedAmulet(Properties properties) {
        super(properties);
    }

    @Override
    public IFormattableTextComponent getDescription(ItemStack stack, World worldIn) {
        return new TranslationTextComponent("item.elementalamulets.speed_amulet.tooltip");
    }

    @Override
    public void curioTick(String identifier, int index, LivingEntity livingEntity, ItemStack stack) {
        if(!livingEntity.world.isRemote()) {
            ModifiableAttributeInstance att = livingEntity.getAttribute(Attributes.MOVEMENT_SPEED);
            AttributeModifier attMod = new AttributeModifier(MODIFIER_UUID, new ResourceLocation(ElementalAmulets.MOD_ID, "speed").toString(),
                    this.getSpeed(stack), AttributeModifier.Operation.MULTIPLY_BASE);
            if(livingEntity.isSprinting()) {
                if (!att.hasModifier(attMod)) {
                    att.applyNonPersistentModifier(attMod);
                }
                //if(livingEntity.ticksExisted % 20 == 0) stack.damageItem(10, livingEntity, livingEntity1 -> livingEntity1.sendBreakAnimation(EquipmentSlotType.MAINHAND));
            } else {
                if (att.hasModifier(attMod)) {
                    att.removeModifier(attMod);
                }
            }
        }
    }

    @Override
    public void onUnequip(SlotContext slotContext, ItemStack newStack, ItemStack stack) {
        ModifiableAttributeInstance att = slotContext.getWearer().getAttribute(Attributes.MOVEMENT_SPEED);
        if(stack.getItem() != newStack.getItem()) {
            if (att.getModifier(MODIFIER_UUID) != null) {
                att.removeModifier(MODIFIER_UUID);
            }
        }
    }

    public float getSpeed(ItemStack stack) {
        return (float) ModConfig.cached.SPEED_AMULET_BOOST*this.getTier(stack);
    }


}
