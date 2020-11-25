package vertx.effect.exp;

import io.vertx.core.Future;
import vertx.effect.core.AbstractVal;
import vertx.effect.Val;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static java.util.Objects.requireNonNull;

public final class Cons<O> extends AbstractVal<O> {
    public static final Val<Void> NULL = Cons.success(null);
    public static final Val<Boolean> TRUE = Cons.success(true);
    public static final Val<Boolean> FALSE = Cons.success(false);
    private static final String ATTEMPTS_LOWER_THAN_ONE_ERROR = "attempts < 1";
    private final Supplier<Future<O>> futureSupplier;

    Cons(final Supplier<Future<O>> futureSupplier) {
        this.futureSupplier = futureSupplier;
    }

    public static <O> Val<O> failure(final Throwable failure) {
        if (failure == null)
            return Cons.failure(new NullPointerException("failure is null"));
        return Cons.of(() -> Future.failedFuture(failure));
    }


    public static <O> Val<O> of(final Supplier<Future<O>> supplier) {
        if (supplier == null)
            return Cons.failure(new NullPointerException("supplier is null"));
        return new Cons<>(requireNonNull(supplier));
    }

    public static <O> Val<O> success(final O o) {
        return new Cons<>(() -> Future.succeededFuture(o));
    }

    @Override
    public Future<O> get() {
        return futureSupplier.get();
    }



    @Override
    public Val<O> retry(final int attempts) {
        if (attempts < 1)
            return Cons.failure(new IllegalArgumentException(ATTEMPTS_LOWER_THAN_ONE_ERROR));
        return retry(this,
                     attempts
                    );
    }


    @Override
    public Val<O> retry(final int attempts,
                        final BiFunction<Throwable, Integer, Val<Void>> actionBeforeRetry) {
        if (attempts < 1)
            return Cons.failure(new IllegalArgumentException(ATTEMPTS_LOWER_THAN_ONE_ERROR));

        if (actionBeforeRetry == null)
            return Cons.failure(new NullPointerException("actionBeforeRetry is null"));

        return retry(this,
                     attempts,
                     requireNonNull(actionBeforeRetry)
                    );
    }

    @Override
    public Val<O> retryIf(final Predicate<Throwable> predicate,
                          final int attempts) {
        if (attempts < 1)
            return Cons.failure(new IllegalArgumentException(ATTEMPTS_LOWER_THAN_ONE_ERROR));
        if(predicate==null)
            return Cons.failure(new NullPointerException("predicate is null"));
        return retry(this,
                     attempts,
                     predicate
                    );
    }

    @Override
    public Val<O> retryIf(final Predicate<Throwable> predicate,
                          final int attempts,
                          final BiFunction<Throwable, Integer, Val<Void>> actionBeforeRetry) {
        if (attempts < 1)
            return Cons.failure(new IllegalArgumentException(ATTEMPTS_LOWER_THAN_ONE_ERROR));
        if (actionBeforeRetry == null)
            return Cons.failure(new NullPointerException("actionBeforeRetry is null"));
        if(predicate==null)
            return Cons.failure(new NullPointerException("predicate is null"));
        return retry(this,
                     attempts,
                     requireNonNull(predicate),
                     requireNonNull(actionBeforeRetry)
                    );
    }



    private Val<O> retry(final Cons<O> exp,
                         final int attempts) {
        if (attempts == 0) return exp;
        return Cons.of(() -> exp.get()
                                .compose(Future::succeededFuture,
                                         e -> retry(exp,
                                                    attempts - 1
                                                   ).get()
                                        )
                      );
    }


    private Val<O> retry(final Cons<O> exp,
                         final int attempts,
                         final BiFunction<Throwable, Integer, Val<Void>> actionBeforeRetry
                        ) {
        if (attempts == 0) return exp;
        return Cons.of(() -> exp.get()
                                .compose(Future::succeededFuture,
                                         e -> actionBeforeRetry.apply(e,
                                                                      attempts
                                                                     )
                                                               .flatMap(id -> retry(exp,
                                                                                    attempts - 1,
                                                                                    actionBeforeRetry
                                                                                   )
                                                                       )
                                                               .get()
                                        )
                      );
    }


    private Val<O> retry(final Cons<O> exp,
                         final int attempts,
                         final Predicate<Throwable> predicate,
                         final BiFunction<Throwable, Integer, Val<Void>> actionBeforeRetry) {

        if (attempts == 0) return exp;
        return Cons.of(() -> exp.get()
                                .compose(Future::succeededFuture,
                                         e -> (predicate.test(e)) ?
                                              actionBeforeRetry.apply(e,
                                                                      attempts
                                                                     )
                                                               .flatMap(id -> retry(exp,
                                                                                    attempts - 1,
                                                                                    predicate,
                                                                                    actionBeforeRetry
                                                                                   )
                                                                       )
                                                               .get() :
                                              Future.failedFuture(e)
                                        )
                      );
    }

    private Val<O> retry(final Cons<O> exp,
                         final int attempts,
                         final Predicate<Throwable> predicate) {
        if (attempts == 0) return exp;
        return Cons.of(() -> exp.get()
                                .compose(Future::succeededFuture,
                                         e -> (predicate.test(e)) ?
                                              retry(exp,
                                                    attempts - 1,
                                                    predicate
                                                   ).get() :
                                              Future.failedFuture(e)
                                        )
                      );
    }


}
