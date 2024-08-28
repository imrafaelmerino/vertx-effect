package vertx.effect;

import io.vertx.core.Future;

import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;

final class CondExpSeq<E> extends CondExp<E> {

    private final List<VIO<Boolean>> tests;
    private final List<Supplier<VIO<E>>> consequences;
    private final Supplier<VIO<E>> otherwise;

    CondExpSeq(final List<VIO<Boolean>> tests,
               final List<Supplier<VIO<E>>> consequences,
               final Supplier<VIO<E>> otherwise
              ) {
        this.tests = tests;
        this.consequences = consequences;
        this.otherwise = otherwise;
    }

    CondExpSeq(final List<VIO<Boolean>> tests,
               final List<Supplier<VIO<E>>> consequences
              ) {
        this(tests, consequences, VIO::NULL);
    }

    private static <E> Future<E> get(List<VIO<Boolean>> tests,
                                     List<Supplier<VIO<E>>> consequences,
                                     Supplier<VIO<E>> otherwise,
                                     int condTestedSoFar
                                    ) {
        return condTestedSoFar == tests.size() ?
                otherwise.get().get() :
                tests.get(condTestedSoFar).get()
                     .flatMap(result -> result ?
                             consequences.get(condTestedSoFar)
                                         .get().get() :
                             get(tests,
                                 consequences,
                                 otherwise,
                                 condTestedSoFar + 1
                                ));

    }

    @Override
    public Future<E> get() {
        return get(tests,
                   consequences,
                   otherwise,
                   0
                  );
    }

    @Override
    public VIO<E> retryEach(final Predicate<Throwable> predicate,
                            final RetryPolicy policy
                           ) {
        Objects.requireNonNull(policy);
        Objects.requireNonNull(predicate);
        return new CondExpSeq<>(
                tests.stream().map(it -> it.retry(predicate, policy)).collect(Collectors.toList()),
                consequences.stream()
                            .map(it -> (Supplier<VIO<E>>) () -> it.get().retry(predicate, policy))
                            .collect(Collectors.toList()),
                otherwise
        );
    }

    @Override
    public VIO<E> retryEach(RetryPolicy policy) {
        return retryEach(e -> true, policy);
    }
}
