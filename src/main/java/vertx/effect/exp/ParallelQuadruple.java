package vertx.effect.exp;

import io.vavr.Tuple4;
import io.vertx.core.CompositeFuture;
import io.vertx.core.Future;
import vertx.effect.RetryPolicy;
import vertx.effect.Val;

import java.util.function.Predicate;


class ParallelQuadruple<A, B, C, D> extends Quadruple<A, B, C, D>  {

    private final Val<A> _1;
    private final Val<B> _2;
    private final Val<C> _3;
    private final Val<D> _4;

    ParallelQuadruple(final Val<A> _1,
                      final Val<B> _2,
                      final Val<C> _3,
                      final Val<D> _4) {
        this._1 = _1;
        this._2 = _2;
        this._3 = _3;
        this._4 = _4;
    }

    @Override
    public Val<Tuple4<A, B, C, D>> retryEach(final RetryPolicy policy) {
        return retryEach(e -> true,
                         policy);
    }

    @Override
    public Val<Tuple4<A, B, C, D>> retryEach(final Predicate<Throwable> predicate,
                                             final RetryPolicy policy) {
        if (policy == null) return Val.fail(new IllegalArgumentException("Quadruple.retryEach: policy is null"));
        if (predicate == null) return Val.fail(new IllegalArgumentException("Quadruple.retryEach: predicate is null"));
        return new ParallelQuadruple<>(_1.retry(predicate,
                                                policy),
                                       _2.retry(predicate,
                                                policy),
                                       _3.retry(predicate,
                                                policy),
                                       _4.retry(predicate,
                                                policy)
        );
    }

    @Override
    public Future<Tuple4<A, B, C, D>> get() {
        return CompositeFuture.all(_1.get(),
                                   _2.get(),
                                   _3.get(),
                                   _4.get()
                                  )
                              .map(it -> new Tuple4<>(it.resultAt(0),
                                                      it.resultAt(1),
                                                      it.resultAt(2),
                                                      it.resultAt(3)
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
}
