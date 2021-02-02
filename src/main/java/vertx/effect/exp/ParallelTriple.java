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

    ParallelTriple(final Val<A> _1,
                   final Val<B> _2,
                   final Val<C> _3) {
        this._1 = _1;
        this._2 = _2;
        this._3 = _3;
    }

    @Override
    public Val<Tuple3<A, B, C>> retry(final RetryPolicy policy) {

        return new ParallelTriple<>(_1.retry(policy),
                                    _2.retry(policy),
                                    _3.retry(policy)
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
