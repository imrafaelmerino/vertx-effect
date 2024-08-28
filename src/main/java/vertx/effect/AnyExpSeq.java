package vertx.effect;

import io.vertx.core.Future;

import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static java.util.Objects.requireNonNull;

final class AnyExpSeq extends AnyExp {

    final List<VIO<Boolean>> exps;

    AnyExpSeq(final List<VIO<Boolean>> exps) {
        this.exps = requireNonNull(exps);
    }

    @Override
    public VIO<Boolean> retryEach(final RetryPolicy policy) {
        return retryEach(e -> true, policy);
    }

    @Override
    public VIO<Boolean> retryEach(final Predicate<Throwable> predicate,
                                  final RetryPolicy policy
                                 ) {
        Objects.requireNonNull(policy);
        Objects.requireNonNull(predicate);
        return new AnyExpSeq(exps.stream()
                                 .map(it -> it.retry(predicate, policy))
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
                        .flatMap(bool -> bool ?
                                         Future.succeededFuture(true) :
                                         get(exps.subList(1,
                                                          exps.size()
                                                         )
                                            )
                                );
    }

}
