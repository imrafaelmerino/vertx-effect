package vertx.effect.exp;

import io.vavr.Tuple2;
import io.vertx.core.Future;
import vertx.effect.RetryPolicy;
import vertx.effect.Val;

import java.util.function.Predicate;

import static java.util.Objects.requireNonNull;

final class SequentialPair<A, B> extends Pair<A, B>  {

    private final Val<A> _1;
    private final Val<B> _2;

    SequentialPair(final Val<A> _1,
                   final Val<B> _2) {
        this._1 = requireNonNull(_1);
        this._2 = requireNonNull(_2);
    }


    @Override
    public Val<Tuple2<A, B>> retryEach(final RetryPolicy policy) {
        return retryEach(e -> true,
                         policy
                        );

    }

    @Override
    public Val<Tuple2<A, B>> retryEach(final Predicate<Throwable> predicate,
                                       final RetryPolicy policy) {
        if (policy == null) return Val.fail(new IllegalArgumentException("Cons.retry: policy is null"));
        if (predicate == null) return Val.fail(new IllegalArgumentException("Cons.retry: predicate is null"));
        return new SequentialPair<>(_1.retry(predicate,
                                             policy
                                            ),
                                    _2.retry(predicate,
                                             policy
                                            )
        );
    }


    @Override
    public Future<Tuple2<A, B>> get() {
        return _1
                .flatMap(first -> _2
                                 .map(sec -> new Tuple2<>(first,
                                                          sec
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
}
