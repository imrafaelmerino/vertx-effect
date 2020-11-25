package vertx.effect.core;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import vertx.effect.Val;
import vertx.effect.exp.Cons;
import vertx.effect.λ;

import java.util.function.Consumer;
import java.util.function.Function;


public abstract class AbstractVal<O> implements Val<O> {


    private static final String LAMBDA_IS_NULL = "λ is null";
    private static final String SUCCESS_CONSUMER_IS_NULL = "successConsumer is null";

    @Override
    public Val<O> recover(final Function<Throwable, O> lambda) {
        if (lambda == null)
            return Cons.failure(new NullPointerException(LAMBDA_IS_NULL));

        return Cons.of(() -> get().compose(Future::succeededFuture,
                                           e -> Future.succeededFuture(lambda.apply(e))
                                          )
                      );
    }

    @Override
    public <P> Val<P> map(final Function<O, P> fn) {
        if (fn == null)
            return Cons.failure(new NullPointerException("fn is null"));
        return Cons.of(() -> get()
                               .map(fn)

                      );
    }

    @Override
    public Val<O> recoverWith(final λ<Throwable, O> lambda) {
        if (lambda == null)
            return Cons.failure(new NullPointerException(LAMBDA_IS_NULL));
        return Cons.of(() -> get().compose(Future::succeededFuture,
                                           e -> lambda.apply(e)
                                                      .get()
                                          )
                      );
    }

    @Override
    public Val<O> fallbackTo(final λ<Throwable, O> lambda) {
        if (lambda == null)
            return Cons.failure(new NullPointerException(LAMBDA_IS_NULL));
        return Cons.of(() -> get().compose(Future::succeededFuture,
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
            return Cons.failure(new NullPointerException(LAMBDA_IS_NULL));
        return Cons.of(() -> get().flatMap(o -> lambda.apply(o)
                                                      .get())
                      );
    }

    @Override
    public Val<O> onSuccess(final Consumer<O> successConsumer) {
        if (successConsumer == null)
            return Cons.failure(new NullPointerException(SUCCESS_CONSUMER_IS_NULL));
        return Cons.of(() -> get().onSuccess(successConsumer::accept));
    }

    @Override
    public Val<O> onComplete(final Consumer<O> successConsumer,
                             final Consumer<Throwable> failureConsumer) {
        if (successConsumer == null)
            return Cons.failure(new NullPointerException(SUCCESS_CONSUMER_IS_NULL));
        if (failureConsumer == null)
            return Cons.failure(new NullPointerException("failureConsumer is null"));
        return Cons.of(() -> get().onComplete(event -> {
                           if (event.succeeded()) successConsumer.accept(event.result());
                           else failureConsumer.accept(event.cause());
                       })
                      );
    }

    @Override
    public <U> Val<U> flatMap(final λ<O, U> successMapper,
                              final λ<Throwable, U> failureMapper) {
        if (successMapper == null)
            return Cons.failure(new NullPointerException("successMapper is null"));
        if (failureMapper == null)
            return Cons.failure(new NullPointerException("failureMapper is null"));
        return Cons.of(() -> get().compose(result -> successMapper.apply(result)
                                                                  .get(),
                                           failure -> failureMapper.apply(failure)
                                                                   .get()
                                          )
                      );

    }

    @Override
    public Val<O> onComplete(final Handler<AsyncResult<O>> handler) {
        if (handler == null)
            return Cons.failure(new NullPointerException("handler is null"));
        return Cons.of(() -> get().onComplete(handler));

    }


}