package vertx.effect.exp;

import vertx.effect.RetryPolicy;
import vertx.effect.Val;
import vertx.effect.core.AbstractVal;

import java.util.function.Predicate;

abstract class Exp<O> extends AbstractVal<O> {

    public abstract Val<O> retryEach(final Predicate<Throwable> predicate,
                                     final RetryPolicy policy);

    public abstract Val<O> retryEach(final RetryPolicy policy);

}
