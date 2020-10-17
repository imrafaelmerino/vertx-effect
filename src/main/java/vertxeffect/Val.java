package vertxeffect;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;

import java.util.function.*;

/**
 A Val is just an alias for a lazy Vertx future. Laziness makes your code more functional and pure.
 It allows us to describe programs before executing them. The get method triggers the execution
 of the val and returns a future.

 @param <O> the type of the value produced by the future */
public interface Val<O> extends Supplier<Future<O>> {

    /**
     Creates a new value by applying a function to the successful result of this value. If this value
     returns an exception then the new value will also contain this exception.

     @param fn  the function which will be applied to the successful result of this value
     @param <P> the type of the returned value
     @return a new value
     */
    <P> Val<P> map(final Function<O, P> fn);

    /**
     Creates a new value by applying a function to the successful result of this value, and returns
     the result of the function as the new value. If this value returns an exception then
     the new value will also contain this exception.

     @param fn  the function which will be applied to the successful result of this value
     @param <Q> the type of the returned value
     @return a new value
     */
    <Q> Val<Q> flatMap(final λ<O, Q> fn);

    <Q> Val<Q> flatMap(final λ<O, Q> successMapper,
                       final λ<Throwable, Q> failureMapper);

    /**
     returns a new value tha will retry its execution if it fails

     @param attempts the number of attempts before returning an error
     @return a new value
     */
    Val<O> retry(final int attempts);




    /**
     returns a new value tha will retry its execution after the an action.

     @param attempts          the number of attempts before returning an error
     @param actionBeforeRetry the function that produces the action to be executed before the retry
     @return a new value
     */
    Val<O> retry(final int attempts,
                 final BiFunction<Throwable, Integer, Val<Void>> actionBeforeRetry);

    /**
     returns a new value tha will retry its execution if it fails with an error that
     satisfies the given predicate.

     @param predicate the predicate against which the returned error will be tested on
     @param attempts  the number of attempts before returning an error
     @return a new value
     */
    Val<O> retryIf(final Predicate<Throwable> predicate,
                   final int attempts
                  );

    /**
     returns a new value tha will retry its execution after an action if it fails with an
     error that satisfies the given predicate.

     @param predicate         the predicate against which the returned error will be tested on
     @param attempts          the number of attempts before returning an error
     @param actionBeforeRetry the function that produces the action to be executed before the retry
     @return a new value
     */
    Val<O> retryIf(final Predicate<Throwable> predicate,
                   final int attempts,
                   final BiFunction<Throwable, Integer, Val<Void>> actionBeforeRetry);


    /**
     Creates a new value that will handle any matching throwable that this value might contain.
     If there is no match, or if this future contains a valid result then the new future will
     contain the same.

     @param fn the function to apply if this value fails
     @return a new value
     */

    Val<O> recover(final Function<Throwable, O> fn);

    /**
     Creates a new value that will handle any matching throwable that this value might contain
     by assigning it another value.

     @param fn the function to apply if this Future fails
     @return a new value
     */

    Val<O> recoverWith(final λ<Throwable, O> fn);

    Val<O> fallbackTo(final λ<Throwable, O> fn);

    /**
     Adds a consumer to be notified of the succeeded result of this value.

     @param handler the consumer that will be called with the succeeded result
     @return a reference to this value, so it can be used fluently
     */
    Val<O> onSuccess(final Consumer<O> handler);

    /**
     Add a handler to be notified of the result.

     @param successHandler the handler that will be called with the succeeded result
     @param failureHandler the handler that will be called with the failed result
     @return a reference to this, so it can be used fluently
     */
    Val<O> onComplete(final Consumer<O> successHandler,
                      final Consumer<Throwable> failureHandler);

    /**
     Add a handler to be notified of the result.

     @param handler the handler that will be called with the result
     @return a reference to this, so it can be used fluently
     */
    Val<O> onComplete(final Handler<AsyncResult<O>> handler);



}
