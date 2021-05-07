package frostygames0.elementalamulets.blocks.containers.slot;

import frostygames0.elementalamulets.ElementalAmulets;
import frostygames0.elementalamulets.core.util.AmuletUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;

public class ElementalOnlySlot extends SlotItemHandler {
    private World world;

    public ElementalOnlySlot(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
        super(itemHandler, index, xPosition, yPosition);
        this.world = world;
    }

    @Override
    public boolean isItemValid(@Nonnull ItemStack stack) {
        return ItemTags.getCollection().get(new ResourceLocation(ElementalAmulets.MOD_ID, "elemental")).contains(stack.getItem());
    }
}
