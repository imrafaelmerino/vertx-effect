package vertx.effect;

import vertx.effect.exp.Cons;

import java.util.function.Function;
import java.util.function.Supplier;

/**
 Function that takes the result and the number of remaining attempts, and returns a failurePolicy.
 An failurePolicy is just an alias for {@code Val<Void>}
 */
public interface RetryPolicy extends Supplier<Function<Throwable, Val<Boolean>>> {

    default RetryPolicy join(final RetryPolicy other) {
        return () -> {
            Function<Throwable, Val<Boolean>> first  = RetryPolicy.this.get();
            Function<Throwable, Val<Boolean>> second = other.get();
            return error -> first
                    .apply(error)
                    .flatMap(bool -> bool ? second
                            .apply(error) : Cons.FALSE);
        }
                ;
    }
}
