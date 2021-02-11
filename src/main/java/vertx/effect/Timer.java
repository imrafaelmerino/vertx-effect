package vertx.effect;

import java.time.Duration;
import java.util.Objects;

/**
 Represents a delay. Sometimes you need the ability to delay the execution of a task with a specified time duration.
 For example, retry policies use delays to wait asynchronously before making retries.

 @see VertxRef#sleep(Duration) */
public class Timer {

    /**
     delay of zero seconds
     */
    public static final Timer ZERO = new Timer(Duration.ofSeconds(0),
                                               Val.succeed(0L)
    );

    /**
     the duration of the delay
     */
    public final Duration duration;

    /**
     the delay represented as a val. When executed,a brand-new timer is scheduled and the id of the timer will be returned
     */
    public final Val<Long> delay;

    Timer(final Duration duration,
          final Val<Long> delay) {
        this.duration = Objects.requireNonNull(duration);
        this.delay = Objects.requireNonNull(delay);
    }

    /**
     Creates a new delay, being the duration multiplied by the given n

     @param n the factor the delay is multiplied
     @return a new Delay
     */
     final Timer multipliedBy(long n) {
        Val<Long> acc = delay;
        for (int i = 0; i < n; i++) {
            acc = acc.flatMap(it -> delay);
        }
        return new Timer(duration.multipliedBy(n),
                         acc
        );
    }



}
