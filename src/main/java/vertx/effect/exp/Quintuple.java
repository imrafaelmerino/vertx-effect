package vertx.effect.exp;

import io.vavr.Tuple5;
import vertx.effect.Val;
import vertx.effect.core.AbstractVal;

import static java.util.Objects.requireNonNull;

public abstract class Quintuple<A, B, C, D, E> extends AbstractVal<Tuple5<A, B, C, D, E>> {

    public static <A, B, C, D, E> Quintuple<A, B, C, D, E> parallel(final Val<A> _1,
                                                                    final Val<B> _2,
                                                                    final Val<C> _3,
                                                                    final Val<D> _4,
                                                                    final Val<E> _5) {
        return new ParallelQuintuple<>(requireNonNull(_1),
                                       requireNonNull(_2),
                                       requireNonNull(_3),
                                       requireNonNull(_4),
                                       requireNonNull(_5)
        );
    }

    public static <A, B, C, D, E> Quintuple<A, B, C, D, E> sequential(final Val<A> _1,
                                                                      final Val<B> _2,
                                                                      final Val<C> _3,
                                                                      final Val<D> _4,
                                                                      final Val<E> _5) {
        return new SequentialQuintuple<>(requireNonNull(_1),
                                         requireNonNull(_2),
                                         requireNonNull(_3),
                                         requireNonNull(_4),
                                         requireNonNull(_5)
        );
    }

    public abstract Val<A> first();

    public abstract Val<B> second();

    public abstract Val<C> third();

    public abstract Val<D> forth();

    public abstract Val<E> fifth();


    public static <O> Val<O> race(final Quintuple<O, O, O, O, O> quintuple) {
        if (quintuple == null)
            return Cons.failure(new IllegalArgumentException("Quintuple.race: pair is null"));
        return ListExp.parallel(quintuple.first(),
                                quintuple.second(),
                                quintuple.third(),
                                quintuple.forth(),
                                quintuple.fifth()
                               )
                      .race();
    }

    public static <O> Val<O> raceFirst(final Quintuple<O, O, O, O, O> quintuple) {
        if (quintuple == null)
            return Cons.failure(new IllegalArgumentException("Quintuple.race: pair is null"));
        return ListExp.parallel(quintuple.first(),
                                quintuple.second(),
                                quintuple.third(),
                                quintuple.forth(),
                                quintuple.fifth()
                               )
                      .raceFirst();
    }
}
