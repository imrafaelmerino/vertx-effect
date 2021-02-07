package vertx.effect.exp;

import io.vavr.Tuple4;
import vertx.effect.Val;
import vertx.effect.core.AbstractVal;

import static java.util.Objects.requireNonNull;

public abstract class Quadruple<A, B, C, D> extends AbstractVal<Tuple4<A, B, C, D>> implements Exp<Tuple4<A, B, C, D>>{

    public static <A, B, C, D> Quadruple<A, B, C, D> parallel(final Val<A> _1,
                                                              final Val<B> _2,
                                                              final Val<C> _3,
                                                              final Val<D> _4) {
        return new ParallelQuadruple<>(requireNonNull(_1),
                                       requireNonNull(_2),
                                       requireNonNull(_3),
                                       requireNonNull(_4)
        );
    }

    public static <A, B, C, D> Quadruple<A, B, C, D> sequential(final Val<A> _1,
                                                                final Val<B> _2,
                                                                final Val<C> _3,
                                                                final Val<D> _4) {
        return new SequentialQuadruple<>(requireNonNull(_1),
                                         requireNonNull(_2),
                                         requireNonNull(_3),
                                         requireNonNull(_4)
        );
    }

    public abstract Val<A> _1();

    public abstract Val<B> _2();

    public abstract Val<C> _3();

    public abstract Val<D> _4();

    public static <O> Val<O> race(final Quadruple<O, O, O, O> quadruple) {
        if (quadruple == null)
            return Cons.failure(new IllegalArgumentException("Quadruple.race: pair is null"));
        return ListExp.parallel(quadruple._1(),
                                quadruple._2(),
                                quadruple._3(),
                                quadruple._4()
                               )
                      .race();
    }


}
