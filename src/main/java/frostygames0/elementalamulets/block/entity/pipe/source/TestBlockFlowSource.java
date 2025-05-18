package frostygames0.elementalamulets.block.entity.pipe.source;

import frostygames0.elementalamulets.element.Element;
import frostygames0.elementalamulets.initialization.ModElements;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class TestBlockFlowSource extends FlowSource {
    private Holder<Element> elementTest;

    public TestBlockFlowSource(Direction direction, BlockPos blockPos) {
        super(direction, blockPos);
    }

    @Override
    public void manage(Level level) {
        if (elementTest != null) {
            return;
        }

        elementTest = level.holderOrThrow(ModElements.FIRE);
    }

    @Override
    public @Nullable Holder<Element> getElement() {
        return elementTest;
    }

    @Override
    public boolean isEndpoint() {
        return true;
    }
}
