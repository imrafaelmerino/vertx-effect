package vertx.effect.exp;

import io.vertx.core.CompositeFuture;
import io.vertx.core.Future;
import vertx.effect.RetryPolicy;
import vertx.effect.Val;
import java.util.List;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static java.util.Objects.requireNonNull;

final class ParallelAll extends All {

    ParallelAll(final List<Val<Boolean>> exps) {
        this.exps = requireNonNull(exps);
    }

    private final List<Val<Boolean>> exps;

    @Override
    public Val<Boolean> retryEach(final RetryPolicy policy) {
        return retryEach(e->true,policy);
    }

    @Override
    public Val<Boolean> retryEach(final Predicate<Throwable> predicate,
                              final RetryPolicy policy) {
        if (policy == null) return Cons.failure(new IllegalArgumentException("Cons.retry: policy is null"));
        if (predicate== null) return Cons.failure(new IllegalArgumentException("Cons.retry: predicate is null"));
        return new ParallelAll(exps.stream()
                                   .map(it -> it.retry(predicate,policy))
                                   .collect(Collectors.toList()));
    }

    @Override
    public Future<Boolean> get() {
        return CompositeFuture.all(exps.stream()
                                       .map(Supplier::get)
                                       .collect(Collectors.toList()))
                              .map(l -> l.result()
                                         .list()
                                         .stream()
                                         .allMatch(it -> it instanceof Boolean && (Boolean) it)
                                  );
    }
}
