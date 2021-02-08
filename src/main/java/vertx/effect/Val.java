package vertx.effect;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static java.util.Objects.requireNonNull;

/**
 A Val is just an alias for a lazy Vertx future. Laziness makes your code more functional and pure.
 It allows us to describe programs before executing them. The get method triggers the execution
 of the val and returns a future.

 @param <O> the type of the value produced by the future */
public abstract class Val<O> implements Supplier<Future<O>> {

    public static final Val<Void> NULL = Val.succeed(null);
    public static final Val<Boolean> TRUE = Val.succeed(true);
    public static final Val<Boolean> FALSE = Val.succeed(false);

    public static <O> Val<O> fail(final Throwable failure) {
        if (failure == null)
            return fail(new NullPointerException("failure is null"));
        return effect(() -> Future.failedFuture(failure));
    }

    public static <O> Val<O> effect(final Supplier<Future<O>> effect) {
        if (effect == null)
            return Val.fail(new NullPointerException("effect is null"));
        return new Cons<>(requireNonNull(effect));
    }

    public static <O> Val<O> succeed(final O constant) {
        return new Cons<>(() -> Future.succeededFuture(constant));
    }

    /**
     Creates a new value by applying a function to the successful result of this value. If this value
     returns an exception then the new value will also contain this exception.

     @param fn  the function which will be applied to the successful result of this value
     @param <P> the type of the returned value
     @return a new value
     */
    public abstract <P> Val<P> map(final Function<O, P> fn);

    /**
     Creates a new value by applying a function to the successful result of this value, and returns
     the result of the function as the new value. If this value returns an exception then
     the new value will also contain this exception.

     @param fn  the function which will be applied to the successful result of this value
     @param <Q> the type of the returned value
     @return a new value
     */
    public abstract <Q> Val<Q> flatMap(final λ<O, Q> fn);

    public abstract <Q> Val<Q> flatMap(final λ<O, Q> successMapper,
                                       final λ<Throwable, Q> failureMapper);

    /**
     returns a new value tha will retry its execution if it fails

     @param retryPolicy the policy to retry
     @return a new value
     */
    public abstract Val<O> retry(RetryPolicy retryPolicy);


    public abstract Val<O> retry(final Predicate<Throwable> predicate,
                                 final RetryPolicy policy);


    public abstract Val<O> retryOnFailure(final Predicate<O> predicate,
                                          final RetryPolicy policy);

    /**
     Creates a new value that will handle any matching throwable that this value might contain.
     If there is no match, or if this future contains a valid result then the new future will
     contain the same.

     @param fn the function to apply if this value fails
     @return a new value
     */

    public abstract Val<O> recover(final Function<Throwable, O> fn);

    /**
     Creates a new value that will handle any matching throwable that this value might contain
     by assigning it another value.

     @param fn the function to apply if this Future fails
     @return a new value
     */

    public abstract Val<O> recoverWith(final λ<Throwable, O> fn);

    public abstract Val<O> fallbackTo(final λ<Throwable, O> fn);

    /**
     Adds a consumer to be notified of the succeeded result of this value.

     @param handler the consumer that will be called with the succeeded result
     @return a reference to this value, so it can be used fluently
     */
    public abstract Val<O> onSuccess(final Consumer<O> handler);

    /**
     Add a handler to be notified of the result.

     @param successHandler the handler that will be called with the succeeded result
     @param failureHandler the handler that will be called with the failed result
     @return a reference to this, so it can be used fluently
     */
    public abstract Val<O> onComplete(final Consumer<O> successHandler,
                                      final Consumer<Throwable> failureHandler);

    /**
     Add a handler to be notified of the result.

     @param handler the handler that will be called with the result
     @return a reference to this, so it can be used fluently
     */
    public abstract Val<O> onComplete(final Handler<AsyncResult<O>> handler);


}
