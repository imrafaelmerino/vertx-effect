package vertx.effect.exp;

import io.vavr.collection.List;
import io.vavr.collection.Seq;
import io.vertx.core.Future;
import vertx.effect.Val;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Predicate;

import static java.util.Objects.requireNonNull;

/**
 Represents a supplier of a vertx future which result is a json object. It has the same
 recursive structure as a json object. Each key has a future associated that it's
 executed asynchronously. When all the futures are completed, all the results are combined into
 a json object.
 */
final class SequentialMapExp<O> extends MapExp<O> {

    @SuppressWarnings({"rawtypes"})
    public static final SequentialMapExp EMPTY = new SequentialMapExp<>();


    SequentialMapExp() {
    }

    SequentialMapExp(final io.vavr.collection.Map<String, Val<? extends O>> bindings) {
        this.bindings = requireNonNull(bindings);
    }


    /**
     returns a new object future inserting the given future at the given key

     @param key the given key
     @param exp the given future
     @return a new JsObjFuture
     */
    @Override
    public MapExp<O> set(final String key,
                         final Val<? extends O> exp
                        ) {
        return new SequentialMapExp<>(bindings.put(requireNonNull(key),
                                                   requireNonNull(exp)
                                                  ));
    }


    @Override
    public Val<Map<String, O>> retry(final int attempts) {
        if (attempts < 1)
            return Cons.failure(new IllegalArgumentException(ATTEMPTS_LOWER_THAN_ONE_ERROR));
        return new SequentialMapExp<>(bindings.mapValues(it -> it.retry(attempts)));
    }


    @Override
    public Val<Map<String, O>> retry(final int attempts,
                                     final BiFunction<Throwable, Integer, Val<Void>> actionBeforeRetry) {
        if (attempts < 1)
            return Cons.failure(new IllegalArgumentException(ATTEMPTS_LOWER_THAN_ONE_ERROR));

        return new SequentialMapExp<>(bindings.mapValues(it -> it.retry(attempts,
                                                                        actionBeforeRetry
                                                                       )
                                                        ));
    }

    @Override
    public Val<Map<String, O>> retry(final Predicate<Throwable> predicate,
                                     final int attempts) {
        if (attempts < 1)
            return Cons.failure(new IllegalArgumentException(ATTEMPTS_LOWER_THAN_ONE_ERROR));
        if (predicate == null)
            return Cons.failure(new NullPointerException("predicate is null"));


        return new SequentialMapExp<>(bindings.mapValues(it -> it.retry(predicate,
                                                                        attempts
                                                                       ))
        );

    }


    @Override
    public Val<Map<String, O>> retry(final Predicate<Throwable> predicate,
                                     final int attempts,
                                     final BiFunction<Throwable, Integer, Val<Void>> actionBeforeRetry) {

        if (attempts < 1)
            return Cons.failure(new IllegalArgumentException(ATTEMPTS_LOWER_THAN_ONE_ERROR));
        if (predicate == null)
            return Cons.failure(new NullPointerException("predicate is null"));
        if (actionBeforeRetry == null)
            return Cons.failure(new NullPointerException("actionBeforeRetry is null"));

        return new SequentialMapExp<>(bindings.mapValues(it -> it.retry(predicate,
                                                                        attempts,
                                                                        actionBeforeRetry
                                                                       )));
    }


    @Override
    public Future<Map<String, O>> get() {

        List<String> keys = bindings.keysIterator()
                                    .toList();
        Seq<Val<? extends O>> values = bindings.values();

        Future<Map<String, O>> acc = Future.succeededFuture(new LinkedHashMap<>());

        for (int i = 0; i < keys.size(); i++) {
            String           k   = keys.get(i);
            Val<? extends O> val = values.get(i);
            acc = acc.flatMap(m -> val.get()
                                      .map(fut -> {
                                               m.put(k,
                                                     fut
                                                    );
                                               return m;
                                           }
                                          )
                             );
        }

        return acc;
    }

}
