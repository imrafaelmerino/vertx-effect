package vertx.effect.exp;

import io.vertx.core.Future;
import vertx.effect.RetryPolicy;
import vertx.effect.Val;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.Objects.requireNonNull;

final class SequentialAny extends Any {

    SequentialAny(final List<Val<Boolean>> exps) {
        this.exps = requireNonNull(exps);
    }

    final List<Val<Boolean>> exps;

    @Override
    public Val<Boolean> retry(final RetryPolicy policy) {
        return new SequentialAny(exps.stream()
                                     .map(it -> it.retry(policy))
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
