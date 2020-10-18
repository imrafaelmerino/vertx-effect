package vertx.effect.exp;

import io.vertx.core.CompositeFuture;
import io.vertx.core.Future;
import vertx.effect.Val;
import vertx.effect.core.AbstractVal;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static java.util.Objects.requireNonNull;

public final class And extends AbstractVal<Boolean> {

    private static final String ATTEMPTS_LOWER_THAN_ONE_ERROR = "attempts < 1";

    And(final List<Val<Boolean>> exps) {
        this.exps = requireNonNull(exps);
    }

    private final List<Val<Boolean>> exps;

    public static Val<Boolean> of(final boolean a,
                                  final boolean... others) {
        List<Val<Boolean>> exps = new ArrayList<>();
        exps.add(Cons.success(a));
        for (final boolean other : others) {
            exps.add(Cons.success(other));
        }
        return new And(exps);
    }

    @SafeVarargs
    public static Val<Boolean> of(final Val<Boolean> a,
                                  final Val<Boolean>... others) {
        List<Val<Boolean>> exps = new ArrayList<>();
        exps.add(requireNonNull(a));
        for (final Val<Boolean> other : others) {
            exps.add(requireNonNull(other));
        }
        return new And(exps);
    }

    @Override
    public <P> Val<P> map(final Function<Boolean, P> fn) {
        if (fn == null)
            return Cons.failure(new NullPointerException("fn is null"));
        return Cons.of(() -> get().map(fn));
    }

    @Override
    public Val<Boolean> retry(final int attempts) {
        if (attempts < 1)
            return Cons.failure(new IllegalArgumentException(ATTEMPTS_LOWER_THAN_ONE_ERROR));

        return new And(exps.stream()
                           .map(it -> it.retry(attempts))
                           .collect(Collectors.toList()));
    }


    @Override
    public Val<Boolean> retry(final int attempts,
                              final BiFunction<Throwable, Integer, Val<Void>> actionBeforeRetry) {
        if (attempts < 1)
            return Cons.failure(new IllegalArgumentException(ATTEMPTS_LOWER_THAN_ONE_ERROR));

        if (actionBeforeRetry == null)
            return Cons.failure(new NullPointerException("actionBeforeRetry is null"));

        return new And(exps.stream()
                           .map(it -> it.retry(attempts,
                                               actionBeforeRetry
                                              ))
                           .collect(Collectors.toList())
        );
    }

    @Override
    public Val<Boolean> retryIf(final Predicate<Throwable> predicate,
                                final int attempts) {
        if (attempts < 1)
            return Cons.failure(new IllegalArgumentException(ATTEMPTS_LOWER_THAN_ONE_ERROR));

        if (predicate == null)
            return Cons.failure(new NullPointerException("predicate is null"));
        return new And(exps.stream()
                           .map(it -> it.retryIf(predicate,
                                                 attempts
                                                ))
                           .collect(Collectors.toList()));
    }


    @Override
    public Val<Boolean> retryIf(final Predicate<Throwable> predicate,
                                final int attempts,
                                final BiFunction<Throwable, Integer, Val<Void>> actionBeforeRetry) {
        if (attempts < 1)
            return Cons.failure(new IllegalArgumentException(ATTEMPTS_LOWER_THAN_ONE_ERROR));
        if (predicate == null)
            return Cons.failure(new NullPointerException("predicate is null"));
        return new And(exps.stream()
                           .map(it -> it.retryIf(predicate,
                                                 attempts,
                                                 actionBeforeRetry
                                                ))
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
