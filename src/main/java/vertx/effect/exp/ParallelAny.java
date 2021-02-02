package vertx.effect.exp;

import io.vertx.core.CompositeFuture;
import io.vertx.core.Future;
import vertx.effect.RetryPolicy;
import vertx.effect.Val;

import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static java.util.Objects.requireNonNull;

final class ParallelAny extends Any {

    ParallelAny(final List<Val<Boolean>> exps) {
        this.exps = requireNonNull(exps);
    }

    final List<Val<Boolean>> exps;


    @Override
    public Val<Boolean> retry(final RetryPolicy policy) {
        return new ParallelAny(exps.stream()
                                   .map(it -> it.retry(policy))
                                   .collect(Collectors.toList()));
    }


    @Override
    public Future<Boolean> get() {
        return CompositeFuture.all(exps.stream()
                                       .map(Supplier::get)
                                       .collect(Collectors.toList())
                                  )
                              .map(l -> l.result()
                                         .list()
                                         .stream()
                                         .anyMatch(it -> it instanceof Boolean && ((Boolean) it))
                                  );
    }

}
