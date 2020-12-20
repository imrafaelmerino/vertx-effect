package vertx.effect.exp;

import io.vertx.core.Future;
import vertx.effect.core.AbstractVal;
import vertx.effect.Val;

import java.util.function.BiFunction;
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
    public Val<O> retry(final Predicate<Throwable> predicate,
                        final int attempts) {
        if (attempts < 1)
            return Cons.failure(new IllegalArgumentException(ATTEMPTS_LOWER_THAN_ONE_ERROR));
        if (predicate == null)
            return Cons.failure(new NullPointerException("predicate is null"));
        return new IfElse<O>(this.predicate.retry(predicate,
                                                  attempts
                                                 ))
                .consequence(consequence.retry(predicate,
                                               attempts
                                              ))
                .alternative(alternative.retry(predicate,
                                               attempts
                                              ));
    }

    @Override
    public Val<O> retry(final Predicate<Throwable> predicate,
                        final int attempts,
                        final BiFunction<Throwable, Integer, Val<Void>> actionBeforeRetry) {
        if (attempts < 1)
            return Cons.failure(new IllegalArgumentException(ATTEMPTS_LOWER_THAN_ONE_ERROR));
        if (predicate == null)
            return Cons.failure(new NullPointerException("predicate is null"));
        if (actionBeforeRetry == null)
            return Cons.failure(new NullPointerException("actionBeforeRetry is null"));
        return new IfElse<O>(this.predicate.retry(predicate,
                                                  attempts,
                                                  actionBeforeRetry
                                                 ))
                .consequence(consequence.retry(predicate,
                                               attempts,
                                               actionBeforeRetry
                                              ))
                .alternative(alternative.retry(predicate,
                                               attempts,
                                               actionBeforeRetry
                                              ));
    }

}
