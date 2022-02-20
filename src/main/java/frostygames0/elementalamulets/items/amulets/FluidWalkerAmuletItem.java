package frostygames0.elementalamulets.items.amulets;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.phys.shapes.CollisionContext;
import top.theillusivec4.curios.api.SlotContext;


import java.util.function.Supplier;

/**
 * @author Frostygames0
 * @date 17.02.2022 13:34
 */
public class FluidWalkerAmuletItem extends AmuletItem {

    private final Supplier<? extends Block> block;
    private final Supplier<? extends Fluid> fluid;

    public FluidWalkerAmuletItem(Item.Properties properties, Supplier<? extends Block> block, Supplier<? extends Fluid> fluid) {
        super(new Properties(properties)
                .hasTier()
                .usesCurioMethods());

        this.block = block;
        this.fluid = fluid;
    }

    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        LivingEntity entity = slotContext.entity();
        Level level = entity.level;

        if (!level.isClientSide()) {
            if (entity.isOnGround()) {
                Block blockToFreeze = block.get();

                BlockState blockstate = blockToFreeze.defaultBlockState();
                BlockPos pos = entity.blockPosition();

                BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos();

                float freeze = Math.min(5, this.getTier(stack) + 1) / 2.0f;

                for (BlockPos blockpos : BlockPos.betweenClosed(pos.offset(-freeze, -1.0f, -freeze), pos.offset(freeze, -1.0f, freeze))) {
                    if (blockpos.closerThan(entity.position(), freeze)) {

                        mutablePos.set(blockpos.getX(), blockpos.getY() + 1, blockpos.getZ());
                        BlockState blockstate1 = level.getBlockState(mutablePos);

                        if (blockstate1.isAir()) {

                            BlockState blockstate2 = level.getBlockState(blockpos);
                            boolean canBeFrozen = blockstate2.getFluidState().getType() == fluid.get() && blockstate2.getValue(LiquidBlock.LEVEL) == 0;

                            if (canBeFrozen && blockstate.canSurvive(level, blockpos) && level.isUnobstructed(blockstate, blockpos, CollisionContext.empty())) {
                                level.setBlockAndUpdate(blockpos, blockstate);
                            }
                        }
                    }
                }
            }
        }
    }
}
