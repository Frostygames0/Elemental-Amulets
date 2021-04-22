package frostygames0.elementalamulets.items;

import frostygames0.elementalamulets.config.ModConfig;
import frostygames0.elementalamulets.items.interfaces.IFireItem;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class FireAmulet extends AmuletItem implements IFireItem {
    private final float fireResist = 1f;
    private final float lavaResist = 0.5f;
    public FireAmulet(Properties p_i48487_1_) {
        super(p_i48487_1_,1);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);
        tooltip.add(new TranslationTextComponent("item.elementalamulets.fire_amulet.tooltip").mergeStyle(TextFormatting.GRAY, TextFormatting.RED));
    }

    /*
    @Override
    public void curioTick(String identifier, int index, LivingEntity livingEntity, ItemStack stack) {
        if (!livingEntity.getEntityWorld().isRemote()) {
            if(livingEntity instanceof PlayerEntity) {
                PlayerEntity player = (PlayerEntity) livingEntity;
                if(player.isBurning()) {
                    player.addPotionEffect(new EffectInstance(Effects.FIRE_RESISTANCE, 40, 0));
                    stack.damageItem(getDamageOnUse(), livingEntity, ent -> CuriosApi.getCuriosHelper().onBrokenCurio(identifier, index, ent));
                }

            }
        }
    }*/

    @Override
    public int getDamageOnUse() {
        return ModConfig.cached.FIRE_AMULET_USAGE_DMG*getTier();
    }

    @Override
    public float getFireResist() {
        return this.fireResist;
    }

    @Override
    public float getLavaResist() {
        return this.lavaResist;
    }

    @Override
    public int getTier() {
        return 1;
    }
}
