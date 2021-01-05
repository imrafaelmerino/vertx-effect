package vertx.effect.exp;

import io.vavr.Tuple3;
import io.vertx.core.CompositeFuture;
import io.vertx.core.Future;
import vertx.effect.RetryPolicy;
import vertx.effect.Val;
import java.util.function.BiFunction;
import java.util.function.Predicate;


public final class ParallelTriple<A, B, C> extends Triple<A, B, C> {

    private final Val<A> _1;
    private final Val<B> _2;
    private final Val<C> _3;
    private static final String ATTEMPTS_LOWER_THAN_ONE_ERROR = "attempts < 1";

    ParallelTriple(final Val<A> _1,
                   final Val<B> _2,
                   final Val<C> _3) {
        this._1 = _1;
        this._2 = _2;
        this._3 = _3;
    }

    @Override
    public Val<Tuple3<A, B, C>> retry(final int attempts) {
        if (attempts < 1)
            return Cons.failure(new IllegalArgumentException(ATTEMPTS_LOWER_THAN_ONE_ERROR));

        return new ParallelTriple<>(_1.retry(attempts),
                                    _2.retry(attempts),
                                    _3.retry(attempts)
        );
    }


    @Override
    public Val<Tuple3<A, B, C>> retry(final int attempts,
                                      final BiFunction<Throwable, Integer, Val<Void>> retryPolicy) {
        if (attempts < 1)
            return Cons.failure(new IllegalArgumentException(ATTEMPTS_LOWER_THAN_ONE_ERROR));
        if (retryPolicy == null)
            return Cons.failure(new NullPointerException("retryPolicy is null"));
        return new ParallelTriple<>(_1.retry(attempts,
                                             retryPolicy
                                            ),
                                    _2.retry(attempts,
                                             retryPolicy
                                            ),
                                    _3.retry(attempts,
                                             retryPolicy
                                            )
        );
    }

    @Override
    public Val<Tuple3<A, B, C>> retry(final Predicate<Throwable> predicate,
                                      final int attempts) {
        if (attempts < 1)
            return Cons.failure(new IllegalArgumentException(ATTEMPTS_LOWER_THAN_ONE_ERROR));
        if (predicate == null)
            return Cons.failure(new NullPointerException("predicate is null"));
        return new ParallelTriple<>(_1.retry(predicate,
                                             attempts
                                            ),
                                    _2.retry(predicate,
                                             attempts
                                            ),
                                    _3.retry(predicate,
                                             attempts
                                            )
        );
    }


    @Override
    public Val<Tuple3<A, B, C>> retry(final Predicate<Throwable> predicate,
                                      final int attempts,
                                      final RetryPolicy<Throwable> retryPolicy) {
        if (attempts < 1)
            return Cons.failure(new IllegalArgumentException(ATTEMPTS_LOWER_THAN_ONE_ERROR));
        if (predicate == null)
            return Cons.failure(new NullPointerException("predicate is null"));
        if (retryPolicy == null)
            return Cons.failure(new NullPointerException("retryPolicy is null"));

        return new ParallelTriple<>(_1.retry(predicate,
                                             attempts,
                                             retryPolicy
                                            ),
                                    _2.retry(predicate,
                                             attempts,
                                             retryPolicy
                                            ),
                                    _3.retry(predicate,
                                             attempts,
                                             retryPolicy
                                            )
        );
    }

    @Override
    public Future<Tuple3<A, B, C>> get() {
        return CompositeFuture.all(_1.get(),
                                   _2.get(),
                                   _3.get()
                                  )
                              .map(it -> new Tuple3<>(it.resultAt(0),
                                                      it.resultAt(1),
                                                      it.resultAt(2)
                              ));
    }

    @Override
    public Val<A> _1() {
        return _1;
    }

    @Override
    public Val<B> _2() {
        return _2;
    }

    @Override
    public Val<C> _3() {
        return _3;
    }

}
