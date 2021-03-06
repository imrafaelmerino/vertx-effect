package vertx.effect;


import java.time.Duration;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Function;

import static java.util.Objects.requireNonNull;


/**

 */
public class RetryPolicies {

    private RetryPolicies() {
    }


    static class LimitRetries implements RetryPolicy {
        public final int maxAttempts;

        public LimitRetries(final int maxAttempts) {
            this.maxAttempts = maxAttempts;
        }

        @Override
        public Optional<Timer> apply(final RetryStatus retryStatus) {
            boolean retry = retryStatus.rsIterNumber < maxAttempts;
            if (retry) return Optional.of(Timer.ZERO);
            return Optional.empty();
        }
    }

    static class IncrementalDelay implements RetryPolicy {
        private final Timer base;

        public IncrementalDelay(final Timer base) {
            this.base = base;
        }

        @Override
        public Optional<Timer> apply(final RetryStatus retryStatus) {
            return Optional.of(base.multipliedBy(retryStatus.rsIterNumber + 1));
        }
    }

    static class ExponentialBackoffDelay implements RetryPolicy {
        private final Function<Duration, Timer> fn;
        private final Timer base;

        public ExponentialBackoffDelay(final Timer base,
                                       final Function<Duration, Timer> fn) {
            this.base = base;
            this.fn = fn;
        }

        @Override
        public Optional<Timer> apply(final RetryStatus rs) {
            int multiplicand = (int) Math.pow(2,
                                              rs.rsIterNumber
                                             );
            return Optional.of(rs.rsCumulativeDelay == 0 ?
                               base : fn.apply(base.duration.multipliedBy(multiplicand))
                              );
        }
    }

    /**
     returns a policy that retries up to N times, with no delay between retries
     @param maxAttempts number of attempts
     @return a policy that retries up to N times, with no delay between retries
     */
    public static RetryPolicy limitRetries(int maxAttempts) {
        if (maxAttempts <= 0) throw new IllegalArgumentException("maxAttempts <= 0");
        return new LimitRetries(maxAttempts);
    }

    /**
     returns a policy that increments by the specified base the delay between retries
     @param base the time incremented between delays
     @return a policy that increments by the base the delay between retries
     @see VertxRef#sleep(Duration) to create delays
     */
    public static RetryPolicy incrementalDelay(final Timer base) {
        return new IncrementalDelay(requireNonNull(base));
    }

    /**
     returns a policy that retries forever, with a fixed delay between retries
     @param timer the fixed delay
     @return a policy that retries forever, with a fixed delay between retries
     @see VertxRef#sleep(Duration) to create delays
     */
    public static RetryPolicy constantDelay(final Timer timer) {
        return rs -> Optional.of(timer);
    }

    /**
     returns a policy that doubles the delay after each retry: delay = 2 * base  attempt
     @param base the base amount of time
     @param fn a function that produces {@link Timer} from duration of times. You only need the method {@link VertxRef#sleep(Duration)} to define one.
     @return a policy that doubles the delay after each retry
     @see VertxRef#sleep(Duration) to create delays
     */
    public static RetryPolicy exponentialBackoffDelay(final Duration base,
                                                      final Function<Duration, Timer> fn) {
        return new ExponentialBackoffDelay(fn.apply(base),
                                           fn
        );
    }


    /**
     returns a policy that adds some jitter to spread out the spikes to an approximately constant rate
     delay = random_between(0,min(cap,base*2*attempt))
     @param base the base amount of time
     @param cap the max upper bound
     @param fn a function that produces {@link Timer} from duration of times. You only need the method {@link VertxRef#sleep(Duration)} to define one.
     @return a policy that adds some jitter to the backoff
     @see VertxRef#sleep(Duration) to create delays

     */
    public static RetryPolicy fullJitter(final Duration base,
                                         final Duration cap,
                                         final Function<Duration, Timer> fn) {
        return rs -> exponentialBackoffDelay(base,
                                             fn
                                            ).capDelay(fn.apply(cap))
                                             .apply(rs)
                                             .map(t -> {
                                                 long bound = t.duration.toMillis();
                                                 Duration duration =
                                                         Duration.ofMillis(ThreadLocalRandom.current()
                                                                                            .nextLong(bound));
                                                 return new Timer(duration,
                                                                  fn.apply(duration).delay
                                                 );
                                             });
    }


    /**
     temp = min(cap,base * 2 ^ attempt)
     delay = temp/2 + random_between(0,temp/2)
     @param base the base
     @param cap the cap
     @param fn function to generates delays from durations
     @return a retry policy
     @see VertxRef#sleep(Duration) to create delays

     */
    public static RetryPolicy equalJitter(final Duration base,
                                          final Duration cap,
                                          final Function<Duration, Timer> fn) {
        return rs -> exponentialBackoffDelay(base,
                                             fn
                                            ).capDelay(fn.apply(cap))
                                             .apply(rs)
                                             .map(t -> {
                                                 Duration temp = t.duration.dividedBy(2);
                                                 Duration duration =
                                                         Duration.ofMillis(temp.toMillis() + ThreadLocalRandom.current()
                                                                                                              .nextLong(temp.toMillis()));
                                                 return new Timer(duration,
                                                                  fn.apply(duration).delay
                                                 );
                                             });

    }

    /**
     delay = min(cap,random_between(base, delay * 3))
     @param base the base
     @param cap the cap
     @param fn function to generates delays from durations
     @return a retry policy
     */
    public static RetryPolicy decorrelatedJitter(final Duration base,
                                                 final Duration cap,
                                                 final Function<Duration, Timer> fn) {

        return rs -> {
            if (rs.rsCumulativeDelay == 0) return Optional.of(fn.apply(base));

            long upperBound = 3 * rs.rsPreviousDelay;
            long l = ThreadLocalRandom.current()
                                      .nextLong(base.toMillis(),
                                                upperBound
                                               );
            return Optional.of(fn.apply(Duration.ofMillis(Math.min(l,
                                                                   cap.toMillis()
                                                                  ))));
        };

    }

}
