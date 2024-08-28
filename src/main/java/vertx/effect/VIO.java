package vertx.effect;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;

import java.time.Duration;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.util.Objects.requireNonNull;

/**
 * A Val is just an alias for a lazy Vertx future. Laziness makes your code more functional and pure. It allows us to
 * describe programs before executing them. The get method triggers the execution of the val and returns a future.
 *
 * @param <O> the type of the value produced by the future
 */
public sealed interface VIO<O> extends Supplier<Future<O>> permits Exp, Val {

    VIO<Boolean> TRUE = VIO.succeed(true);
    VIO<Boolean> FALSE = VIO.succeed(false);

    static <O> VIO<O> fail(final Throwable failure) {
        if (failure == null) return fail(new NullPointerException("failure is null"));
        return effect(() -> Future.failedFuture(failure));
    }

    static <O> VIO<O> effect(final Supplier<Future<O>> effect) {
        Objects.requireNonNull(effect);
        return new Val<>(requireNonNull(effect));
    }

    @SafeVarargs
    @SuppressWarnings({"rawtypes", "varargs"})
    static <O> VIO<O> race(final VIO<O> first,
                           final VIO<O>... others
                          ) {
        Objects.requireNonNull(first);
        Objects.requireNonNull(others);
        List<VIO<O>> list = new ArrayList<>();
        list.add(first);
        if (others.length > 0) list.addAll(Arrays.stream(others)
                                                 .toList()
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

    static <O> VIO<O> succeed(final O constant) {
        return new Val<>(() -> Future.succeededFuture(constant));
    }

    /**
     * Sleeps for the specified duration before evaluating this effect.
     * <p>This method introduces a pause in the execution flow for the specified duration using the
     * {@link Thread#sleep(long)} method. It can be useful for testing purposes, or when working with virtual threads.
     * However, it should be used with caution, as introducing delays in a program's execution blocking threads can
     * impact performance and behavior.
     *
     * @param duration The duration to sleep for.
     * @return An {@code IO<O>} representing the delayed operation.
     */
    default VIO<O> sleep(final Duration duration) {
        Objects.requireNonNull(duration);
        return VIO.lazy(() -> {
            try {
                Thread.sleep(duration.toMillis());
                return null;
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

        }).then(nill -> this);

    }

    static <O> VIO<O> lazy(final Supplier<O> supplier){
        return VIO.effect(()-> Future.succeededFuture(supplier.get()));
    }

    static <E> VIO<E> NULL() {
        return VIO.succeed(null);
    }

    /**
     * Creates a new value by applying a function to the successful result of this value. If this value returns an
     * exception then the new value will also contain this exception.
     *
     * @param fn  the function which will be applied to the successful result of this value
     * @param <P> the type of the returned value
     * @return a new value
     */
    default <P> VIO<P> map(final Function<O, P> fn) {
        Objects.requireNonNull(fn);
        return VIO.effect(() -> get()
                                  .map(fn)
                         );
    }

    /**
     * Creates a new value by applying a function to the successful result of this value, and returns the result of the
     * function as the new value. If this value returns an exception then the new value will also contain this
     * exception.
     *
     * @param lambda the function which will be applied to the successful result of this value
     * @param <Q>    the type of the returned value
     * @return a new value
     */
    default <Q> VIO<Q> then(final Lambda<O, Q> lambda) {
        Objects.requireNonNull(lambda);
        return VIO.effect(() -> get().flatMap(o -> lambda.apply(o)
                                                         .get())
                         );
    }

    default <U> VIO<U> then(final Lambda<O, U> successMapper,
                            final Lambda<Throwable, U> failureMapper
                           ) {
        Objects.requireNonNull(successMapper);
        Objects.requireNonNull(failureMapper);

        return VIO.effect(() -> get().compose(result -> successMapper.apply(result)
                                                                     .get(),
                                              failure -> failureMapper.apply(failure)
                                                                      .get()
                                             )
                         );

    }


    default VIO<O> retry(final Predicate<Throwable> predicate,
                         final RetryPolicy policy
                        ) {
        Objects.requireNonNull(policy);
        Objects.requireNonNull(predicate);

        return retry(this,
                     policy,
                     new RetryStatus(0,
                                     0,
                                     -1
                     ),
                     predicate
                    );
    }

    private VIO<O> retry(VIO<O> exp,
                         Function<RetryStatus, Optional<Delay>> policy,
                         RetryStatus rs,
                         Predicate<Throwable> predicate
                        ) {

        return exp.then(VIO::succeed,
                        exc -> {
                            if (predicate.test(exc)) {
                                Optional<Delay> delayOpt = policy.apply(rs);
                                if (delayOpt.isEmpty()) return VIO.fail(exc);
                                Delay delay = delayOpt.get();
                                return delay.effect.then(nill -> {
                                                             long delayDuration = delay.duration.toMillis();
                                                             return retry(exp,
                                                                          policy,
                                                                          new RetryStatus(rs.counter + 1,
                                                                                          rs.cumulativeDelay + delayDuration,
                                                                                          delayDuration
                                                                          ),
                                                                          predicate
                                                                         );
                                                         }
                                                        );
                            } else return VIO.fail(exc);
                        }
                       );
    }

    default VIO<O> repeat(final Predicate<O> predicate,
                          final RetryPolicy policy
                         ) {
        return repeat(this,
                      policy,
                      new RetryStatus(0,
                                      0,
                                      0
                      ),
                      predicate
                     );
    }

    private VIO<O> repeat(VIO<O> exp,
                          Function<RetryStatus, Optional<Delay>> policy,
                          RetryStatus rs,
                          Predicate<O> predicate
                         ) {

        return exp.then(o -> {
                            if (predicate.test(o)) {
                                Optional<Delay> delayOpt = policy.apply(rs);
                                if (delayOpt.isEmpty()) return VIO.succeed(o);
                                Delay delay = delayOpt.get();
                                return delay.effect.then(nill -> {
                                                             long delayDuration = delay.duration.toMillis();
                                                             return repeat(exp,
                                                                           policy,
                                                                           new RetryStatus(rs.counter + 1,
                                                                                           rs.cumulativeDelay + delayDuration,
                                                                                           delayDuration
                                                                           ),
                                                                           predicate
                                                                          );
                                                         }
                                                        );
                            } else return VIO.succeed(o);
                        }
                       );

    }

    default VIO<O> retry(final RetryPolicy policy) {
        Objects.requireNonNull(policy);

        return retry(this,
                     policy,
                     new RetryStatus(0, 0, 0),
                     e -> true
                    );
    }

    /**
     * Creates a new value that will handle any matching throwable that this value might contain. If there is no match,
     * or if this future contains a valid result then the new future will contain the same.
     *
     * @param lambda the function to apply if this value fails
     * @return a new value
     */

    default VIO<O> recover(final Function<Throwable, O> lambda) {
        Objects.requireNonNull(lambda);
        return VIO.effect(() -> get().compose(Future::succeededFuture,
                                              e -> Future.succeededFuture(lambda.apply(e))
                                             )
                         );
    }

    /**
     * Creates a new value that will handle any matching throwable that this value might contain by assigning it another
     * value.
     *
     * @param lambda the function to apply if this Future fails
     * @return a new value
     */

    default VIO<O> recoverWith(final Lambda<Throwable, O> lambda) {
        Objects.requireNonNull(lambda);
        return VIO.effect(() -> get().compose(Future::succeededFuture,
                                              e -> lambda.apply(e)
                                                         .get()
                                             )
                         );
    }

    default VIO<O> fallbackTo(final Lambda<Throwable, O> lambda) {
        Objects.requireNonNull(lambda);
        return VIO.effect(() -> get().compose(Future::succeededFuture,
                                              e -> lambda.apply(e)
                                                         .get()
                                                         .compose(Future::succeededFuture,
                                                                  e1 -> Future.failedFuture(e)
                                                                 )
                                             )
                         );

    }

    /**
     * Adds a consumer to be notified of the succeeded result of this value.
     *
     * @param successConsumer the consumer that will be called with the succeeded result
     * @return a reference to this value, so it can be used fluently
     */
    default VIO<O> onSuccess(final Consumer<O> successConsumer) {
        Objects.requireNonNull(successConsumer);
        return VIO.effect(() -> get().onSuccess(successConsumer::accept));
    }

    /**
     * Add a handler to be notified of the result.
     *
     * @param successConsumer the handler that will be called with the succeeded result
     * @param failureConsumer the handler that will be called with the failed result
     * @return a reference to this, so it can be used fluently
     */
    default VIO<O> onComplete(final Consumer<O> successConsumer,
                              final Consumer<Throwable> failureConsumer
                             ) {
        Objects.requireNonNull(successConsumer);
        Objects.requireNonNull(failureConsumer);

        return VIO.effect(() -> get().onComplete(event -> {
                              if (event.succeeded()) successConsumer.accept(event.result());
                              else failureConsumer.accept(event.cause());
                          })
                         );
    }

    /**
     * Add a handler to be notified of the result.
     *
     * @param handler the handler that will be called with the result
     * @return a reference to this, so it can be used fluently
     */
    default VIO<O> onComplete(final Handler<AsyncResult<O>> handler) {
        Objects.requireNonNull(handler);
        return VIO.effect(() -> get().onComplete(handler));

    }


    default O result() {
        return get().result();
    }


}
