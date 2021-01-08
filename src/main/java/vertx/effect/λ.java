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
        return input -> this.apply(input)
                            .flatMap(other);
    }

    default <A> λ<A, O> compose(final λ<A, I> fn) {
        Objects.requireNonNull(fn);
        return a -> fn.apply(a)
                      .flatMap(this);
    }

}
