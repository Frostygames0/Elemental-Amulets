package frostygames0.elementalamulets.block.entity;

import com.mojang.datafixers.util.Pair;
import frostygames0.elementalamulets.block.entity.pipe.BaseElementalPipeBlockEntity;
import frostygames0.elementalamulets.block.entity.pipe.ElementalPipeBlockEntity;
import frostygames0.elementalamulets.block.entity.pipe.PipeHelper;
import frostygames0.elementalamulets.block.entity.pipe.PressurizerPipeBlockEntity;
import frostygames0.elementalamulets.block.pipe.ElementalPipeBlock;
import frostygames0.elementalamulets.initialization.ModBlockEntities;
import frostygames0.elementalamulets.initialization.ModCapabilities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.*;

public class TestBlockEntity extends BlockEntity {
    public TestBlockEntity(BlockPos pos, BlockState blockState) {
        super(ModBlockEntities.TEST_BLOCK.get(), pos, blockState);
    }
}
