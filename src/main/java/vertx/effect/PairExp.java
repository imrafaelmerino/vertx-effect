package vertx.effect;

import fun.tuple.Pair;

public abstract class PairExp<A, B> extends Exp<Pair<A, B>> {

    public static <A, B> PairExp<A, B> seq(final VIO<A> _1,
                                           final VIO<B> _2
                                          ) {
        return new PairExpSeq<>(_1,
                                _2
        );
    }

    public static <A, B> PairExp<A, B> par(final VIO<A> _1,
                                           final VIO<B> _2
                                          ) {
        return new PairExpPar<>(_1,
                                _2
        );
    }

    public abstract VIO<A> first();

    public abstract VIO<B> second();


}
