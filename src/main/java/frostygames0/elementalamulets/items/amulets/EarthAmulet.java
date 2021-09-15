package frostygames0.elementalamulets.items.amulets;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

/**
 * @author Frostygames0
 * @date 13.09.2021 19:35
 */
public class EarthAmulet extends AmuletItem {
    public EarthAmulet(Properties properties) {
        super(properties);
    }

    @Override
    public void curioTick(String identifier, int index, LivingEntity livingEntity, ItemStack stack) {
        World world = livingEntity.level;
        if(!world.isClientSide()) {

        }
    }
}
