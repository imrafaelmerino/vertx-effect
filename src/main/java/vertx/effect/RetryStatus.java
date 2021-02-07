package vertx.effect;

import java.util.Objects;

/**
 Datatype with stats about retries made thus far.
 */
public class RetryStatus {

    /**
     Delay incurred so far from retries in milliseconds
     */
    public final long rsCumulativeDelay;
    /**
     Latest attempt's delay. Will always be -1 on first run.
     */
    public final long rsPreviousDelay;
    /**
     Iteration number, where 0 is the first tr
     */
    public final int rsIterNumber;

    public RetryStatus(final int rsIterNumber,
                       final long rsCumulativeDelay,
                       final long rsPreviousDelay) {
        this.rsIterNumber = rsIterNumber;
        this.rsCumulativeDelay = rsCumulativeDelay;
        this.rsPreviousDelay = rsPreviousDelay;
    }


    @Override
    public String toString() {
        return String.format("(%s,%s,%s)",
                             rsIterNumber,
                             rsPreviousDelay,
                             rsCumulativeDelay
                            );
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final RetryStatus that = (RetryStatus) o;
        return rsCumulativeDelay == that.rsCumulativeDelay &&
                rsPreviousDelay == that.rsPreviousDelay &&
                rsIterNumber == that.rsIterNumber;
    }

    @Override
    public int hashCode() {
        return Objects.hash(rsCumulativeDelay,
                            rsPreviousDelay,
                            rsIterNumber
                           );
    }
}
