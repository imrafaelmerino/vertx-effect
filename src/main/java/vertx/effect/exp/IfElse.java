package vertx.effect.exp;

import io.vertx.core.Future;
import vertx.effect.RetryPolicy;
import vertx.effect.Val;

import java.util.function.Predicate;

import static java.util.Objects.requireNonNull;

public final class IfElse<O> extends Exp<O> {

    private final Val<Boolean> predicate;
    private Val<O> consequence;
    private Val<O> alternative;

    public static <O> IfElse<O> predicate(Val<Boolean> predicate) {
        return new IfElse<>(requireNonNull(predicate));
    }

    public static <O> IfElse<O> predicate(boolean bool) {
        return new IfElse<>(requireNonNull(Val.succeed(bool)));
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
    public Val<O> retryEach(final RetryPolicy policy) {
        return retryEach(e -> true,
                         policy);
    }

    @Override
    public Val<O> retryEach(final Predicate<Throwable> retryPredicate,
                            final RetryPolicy policy) {
        if (policy == null) return Val.fail(new IllegalArgumentException("Cons.retry: policy is null"));
        if (predicate == null) return Val.fail(new IllegalArgumentException("Cons.retry: predicate is null"));
        return new IfElse<O>(predicate.retry(retryPredicate,
                                             policy
                                            ))
                .consequence(consequence.retry(retryPredicate,
                                               policy
                                              ))
                .alternative(alternative.retry(retryPredicate,
                                               policy
                                              ));
    }

}
