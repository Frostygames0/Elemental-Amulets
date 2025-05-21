package frostygames0.elementalamulets.block.entity.pipe.source;

import frostygames0.elementalamulets.block.entity.pipe.BaseElementalPipeBlockEntity;
import frostygames0.elementalamulets.block.entity.pipe.PipeHelper;
import frostygames0.elementalamulets.element.Element;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.Nullable;

import java.lang.ref.WeakReference;

public class OtherPipeFlowSource extends FlowSource {
    private WeakReference<BaseElementalPipeBlockEntity> pipeCache;

    public OtherPipeFlowSource(Direction direction, BlockPos blockPos) {
        super(direction, blockPos);
    }

    @Override
    public void manage(Level level, BlockEntity blockEntity) {
        if (pipeCache != null && pipeCache.get() != null && !pipeCache.get().isRemoved()) {
            return;
        }

        pipeCache = null;
        PipeHelper.getPipeBlockEntity(level, blockPos.relative(direction)).ifPresent(pipe -> pipeCache = new WeakReference<>(pipe));
    }

    @Override
    public @Nullable Holder<Element> getElement() {
        if (pipeCache == null || pipeCache.get() == null) {
            return null;
        }

        return pipeCache.get().getElement(direction.getOpposite(), false).orElse(null);
    }

    @Override
    public boolean isEndpoint() {
        return false;
    }
}
