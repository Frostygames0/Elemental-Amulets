package frostygames0.elementalamulets.pipes;

public class PipeConnection2 {

    public void setConnectionType() {}

    public void addPressure(float pressure) {}

    public void resetPressure() {}

    public enum FlowState {
        IDLE,
        FLOW_START,
        FLOW_STOP,
        FLOWING;

        private final 

        FlowState tickAndGetNext(PipeConnection2 connection) {
            boolean hasFlow = false;
            boolean hasPressure = false;

            return switch (this) {
                case IDLE -> {
                    if (!hasFlow) {
                        if (hasPressure) {
                            yield FLOW_START;
                        }
                    } else {
                        yield FLOWING;
                    }

                    yield this;
                }
                case FLOW_START -> {
                    if (!hasPressure) {
                        yield IDLE;
                    }

                    if (true) {
                        yield FLOWING;
                    }

                    yield this;
                }
                case FLOWING -> {
                    //
                    if (true) {
                        yield FLOW_STOP;
                    }

                    // tick flow

                    yield this;
                }
                case FLOW_STOP -> {
                    // stop flow
                    yield IDLE;
                }
            };
        }
    }
}
