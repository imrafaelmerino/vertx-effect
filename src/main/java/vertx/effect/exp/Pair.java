package vertx.effect.exp;

import io.vavr.Tuple2;
import vertx.effect.Val;
import vertx.effect.core.AbstractVal;

public abstract class Pair<A, B> extends AbstractVal<Tuple2<A, B>> {

    public static <A, B> Pair<A, B> sequential(final Val<A> _1,
                                               final Val<B> _2) {
        return new SequentialPair<>(_1,
                                    _2
        );
    }

    public static <A, B> Pair<A, B> parallel(final Val<A> _1,
                                             final Val<B> _2) {
        return new ParallelPair<>(_1,
                                  _2
        );
    }

    public abstract Val<A> first();

    public abstract Val<B> second();

    public static <O> Val<O> race(final Pair<O, O> pair) {
        if (pair == null)
            return Cons.failure(new IllegalArgumentException("Pair.race: pair is null"));
        return ListExp.parallel(pair.first(),
                                pair.second()
                               )
                      .race();
    }

    public static <O> Val<O> raceFirst(final Pair<O, O> pair) {
        if (pair == null)
            return Cons.failure(new IllegalArgumentException("Pair.raceFirst: pair is null"));
        return ListExp.parallel(pair.first(),
                                pair.second()
                               )
                      .raceFirst();
    }
}
