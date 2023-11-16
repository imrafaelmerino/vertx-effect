package vertx.effect.stub;


import vertx.effect.VIO;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.function.IntFunction;
import java.util.function.Supplier;

import static java.util.Objects.requireNonNull;

final class FailureStub<O> extends VIOStub<O> implements Supplier<VIO<O>> {
    private final IntFunction<Duration> delay;
    private final IntFunction<Throwable> getError;
    private final O value;
    private final VIO<O> effect;
    private int counter;

    FailureStub(
            final IntFunction<Throwable> getError,
            final O value
               ) {
        this(requireNonNull(getError), n -> Duration.of(0, ChronoUnit.MILLIS), value);
    }

    FailureStub(final IntFunction<Throwable> error,
                final IntFunction<Duration> delay,
                final O value
               ) {
        this.getError = requireNonNull(error);
        this.delay = requireNonNull(delay);
        this.value = value;
        this.effect = VIO.effect(() -> {
            counter += 1;
            long millis = delay.apply(counter).toMillis();
            Throwable ex = error.apply(counter);
            return ex != null ?
                    delayExc(millis, ex)
                            .onFailure(exc -> System.out.println(THREAD_NAME.get() + "counter= " + counter + ", exception returned: " + ex)) :
                    delayValue(millis, value)
                            .onSuccess(val -> System.out.println(THREAD_NAME.get() + "counter= " + counter + ", value returned: " + val));

        });
    }

    @Override
    public VIO<O> get() {
        //danger zone, counter is mutable, we have to return a brand new instance
        return new FailureStub<>(getError, delay, value).effect;
    }
}
