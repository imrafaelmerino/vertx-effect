package vertx.effect.exp;

import io.vavr.Tuple2;
import vertx.effect.Val;

public abstract class Pair<A, B> extends Exp<Tuple2<A, B>>{

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

    public abstract Val<A> _1();

    public abstract Val<B> _2();

    public static <O> Val<O> race(final Pair<O, O> pair) {
        if (pair == null)
            return Val.fail(new IllegalArgumentException("Pair.race: pair is null"));
        return ListExp.parallel(pair._1(),
                                pair._2()
                               )
                      .race();
    }


}
