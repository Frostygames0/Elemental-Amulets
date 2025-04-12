package frostygames0.elementalamulets.inventory.menu.slots;

import frostygames0.elementalamulets.block.entity.extractor.AbstractElementalExtractorBlockEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.SlotItemHandler;

public class ElementalExtractorFuelSlot extends SlotItemHandler {
    private final Level level;

    public ElementalExtractorFuelSlot(IItemHandler container, int slot, int x, int y, Level level) {
        super(container, slot, x, y);

        this.level = level;
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        return AbstractElementalExtractorBlockEntity.isFuel(stack, this.level);
    }
}
