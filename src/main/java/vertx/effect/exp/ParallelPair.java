package vertx.effect.exp;

import io.vavr.Tuple2;
import io.vertx.core.CompositeFuture;
import io.vertx.core.Future;
import vertx.effect.RetryPolicy;
import vertx.effect.Val;


import static java.util.Objects.requireNonNull;

final class ParallelPair<A, B> extends Pair<A, B> {

    private final Val<A> _1;
    private final Val<B> _2;

    ParallelPair(final Val<A> _1,
                 final Val<B> _2) {
        this._1 = requireNonNull(_1);
        this._2 = requireNonNull(_2);
    }

    @Override
    public Val<Tuple2<A, B>> retry(final RetryPolicy policy) {
        return new ParallelPair<>(_1.retry(policy),
                                  _2.retry(policy)
        );
    }

    @Override
    public Future<Tuple2<A, B>> get() {
        return CompositeFuture.all(_1.get(),
                                   _2.get()
                                  )
                              .map(it -> new Tuple2<>(it.resultAt(0),
                                                      it.resultAt(1))
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
}
