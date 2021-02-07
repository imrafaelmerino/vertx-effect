package vertx.effect.exp;

import io.vertx.core.Future;
import vertx.effect.Delay;
import vertx.effect.RetryPolicy;
import vertx.effect.RetryStatus;
import vertx.effect.Val;
import vertx.effect.core.AbstractVal;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static java.util.Objects.requireNonNull;

public final class Cons<O> extends AbstractVal<O> {
    public static final Val<Void> NULL = Cons.success(null);
    public static final Val<Boolean> TRUE = Cons.success(true);
    public static final Val<Boolean> FALSE = Cons.success(false);
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



}
