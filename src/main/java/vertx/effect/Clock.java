package vertx.effect;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

/**
 Clock provides the current time, as a pure alternative to:

 Java's System.currentTimeMillis for getting the "real-time clock" and System.nanoTime for a monotonic clock useful
 for time measurements. It's very useful for testing you code traveling in time!
 */
public final class Clock {

    private Clock(){}

    /**
     lambda that produces monotonic clock measurement in nanoseconds
     */
    public static λ<Void,Long> monotonic = v -> Val.succeed(System.nanoTime());

    /**
     lambda that produces the current time, as a Unix timestamp (number of time units since the Unix epoch)
     */
    public static λ<Void,Long> realTime = v -> Val.succeed(System.currentTimeMillis());

    /**
     returns lambda that produces monotonic clock measurement in the specified unit
     @param unit the unit of the returned measurement
     @return  a lambda that produces monotonic clock measurement in the specified unit
     */
    public static λ<Void,Long> monotonic(TimeUnit unit){
        return v -> monotonic.apply(null).map( time -> unit.convert(Duration.ofNanos(time)));
    }

    /**
     returns a lambda that produces the current time, as a Unix timestamp (number of time units since the Unix epoch)
     in the specified unit
     @param unit the unit of the returned measurement
     @return a lambda that produces the current time, as a Unix timestamp, in the specified unit
     */
    public static λ<Void,Long> realTime(TimeUnit unit){
        return v -> realTime.apply(null).map( time -> unit.convert(Duration.ofMillis(time)));
    }

}
