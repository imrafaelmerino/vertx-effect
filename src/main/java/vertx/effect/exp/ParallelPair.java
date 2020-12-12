package vertx.effect.exp;

import io.vavr.Tuple2;
import io.vertx.core.CompositeFuture;
import io.vertx.core.Future;
import vertx.effect.Val;
import java.util.function.BiFunction;
import java.util.function.Predicate;

import static java.util.Objects.requireNonNull;

final class ParallelPair<A, B> extends Pair<A, B> {

    private final Val<A> _1;
    private final Val<B> _2;
    private static final String ATTEMPTS_LOWER_THAN_ONE_ERROR = "attempts < 1";

    ParallelPair(final Val<A> _1,
                 final Val<B> _2) {
        this._1 = requireNonNull(_1);
        this._2 = requireNonNull(_2);
    }

    @Override
    public Val<Tuple2<A, B>> retry(final int attempts) {
        if (attempts < 1)
            return Cons.failure(new IllegalArgumentException(ATTEMPTS_LOWER_THAN_ONE_ERROR));
        return new ParallelPair<>(_1.retry(attempts),
                                  _2.retry(attempts)
        );
    }

    @Override
    public Val<Tuple2<A, B>> retry(final int attempts,
                                   final BiFunction<Throwable, Integer, Val<Void>> actionBeforeRetry) {
        if (attempts < 1)
            return Cons.failure(new IllegalArgumentException(ATTEMPTS_LOWER_THAN_ONE_ERROR));
        if (actionBeforeRetry == null)
            return Cons.failure(new NullPointerException("actionBeforeRetry is null"));
        return new ParallelPair<>(_1.retry(attempts,
                                           actionBeforeRetry
                                          ),
                                  _2.retry(attempts,
                                   actionBeforeRetry
                                  )
        );
    }

    @Override
    public Val<Tuple2<A, B>> retryIf(final Predicate<Throwable> predicate,
                                     final int attempts) {

        if (attempts < 1)
            return Cons.failure(new IllegalArgumentException(ATTEMPTS_LOWER_THAN_ONE_ERROR));
        if (predicate == null)
            return Cons.failure(new NullPointerException("predicate is null"));
        return new ParallelPair<>(_1.retryIf(predicate,
                                             attempts
                                            ),
                                  _2.retryIf(predicate,
                                     attempts
                                    )
        );
    }


    @Override
    public Val<Tuple2<A, B>> retryIf(final Predicate<Throwable> predicate,
                                     final int attempts,
                                     final BiFunction<Throwable, Integer, Val<Void>> actionBeforeRetry) {
        if (attempts < 1)
            return Cons.failure(new IllegalArgumentException(ATTEMPTS_LOWER_THAN_ONE_ERROR));
        if (predicate == null)
            return Cons.failure(new NullPointerException("predicate is null"));
        if (actionBeforeRetry == null)
            return Cons.failure(new NullPointerException("actionBeforeRetry is null"));

        return new ParallelPair<>(_1.retryIf(predicate,
                                             attempts
                                            ),
                                  _2.retryIf(predicate,
                                     attempts
                                    )
        );
    }


    @Override
    public Future<Tuple2<A, B>> get() {
        return CompositeFuture.all(_1.get(),
                                   _2.get()
                                  )
                              .map(it -> new Tuple2<>(it.resultAt(0),
                                                      it.resultAt(1)
                                  )
                                  );
    }



}
