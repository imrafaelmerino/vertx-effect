package vertx.effect;

import java.time.Duration;
import java.util.Objects;

/**
 * Represents a delay. Sometimes you need the ability to delay the execution of a task with a specified time duration.
 * For example, retry policies use delays to wait asynchronously before making retries.
 *
 * @see VertxRef#delay(Duration)
 */
public final class Delay {

    /**
     * delay of zero seconds
     */
    public static final Delay ZERO =
            new Delay(Duration.ofSeconds(0), VIO.succeed(0L));

    /**
     * the duration of the delay
     */
    final Duration duration;

    /**
     * the delay represented as a val. When executed,a brand-new timer is scheduled and the id of the timer will be
     * returned
     */
    final VIO<Long> effect;

    Delay(final Duration duration,
          final VIO<Long> effect
         ) {
        this.duration = Objects.requireNonNull(duration);
        this.effect = Objects.requireNonNull(effect);
    }

    /**
     * Creates a new delay, being the duration multiplied by the given n
     *
     * @param n the factor the delay is multiplied
     * @return a new Delay
     */
    Delay multipliedBy(long n) {
        VIO<Long> acc = effect;
        for (int i = 0; i < n; i++) acc = acc.then(it -> effect);
        return new Delay(duration.multipliedBy(n), acc);
    }


}
