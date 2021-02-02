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
    public Val<Tuple4<A, B, C, D>> retry(final RetryPolicy policy) {
        return new SequentialQuadruple<>(_1.retry(policy),
                                         _2.retry(policy),
                                         _3.retry(policy),
                                         _4.retry(policy)
        );
    }


    @Override
    public Future<Tuple4<A, B, C, D>> get() {
        return _1
                 .flatMap(first -> _2
                                     .flatMap(sec -> _3
                                                       .flatMap(third -> _4
                                                                           .map(fourth -> new Tuple4<>(first,
                                                                                                       sec,
                                                                                                       third,
                                                                                                       fourth
                                                                                )
                                                                               )
                                                               )
                                             )
                         ).get();
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
