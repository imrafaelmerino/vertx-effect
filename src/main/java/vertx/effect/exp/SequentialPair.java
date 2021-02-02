package vertx.effect.exp;

import io.vavr.Tuple2;
import io.vertx.core.Future;
import vertx.effect.RetryPolicy;
import vertx.effect.Val;

import static java.util.Objects.requireNonNull;

final class SequentialPair<A, B> extends Pair<A, B> {

    private final Val<A> _1;
    private final Val<B> _2;
    private static final String ATTEMPTS_LOWER_THAN_ONE_ERROR = "attempts < 1";

    SequentialPair(final Val<A> _1,
                   final Val<B> _2) {
        this._1 = requireNonNull(_1);
        this._2 = requireNonNull(_2);
    }


    @Override
    public Val<Tuple2<A, B>> retry(final RetryPolicy policy) {
        return new SequentialPair<>(_1.retry(policy),
                                    _2.retry(policy)
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
