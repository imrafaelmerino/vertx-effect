package vertx.effect.exp;

import vertx.effect.RetryPolicy;
import vertx.effect.Val;

import java.util.function.Predicate;

interface Exp<O> extends Val<O>{

    Val<O> retryEach(final Predicate<Throwable> predicate,
                     final RetryPolicy policy);

    Val<O> retryEach(final RetryPolicy policy);

}
