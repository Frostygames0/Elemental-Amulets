package frostygames0.elementalamulets.block.entity.pipe;

import frostygames0.elementalamulets.block.entity.pipe.source.*;
import frostygames0.elementalamulets.element.Element;
import frostygames0.elementalamulets.element.ElementHelper;
import frostygames0.elementalamulets.initialization.ModCapabilities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class PipeConnection {
    public static final String TAG_FLOW = "Flow";
    public static final String TAG_INBOUND_PRESSURE = "InboundPressure";
    public static final String TAG_OUTBOUND_PRESSURE = "OutboundPressure";
    public static final String TAG_ELEMENT_STORAGE_FLOW_SOURCE = "ElementStorageFlowSource";
    public static final String TAG_INBOUND = "Inbound";
    public static final String TAG_ELEMENT = "Element";
    public static final String TAG_PROGRESS = "Progress";

    public static final float MAX_PRESSURE = 16f;
    private static final Boolean[] TRUE_AND_FALSE = new Boolean[]{true, false};

    private final Direction side;

    private float inboundPressure;
    private float outboundPressure;

    private FlowSource source;
    private FlowSource previousSource;

    private Flow flow;

    public PipeConnection(Direction side) {
        this.side = side;
    }

    public boolean manageFlows(Level level, BlockPos blockPos, @Nullable Holder<Element> internalElement) {
        if (source == null) {
            if (!tryLocateAndSetSource(level, blockPos)) {
                return false;
            }
        }

        // If we have no flow, try starting it
        if (!hasFlow()) {
            // If we have no pressure, there is no need to start the flow
            if (!hasPressure()) {
                return false;
            }

            // This weird piece of code tries to start the inbound flow first, and if it fails it tries to start the outbound flow
            var isInboundPressureTheStrongest = comparePressure() < 0;
            for (var bool : TRUE_AND_FALSE) {
                boolean inbound = isInboundPressureTheStrongest == bool;
                var pressure = inbound ? getInboundPressure() : getOutboundPressure();

                if (pressure == 0) {
                    continue;
                }

                // If we are trying to start inbound flow, then we get inbound element, otherwise we get element that is currently inside our pipe
                var elementForFlow = inbound ? source.getElement() : internalElement;
                if (tryStartingNewFlow(inbound, elementForFlow)) {
                    return true;
                }
            }

            return false;
        }

        var providedElement = flow.inbound ? source.getElement() : internalElement;
        // If we have a flow and either we have no pressure or element to flow or elements mismatch - stop the flow
        if (!hasPressure() || providedElement == null || !providedElement.equals(flow.element)) {
            flow = null;
            return true;
        }

        if (flow.inbound != comparePressure() < 0) {
            boolean inbound = !flow.inbound;
            if (inbound && (providedElement != null) || !inbound && (internalElement != null)) {
                PipeHelper.traversePipesAndResetNetworks(level, blockPos, side);
                tryStartingNewFlow(inbound, inbound ? source.getElement() : internalElement);
                return true;
            }
        }

        if (!source.isEndpoint()) {
            return false;
        }
        if (!flow.inbound) {
            return false;
        }

//        network = retainedNetwork;
//        if (!hasNetwork())
//            network = Optional.of(new FluidNetwork(world, new BlockFace(pos, side), flowSource::provideHandler));
//        network.get()
//                .tick();

        return false;
    }

    public void manageSource(Level level, BlockPos blockPos) {
        if (source == null && !tryLocateAndSetSource(level, blockPos)) {
            return;
        }

        source.manage(level, level.getBlockEntity(blockPos));
    }


    public void tickFlow(Level level, BlockPos blockPos) {
        if (!hasFlow()) {
            return;
        }

        if (level.isClientSide) {
            if (source == null) {
                tryLocateAndSetSource(level, blockPos);
            }
        }

        flow.tick();
    }

    private boolean tryLocateAndSetSource(Level level, BlockPos blockPos) {
        var relativePos = blockPos.relative(side);
        if (!level.isLoaded(relativePos)) {
            return false;
        }

        if (PipeHelper.isOpenEnd(level, blockPos, side)) {
            source = new OpenFlowSource();
            return true;
        }

        var cap = level.getCapability(ModCapabilities.ELEMENT_STORAGE_BLOCK, relativePos, side.getOpposite());
        if (cap != null) {
            if (previousSource instanceof ElementStorageFlowSource elementStorageFlowSource && elementStorageFlowSource.getElementStorage() == cap) {
                source = previousSource;
            } else {
                source = new ElementStorageFlowSource(side, blockPos);
            }
            return true;
        }

        source = PipeHelper.getPipeBlockEntity(level, relativePos).isPresent() ? new OtherPipeFlowSource(side, blockPos) : new BlockedFlowSource();
        return true;
    }

    private boolean tryStartingNewFlow(boolean inbound, @Nullable Holder<Element> element) {
        if (element == null) {
            return false;
        }

        flow = new Flow(inbound, element);
        return true;
    }

    public boolean tryFlipFlowsIfPressureReversed() {
        if (!hasFlow()) {
            return false;
        }
        boolean singlePressure = comparePressure() != 0 && (getInboundPressure() == 0 || getOutboundPressure() == 0);
        if (!singlePressure || comparePressure() < 0 == flow.inbound) {
            return false;
        }
        flow.inbound = !flow.inbound;
        if (!flow.complete) {
            flow = null;
        }
        return true;
    }

    public Optional<Holder<Element>> getElement(boolean inbound) {
        if (!hasFlow()) {
            return Optional.empty();
        }

        if (!flow.complete) {
            return Optional.empty();
        }

        if (inbound != flow.inbound) {
            return Optional.empty();
        }

        return Optional.of(flow.element);
    }

    public boolean hasFlow() {
        return flow != null;
    }

    public boolean isFlowInbound() {
        return flow.inbound;
    }

    public boolean hasPressure() {
        return inboundPressure != 0 || outboundPressure != 0;
    }

    public float getInboundPressure() {
        return inboundPressure;
    }

    public float getOutboundPressure() {
        return outboundPressure;
    }

    /**
     * @return 0 if pressures are equal;
     * positive number if outbound > inbound;
     * negative number if inbound > outbound;
     */
    private float comparePressure() {
        return getOutboundPressure() - getInboundPressure();
    }

    void setPressure(boolean inbound, float pressure) {
        if (inbound) {
            inboundPressure = pressure;
        } else {
            outboundPressure = pressure;
        }
    }

    public void addPressure(boolean inbound, float pressure) {
        if (inbound) {
            inboundPressure = Mth.clamp(inboundPressure + pressure, 0, MAX_PRESSURE);
        } else {
            outboundPressure = Mth.clamp(outboundPressure + pressure, 0, MAX_PRESSURE);
        }
    }

    public void resetConnection() {
        inboundPressure = 0;
        outboundPressure = 0;

        if (source != null) {
            previousSource = source;
        }

        source = null;
        resetNetwork();
    }

    public void resetNetwork() {

    }

    public void serializeNBT(CompoundTag tag, HolderLookup.Provider provider) {
        var connectionTag = new CompoundTag();
        tag.put(side.getName(), connectionTag);

        connectionTag.putFloat(TAG_INBOUND_PRESSURE, inboundPressure);
        connectionTag.putFloat(TAG_OUTBOUND_PRESSURE, outboundPressure);

        if (source instanceof ElementStorageFlowSource elementStorageFlowSource) {
            connectionTag.put(TAG_ELEMENT_STORAGE_FLOW_SOURCE, elementStorageFlowSource.serializeNBT(provider));
        }

        if (hasFlow()) {
            connectionTag.put(TAG_FLOW, flow.serializeNBT(provider));
        }
    }

    public void deserializeNBT(CompoundTag tag, BlockPos blockPos, HolderLookup.Provider provider) {
        var connectionTag = tag.getCompound(side.getName());

        inboundPressure = Mth.clamp(connectionTag.getFloat(TAG_INBOUND_PRESSURE), 0, MAX_PRESSURE);
        outboundPressure = Mth.clamp(connectionTag.getFloat(TAG_OUTBOUND_PRESSURE), 0, MAX_PRESSURE);

        source = null;
        if (connectionTag.contains(TAG_ELEMENT_STORAGE_FLOW_SOURCE)) {
            source = ElementStorageFlowSource.deserializeFromNBT(blockPos, connectionTag.getCompound(TAG_ELEMENT_STORAGE_FLOW_SOURCE), provider);
        }

        if (connectionTag.contains(TAG_FLOW)) {
            var flowTag = connectionTag.getCompound(TAG_FLOW);
            var flow = this.flow;
            if (flow == null) {
                flow = new Flow(false, null);
            }

            flow.deserializeNBT(flowTag, provider);
            this.flow = flow;
        } else {
            flow = null;
        }
    }

    @Override
    public String toString() {
        return String.format("%s - P: [I: %s, O: %s], F: [%s]", side.getName(), getInboundPressure(), getOutboundPressure(), flow == null ? "N" : flow.toString());
    }

    private class Flow {
        private boolean inbound;
        private Holder<Element> element;

        private float progress;
        private boolean complete;

        public Flow(boolean inbound, Holder<Element> element) {
            this.inbound = inbound;
            this.element = element;
        }

        private void tick() {
            if (complete) {
                return;
            }

            var pressure = inbound ? getInboundPressure() : getOutboundPressure();

            float flowSpeed = 1 / 32f + Mth.clamp(pressure / 128f, 0, 1) * 31 / 32f;
            progress = Math.min(progress + flowSpeed, 1);
            if (progress >= 1) {
                complete = true;
            }
        }

        private CompoundTag serializeNBT(HolderLookup.Provider provider) {
            var flowTag = new CompoundTag();

            flowTag.putBoolean(TAG_INBOUND, inbound);

            ElementHelper.serializeToNbt(element, provider).ifPresent(tag -> flowTag.put(TAG_ELEMENT, tag));

            if (!complete) {
                flowTag.putFloat(TAG_PROGRESS, progress);
            }

            return flowTag;
        }

        private void deserializeNBT(CompoundTag tag, HolderLookup.Provider provider) {
            inbound = tag.getBoolean(TAG_INBOUND);

            element = ElementHelper.deserializeFromNbt(tag.get(TAG_ELEMENT), provider).orElse(null);

            if (tag.contains(TAG_PROGRESS)) {
                progress = tag.getFloat(TAG_PROGRESS);
            } else {
                complete = true;
            }
        }

        @Override
        public String toString() {
            return String.format("E: %s, I: %s, P: %s", element.value().name().getString(), inbound, complete ? "c" : progress);
        }
    }
}
