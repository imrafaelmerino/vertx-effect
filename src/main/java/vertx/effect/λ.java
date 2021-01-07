package vertx.effect;

import vertx.effect.exp.Cons;

import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;

import static java.util.Objects.requireNonNull;

public interface λ<I, O> extends Function<I, Val<O>> {

    static <I> λ<I, I> identity() {
        return Cons::success;
    }

    static <I, O> λ<I, O> fail(final Supplier<Exception> supplier) {
        Objects.requireNonNull(supplier);
        return e -> Cons.failure(supplier.get());
    }

    default <Q> λ<I, Q> andThen(final λ<O, Q> other) {
        requireNonNull(other);
        return input -> {
            if (input == null) return Cons.failure(new IllegalArgumentException("input is null"));
            return Cons.of(() -> this.apply(input)
                                     .get()
                                     .flatMap(it -> other.apply(it)
                                                         .get()
                                             )
                          );
        };
    }
}
