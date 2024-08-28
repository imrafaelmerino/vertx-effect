package vertx.effect;

import java.time.Duration;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Clock provides the current time, as a pure alternative to:
 * <p>
 * Java's System.currentTimeMillis for getting the "real-time clock" and System.nanoTime for a monotonic clock useful
 * for time measurements. It's very useful for testing you code traveling in time!
 */
public interface Clock extends Supplier<Long> {


    /**
     * Creates a monotonic clock, appropriate for time measurements. When invoked, it returns the current value of the
     * running Java Virtual Machine's high-resolution time source, in nanoseconds It uses the {@link System#nanoTime }
     * method.
     *
     * @see System#nanoTime
     */
    Clock monotonic = System::nanoTime;

    /**
     * Creates a realtime or wall-clock watch. It produces the current time, as a Unix timestamp in milliseconds (number
     * of time units since the Unix epoch). This clock is not appropriate for measuring duration of intervals ( use
     * {@link Clock#monotonic} instead ). It uses the {@link System#currentTimeMillis } method.
     *
     * @see System#currentTimeMillis
     */
    Clock realTime = System::currentTimeMillis;
    Function<Supplier<Long>, Clock> custom = s -> s::get;

    /**
     * returns lambda that produces monotonic clock measurement in the specified unit
     *
     * @param unit the unit of the returned measurement
     * @return a lambda that produces monotonic clock measurement in the specified unit
     */
    static Clock monotonic(TimeUnit unit) {
        return () -> {
            Long time = monotonic.get();
            return unit.convert(Duration.ofNanos(time));
        };
    }

    /**
     * returns a lambda that produces the current time, as a Unix timestamp (number of time units since the Unix epoch)
     * in the specified unit
     *
     * @param unit the unit of the returned measurement
     * @return a lambda that produces the current time, as a Unix timestamp, in the specified unit
     */
    static Clock realTime(TimeUnit unit) {
        return () -> {
            Long time = realTime.get();
            return unit.convert(Duration.ofMillis(time));
        };
    }


}
