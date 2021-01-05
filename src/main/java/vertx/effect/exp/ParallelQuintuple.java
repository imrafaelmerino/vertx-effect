package vertx.effect.exp;

import io.vavr.Tuple5;
import io.vertx.core.CompositeFuture;
import io.vertx.core.Future;
import vertx.effect.RetryPolicy;
import vertx.effect.Val;
import java.util.function.BiFunction;
import java.util.function.Predicate;


final class ParallelQuintuple<A, B, C, D, E> extends Quintuple<A, B, C, D, E> {

    private final Val<A> _1;
    private final Val<B> _2;
    private final Val<C> _3;
    private final Val<D> _4;
    private final Val<E> _5;
    private static final String ATTEMPTS_LOWER_THAN_ONE_ERROR = "attempts < 1";


    ParallelQuintuple(final Val<A> _1,
                      final Val<B> _2,
                      final Val<C> _3,
                      final Val<D> _4,
                      final Val<E> _5) {
        this._1 = _1;
        this._2 = _2;
        this._3 = _3;
        this._4 = _4;
        this._5 = _5;
    }

    @Override
    public Val<Tuple5<A, B, C, D, E>> retry(final int attempts) {
        if (attempts < 1)
            return Cons.failure(new IllegalArgumentException(ATTEMPTS_LOWER_THAN_ONE_ERROR));
        return new ParallelQuintuple<>(_1.retry(attempts),
                                       _2.retry(attempts),
                                       _3.retry(attempts),
                                       _4.retry(attempts),
                                       _5.retry(attempts)
        );
    }


    @Override
    public Val<Tuple5<A, B, C, D, E>> retry(final int attempts,
                                            final BiFunction<Throwable, Integer, Val<Void>> retryPolicy) {
        if (attempts < 1)
            return Cons.failure(new IllegalArgumentException(ATTEMPTS_LOWER_THAN_ONE_ERROR));
        if (retryPolicy == null)
            return Cons.failure(new NullPointerException("retryPolicy is null"));

        return new ParallelQuintuple<>(_1.retry(attempts,
                                                retryPolicy
                                               ),
                                       _2.retry(attempts,
                                                retryPolicy
                                               ),
                                       _3.retry(attempts,
                                                retryPolicy
                                               ),
                                       _4.retry(attempts,
                                                retryPolicy
                                               ),
                                       _5.retry(attempts,
                                                retryPolicy
                                               )
        );
    }

    @Override
    public Val<Tuple5<A, B, C, D, E>> retry(final Predicate<Throwable> predicate,
                                            final int attempts) {
        if (attempts < 1)
            return Cons.failure(new IllegalArgumentException(ATTEMPTS_LOWER_THAN_ONE_ERROR));
        if (predicate == null)
            return Cons.failure(new NullPointerException("predicate is null"));


        return new ParallelQuintuple<>(_1.retry(predicate,
                                                attempts
                                               ),
                                       _2.retry(predicate,
                                                attempts
                                               ),
                                       _3.retry(predicate,
                                                attempts
                                               ),
                                       _4.retry(predicate,
                                                attempts
                                               ),
                                       _5.retry(predicate,
                                                attempts
                                               )
        );
    }


    @Override
    public Val<Tuple5<A, B, C, D, E>> retry(final Predicate<Throwable> predicate,
                                            final int attempts,
                                            final RetryPolicy<Throwable> retryPolicy) {
        if (attempts < 1)
            return Cons.failure(new IllegalArgumentException(ATTEMPTS_LOWER_THAN_ONE_ERROR));
        if (predicate == null)
            return Cons.failure(new NullPointerException("predicate is null"));
        if (retryPolicy == null)
            return Cons.failure(new NullPointerException("retryPolicy is null"));

        return new ParallelQuintuple<>(_1.retry(predicate,
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
                                               ),
                                       _4.retry(predicate,
                                                attempts,
                                                retryPolicy
                                               ),
                                       _5.retry(predicate,
                                                attempts,
                                                retryPolicy
                                               )
        );
    }


    @Override
    public Future<Tuple5<A, B, C, D, E>> get() {
        return CompositeFuture.all(_1.get(),
                                   _2.get(),
                                   _3.get(),
                                   _4.get(),
                                   _5.get()
                                  )
                              .map(it -> new Tuple5<>(it.resultAt(0),
                                                      it.resultAt(1),
                                                      it.resultAt(2),
                                                      it.resultAt(3),
                                                      it.resultAt(4)
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

    @Override
    public Val<D> _4() {
        return _4;
    }

    @Override
    public Val<E> _5() {
        return _5;
    }


}
