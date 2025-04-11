package frostygames0.elementalamulets.inventory.provider;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.neoforged.neoforge.items.IItemHandler;
import org.jetbrains.annotations.Nullable;

import java.util.EnumMap;

public class DynamicItemHandlerProvider implements IDynamicItemHandlerProvider {
    private final EnumProperty<Direction> property;

    private final EnumMap<Direction, IItemHandler> map = new EnumMap<>(Direction.class);
    private final EnumMap<BlockFace, IItemHandler> relativeMap;

    private BlockState state;

    private DynamicItemHandlerProvider(EnumProperty<Direction> property, EnumMap<BlockFace, IItemHandler> relativeMap) {
        this.property = property;
        this.relativeMap = relativeMap;
    }

    public void updateSides(BlockState newState) {
        var oldState = state;
        this.state = newState;

        if (this.state.getValue(property) != oldState.getValue(property)) {
            var newFace = this.state.getValue(property);

            map.clear();

            for (var entry : this.relativeMap.entrySet()) {
                var relative = entry.getKey();
                var handler = entry.getValue();

                switch (relative) {
                    case FRONT -> map.put(newFace, handler);
                    case BACK -> map.put(newFace.getOpposite(), handler);
                    case RIGHT -> map.put(newFace.getClockWise(), handler);
                    case LEFT -> map.put(newFace.getCounterClockWise(), handler);
                    case TOP -> map.put(newFace.getClockWise(Direction.Axis.X), handler);
                    case BOTTOM -> map.put(newFace.getCounterClockWise(Direction.Axis.X), handler);
                    case ANY -> map.put(null, handler); // Internal ItemHandler
                }
            }
        }
    }


    @Override
    public IItemHandler getItemHandlerForDirection(@Nullable Direction direction) {
        return map.getOrDefault(direction, null);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static Builder extendExisting(DynamicItemHandlerProvider existing) {
        return new Builder(existing.relativeMap);
    }

    public static class Builder {
        private final EnumMap<BlockFace, IItemHandler> relativeMap;

        private Builder() {
            this.relativeMap = new EnumMap<>(BlockFace.class);
        }

        private Builder(EnumMap<BlockFace, IItemHandler> existing) {
            this();
            this.relativeMap.putAll(existing);
        }

        public Builder addItemHandlerForFace(BlockFace blockFace, IItemHandler handler) {
            relativeMap.put(blockFace, handler);
            return this;
        }

        public Builder removeItemHandlerForFace(BlockFace blockFace) {
            relativeMap.remove(blockFace);
            return this;
        }

        public DynamicItemHandlerProvider build(EnumProperty<Direction> property) {
            return new DynamicItemHandlerProvider(property, relativeMap);
        }
    }
}
