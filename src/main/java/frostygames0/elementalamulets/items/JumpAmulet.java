package frostygames0.elementalamulets.items;

import frostygames0.elementalamulets.config.ModConfig;
import frostygames0.elementalamulets.items.interfaces.IJumpItem;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class JumpAmulet extends AmuletItem implements IJumpItem {
    public JumpAmulet(Item.Properties properties, int tier) {
        super(properties, tier);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);
        tooltip.add(new TranslationTextComponent("item.elementalamulets.jump_amulet.tooltip").mergeStyle(TextFormatting.GRAY));
    }

    @Override
    public float getJump() {
        return 0.3f*getTier();
    }

    @Override
    public float getFallResist() {
        return getJump()*10-1f;
    }

    @Override
    public int getDamageOnUse() {
        return ModConfig.cached.JUMP_AMULET_USAGE_DMG * getTier();
    }
}
