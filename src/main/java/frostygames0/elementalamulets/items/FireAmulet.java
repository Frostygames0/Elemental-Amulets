package frostygames0.elementalamulets.items;

import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;
import javafx.scene.control.Tooltip;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.CapabilityProvider;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fluids.FluidStack;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurio;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class FireAmulet extends AbstractAmuletItem {
    public FireAmulet(Properties p_i48487_1_) {
        super(p_i48487_1_);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);
        tooltip.add(new TranslationTextComponent("item.elementalamulets.fire_amulet.tooltip", TextFormatting.GRAY, TextFormatting.RED));
    }

    @Override
    public void curioTick(String identifier, int index, LivingEntity livingEntity, ItemStack stack) {
        if (!livingEntity.getEntityWorld().isRemote()) {
            if(livingEntity instanceof PlayerEntity) {
                PlayerEntity player = (PlayerEntity) livingEntity;
                if(player.isBurning()) {
                    player.addPotionEffect(new EffectInstance(Effects.FIRE_RESISTANCE, 40, 0));
                    stack.damageItem(1, livingEntity,livingEntity1 -> CuriosApi.getCuriosHelper().onBrokenCurio(identifier, index, livingEntity1));
                }

            }
        }
    }
    /* FIXME not working for some reasons :/ */
    @Override
    public void curioBreak(ItemStack stack, LivingEntity livingEntity) {
            livingEntity.playSound(SoundEvents.BLOCK_BEACON_DEACTIVATE, 100, 1);
            if(livingEntity instanceof PlayerEntity) {
                PlayerEntity player = (PlayerEntity) livingEntity;
                player.sendStatusMessage(new StringTextComponent("Your amulet just got destroyed!"), true);
            }
    }
}