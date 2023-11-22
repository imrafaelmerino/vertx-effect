package vertx.effect;

import java.util.Objects;

/**
 * Datatype with stats about retries made thus far.
 */
public final class RetryStatus {

    /**
     * Delay incurred so far from retries in milliseconds
     */
    public final long cumulativeDelay;
    /**
     * Latest attempt's delay. Will always be -1 on first run.
     */
    public final long previousDelay;
    /**
     * Iteration number, where 0 is the first tr
     */
    public final int counter;

    public RetryStatus(final int counter,
                       final long cumulativeDelay,
                       final long rsPreviousDelay
                      ) {
        this.counter = counter;
        this.cumulativeDelay = cumulativeDelay;
        this.previousDelay = rsPreviousDelay;
    }


    @Override
    public String toString() {
        return String.format("(%s,%s,%s)",
                             counter,
                             previousDelay,
                             cumulativeDelay
                            );
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof RetryStatus)) return false;
        final RetryStatus that = (RetryStatus) o;
        return cumulativeDelay == that.cumulativeDelay &&
               previousDelay == that.previousDelay &&
               counter == that.counter;
    }

    @Override
    public int hashCode() {
        return Objects.hash(cumulativeDelay,
                            previousDelay,
                            counter
                           );
    }
}
