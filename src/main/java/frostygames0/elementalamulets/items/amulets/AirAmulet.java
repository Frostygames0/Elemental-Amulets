package frostygames0.elementalamulets.items.amulets;

import frostygames0.elementalamulets.ElementalAmulets;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeMod;
import top.theillusivec4.curios.api.SlotContext;

import java.util.UUID;

public class AirAmulet extends AmuletItem {
    public static UUID MODIFIER_UUID = UUID.fromString("2589aeb9-2b6a-44dc-8fab-97c9743dacdf");
    public AirAmulet(Properties properties) {
        super(properties, true);
    }

    @Override
    protected IFormattableTextComponent getDescription(ItemStack stack, World worldIn) {
        return new TranslationTextComponent(this.getDefaultTranslationKey());
    }

    @Override
    public void curioTick(String identifier, int index, LivingEntity livingEntity, ItemStack stack) {
        ModifiableAttributeInstance gravity = livingEntity.getAttribute(ForgeMod.ENTITY_GRAVITY.get());
        AttributeModifier attMod = new AttributeModifier(MODIFIER_UUID, new ResourceLocation(ElementalAmulets.MOD_ID, "speed").toString(),
                this.getFloating(stack), AttributeModifier.Operation.MULTIPLY_BASE);
        boolean flag = livingEntity.getMotion().y <= 0.0D;
        if (flag) {
            if (!gravity.hasModifier(attMod)) gravity.applyNonPersistentModifier(attMod);
            livingEntity.fallDistance = 0.0F;
        } else if (gravity.hasModifier(attMod)) {
            gravity.removeModifier(attMod);
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

    public float getFloating(ItemStack stack) {
        return this.getTier(stack);
    }
}
