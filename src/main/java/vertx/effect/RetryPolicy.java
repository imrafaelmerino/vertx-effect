package vertx.effect;

import java.util.function.BiFunction;

public interface RetryPolicy<O> extends BiFunction<O,Integer,Val<Void>> {}
