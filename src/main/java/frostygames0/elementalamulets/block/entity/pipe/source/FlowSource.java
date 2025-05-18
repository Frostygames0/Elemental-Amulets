package frostygames0.elementalamulets.block.entity.pipe.source;

import frostygames0.elementalamulets.element.Element;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;

public abstract class FlowSource {
    protected final Direction direction;
    protected final BlockPos blockPos;

    public FlowSource(Direction direction, BlockPos blockPos) {
        this.direction = direction;
        this.blockPos = blockPos;
    }

    public abstract boolean isEndpoint();

    public void manage(Level level) {
    }

    @Nullable
    public abstract Holder<Element> getElement();
}
