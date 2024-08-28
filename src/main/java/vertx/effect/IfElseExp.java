package vertx.effect;

import io.vertx.core.Future;

import java.util.function.Predicate;
import java.util.function.Supplier;

import static java.util.Objects.requireNonNull;

public final class IfElseExp<O> extends Exp<O> {

    private final VIO<Boolean> predicate;
    private Supplier<VIO<O>> consequence;
    private Supplier<VIO<O>> alternative;

    public static <O> IfElseExp<O> predicate(VIO<Boolean> condition) {
        return new IfElseExp<>(requireNonNull(condition));
    }

    public static <O> IfElseExp<O> predicate(boolean condition) {
        return new IfElseExp<>(VIO.succeed(condition));
    }

    public static <O> IfElseExp<O> predicate(Supplier<Boolean> condition) {
        return new IfElseExp<>(VIO.effect(() -> Future.succeededFuture(condition.get())));
    }


    IfElseExp(final VIO<Boolean> predicate) {
        this.predicate = requireNonNull(predicate);
    }

    public IfElseExp<O> consequence(final Supplier<VIO<O>> consequence) {
        this.consequence = requireNonNull(consequence);
        return this;
    }

    public IfElseExp<O> alternative(final Supplier<VIO<O>> alternative) {
        this.alternative = requireNonNull(alternative);
        return this;
    }


    @Override
    public Future<O> get() {
        return predicate.get()
                        .flatMap(c -> Boolean.TRUE.equals(c) ?
                                         consequence.get().get() :
                                         alternative.get().get()
                                );
    }


    @Override
    public VIO<O> retryEach(final RetryPolicy policy) {
        return retryEach(e -> true,
                         policy
                        );
    }

    @Override
    public VIO<O> retryEach(final Predicate<Throwable> retryPredicate,
                            final RetryPolicy policy
                           ) {
        if (policy == null) throw new IllegalArgumentException("policy is null");
        if (retryPredicate == null) throw new IllegalArgumentException("predicate is null");
        return new IfElseExp<O>(predicate.retry(retryPredicate,
                                                policy
                                               ))
                .consequence(() -> consequence.get().retry(retryPredicate, policy))
                .alternative(() -> alternative.get().retry(retryPredicate, policy));
    }

}
