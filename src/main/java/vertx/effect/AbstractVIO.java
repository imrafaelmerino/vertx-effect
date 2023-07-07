package vertx.effect;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;


abstract class AbstractVIO<O> extends VIO<O> {

    @Override
    public VIO<O> recover(final Function<Throwable, O> lambda) {
        Objects.requireNonNull(lambda);
        return VIO.effect(() -> get().compose(Future::succeededFuture,
                                              e -> Future.succeededFuture(lambda.apply(e))
                                             )
                         );
    }

    @Override
    public <P> VIO<P> map(final Function<O, P> fn) {
        Objects.requireNonNull(fn);
        return VIO.effect(() -> get()
                                  .map(fn)
                         );
    }

    @Override
    public VIO<O> recoverWith(final Lambda<Throwable, O> lambda) {
        Objects.requireNonNull(lambda);
        return VIO.effect(() -> get().compose(Future::succeededFuture,
                                              e -> lambda.apply(e)
                                                         .get()
                                             )
                         );
    }

    @Override
    public VIO<O> fallbackTo(final Lambda<Throwable, O> lambda) {
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

    @Override
    public <Q> VIO<Q> then(final Lambda<O, Q> lambda) {
        Objects.requireNonNull(lambda);
        return VIO.effect(() -> get().flatMap(o -> lambda.apply(o)
                                                         .get())
                         );
    }

    @Override
    public VIO<O> onSuccess(final Consumer<O> successConsumer) {
        Objects.requireNonNull(successConsumer);
        return VIO.effect(() -> get().onSuccess(successConsumer::accept));
    }

    @Override
    public VIO<O> onComplete(final Consumer<O> successConsumer,
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

    @Override
    public <U> VIO<U> then(final Lambda<O, U> successMapper,
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

    @Override
    public VIO<O> onComplete(final Handler<AsyncResult<O>> handler) {
        Objects.requireNonNull(handler);
        return VIO.effect(() -> get().onComplete(handler));

    }

    @Override
    public VIO<O> repeat(final Predicate<O> predicate,
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

    @Override
    public VIO<O> retry(final RetryPolicy policy) {
        Objects.requireNonNull(policy);

        return retry(this,
                     policy,
                     new RetryStatus(0, 0, 0),
                     e -> true
                    );
    }


    @Override
    public VIO<O> retry(final Predicate<Throwable> predicate,
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

}