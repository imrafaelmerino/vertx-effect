package vertx.effect.exp;

import io.vertx.core.CompositeFuture;
import io.vertx.core.Future;
import vertx.effect.RetryPolicy;
import vertx.effect.Val;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static java.util.Objects.requireNonNull;

final class ParallelAll extends All {

    private static final String ATTEMPTS_LOWER_THAN_ONE_ERROR = "attempts < 1";

    ParallelAll(final List<Val<Boolean>> exps) {
        this.exps = requireNonNull(exps);
    }

    private final List<Val<Boolean>> exps;

    @Override
    public Val<Boolean> retry(final int attempts) {
        if (attempts < 1)
            return Cons.failure(new IllegalArgumentException(ATTEMPTS_LOWER_THAN_ONE_ERROR));

        return new ParallelAll(exps.stream()
                                   .map(it -> it.retry(attempts))
                                   .collect(Collectors.toList()));
    }


    @Override
    public Val<Boolean> retry(final int attempts,
                              final BiFunction<Throwable, Integer, Val<Void>> retryPolicy) {
        if (attempts < 1)
            return Cons.failure(new IllegalArgumentException(ATTEMPTS_LOWER_THAN_ONE_ERROR));

        if (retryPolicy == null)
            return Cons.failure(new NullPointerException("retryPolicy is null"));

        return new ParallelAll(exps.stream()
                                   .map(it -> it.retry(attempts,
                                               retryPolicy
                                              ))
                                   .collect(Collectors.toList())
        );
    }

    @Override
    public Val<Boolean> retry(final Predicate<Throwable> predicate,
                              final int attempts) {
        if (attempts < 1)
            return Cons.failure(new IllegalArgumentException(ATTEMPTS_LOWER_THAN_ONE_ERROR));

        if (predicate == null)
            return Cons.failure(new NullPointerException("predicate is null"));
        return new ParallelAll(exps.stream()
                                   .map(it -> it.retry(predicate,
                                                       attempts
                                                      ))
                                   .collect(Collectors.toList()));
    }


    @Override
    public Val<Boolean> retry(final Predicate<Throwable> predicate,
                              final int attempts,
                              final RetryPolicy<Throwable> retryPolicy) {
        if (attempts < 1)
            return Cons.failure(new IllegalArgumentException(ATTEMPTS_LOWER_THAN_ONE_ERROR));
        if (predicate == null)
            return Cons.failure(new NullPointerException("predicate is null"));
        return new ParallelAll(exps.stream()
                                   .map(it -> it.retry(predicate,
                                                       attempts,
                                                       retryPolicy
                                                      ))
                                   .collect(Collectors.toList()));
    }



    @Override
    public Future<Boolean> get() {
        return CompositeFuture.all(exps.stream()
                                       .map(Supplier::get)
                                       .collect(Collectors.toList()))
                              .map(l -> l.result()
                                         .list()
                                         .stream()
                                         .allMatch(it -> it instanceof Boolean && (Boolean) it)
                                  );
    }
}
