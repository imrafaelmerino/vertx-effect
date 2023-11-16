package vertx.effect;

import java.util.function.Predicate;

abstract sealed class Exp<O> implements VIO<O> permits AllExp, AnyExp, CondExp, IfElseExp, JsArrayExp, JsObjExp, ListExp, MapExp, PairExp, SwitchExp, TripleExp {

    public abstract VIO<O> retryEach(final Predicate<Throwable> predicate,
                                     final RetryPolicy policy
                                    );

    public abstract VIO<O> retryEach(final RetryPolicy policy);

}
