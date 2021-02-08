package vertx.effect;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

public final class Clock {

    private Clock(){}

    public static λ<Void,Long> monotonic = v -> Val.succeed(System.nanoTime());

    public static λ<Void,Long> realTime = v -> Val.succeed(System.currentTimeMillis());

    public static λ<Void,Long> monotonic(TimeUnit unit){
        return v -> monotonic.apply(null).map( time -> unit.convert(Duration.ofNanos(time)));
    }

    public static λ<Void,Long> realTime(TimeUnit unit){
        return v -> realTime.apply(null).map( time -> unit.convert(Duration.ofMillis(time)));
    }

}
