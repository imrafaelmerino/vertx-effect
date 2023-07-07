package vertx.effect;

import io.vertx.core.MultiMap;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Supplier;

import static java.util.Objects.requireNonNull;

public interface Lambdac<I, O> extends BiFunction<MultiMap, I, VIO<O>> {

    MultiMap empty = MultiMap.caseInsensitiveMultiMap();

    static <I> Lambdac<I, I> identity() {
        return (context, val) -> VIO.succeed(val);
    }

    static <I, O> Lambdac<I, O> fail(final Supplier<Exception> supplier) {
        Objects.requireNonNull(supplier);
        return (context, val) ->
                VIO.fail(supplier.get());
    }


    default Lambda<I, O> apply(final MultiMap context) {
        requireNonNull(context);
        return input -> {
            if (input == null)
                return VIO.fail(new IllegalArgumentException("input is null"));
            return this.apply(context,
                              input
                             );
        };
    }

    default VIO<O> apply(final I input) {
        return this.apply(empty,
                          requireNonNull(input)
                         );
    }

    default <Q> Lambdac<Q, O> compose(final Lambdac<Q, I> other) {
        requireNonNull(other);
        return (context, input) -> {
            if (input == null)
                return VIO.fail(new IllegalArgumentException("input is null"));
            return other.apply(context, input)
                        .then(i -> this.apply(context, i));
        };
    }

    default <Q> Lambdac<I, Q> then(final Lambdac<O, Q> other) {
        requireNonNull(other);
        return (context, input) -> {
            if (input == null)
                return VIO.fail(new IllegalArgumentException("input is null"));
            return this.apply(context,
                              input
                             )
                       .then(it -> other.apply(context, it));
        };
    }

}
