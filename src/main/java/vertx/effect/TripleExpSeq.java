package vertx.effect;

import fun.tuple.Triple;
import io.vertx.core.Future;

import java.util.function.Predicate;


public final class TripleExpSeq<A, B, C> extends TripleExp<A, B, C> {

    private final VIO<A> _1;
    private final VIO<B> _2;
    private final VIO<C> _3;

    TripleExpSeq(final VIO<A> _1,
                 final VIO<B> _2,
                 final VIO<C> _3
                ) {
        this._1 = _1;
        this._2 = _2;
        this._3 = _3;
    }


    @Override
    public VIO<Triple<A, B, C>> retryEach(final RetryPolicy policy) {
        return retryEach(e -> true,
                         policy
                        );

    }

    @Override
    public VIO<Triple<A, B, C>> retryEach(final Predicate<Throwable> predicate,
                                          final RetryPolicy policy
                                         ) {
        if (policy == null) throw new IllegalArgumentException("policy is null");
        if (predicate == null) throw new IllegalArgumentException("predicate is null");
        return new TripleExpSeq<>(_1.retry(predicate,
                                           policy
                                          ),
                                  _2.retry(predicate,
                                           policy
                                          ),
                                  _3.retry(predicate,
                                           policy
                                          )
        );
    }


    @Override
    public Future<Triple<A, B, C>> get() {
        return _1.then(first -> _2
                         .then(sec -> _3
                                 .map(third -> Triple.of(first,
                                                         sec,
                                                         third
                                                        )
                                     )
                              )
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

    @Override
    public VIO<C> third() {
        return _3;
    }
}
