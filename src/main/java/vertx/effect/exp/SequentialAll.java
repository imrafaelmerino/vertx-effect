package vertx.effect.exp;

import io.vertx.core.Future;
import vertx.effect.RetryPolicy;
import vertx.effect.Val;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static java.util.Objects.requireNonNull;

final class SequentialAll extends All {

    private static final String ATTEMPTS_LOWER_THAN_ONE_ERROR = "attempts < 1";

    SequentialAll(final List<Val<Boolean>> exps) {
        this.exps = requireNonNull(exps);
    }

    private final List<Val<Boolean>> exps;


    @Override
    public Val<Boolean> retry(final int attempts) {
        if (attempts < 1)
            return Cons.failure(new IllegalArgumentException(ATTEMPTS_LOWER_THAN_ONE_ERROR));

        return new SequentialAll(exps.stream()
                                     .map(it -> it.retry(attempts))
                                     .collect(Collectors.toList()));
    }


    @Override
    public Val<Boolean> retry(final int attempts,
                              final BiFunction<Throwable, Integer, Val<Void>> actionBeforeRetry) {
        if (attempts < 1)
            return Cons.failure(new IllegalArgumentException(ATTEMPTS_LOWER_THAN_ONE_ERROR));

        if (actionBeforeRetry == null)
            return Cons.failure(new NullPointerException("actionBeforeRetry is null"));

        return new SequentialAll(exps.stream()
                                     .map(it -> it.retry(attempts,
                                                         actionBeforeRetry
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
        return new SequentialAll(exps.stream()
                                     .map(it -> it.retry(predicate,
                                                         attempts
                                                        ))
                                     .collect(Collectors.toList()));
    }


    @Override
    public Val<Boolean> retry(final Predicate<Throwable> predicate,
                              final int attempts,
                              final RetryPolicy<Throwable> actionBeforeRetry) {
        if (attempts < 1)
            return Cons.failure(new IllegalArgumentException(ATTEMPTS_LOWER_THAN_ONE_ERROR));
        if (predicate == null)
            return Cons.failure(new NullPointerException("predicate is null"));
        return new SequentialAll(exps.stream()
                                     .map(it -> it.retry(predicate,
                                                         attempts,
                                                         actionBeforeRetry
                                                        ))
                                     .collect(Collectors.toList()));
    }


    @Override
    public Future<Boolean> get() {
        return get(exps);
    }

    private Future<Boolean> get(List<Val<Boolean>> exps) {

        if (exps.size() == 1) return exps.get(0)
                                         .get();
        else return exps.get(0)
                        .get()
                        .flatMap(bool -> {
                                     if (!bool) return Future.succeededFuture(false);
                                     else return get(exps.subList(1,
                                                                  exps.size()
                                                                 )
                                                    );
                                 }
                                );
    }
}
