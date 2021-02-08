package vertx.effect;

import java.time.Duration;
import java.util.Objects;

/**
 Represents a delay. Sometimes you need the ability to delay the execution of a task with a specified time duration.
 For example, retry policies use delays to wait asynchronously before making retries.
 @see VertxRef#sleep(Duration)
 */
public class Delay {

    /**
     delay of zero seconds
     */
    public static final Delay ZERO = new Delay(Duration.ofSeconds(0),
                                               Val.succeed(0L)
    );

    /**
     the duration of the delay
     */
    public final Duration duration;

    /**
     the delay represented as a val. When executed,a brand-new timer is scheduled and the id of the timer will be returned
     */
    public final Val<Long> val;

    Delay(final Duration duration,
          final Val<Long> val) {
        this.duration = Objects.requireNonNull(duration);
        this.val = Objects.requireNonNull(val);
    }

    /**
     Creates a new delay, being the duration multiplied by the given n
     @param n the factor the delay is multiplied
     @return a new Delay
     */
    public final Delay multipliedBy(long n) {
        Val<Long> acc = val;
        for (int i = 0; i < n; i++) {
            acc = acc.flatMap(it -> val);
        }
        return new Delay(duration.multipliedBy(n),
                         acc
        );
    }

    /**
     Creates a new delay adding the given delay to this
     @param delay the delay to be added to this
     @return a new delay
     */
    final Delay plus(Delay delay) {
        return new Delay(duration.plus(delay.duration),
                         val.flatMap(it -> delay.val)
        );
    }

    /**
     returns the maximum delay
     @param other the other delay
     @return the maximum delay
     */
    public Delay max(Delay other) {
        if (this.duration.compareTo(other.duration) >= 0) return this;
        return other;
    }

    /**
     returns the minimum delay
     @param other the other delay
     @return the minimum delay
     */
    public Delay min(Delay other) {
        if (this.duration.compareTo(other.duration) <= 0) return this;
        return other;
    }

}
