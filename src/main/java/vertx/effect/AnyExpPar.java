package vertx.effect;


import io.vertx.core.Future;

import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static java.util.Objects.requireNonNull;

final class AnyExpPar extends AnyExp {

    final List<VIO<Boolean>> exps;

    AnyExpPar(final List<VIO<Boolean>> exps) {
        this.exps = requireNonNull(exps);
    }

    @Override
    public VIO<Boolean> retryEach(final RetryPolicy policy) {
        return retryEach(e -> true,
                         policy
                        );
    }

    @Override
    public VIO<Boolean> retryEach(final Predicate<Throwable> predicate,
                                  final RetryPolicy policy
                                 ) {
        Objects.requireNonNull(policy);
        Objects.requireNonNull(predicate);
        return new AnyExpPar(exps.stream()
                                 .map(it -> it.retry(predicate,
                                                     policy
                                                    ))
                                 .collect(Collectors.toList()));
    }


    @Override
    public Future<Boolean> get() {
        return Future.all(exps.stream()
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
