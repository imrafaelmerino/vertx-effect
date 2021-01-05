package vertx.effect.exp;

import io.vavr.Tuple4;
import io.vertx.core.Future;
import vertx.effect.RetryPolicy;
import vertx.effect.Val;

import java.util.function.BiFunction;
import java.util.function.Predicate;


class SequentialQuadruple<A, B, C, D> extends Quadruple<A, B, C, D> {

    private final Val<A> _1;
    private final Val<B> _2;
    private final Val<C> _3;
    private final Val<D> _4;
    private static final String ATTEMPTS_LOWER_THAN_ONE_ERROR = "attempts < 1";

    SequentialQuadruple(final Val<A> _1,
                        final Val<B> _2,
                        final Val<C> _3,
                        final Val<D> _4) {
        this._1 = _1;
        this._2 = _2;
        this._3 = _3;
        this._4 = _4;
    }


    @Override
    public Val<Tuple4<A, B, C, D>> retry(final int attempts) {
        if (attempts < 1)
            return Cons.failure(new IllegalArgumentException(ATTEMPTS_LOWER_THAN_ONE_ERROR));

        return new SequentialQuadruple<>(_1.retry(attempts),
                                         _2.retry(attempts),
                                         _3.retry(attempts),
                                         _4.retry(attempts)
        );
    }


    @Override
    public Val<Tuple4<A, B, C, D>> retry(final int attempts,
                                         final BiFunction<Throwable, Integer, Val<Void>> retryPolicy) {

        if (attempts < 1)
            return Cons.failure(new IllegalArgumentException(ATTEMPTS_LOWER_THAN_ONE_ERROR));
        if (retryPolicy == null)
            return Cons.failure(new NullPointerException("retryPolicy is null"));


        return new SequentialQuadruple<>(_1.retry(attempts,
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
                                                 )
        );
    }

    @Override
    public Val<Tuple4<A, B, C, D>> retry(final Predicate<Throwable> predicate,
                                         final int attempts) {
        if (attempts < 1)
            return Cons.failure(new IllegalArgumentException(ATTEMPTS_LOWER_THAN_ONE_ERROR));
        if (predicate == null)
            return Cons.failure(new NullPointerException("predicate is null"));

        return new SequentialQuadruple<>(_1.retry(predicate,
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
                                                 )
        );
    }


    @Override
    public Val<Tuple4<A, B, C, D>> retry(final Predicate<Throwable> predicate,
                                         final int attempts,
                                         final RetryPolicy<Throwable> retryPolicy) {
        if (attempts < 1)
            return Cons.failure(new IllegalArgumentException(ATTEMPTS_LOWER_THAN_ONE_ERROR));
        if (predicate == null)
            return Cons.failure(new NullPointerException("predicate is null"));
        if (retryPolicy == null)
            return Cons.failure(new NullPointerException("retryPolicy is null"));

        return new SequentialQuadruple<>(_1.retry(predicate,
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
                                                 )
        );
    }

    @Override
    public Future<Tuple4<A, B, C, D>> get() {
        return _1.get()
                 .flatMap(first -> _2.get()
                                     .flatMap(sec -> _3.get()
                                                       .flatMap(third -> _4.get()
                                                                           .map(fourth -> new Tuple4<>(first,
                                                                                                       sec,
                                                                                                       third,
                                                                                                       fourth
                                                                                )
                                                                               )
                                                               )
                                             )
                         );
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

}
