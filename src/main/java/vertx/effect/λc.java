package vertx.effect;

import io.vertx.core.MultiMap;
import vertx.effect.exp.Cons;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Supplier;

import static java.util.Objects.requireNonNull;

public interface λc<I, O> extends BiFunction<MultiMap, I, Val<O>> {

    MultiMap empty = MultiMap.caseInsensitiveMultiMap();

    static <I> λc<I, I> identity() {
        return (context, val) -> Cons.success(val);
    }

    static <I, O> λc<I, O> fail(final Supplier<Exception> supplier) {
        Objects.requireNonNull(supplier);
        return (context, val) ->
                Cons.failure(supplier.get());
    }


    default λ<I, O> apply(final MultiMap context) {
        requireNonNull(context);
        return input -> {
            if (input == null)
                return Cons.failure(new IllegalArgumentException("input is null"));
            return this.apply(context,
                              input
                             );
        };
    }

    default Val<O> apply(final I input) {
        if (input == null)
            return Cons.failure(new IllegalArgumentException("input is null"));
        return this.apply(empty,
                          requireNonNull(input)
                         );
    }

    default <Q> λc<Q, O> compose(final λc<Q, I> other) {
        requireNonNull(other);
        return (context, input) -> {
            if (input == null)
                return Cons.failure(new IllegalArgumentException("input is null"));
            return other.apply(context,
                               input
                              )
                        .flatMap(i -> this.apply(context,
                                                 i
                                                )
                                );
        };
    }

    default <Q> λc<I, Q> andThen(final λc<O, Q> other) {
        requireNonNull(other);
        return (context, input) -> {
            if (input == null)
                return Cons.failure(new IllegalArgumentException("input is null"));
            return this.apply(context,
                              input
                             )
                       .flatMap(it -> other.apply(context,
                                                  it
                                                 )
                               );
        };
    }

}
