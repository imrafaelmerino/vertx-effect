package vertxeffect;

import vertxeffect.exp.Cons;

import java.util.function.Function;

import static java.util.Objects.requireNonNull;

public interface λ<I, O> extends Function<I, Val<O>> {

    static <I> λ<I, I> identity() {
        return Cons::success;
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
