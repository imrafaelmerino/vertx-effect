package vertx.effect.stub;

import io.vertx.core.Future;
import vertx.effect.VIO;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.function.IntFunction;
import java.util.function.Supplier;

public abstract sealed class VIOStub<O> implements Supplier<VIO<O>> permits FailureStub, ValStub {

    final Supplier<String> THREAD_NAME = () -> "IOMock [Thread " + Thread.currentThread().getName() + "] ";

    public static <O> VIOStub<O> succeed(final IntFunction<O> value,
                                         final IntFunction<Duration> delay
                                        ) {
        return new ValStub<>(value, delay);
    }

    public static <O> VIOStub<O> succeed(final IntFunction<O> value) {
        return new ValStub<>(value);
    }

    public static <O> VIOStub<O> failThenSucceed(final IntFunction<Throwable> error,
                                                 final O value
                                                ) {
        return new FailureStub<>(error, value);
    }

    public static <O> VIOStub<O> failThenSucceed(final IntFunction<Throwable> error,
                                                 final IntFunction<Duration> delay,
                                                 final O value
                                                ) {

        return new FailureStub<>(error, delay, value);
    }

    void sleep(long millis) {
        if (millis != 0) System.out.println(THREAD_NAME.get() + "sleeping " + millis + " ms.");
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    Future<O> delayValue(long millis,
                         O value
                        ) {
        return Future.fromCompletionStage(CompletableFuture.supplyAsync(() -> {
            sleep(millis);
            return value;
        }));

    }

    Future<O> delayExc(long millis,
                       Throwable failure
                      ) {
        return delayValue(millis,
                          null
                         ).flatMap(it -> Future.failedFuture(failure));
    }

    @Override
    public abstract VIO<O> get();
}
