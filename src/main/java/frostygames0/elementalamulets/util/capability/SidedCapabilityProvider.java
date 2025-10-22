package frostygames0.elementalamulets.util.capability;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import org.jetbrains.annotations.Nullable;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

public class SidedCapabilityProvider<T> implements IContextualCapabilityProvider<T, Direction> {
    private final EnumProperty<Direction> property;

    private final Map<Direction, T> map = new HashMap<>();
    private final EnumMap<Face, T> relativeMap;

    private BlockState state;

    private SidedCapabilityProvider(EnumProperty<Direction> property, EnumMap<Face, T> relativeMap) {
        this.property = property;
        this.relativeMap = relativeMap;
    }

    public void onUpdateState(BlockState newState) {
        var oldState = state;
        state = newState;

        if (oldState == null || state.getValue(property) != oldState.getValue(property)) {
            var newFace = state.getValue(property);

            map.clear();

            for (var entry : relativeMap.entrySet()) {
                var relative = entry.getKey();
                var handler = entry.getValue();

                switch (relative) {
                    case FRONT -> map.put(newFace, handler);
                    case BACK -> map.put(newFace.getOpposite(), handler);
                    case RIGHT -> map.put(newFace.getClockWise(), handler);
                    case LEFT -> map.put(newFace.getCounterClockWise(), handler);
                    case TOP -> map.put(newFace.getCounterClockWise(Direction.Axis.X), handler);
                    case BOTTOM -> map.put(newFace.getClockWise(Direction.Axis.X), handler);
                    case ANY -> map.put(null, handler); // Internal ItemHandler
                }
            }
        }
    }


    public T getCapability(@Nullable Direction side) {
        return map.getOrDefault(side, null);
    }

    public static <T> Builder<T> builder() {
        return new Builder<>();
    }

    public static class Builder<T> {
        private final EnumMap<Face, T> relativeMap;

        private Builder() {
            relativeMap = new EnumMap<>(Face.class);
        }

        public Builder<T> addCapability(Face face, T cap) {
            relativeMap.put(face, cap);
            return this;
        }

        public SidedCapabilityProvider<T> build(EnumProperty<Direction> property) {
            return new SidedCapabilityProvider<>(property, relativeMap);
        }
    }

    public enum Face {
        FRONT,
        BACK,
        LEFT,
        RIGHT,
        TOP,
        BOTTOM,
        ANY
    }
}
