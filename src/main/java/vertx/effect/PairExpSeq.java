package vertx.effect;

import fun.tuple.Pair;
import io.vertx.core.Future;

import java.util.Objects;
import java.util.function.Predicate;

import static java.util.Objects.requireNonNull;

final class PairExpSeq<A, B> extends PairExp<A, B> {

    private final VIO<A> _1;
    private final VIO<B> _2;

    PairExpSeq(
            final VIO<A> _1,
            final VIO<B> _2
              ) {
        this._1 = requireNonNull(_1);
        this._2 = requireNonNull(_2);
    }


    @Override
    public VIO<Pair<A, B>> retryEach(final RetryPolicy policy) {
        return retryEach(e -> true,
                         policy
                        );

    }

    @Override
    public VIO<Pair<A, B>> retryEach(final Predicate<Throwable> predicate,
                                     final RetryPolicy policy
                                    ) {
        Objects.requireNonNull(policy);
        Objects.requireNonNull(predicate);
        return new PairExpSeq<>(_1.retry(predicate,
                                         policy
                                        ),
                                _2.retry(predicate,
                                         policy
                                        )
        );
    }


    @Override
    public Future<Pair<A, B>> get() {
        return _1
                .then(first -> _2
                        .map(sec -> Pair.of(first, sec))
                     )
                .get();

    }


    @Override
    public VIO<A> first() {
        return _1;
    }

    @Override
    public VIO<B> second() {
        return _2;
    }
}
