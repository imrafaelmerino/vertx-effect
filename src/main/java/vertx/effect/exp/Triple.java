package vertx.effect.exp;

import io.vavr.Tuple3;
import vertx.effect.Val;
import vertx.effect.core.AbstractVal;

import static java.util.Objects.requireNonNull;

public abstract class Triple<A, B, C> extends AbstractVal<Tuple3<A, B, C>> {

    public static <A, B, C> Triple<A, B, C> parallel(final Val<A> _1,
                                                     final Val<B> _2,
                                                     final Val<C> _3) {
        return new ParallelTriple<>(requireNonNull(_1),
                                    requireNonNull(_2),
                                    requireNonNull(_3)
        );
    }

    public static <A, B, C> Triple<A, B, C> sequential(final Val<A> _1,
                                                       final Val<B> _2,
                                                       final Val<C> _3) {
        return new SequentialTriple<A, B, C>(requireNonNull(_1),
                                             requireNonNull(_2),
                                             requireNonNull(_3)
        );
    }

    public abstract Val<A> first();

    public abstract Val<B> second();

    public abstract Val<C> third();

    public static <O> Val<O> race(final Triple<O, O, O> triple) {
        if (triple == null)
            return Cons.failure(new IllegalArgumentException("Triple.race: pair is null"));
        return ListExp.parallel(triple.first(),
                                triple.second(),
                                triple.third()
                               )
                      .race();
    }

    public static <O> Val<O> raceFirst(final Triple<O, O, O> triple) {
        if (triple == null)
            return Cons.failure(new IllegalArgumentException("Triple.race: pair is null"));
        return ListExp.parallel(triple.first(),
                                triple.second(),
                                triple.third()
                               )
                      .raceFirst();
    }
}
