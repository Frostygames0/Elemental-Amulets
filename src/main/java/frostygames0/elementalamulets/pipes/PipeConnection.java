package frostygames0.elementalamulets.pipes;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import frostygames0.elementalamulets.element.ElementType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.Mth;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class PipeConnection {
    public static final int MAX_PRESSURE = 16;

//    public static final Codec<PipeConnection> CODEC = RecordCodecBuilder.create(instance -> instance.group(
//            ConnectionType.CODEC.fieldOf("type").forGetter(pipeConnection -> pipeConnection.connectionType),
//            Pressure.CODEC.fieldOf("pressure").forGetter(pipeConnection -> pipeConnection.pressure),
//            Flow.CODEC.lenientOptionalFieldOf("flow").forGetter(pipeConnection -> Optional.ofNullable(pipeConnection.flow))
//    ).apply(instance, PipeConnection::new));

//    public static final Codec<Map<Direction, PipeConnection>> MAP_CODEC = Codec.unboundedMap(net.minecraft.core.Direction.CODEC, CODEC);



    private final Direction direction;
    private final Pressure pressure;
    private ConnectionType connectionType;

    @Nullable
    private FlowSource source;
    @Nullable
    private Flow flow;

    @Nullable
    private Runnable onFlowOverwrite;

    public PipeConnection(Direction direction) {
        this(direction, ConnectionType.UNKNOWN, new Pressure(), Optional.empty());
    }

    private PipeConnection(Direction direction, ConnectionType connectionType, Pressure pressure, Optional<Flow> flowOptional) {
        this.direction = direction;
        this.connectionType = connectionType;
        this.pressure = pressure;

        flow = flowOptional.orElse(null);

//        flowOptional.ifPresent(flow -> {
//            this.flow = flow;
//            this.flow.parent = this;
//        });
    }

    public enum Test {
        IDLE,
        UPDATE_FLOW,
        TICK_FLOW,
        STOP_FLOW;

        public Test tickAndGetNext(PipeConnection connection) {
            return switch(this) {
                case IDLE -> {
                    if (connection.hasPressure()) {
                        yield UPDATE_FLOW;
                    }
                }
                case UPDATE_FLOW -> {
                    if (!connection.hasFlow()) {
                        // idle
                    }

                    var pressure = connection.pressure;
                    if (!pressure.hasPressure()) {
                        // stop flow anyways
                    }

                    if (!connection.hasSource()) {
                        // stop flow
                    }

                    var providedElement = connection.flow.relativeDirection == RelativeDirection.INWARD ? connection.source.getElement() : null;
                    if (connection.flow.element != connection.source.getElement()) {
                        // stop flow
                    }

                    for (var pressureDirection : pressure.getByStrength()) {
                        if (pressure.getPressure(pressureDirection) == 0) {
                            continue;
                        }

                        var elementForFlow = pressureDirection == RelativeDirection.INWARD ? connection.source.getElement() : null;
                        // If connection has flow, which is in wrong direction but finished, we can invert it!
                        if (connection.hasFlow()) {
                            if (connection.flow.isFinished()) {
                                // flip the flow without stopping it
                            } else {
                                // stop the flow
                            }
                            break;
                        }



                    }
                }
                case TICK_FLOW -> {

                }
                case STOP_FLOW -> null;
            };
        }
    }

    private void stateStartFlow() {
        for (var pressureDirection : pressure.getByStrength()) {
            if (flow.relativeDirection == pressureDirection) {
                // Go to yobaniy sustain state
            }

            if (getPressure(pressureDirection) == 0) {
                continue;
            }

            if (flow.isFinished())

            if (tryStartFlow(pressureDirection == RelativeDirection.INWARD ? source.getElement() : null, pressureDirection)) {

            }

        }
    }

    void setConnectionType(ConnectionType connectionType) {
        if (connectionType.isUnknown()) {
            return;
        }

        this.connectionType = connectionType;
        source = null;
    }

    private boolean tryCreateFlowSource(BlockPos blockPos, Direction direction) {
        if (connectionType.isUnknown()) {
            return false;
        }

        if (source != null) {
            return false;
        }

        source = connectionType.createFlowSource(blockPos, direction);
        return source != null;
    }

    public void updateFlowSource(Level level, BlockEntity blockEntity) {
        if (source != null) {
            source.update(level, blockEntity);
        }
    }

    private boolean tryStartFlowByStrength() {
        for (var pressureDirection : pressure.getByStrength()) {
            if (getPressure(pressureDirection) == 0) {
                continue;
            }

            if (tryStartFlow(pressureDirection == RelativeDirection.INWARD ? source.getElement() : elementInInwardPipes, pressureDirection)) {
                return true;
            }

        }
        return false;
    }

    private boolean canFlowBeSustained() {
        @Nullable var providedElement = flow.relativeDirection == RelativeDirection.INWARD ? source.getElement() : null;
        return hasPressure() && flow.element.equals(providedElement);
    }

    private boolean manageActiveFlow(Holder<ElementType> outwardElement) {
        if (!canFlowBeSustained()) {
            stopFlow();
            return true;
        }

        var strongestDirection = getStrongestDirection();
        if (flow.relativeDirection == strongestDirection) {
            return false;
        }

        if (tryStartFlow(strongestDirection == RelativeDirection.INWARD ? source.getElement() : outwardElement, strongestDirection)) {
            if (onFlowOverwrite != null) {
                onFlowOverwrite.run();
            }

            return true;
        }

        return false;
    }

    private boolean hasSource() {
        return source != null;
    }

    public boolean manage(BlockPos blockPos, Direction direction, @Nullable Holder<ElementType> elementInInwardPipes, Runnable onFlowPressureStrengthChanged) {
        if (connectionType.isUnknown()) {
            return false;
        }

        if (!hasSource() && !tryCreateFlowSource(blockPos, direction)) {
            return false;
        }

        if (!hasFlow()) {
            if (!hasPressure()) {
                return false;
            }

            return tryStartFlowByStrength(elementInInwardPipes);
        }

        return manageActiveFlow(elementInInwardPipes);
    }

    private boolean tryStartFlowByStrength(Holder<ElementType> outwardElement) {
        for (var direction : pressure.getByStrength()) {
            if (getPressure(direction) == 0) {
                continue;
            }

            if (tryStartFlow(direction == RelativeDirection.INWARD ? source.getElement() : outwardElement, direction)) {
                return true;
            }

        }

        return false;
    }

    private boolean tryStartFlow(Holder<ElementType> element, RelativeDirection direction) {
        if (element == null) {
            return false;
        }

        flow = new Flow(direction, element);
        return true;
    }

    private void stopFlow() {
        flow = null;
    }

    public boolean tryFlipFlowsIfPressureReversed() {
        if (hasNoFlow()) {
            return false;
        }

        boolean singlePressure = getStrongestDirection() != null && (getPressure(RelativeDirection.INWARD) == 0 || getPressure(RelativeDirection.OUTWARD) == 0);
        if (!singlePressure || getStrongestDirection() == flow.relativeDirection) {
            return false;
        }

        flow.relativeDirection = flow.relativeDirection.invert();
        if (!flow.isFinished()) {
            flow = null;
        }

        return true;
    }

    public boolean isConnectedToEndpoint() {
        return connectionType.isEndpoint();
    }

    @Nullable
    public Holder<ElementType> getFlowElement() {
        if (hasNoFlow()) {
            return null;
        }

        if (!flow.isFinished()) {
            return null;
        }

        return flow.element;
    }

    public void setPressure(float value, RelativeDirection relativeDirection) {
        pressure.setPressure(value, relativeDirection);
    }

    public void addPressure(float value, RelativeDirection relativeDirection) {
        pressure.addPressure(value, relativeDirection);
    }

    public float getPressure(RelativeDirection relativeDirection) {
        return pressure.getPressure(relativeDirection);
    }

    public RelativeDirection getStrongestDirection() {
        return pressure.getStrongest();
    }

    public boolean hasPressure() {
        return pressure.hasPressure();
    }

    public boolean hasNoPressure() {
        return !hasPressure();
    }

    public boolean hasFlow() {
        return flow != null;
    }

    public boolean hasNoFlow() {
        return !hasFlow();
    }

    @Nullable
    public PipeConnection.RelativeDirection getFlowDirection() {
        if (!hasFlow()) {
            return null;
        }
        return flow.relativeDirection;
    }

    public boolean isFlowFinished() {
        if (!hasFlow()) {
            return false;
        }

        return flow.isFinished();
    }

    public void resetPressure() {
        pressure.reset();
        source = null;
    }

    private Codec<Flow> flowCodec() {
        return RecordCodecBuilder.create(instance ->
                instance.group(
                        RelativeDirection.CODEC.fieldOf("direction").forGetter(flow -> flow.relativeDirection),
                        ElementType.CODEC.fieldOf("element").forGetter(flow -> flow.element),
                        ExtraCodecs.floatRange(0, 1f).fieldOf("progress").forGetter(flow -> flow.progress)
                ).apply(instance, Flow::new));
    }

    public Codec<PipeConnection> codec() {
        return RecordCodecBuilder.create(instance -> instance.group(
                ConnectionType.CODEC.fieldOf("type").forGetter(pipeConnection -> pipeConnection.connectionType),
                Pressure.CODEC.fieldOf("pressure").forGetter(pipeConnection -> pipeConnection.pressure),
                flowCodec().lenientOptionalFieldOf("flow").forGetter(pipeConnection -> Optional.ofNullable(pipeConnection.flow))
        ).apply(instance, (type, pressure, flow) -> new PipeConnection(direction, type, pressure, flow)));
    }

    private class Flow {
        private final Holder<ElementType> element;

        private RelativeDirection relativeDirection;
        private float progress;

        private Flow(RelativeDirection relativeDirection, Holder<ElementType> element) {
            this(relativeDirection, element, 0);
        }

        private Flow(RelativeDirection relativeDirection, Holder<ElementType> element, float progress) {
            this.relativeDirection = relativeDirection;
            this.element = element;

            this.progress = progress;
        }

        private void tick() {
            if (isFinished()) {
                return;
            }

            var pressure = getPressure(relativeDirection);

            float flowSpeed = 1 / 32f + Mth.clamp(pressure / 128f, 0, 1) * 31 / 32f;
            progress = Math.min(progress + flowSpeed, 1);
        }

        public boolean isFinished() {
            return progress >= 1f;
        }
    }

    private static class Pressure {
        private static final Codec<Pressure> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                        ExtraCodecs.floatRange(0, MAX_PRESSURE).fieldOf("inward").forGetter(pressure -> pressure.inward),
                        ExtraCodecs.floatRange(0, MAX_PRESSURE).fieldOf("outward").forGetter(pressure -> pressure.outward))
                .apply(instance, Pressure::new));

        private float inward;
        private float outward;

        private Pressure() {
            this(0, 0);
        }

        private Pressure(float inward, float outward) {
            this.inward = inward;
            this.outward = outward;
        }

        private void setPressure(float value, RelativeDirection relativeDirection) {
            if (relativeDirection == RelativeDirection.INWARD) {
                inward = Mth.clamp(value, 0, MAX_PRESSURE);
            } else {
                outward = Mth.clamp(value, 0, MAX_PRESSURE);
            }
        }

        private void addPressure(float value, RelativeDirection relativeDirection) {
            if (relativeDirection == RelativeDirection.INWARD) {
                inward = Mth.clamp(inward + value, 0, MAX_PRESSURE);
            } else {
                outward = Mth.clamp(outward + value, 0, MAX_PRESSURE);
            }
        }

        private float getPressure(RelativeDirection relativeDirection) {
            return relativeDirection == RelativeDirection.INWARD ? inward : outward;
        }

        @Nullable
        private RelativeDirection getStrongest() {
            if (inward > outward) {
                return RelativeDirection.INWARD;
            } else if (outward > inward) {
                return RelativeDirection.OUTWARD;
            }

            return null;
        }

        private RelativeDirection[] getByStrength() {
            if (outward > inward) {
                return new RelativeDirection[] {RelativeDirection.OUTWARD, RelativeDirection.INWARD};
            }

            return new RelativeDirection[] {RelativeDirection.INWARD, RelativeDirection.OUTWARD};
        }

        private boolean hasPressure() {
            return inward != 0 || outward != 0;
        }

        private boolean isSingle() {
            return (inward == 0) != (outward == 0);
        }

        private void reset() {
            inward = 0;
            outward = 0;
        }
    }

    public enum RelativeDirection implements StringRepresentable {
        INWARD,
        OUTWARD;

        public static final RelativeDirection[] VALUES = RelativeDirection.values();
        public static final Codec<RelativeDirection> CODEC = StringRepresentable.fromValues(() -> VALUES);

        public RelativeDirection invert() {
            return this == INWARD ? OUTWARD : INWARD;
        }

        @Override
        public String getSerializedName() {
            return this == INWARD ? "inward" : "outward";
        }
    }
}
