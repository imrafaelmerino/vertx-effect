package vertx.effect;

import io.vertx.core.AsyncResult;

import io.vertx.core.Future;
import io.vertx.core.Handler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.util.Objects.requireNonNull;

/**
 * A Val is just an alias for a lazy Vertx future. Laziness makes your code more functional and pure.
 * It allows us to describe programs before executing them. The get method triggers the execution
 * of the val and returns a future.
 *
 * @param <O> the type of the value produced by the future
 */
public abstract class VIO<O> implements Supplier<Future<O>> {

    public static final VIO<Boolean> TRUE = VIO.succeed(true);
    public static final VIO<Boolean> FALSE = VIO.succeed(false);

    public static <O> VIO<O> fail(final Throwable failure) {
        if (failure == null) return fail(new NullPointerException("failure is null"));
        return effect(() -> Future.failedFuture(failure));
    }

    public static <O> VIO<O> effect(final Supplier<Future<O>> effect) {
        Objects.requireNonNull(effect);
        return new Val<>(requireNonNull(effect));
    }

    @SafeVarargs
    @SuppressWarnings({"rawtypes", "varargs"})
    public static <O> VIO<O> race(final VIO<O> first,
                                  final VIO<O>... others
                                 ) {
        Objects.requireNonNull(first);
        Objects.requireNonNull(others);
        List<VIO<O>> list = new ArrayList<>();
        list.add(first);
        if (others.length > 0) list.addAll(Arrays.stream(others)
                                                 .collect(Collectors.toList())
                                          );

        return VIO.effect(() ->
                          {
                              List<Future<?>> futures = list.stream()
                                                            .map(Supplier::get)
                                                            .collect(Collectors.toList());
                              return Future.any(futures)
                                           .map(cf -> {
                                                    int index = IntStream.range(0, futures.size())
                                                                         .filter(cf::succeeded)
                                                                         .findFirst()
                                                                         .getAsInt();
                                                    return cf.resultAt(index);
                                                }
                                               );
                          });
    }

    public static <O> VIO<O> succeed(final O constant) {
        return new Val<>(() -> Future.succeededFuture(constant));
    }

    protected static <E> VIO<E> NULL() {
        return VIO.succeed(null);
    }

    /**
     * Creates a new value by applying a function to the successful result of this value. If this value
     * returns an exception then the new value will also contain this exception.
     *
     * @param fn  the function which will be applied to the successful result of this value
     * @param <P> the type of the returned value
     * @return a new value
     */
    public abstract <P> VIO<P> map(final Function<O, P> fn);

    /**
     * Creates a new value by applying a function to the successful result of this value, and returns
     * the result of the function as the new value. If this value returns an exception then
     * the new value will also contain this exception.
     *
     * @param fn  the function which will be applied to the successful result of this value
     * @param <Q> the type of the returned value
     * @return a new value
     */
    public abstract <Q> VIO<Q> then(final Lambda<O, Q> fn);

    public abstract <Q> VIO<Q> then(final Lambda<O, Q> successMapper,
                                    final Lambda<Throwable, Q> failureMapper
                                   );

    /**
     * returns a new value tha will retry its execution if it fails
     *
     * @param retryPolicy the policy to retry
     * @return a new value
     */
    public abstract VIO<O> retry(RetryPolicy retryPolicy);


    public abstract VIO<O> retry(final Predicate<Throwable> predicate,
                                 final RetryPolicy policy
                                );


    public abstract VIO<O> repeat(final Predicate<O> predicate,
                                  final RetryPolicy policy
                                 );

    /**
     * Creates a new value that will handle any matching throwable that this value might contain.
     * If there is no match, or if this future contains a valid result then the new future will
     * contain the same.
     *
     * @param fn the function to apply if this value fails
     * @return a new value
     */

    public abstract VIO<O> recover(final Function<Throwable, O> fn);

    /**
     * Creates a new value that will handle any matching throwable that this value might contain
     * by assigning it another value.
     *
     * @param fn the function to apply if this Future fails
     * @return a new value
     */

    public abstract VIO<O> recoverWith(final Lambda<Throwable, O> fn);

    public abstract VIO<O> fallbackTo(final Lambda<Throwable, O> fn);

    /**
     * Adds a consumer to be notified of the succeeded result of this value.
     *
     * @param handler the consumer that will be called with the succeeded result
     * @return a reference to this value, so it can be used fluently
     */
    public abstract VIO<O> onSuccess(final Consumer<O> handler);

    /**
     * Add a handler to be notified of the result.
     *
     * @param successHandler the handler that will be called with the succeeded result
     * @param failureHandler the handler that will be called with the failed result
     * @return a reference to this, so it can be used fluently
     */
    public abstract VIO<O> onComplete(final Consumer<O> successHandler,
                                      final Consumer<Throwable> failureHandler
                                     );

    /**
     * Add a handler to be notified of the result.
     *
     * @param handler the handler that will be called with the result
     * @return a reference to this, so it can be used fluently
     */
    public abstract VIO<O> onComplete(final Handler<AsyncResult<O>> handler);


    public O result(){
        return get().result();
    }



}
