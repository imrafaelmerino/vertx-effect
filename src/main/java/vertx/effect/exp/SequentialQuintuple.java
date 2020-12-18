package vertx.effect.exp;

import io.vavr.Tuple5;
import io.vertx.core.Future;
import vertx.effect.Val;

import java.util.function.BiFunction;
import java.util.function.Predicate;


final class SequentialQuintuple<A, B, C, D, E> extends Quintuple<A, B, C, D, E> {

    private final Val<A> _1;
    private final Val<B> _2;
    private final Val<C> _3;
    private final Val<D> _4;
    private final Val<E> _5;
    private static final String ATTEMPTS_LOWER_THAN_ONE_ERROR = "attempts < 1";


    SequentialQuintuple(final Val<A> _1,
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
        return new SequentialQuintuple<>(_1.retry(attempts),
                                         _2.retry(attempts),
                                         _3.retry(attempts),
                                         _4.retry(attempts),
                                         _5.retry(attempts)
        );
    }


    @Override
    public Val<Tuple5<A, B, C, D, E>> retry(final int attempts,
                                            final BiFunction<Throwable, Integer, Val<Void>> actionBeforeRetry) {
        if (attempts < 1)
            return Cons.failure(new IllegalArgumentException(ATTEMPTS_LOWER_THAN_ONE_ERROR));
        if (actionBeforeRetry == null)
            return Cons.failure(new NullPointerException("actionBeforeRetry is null"));

        return new SequentialQuintuple<>(_1.retry(attempts,
                                                  actionBeforeRetry
                                                 ),
                                         _2.retry(attempts,
                                                  actionBeforeRetry
                                                 ),
                                         _3.retry(attempts,
                                                  actionBeforeRetry
                                                 ),
                                         _4.retry(attempts,
                                                  actionBeforeRetry
                                                 ),
                                         _5.retry(attempts,
                                                  actionBeforeRetry
                                                 )
        );
    }

    @Override
    public Val<Tuple5<A, B, C, D, E>> retryIf(final Predicate<Throwable> predicate,
                                              final int attempts) {
        if (attempts < 1)
            return Cons.failure(new IllegalArgumentException(ATTEMPTS_LOWER_THAN_ONE_ERROR));
        if (predicate == null)
            return Cons.failure(new NullPointerException("predicate is null"));


        return new SequentialQuintuple<>(_1.retryIf(predicate,
                                                    attempts
                                                   ),
                                         _2.retryIf(predicate,
                                                    attempts
                                                   ),
                                         _3.retryIf(predicate,
                                                    attempts
                                                   ),
                                         _4.retryIf(predicate,
                                                    attempts
                                                   ),
                                         _5.retryIf(predicate,
                                                    attempts
                                                   )
        );
    }


    @Override
    public Val<Tuple5<A, B, C, D, E>> retryIf(final Predicate<Throwable> predicate,
                                              final int attempts,
                                              final BiFunction<Throwable, Integer, Val<Void>> actionBeforeRetry) {
        if (attempts < 1)
            return Cons.failure(new IllegalArgumentException(ATTEMPTS_LOWER_THAN_ONE_ERROR));
        if (predicate == null)
            return Cons.failure(new NullPointerException("predicate is null"));
        if (actionBeforeRetry == null)
            return Cons.failure(new NullPointerException("actionBeforeRetry is null"));

        return new SequentialQuintuple<>(_1.retryIf(predicate,
                                                    attempts,
                                                    actionBeforeRetry
                                                   ),
                                         _2.retryIf(predicate,
                                                    attempts,
                                                    actionBeforeRetry
                                                   ),
                                         _3.retryIf(predicate,
                                                    attempts,
                                                    actionBeforeRetry
                                                   ),
                                         _4.retryIf(predicate,
                                                    attempts,
                                                    actionBeforeRetry
                                                   ),
                                         _5.retryIf(predicate,
                                                    attempts,
                                                    actionBeforeRetry
                                                   )
        );
    }


    @Override
    public Future<Tuple5<A, B, C, D, E>> get() {
        return _1.get()
                 .flatMap(first -> _2.get()
                                     .flatMap(sec -> _3.get()
                                                       .flatMap(third -> _4.get()
                                                                           .flatMap(fourth -> _5.get()
                                                                                                .map(fifth -> new Tuple5<>(first,
                                                                                                                           sec,
                                                                                                                           third,
                                                                                                                           fourth,
                                                                                                                           fifth
                                                                                                ))
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

    @Override
    public Val<E> _5() {
        return _5;
    }
}
