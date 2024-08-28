package vertx.effect;


import io.vertx.core.Future;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static java.util.Objects.requireNonNull;

/**
 * It's an immutable expression that implements multiple predicate-value branches like the Cond expression. However, it
 * evaluates a type I value and allows multiple value clauses based on evaluating that value.
 *
 * @param <O> the type of the value this expression will be reduced
 */
public final class SwitchExp<I, O> extends Exp<O> {
    private final VIO<I> val;
    private final List<Predicate<I>> predicates;
    private final List<Lambda<I, O>> lambdas;
    private final Lambda<I, O> otherwise;

    SwitchExp(final VIO<I> val,
              final List<Predicate<I>> predicates,
              final List<Lambda<I, O>> lambdas,
              final Lambda<I, O> otherwise
             ) {
        this.val = val;
        this.predicates = predicates;
        this.lambdas = lambdas;
        this.otherwise = otherwise;
    }

    /**
     * Creates a SwitchMatcher from a given value that will be evaluated and matched against the branches defined with
     * the {@link SwitchMatcher#match(Object, Lambda, Object, Lambda, Lambda) match} method
     *
     * @param input the input that will be evaluated
     * @param <I>   the type of the input
     * @param <O>   the type of the expression result
     * @return a SwitchMatcher
     */
    public static <I, O> SwitchMatcher<I, O> eval(final I input) {
        return new SwitchMatcher<>(VIO.succeed(requireNonNull(input)));
    }

    /**
     * Creates a SwitchMatcher from a given effect that will be evaluated and matched against the branches defined with
     * the {@link SwitchMatcher#match(Object, Lambda, Object, Lambda, Lambda) match} method
     *
     * @param input the effect that will be evaluated
     * @param <I>   the type of the input
     * @param <O>   the type of the expression result
     * @return a SwitchMatcher
     */
    public static <I, O> SwitchMatcher<I, O> eval(final VIO<I> input) {
        return new SwitchMatcher<>(requireNonNull(input));
    }

    private static <I, O> Future<O> get(final Future<I> val,
                                        final List<Predicate<I>> tests,
                                        final List<Lambda<I, O>> lambdas,
                                        final Lambda<I, O> otherwise,
                                        final int condTestedSoFar
                                       ) {
        return condTestedSoFar == tests.size() ?
                val.compose(i -> otherwise.apply(i).get()) :
                val.compose(i ->
                                    tests.get(condTestedSoFar).test(i) ?
                                            lambdas.get(condTestedSoFar).apply(i).get() :
                                            get(val, tests, lambdas, otherwise, condTestedSoFar + 1)
                           );


    }

    @Override
    public Future<O> get() {
        return get(val.get(), predicates, lambdas, otherwise, 0);
    }

    @Override
    public SwitchExp<I, O> retryEach(final Predicate<Throwable> predicate,
                                     final RetryPolicy policy
                                    ) {
        requireNonNull(predicate);
        requireNonNull(policy);
        return new SwitchExp<>(val.retry(predicate, policy),
                               predicates,
                               lambdas.stream().map(it -> it.map(a -> a.retry(predicate, policy))).collect(Collectors.toList()),
                               otherwise.map(it -> it.retry(predicate, policy))

        );
    }

    @Override
    public VIO<O> retryEach(RetryPolicy policy) {
        return retryEach(e -> true, policy);
    }


}
