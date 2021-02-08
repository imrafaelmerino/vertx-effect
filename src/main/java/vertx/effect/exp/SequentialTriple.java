package vertx.effect.exp;

import io.vavr.Tuple3;
import io.vertx.core.Future;
import vertx.effect.RetryPolicy;
import vertx.effect.Val;

import java.util.function.Predicate;


public final class SequentialTriple<A, B, C> extends Triple<A, B, C>  {

    private final Val<A> _1;
    private final Val<B> _2;
    private final Val<C> _3;

    SequentialTriple(final Val<A> _1,
                     final Val<B> _2,
                     final Val<C> _3) {
        this._1 = _1;
        this._2 = _2;
        this._3 = _3;
    }


    @Override
    public Val<Tuple3<A, B, C>> retryEach(final RetryPolicy policy) {
        return retryEach(e -> true,
                         policy);

    }

    @Override
    public Val<Tuple3<A, B, C>> retryEach(final Predicate<Throwable> predicate,
                                          final RetryPolicy policy) {
        if (policy == null) return Val.fail(new IllegalArgumentException("Cons.retry: policy is null"));
        if (predicate == null) return Val.fail(new IllegalArgumentException("Cons.retry: predicate is null"));
        return new SequentialTriple<>(_1.retry(predicate,
                                               policy),
                                      _2.retry(predicate,
                                               policy),
                                      _3.retry(predicate,
                                               policy)
        );
    }


    @Override
    public Future<Tuple3<A, B, C>> get() {
        return _1.flatMap(first -> _2
                                  .flatMap(sec -> _3
                                                   .map(third -> new Tuple3<>(first,
                                                                              sec,
                                                                              third
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
}
