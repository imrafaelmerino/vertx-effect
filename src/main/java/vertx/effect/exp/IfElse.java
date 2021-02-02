package vertx.effect.exp;

import io.vertx.core.Future;
import vertx.effect.RetryPolicy;
import vertx.effect.core.AbstractVal;
import vertx.effect.Val;



import static java.util.Objects.requireNonNull;

public final class IfElse<O> extends AbstractVal<O> {

    private final Val<Boolean> predicate;
    private Val<O> consequence;
    private Val<O> alternative;

    public static <O> IfElse<O> predicate(Val<Boolean> predicate) {
        return new IfElse<>(requireNonNull(predicate));
    }

    public static <O> IfElse<O> predicate(boolean bool) {
        return new IfElse<>(requireNonNull(Cons.success(bool)));
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
    public Val<O> retry(final RetryPolicy policy) {

        return new IfElse<O>(predicate.retry(policy))
                .consequence(consequence.retry(policy))
                .alternative(alternative.retry(policy));
    }

}
