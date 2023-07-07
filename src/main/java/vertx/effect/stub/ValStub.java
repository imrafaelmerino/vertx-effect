package vertx.effect.stub;

import vertx.effect.VIO;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.function.IntFunction;
import java.util.function.Supplier;

import static java.util.Objects.requireNonNull;

final class ValStub<O> extends VIOStub<O> implements Supplier<VIO<O>> {

    private int counter;
    private final IntFunction<O> getValue;
    private final IntFunction<Duration> delay;
    private final VIO<O> effect;

    ValStub(final IntFunction<O> getValue,
            final IntFunction<Duration> delay
           ) {
        this.getValue = requireNonNull(getValue);
        this.delay = requireNonNull(delay);
        this.effect = VIO.effect(() -> {
            counter += 1;
            System.out.println(THREAD_NAME.get() + "counter: " + counter);
            return delayValue(delay.apply(counter).toMillis(),
                              getValue.apply(counter)
                             ).onSuccess(val -> System.out.println(THREAD_NAME.get() + "value returned: " + val));
        });

    }

    ValStub(final IntFunction<O> getValue) {
        this(getValue,
             $ -> Duration.of(0,
                              ChronoUnit.MILLIS
                             )
            );

    }

    @Override
    public VIO<O> get() {
        //danger zone, counter is mutable, we have to return a brand-new instance
        return new ValStub<>(getValue, delay).effect;
    }
}
