package vertx.effect;

import io.vertx.core.MultiMap;
import vertx.effect.exp.Cons;

import java.util.function.BiFunction;

import static java.util.Objects.requireNonNull;

public interface λc<I, O> extends BiFunction<MultiMap, I, Val<O>> {

    MultiMap empty = MultiMap.caseInsensitiveMultiMap();

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

    default <Q> λc<I, Q> andThen(final λc<O, Q> other) {
        requireNonNull(other);
        return (context, input) -> {
            if (input == null)
                return Cons.failure(new IllegalArgumentException("input is null"));
            return Cons.of(() -> this.apply(context,
                                            input
                                           )
                                     .get()
                                     .flatMap(it -> other.apply(context,
                                                                it
                                                               )
                                                         .get()
                                             )
                          );
        };
    }

}
