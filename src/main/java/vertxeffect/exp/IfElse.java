package vertxeffect.exp;

import io.vertx.core.Future;
import vertxeffect.Val;
import vertxeffect.core.AbstractVal;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;

import static java.util.Objects.requireNonNull;

public final class IfElse<O> extends AbstractVal<O> {

    private final Val<Boolean> predicate;
    private static final String ATTEMPTS_LOWER_THAN_ONE_ERROR = "attempts < 1";
    private Val<O> consequence;
    private Val<O> alternative;

    public static <O> IfElse<O> predicate(Val<Boolean> predicate) {
        return new IfElse<>(requireNonNull(predicate));
    }

    IfElse(final Val<Boolean> predicate) {
        this.predicate = requireNonNull(predicate);
    }

    public IfElse<O> consequence(final Val<O> consequence) {
        this.consequence = requireNonNull(consequence);
        return this;
    }

    public IfElse<O> alternative(final Val<O> alternative) {
        this.alternative = requireNonNull(alternative);
        return this;
    }


    @Override
    public Future<O> get() {
        final Future<Boolean> futureCon = predicate.get();
        return futureCon.flatMap(c -> {
            if (Boolean.TRUE.equals(c)) return consequence.get();
            else return alternative.get();
        });
    }

    @Override
    public <P> Val<P> map(final Function<O, P> fn) {
        if (fn == null)
            return Cons.failure(new NullPointerException("fn is null"));
        return new IfElse<P>(predicate).alternative(alternative.map(fn))
                                       .consequence(consequence.map(fn));
    }

    @Override
    public Val<O> retry(final int attempts) {
        if (attempts < 1)
            return Cons.failure(new IllegalArgumentException(ATTEMPTS_LOWER_THAN_ONE_ERROR));
        return new IfElse<O>(predicate.retry(attempts))
                .consequence(consequence.retry(attempts))
                .alternative(alternative.retry(attempts));
    }


    @Override
    public Val<O> retry(final int attempts,
                        final BiFunction<Throwable, Integer, Val<Void>> actionBeforeRetry) {
        if (attempts < 1)
            return Cons.failure(new IllegalArgumentException(ATTEMPTS_LOWER_THAN_ONE_ERROR));
        return new IfElse<O>(predicate.retry(attempts,
                                             actionBeforeRetry
                                            ))
                .consequence(consequence.retry(attempts,
                                               actionBeforeRetry
                                              ))
                .alternative(alternative.retry(attempts,
                                               actionBeforeRetry
                                              ));
    }

    @Override
    public Val<O> retryIf(final Predicate<Throwable> predicate,
                          final int attempts) {
        if (attempts < 1)
            return Cons.failure(new IllegalArgumentException(ATTEMPTS_LOWER_THAN_ONE_ERROR));
        if (predicate == null)
            return Cons.failure(new NullPointerException("predicate is null"));
        return new IfElse<O>(this.predicate.retryIf(predicate,
                                                    attempts
                                                   ))
                .consequence(consequence.retryIf(predicate,
                                                 attempts
                                                ))
                .alternative(alternative.retryIf(predicate,
                                                 attempts
                                                ));
    }

    @Override
    public Val<O> retryIf(final Predicate<Throwable> predicate,
                          final int attempts,
                          final BiFunction<Throwable, Integer, Val<Void>> actionBeforeRetry) {
        if (attempts < 1)
            return Cons.failure(new IllegalArgumentException(ATTEMPTS_LOWER_THAN_ONE_ERROR));
        if (predicate == null)
            return Cons.failure(new NullPointerException("predicate is null"));
        if (actionBeforeRetry == null)
            return Cons.failure(new NullPointerException("actionBeforeRetry is null"));
        return new IfElse<O>(this.predicate.retryIf(predicate,
                                                    attempts,
                                                    actionBeforeRetry
                                                   ))
                .consequence(consequence.retryIf(predicate,
                                                 attempts,
                                                 actionBeforeRetry
                                                ))
                .alternative(alternative.retryIf(predicate,
                                                 attempts,
                                                 actionBeforeRetry
                                                ));
    }

}
