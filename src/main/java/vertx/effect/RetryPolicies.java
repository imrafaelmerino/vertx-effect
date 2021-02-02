package vertx.effect;

import vertx.effect.exp.Cons;

import java.time.Duration;
import java.time.Instant;
import java.util.function.Function;
import java.util.function.Predicate;

import static java.util.Objects.requireNonNull;

public class RetryPolicies {

    static class LimitRetries implements Function<Throwable, Val<Boolean>> {
        private int tries = 0;
        public final int maxAttempts;

        public LimitRetries(final int maxAttempts) {
            this.maxAttempts = maxAttempts;
        }

        @Override
        public Val<Boolean> apply(final Throwable error) {
            return Cons.success((tries=tries + 1) <= maxAttempts);
        }

        @Override
        public String toString() {
            return "LimitRetries{" +
                    "tries=" + tries +
                    ", maxAttempts=" + maxAttempts +
                    '}';
        }
    }


    static class DuringPeriod implements Function<Throwable, Val<Boolean>> {
        long elapsed = 0L;
        final long max;

        DuringPeriod(final Duration maxDuration) {
            this.max = maxDuration.toMillis();
        }

        @Override
        public Val<Boolean> apply(final Throwable error) {
            long now = Instant.now()
                              .toEpochMilli();
            if (this.elapsed == 0) {
                this.elapsed = now;
                return Cons.TRUE;
            }
            this.elapsed = now - this.elapsed;
            return Cons.success(this.elapsed > max);
        }

        @Override
        public String toString() {
            return "DuringPeriod{" +
                    "elapsed=" + elapsed +
                    ", max=" + max +
                    '}';
        }
    }

    static class IncrementalDelay implements Function<Throwable, Val<Boolean>> {
        private int n;
        private final Delay delay;

        public IncrementalDelay(final Delay delay) {
            this.delay = delay;
        }

        @Override
        public Val<Boolean> apply(final Throwable error) {
            return delay.times((++n)).val
                    .map(it -> true);

        }

        @Override
        public String toString() {
            return "IncrementalDelay{" +
                    "n=" + n +
                    ", delay=" + delay +
                    '}';
        }
    }

    static class ExponentialBackoffDelay implements Function<Throwable, Val<Boolean>> {
        private Delay acc;

        public ExponentialBackoffDelay(final Delay delay) {
            this.acc = delay;
        }

        @Override
        public Val<Boolean> apply(final Throwable error) {
            acc = acc.times(2);
            return acc.val
                    .map(it -> true);

        }

        @Override
        public String toString() {
            return "ExponentialBackoffDelay{" +
                    "acc=" + acc +
                    '}';
        }
    }

    public static RetryPolicy limitRetries(int maxAttempts) {
        if (maxAttempts <= 0) throw new IllegalArgumentException("maxAttempts <= 0");
        return () -> new LimitRetries(maxAttempts);
    }

    public static RetryPolicy incrementalDelay(final Delay delay) {
        return () -> new IncrementalDelay(requireNonNull(delay));
    }

    public static RetryPolicy constantDelay(final Delay delay) {
        return () -> i -> delay.val.map(it -> true);
    }

    public static RetryPolicy exponentialBackoffDelay(final Delay delay) {
        return () -> new ExponentialBackoffDelay(delay);
    }

    public static RetryPolicy retryDuring(final Duration duration) {
        return () -> new DuringPeriod(requireNonNull(duration));
    }

    public static RetryPolicy retryIf(final Predicate<Throwable> predicate) {
        return () -> error -> Cons.success(predicate.test(error));
    }

}
