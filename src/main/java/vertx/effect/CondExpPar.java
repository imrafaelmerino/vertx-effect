package vertx.effect;


import io.vertx.core.Future;

import java.util.List;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;


final class CondExpPar<E> extends CondExp<E> {

    private final List<VIO<Boolean>> tests;
    private final List<Supplier<VIO<E>>> consequences;
    private final Supplier<VIO<E>> otherwise;

    CondExpPar(final List<VIO<Boolean>> tests,
               final List<Supplier<VIO<E>>> consequences,
               final Supplier<VIO<E>> otherwise
              ) {
        this.tests = tests;
        this.consequences = consequences;
        this.otherwise = otherwise;
    }

    CondExpPar(final List<VIO<Boolean>> tests,
               final List<Supplier<VIO<E>>> consequences
              ) {
        this(tests, consequences, VIO::NULL);
    }

    @Override
    public Future<E> get() {
        return Future.all(tests.stream()
                               .map(Supplier::get)
                               .collect(Collectors.toList()))
                     .flatMap(result -> getFirstThatIsTrueOrDefault(result.list()));
    }

    private Future<E> getFirstThatIsTrueOrDefault(List<Boolean> predicatesResults) {
        for (int i = 0; i < predicatesResults.size(); i++)
            if (predicatesResults.get(i)) return consequences.get(i).get().get();
        return otherwise.get().get();
    }


    @Override
    public VIO<E> retryEach(final Predicate<Throwable> predicate,
                            final RetryPolicy policy
                           ) {
        if (policy == null)
            throw new IllegalArgumentException("policy is null");
        if (predicate == null)
            throw new IllegalArgumentException("predicate is null");
        return new CondExpPar<>(tests.stream().map(it -> it.retry(predicate, policy)).collect(Collectors.toList()),
                                consequences.stream().map(it -> (Supplier<VIO<E>>) () -> it.get().retry(predicate, policy)
                                                         ).collect(Collectors.toList()),
                                otherwise
        );
    }

    @Override
    public VIO<E> retryEach(RetryPolicy policy) {
        return retryEach(e -> true, policy);
    }


}
