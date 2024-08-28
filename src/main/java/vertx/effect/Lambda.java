package vertx.effect;

import java.util.function.Function;

import static java.util.Objects.requireNonNull;

public interface Lambda<I, O> extends Function<I, VIO<O>> {

    static <I> Lambda<I, I> identity() {
        return VIO::succeed;
    }

    default <Q> Lambda<I, Q> then(final Lambda<O, Q> other) {
        requireNonNull(other);
        return input -> this.apply(input)
                            .then(other);
    }

    /**
     * map this effect into another one using the given map function
     *
     * @param map the map function
     * @param <Q> the type of new effect
     * @return a new effect
     */
    default <Q> Lambda<I, Q> map(Function<VIO<O>, VIO<Q>> map) {
        return i -> map.apply(this.apply(i));
    }


}
