package vertx.effect.exp;

import io.vavr.Tuple5;
import io.vertx.core.Future;
import vertx.effect.RetryPolicy;
import vertx.effect.Val;

import java.util.function.Predicate;


final class SequentialQuintuple<A, B, C, D, E> extends Quintuple<A,B,C,D,E> {

    private final Val<A> _1;
    private final Val<B> _2;
    private final Val<C> _3;
    private final Val<D> _4;
    private final Val<E> _5;


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
    public Val<Tuple5<A, B, C, D, E>> retryEach(final RetryPolicy policy) {
        return retryEach(e -> true,
                         policy);
    }

    @Override
    public Val<Tuple5<A, B, C, D, E>> retryEach(final Predicate<Throwable> predicate,
                                                final RetryPolicy policy) {
        if (policy == null) return Val.fail(new IllegalArgumentException("Cons.retry: policy is null"));
        if (predicate == null) return Val.fail(new IllegalArgumentException("Cons.retry: predicate is null"));
        return new SequentialQuintuple<>(_1.retry(predicate,
                                                  policy),
                                         _2.retry(predicate,
                                                  policy),
                                         _3.retry(predicate,
                                                  policy),
                                         _4.retry(predicate,
                                                  policy),
                                         _5.retry(predicate,
                                                  policy)
        );
    }


    @Override
    public Future<Tuple5<A, B, C, D, E>> get() {
        return _1
                .flatMap(first -> _2
                                 .flatMap(sec -> _3
                                                  .flatMap(third -> _4
                                                                   .flatMap(fourth -> _5
                                                                                    .map(fifth -> new Tuple5<>(first,
                                                                                                               sec,
                                                                                                               third,
                                                                                                               fourth,
                                                                                                               fifth
                                                                                    ))
                                                                           )
                                                          )
                                         )
                        )
                .get();
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
