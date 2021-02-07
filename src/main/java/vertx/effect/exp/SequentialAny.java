package vertx.effect.exp;

import io.vertx.core.Future;
import vertx.effect.RetryPolicy;
import vertx.effect.Val;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static java.util.Objects.requireNonNull;

final class SequentialAny extends Any {

    SequentialAny(final List<Val<Boolean>> exps) {
        this.exps = requireNonNull(exps);
    }

    final List<Val<Boolean>> exps;

    @Override
    public Val<Boolean> retryEach(final RetryPolicy policy) {
        return retryEach(e -> true,policy);
    }

    @Override
    public Val<Boolean> retryEach(final Predicate<Throwable> predicate,
                              final RetryPolicy policy) {
        if (policy == null) return Cons.failure(new IllegalArgumentException("Cons.retry: policy is null"));
        if (predicate== null) return Cons.failure(new IllegalArgumentException("Cons.retry: predicate is null"));
        return new SequentialAny(exps.stream()
                                     .map(it -> it.retry(predicate,policy))
                                     .collect(Collectors.toList()));
    }


    @Override
    public Future<Boolean> get() {
        return get(exps);
    }

    private Future<Boolean> get(List<Val<Boolean>> exps) {

        if (exps.size() == 1) return exps.get(0)
                                         .get();
        else return exps.get(0)
                        .get()
                        .flatMap(bool -> {
                                     if (bool) return Future.succeededFuture(true);
                                     else return get(exps.subList(1,
                                                                  exps.size()
                                                                 )
                                                    );
                                 }
                                );
    }

}
