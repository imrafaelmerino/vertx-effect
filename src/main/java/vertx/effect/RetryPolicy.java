package vertx.effect;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;


/**
 * A RetryPolicy is a function that takes an RetryStatus and possibly returns a delay. Iteration numbers start at zero
 * and increase by one on each retry. An Optional.empty() return value from the function implies we have reached the
 * retry limit. You can collapse multiple strategies into one using append. There are a number of policies available in
 * {@link RetryPolicies}. There are also a few combinators to transform policies, including: {@link #capDelay(Delay)},
 * {@link #limitRetriesByDelay(Duration)} and {@link #limitRetriesByCumulativeDelay(Duration)}. Always S¡simulate any
 * policy you define with {@link #simulate(int)}
 */
public interface RetryPolicy extends Function<RetryStatus, Optional<Delay>> {

    /**
     * The semantics of this combination is as follows: If either policy (this or other) returns Optional.empty(), the
     * combined policy returns Optional.empty(). This can be used to inhibit after a number of retries, for example. If
     * both policies return a delay, the larger delay will be used. This is quite natural when combining multiple
     * policies to achieve a certain effect. For an example of composing policies like this, we can use join to create a
     * policy that retries up to 5 times, starting with a 10 ms delay and increasing exponentially.
     *
     * @param other the other retry policy to be appended
     * @return a new retry policy
     */
    default RetryPolicy append(final RetryPolicy other) {
        return retryStatus -> {
            Optional<Delay> aOpt = RetryPolicy.this.apply(retryStatus);
            if (aOpt.isEmpty()) return aOpt;
            Optional<Delay> bOpt = other.apply(retryStatus);
            if (bOpt.isEmpty()) return bOpt;
            return Optional.of(aOpt.get().duration.compareTo(bOpt.get().duration) >= 0 ? aOpt.get() : bOpt.get());
        };
    }

    /**
     * There is also an operator followedBy to sequentially compose policies, i.e. if the first one wants to give up,
     * use the second one. As an example, we can retry with a 100ms delay 5 times and then retry every minute.
     *
     * @param other the other policy to be applied after this policy gives up
     * @return a new retry policy
     */
    default RetryPolicy followedBy(final RetryPolicy other) {
        return rs -> {
            Optional<Delay> delay = this.apply(rs);
            if (delay.isEmpty()) return other.apply(rs);
            return delay;
        };
    }

    /**
     * set an upper bound on the delay between retries
     *
     * @param cap the upper bound
     * @return a new policy
     * @see VertxRef#delay(Duration) to create delays
     */
    default RetryPolicy capDelay(final Delay cap) {
        return rs -> {
            Optional<Delay> delay = this.apply(rs);
            if (delay.isEmpty()) return delay;
            if (delay.get().duration.toMillis() >= cap.duration.toMillis()) return Optional.of(cap);
            return delay;
        };

    }

    /**
     * give up when the delay between retries reaches a certain limit
     *
     * @param max the limit
     * @return a new policy
     */
    default RetryPolicy limitRetriesByDelay(final Duration max) {
        return rs -> {
            Optional<Delay> delay = this.apply(rs);
            if (delay.isEmpty()) return delay;
            if (delay.get().duration.toMillis() >= max.toMillis()) return Optional.empty();
            return delay;
        };
    }

    /**
     * give up when the total delay reaches a certain limit
     *
     * @param max the limit
     * @return a new policy
     */
    default RetryPolicy limitRetriesByCumulativeDelay(final Duration max) {
        return rs -> {
            if (rs.cumulativeDelay <= max.toMillis()) return this.apply(rs);
            return Optional.empty();
        };
    }

    /**
     * runs this policy up to N iterations and gather results
     *
     * @param iterations the number of iterations
     * @return a list of {@link RetryStatus} that represents a simulation
     */
    default List<RetryStatus> simulate(int iterations) {
        List<RetryStatus> simulation = new ArrayList<>();
        RetryStatus first = new RetryStatus(0, 0, -1);
        RetryStatus next = first;

        for (int i = 1; i <= iterations; i++) {
            Optional<Delay> opt = this.apply(next);
            if (opt.isPresent()) {
                simulation.add(next);
                long delay = opt.get().duration.toMillis();
                next = new RetryStatus(next.counter + 1,
                                       next.cumulativeDelay + delay,
                                       delay
                );
            } else {
                break;
            }
        }
        return simulation;

    }


}
