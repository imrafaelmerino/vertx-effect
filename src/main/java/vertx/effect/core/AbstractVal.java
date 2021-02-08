package vertx.effect.core;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import vertx.effect.*;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;


public abstract class AbstractVal<O> extends Val<O> {

    private static final String LAMBDA_IS_NULL = "λ is null";
    private static final String SUCCESS_CONSUMER_IS_NULL = "successConsumer is null";

    @Override
    public Val<O> recover(final Function<Throwable, O> lambda) {
        if (lambda == null)
            return Val.fail(new NullPointerException(LAMBDA_IS_NULL));

        return Val.effect(() -> get().compose(Future::succeededFuture,
                                           e -> Future.succeededFuture(lambda.apply(e))
                                             )
                         );
    }

    @Override
    public <P> Val<P> map(final Function<O, P> fn) {
        if (fn == null)
            return Val.fail(new NullPointerException("fn is null"));
        return Val.effect(() -> get()
                               .map(fn)

                         );
    }

    @Override
    public Val<O> recoverWith(final λ<Throwable, O> lambda) {
        if (lambda == null)
            return Val.fail(new NullPointerException(LAMBDA_IS_NULL));
        return Val.effect(() -> get().compose(Future::succeededFuture,
                                           e -> lambda.apply(e)
                                                      .get()
                                             )
                         );
    }

    @Override
    public Val<O> fallbackTo(final λ<Throwable, O> lambda) {
        if (lambda == null)
            return Val.fail(new NullPointerException(LAMBDA_IS_NULL));
        return Val.effect(() -> get().compose(Future::succeededFuture,
                                           e -> lambda.apply(e)
                                                      .get()
                                                      .compose(Future::succeededFuture,
                                                               e1 -> Future.failedFuture(e)
                                                              )
                                             )
                         );

    }

    @Override
    public <Q> Val<Q> flatMap(final λ<O, Q> lambda) {
        if (lambda == null)
            return Val.fail(new NullPointerException(LAMBDA_IS_NULL));
        return Val.effect(() -> get().flatMap(o -> lambda.apply(o)
                                                         .get())
                         );
    }

    @Override
    public Val<O> onSuccess(final Consumer<O> successConsumer) {
        if (successConsumer == null)
            return Val.fail(new NullPointerException(SUCCESS_CONSUMER_IS_NULL));
        return Val.effect(() -> get().onSuccess(successConsumer::accept));
    }

    @Override
    public Val<O> onComplete(final Consumer<O> successConsumer,
                             final Consumer<Throwable> failureConsumer) {
        if (successConsumer == null)
            return Val.fail(new NullPointerException(SUCCESS_CONSUMER_IS_NULL));
        if (failureConsumer == null)
            return Val.fail(new NullPointerException("failureConsumer is null"));
        return Val.effect(() -> get().onComplete(event -> {
                           if (event.succeeded()) successConsumer.accept(event.result());
                           else failureConsumer.accept(event.cause());
                       })
                         );
    }

    @Override
    public <U> Val<U> flatMap(final λ<O, U> successMapper,
                              final λ<Throwable, U> failureMapper) {
        if (successMapper == null)
            return Val.fail(new NullPointerException("successMapper is null"));
        if (failureMapper == null)
            return Val.fail(new NullPointerException("failureMapper is null"));
        return Val.effect(() -> get().compose(result -> successMapper.apply(result)
                                                                     .get(),
                                           failure -> failureMapper.apply(failure)
                                                                   .get()
                                             )
                         );

    }

    @Override
    public Val<O> onComplete(final Handler<AsyncResult<O>> handler) {
        if (handler == null)
            return Val.fail(new NullPointerException("handler is null"));
        return Val.effect(() -> get().onComplete(handler));

    }

    @Override
    public Val<O> retryOnFailure(final Predicate<O> predicate,
                                 final RetryPolicy policy) {
        return retryOnFailure(this,
                              policy,
                              new RetryStatus(0,
                                              0,
                                              0
                              ),
                              predicate
                             );
    }

    private Val<O> retryOnFailure(Val<O> exp,
                                  Function<RetryStatus, Optional<Timer>> policy,
                                  RetryStatus rs,
                                  Predicate<O> predicate) {

        return exp.flatMap(o -> {
                               if (predicate.test(o)) {
                                   Optional<Timer> delayOpt = policy.apply(rs);
                                   if (delayOpt.isEmpty()) return Val.succeed(o);
                                   Timer timer = delayOpt.get();
                                   return timer.delay.flatMap(nill -> {
                                                                long delayDuration = timer.duration.toMillis();
                                                                return retryOnFailure(exp,
                                                                                      policy,
                                                                                      new RetryStatus(rs.rsIterNumber + 1,
                                                                                                      rs.rsCumulativeDelay + delayDuration,
                                                                                                      delayDuration
                                                                                      ),
                                                                                      predicate
                                                                                     );
                                                            }
                                                             );
                               }
                               else return Val.succeed(o);
                           }
                          );

    }

    @Override
    public Val<O> retry(final RetryPolicy policy) {
        if (policy == null) return Val.fail(new IllegalArgumentException("Cons.retry: policy is null"));

        return retry(this,
                     policy,
                     new RetryStatus(0,
                                     0,
                                     0
                     ),
                     e -> true
                    );
    }


    @Override
    public Val<O> retry(final Predicate<Throwable> predicate,
                        final RetryPolicy policy) {
        if (policy == null) return Val.fail(new IllegalArgumentException("Cons.retry: policy is null"));
        if (predicate == null) return Val.fail(new IllegalArgumentException("Cons.retry: predicate is null"));
        return retry(this,
                     policy,
                     new RetryStatus(0,
                                     0,
                                     -1
                     ),
                     predicate
                    );
    }


    private Val<O> retry(Val<O> exp,
                         Function<RetryStatus, Optional<Timer>> policy,
                         RetryStatus rs,
                         Predicate<Throwable> predicate) {

        return exp.flatMap(o -> {
                               return Val.succeed(o);
                           },
                           exc -> {
                               if (predicate.test(exc)) {
                                   Optional<Timer> delayOpt = policy.apply(rs);
                                   if (delayOpt.isEmpty()) return Val.fail(exc);
                                   Timer timer = delayOpt.get();
                                   return timer.delay.flatMap(nill -> {
                                                                long delayDuration = timer.duration.toMillis();
                                                                return retry(exp,
                                                                             policy,
                                                                             new RetryStatus(rs.rsIterNumber + 1,
                                                                                             rs.rsCumulativeDelay + delayDuration,
                                                                                             delayDuration
                                                                             ),
                                                                             predicate
                                                                            );
                                                            }
                                                             );
                               }
                               else return Val.fail(exc);
                           }
                          );
    }

}