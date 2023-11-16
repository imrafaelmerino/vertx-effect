package vertx.effect;

import java.util.function.Predicate;

abstract class Exp<O> extends AbstractVIO<O> {

    public abstract VIO<O> retryEach(final Predicate<Throwable> predicate,
                                     final RetryPolicy policy
                                    );

    public abstract VIO<O> retryEach(final RetryPolicy policy);

}
