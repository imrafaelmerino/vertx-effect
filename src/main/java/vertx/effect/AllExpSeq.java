package vertx.effect;

import io.vertx.core.Future;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static java.util.Objects.requireNonNull;

final class AllExpSeq extends AllExp {

    private final List<VIO<Boolean>> exps;

    AllExpSeq(final List<VIO<Boolean>> exps) {
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
        if (policy == null) throw new IllegalArgumentException("policy is null");
        if (predicate == null) throw new IllegalArgumentException("predicate is null");
        return new AllExpSeq(exps.stream()
                                 .map(it -> it.retry(policy))
                                 .collect(Collectors.toList()));
    }

    @Override
    public Future<Boolean> get() {
        return get(exps);
    }

    private Future<Boolean> get(List<VIO<Boolean>> exps) {

        if (exps.size() == 1) return exps.get(0)
                                         .get();
        else return exps.get(0)
                        .get()
                        .flatMap(bool -> {
                                     if (!bool) return Future.succeededFuture(false);
                                     else return get(exps.subList(1,
                                                                  exps.size()
                                                                 )
                                                    );
                                 }
                                );
    }
}
