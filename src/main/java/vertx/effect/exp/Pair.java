package vertx.effect.exp;

import io.vavr.Tuple2;
import vertx.effect.Val;
import vertx.effect.core.AbstractVal;

public abstract class Pair<A, B> extends AbstractVal<Tuple2<A, B>> {


    public static <A, B> Pair<A, B> sequential(final Val<A> _1,
                                        final Val<B> _2) {
        return new SeqPair<>(_1,
                             _2
        );
    }

    public static <A, B> Pair<A, B> parallel(final Val<A> _1,
                                      final Val<B> _2) {
        return new ParallelPair<>(_1,
                                  _2
        );
    }
}
