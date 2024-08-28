package vertx.effect;


import fun.tuple.Triple;

import static java.util.Objects.requireNonNull;

public abstract sealed class TripleExp<A, B, C> extends Exp<Triple<A, B, C>> permits TripleExpPar, TripleExpSeq {

    public static <A, B, C> TripleExp<A, B, C> par(final VIO<A> _1,
                                                   final VIO<B> _2,
                                                   final VIO<C> _3
                                                  ) {
        return new TripleExpPar<>(requireNonNull(_1),
                                  requireNonNull(_2),
                                  requireNonNull(_3)
        );
    }

    public static <A, B, C> TripleExp<A, B, C> seq(final VIO<A> _1,
                                                   final VIO<B> _2,
                                                   final VIO<C> _3
                                                  ) {
        return new TripleExpSeq<>(requireNonNull(_1), requireNonNull(_2), requireNonNull(_3)
        );
    }

    public abstract VIO<A> first();

    public abstract VIO<B> second();

    public abstract VIO<C> third();


}
