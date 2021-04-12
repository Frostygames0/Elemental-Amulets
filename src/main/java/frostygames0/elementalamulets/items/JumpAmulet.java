package frostygames0.elementalamulets.items;

import frostygames0.elementalamulets.items.interfaces.IJumpItem;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class JumpAmulet extends AbstractAmuletItem implements IJumpItem {
    private final float jumpBoost = 0.3f;
    private final float fallResist = 1f;
    public JumpAmulet(Properties properties) {
        super(properties);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);
        tooltip.add(new TranslationTextComponent("item.elementalamulets.jump_amulet.tooltip", TextFormatting.GRAY, TextFormatting.GREEN));
    }

    @Override
    public float getJump() {
        return jumpBoost;
    }

    @Override
    public float getFallResist() {
        return fallResist;
    }
}
