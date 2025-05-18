package frostygames0.elementalamulets.block.entity.pipe.source;

import frostygames0.elementalamulets.block.entity.pipe.ElementalPipeBlockEntity;
import frostygames0.elementalamulets.block.entity.pipe.PipeHelper;
import frostygames0.elementalamulets.element.Element;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.lang.ref.WeakReference;

public class OtherPipeFlowSource extends FlowSource {
    private WeakReference<ElementalPipeBlockEntity> pipeCache;

    public OtherPipeFlowSource(Direction direction, BlockPos blockPos) {
        super(direction, blockPos);
    }

    @Override
    public void manage(Level level) {
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
