package vertx.effect.exp;

import io.vavr.collection.List;
import io.vavr.collection.Map;
import io.vavr.collection.TreeMap;
import io.vertx.core.CompositeFuture;
import io.vertx.core.Future;
import jsonvalues.JsObj;
import jsonvalues.JsValue;
import vertx.effect.Val;

import java.util.function.BiFunction;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static java.util.Objects.requireNonNull;

/**
 Represents a supplier of a vertx future which result is a json object. It has the same
 recursive structure as a json object. Each key has a future associated that it's
 executed asynchronously. When all the futures are completed, all the results are combined into
 a json object.
 */
final class ParallelJsObjExp extends JsObjExp {
    private static final String ATTEMPTS_LOWER_THAN_ONE_ERROR = "attempts < 1";

    Map<String, Val<? extends JsValue>> bindings = TreeMap.empty();

    ParallelJsObjExp() {
    }

    ParallelJsObjExp(final Map<String, Val<? extends JsValue>> bindings) {
        this.bindings = bindings;
    }

    /**
     returns a new object future inserting the given future at the given key

     @param key    the given key
     @param future the given future
     @return a new JsObjFuture
     */
    @Override
    public ParallelJsObjExp set(final String key,
                                final Val<? extends JsValue> future
                               ) {
        final Map<String, Val<? extends JsValue>> a = bindings.put(requireNonNull(key),
                                                                   requireNonNull(future)
                                                                  );
        return new ParallelJsObjExp(a);
    }


    /**
     it triggers the execution of all the completable futures, combining the results into a JsObj

     @return a Future of a json object
     */
    @Override
    @SuppressWarnings({"unchecked", "rawtypes"})
    public Future<JsObj> get() {

        List<String> keys = bindings.keysIterator()
                                    .toList();


        java.util.List futures = bindings.values()
                                         .map(Supplier::get)
                                         .toJavaList();
        return CompositeFuture.all(futures)
                              .map(r -> {
                                  JsObj result = JsObj.empty();
                                  java.util.List<?> list = r.result()
                                                            .list();
                                  for (int i = 0; i < list.size(); i++) {
                                      result = result.set(keys.get(i),
                                                          ((JsValue) list.get(i))
                                                         );
                                  }

                                  return result;

                              });
    }

    @Override
    public Val<JsObj> retry(final int attempts) {
        if (attempts < 1)
            return Cons.failure(new IllegalArgumentException(ATTEMPTS_LOWER_THAN_ONE_ERROR));

        return new ParallelJsObjExp(bindings.mapValues(it -> it.retry(attempts)));
    }


    @Override
    public Val<JsObj> retry(final int attempts,
                            final BiFunction<Throwable, Integer, Val<Void>> actionBeforeRetry) {
        if (attempts < 1)
            return Cons.failure(new IllegalArgumentException(ATTEMPTS_LOWER_THAN_ONE_ERROR));

        if (actionBeforeRetry == null)
            return Cons.failure(new NullPointerException("actionBeforeRetry is null"));

        return new ParallelJsObjExp(bindings.mapValues(it -> it.retry(attempts,
                                                                      actionBeforeRetry
                                                                     )));
    }

    @Override
    public Val<JsObj> retry(final Predicate<Throwable> predicate,
                            final int attempts) {
        if (attempts < 1)
            return Cons.failure(new IllegalArgumentException(ATTEMPTS_LOWER_THAN_ONE_ERROR));
        if (predicate == null)
            return Cons.failure(new NullPointerException("predicate is null"));
        return new ParallelJsObjExp(bindings.mapValues(it -> it.retry(predicate,
                                                                      attempts
                                                                     )));

    }


    @Override
    public Val<JsObj> retry(final Predicate<Throwable> predicate,
                            final int attempts,
                            final BiFunction<Throwable, Integer, Val<Void>> actionBeforeRetry) {
        if (attempts < 1)
            return Cons.failure(new IllegalArgumentException(ATTEMPTS_LOWER_THAN_ONE_ERROR));
        if (predicate == null)
            return Cons.failure(new NullPointerException("predicate is null"));
        if (actionBeforeRetry == null)
            return Cons.failure(new NullPointerException("actionBeforeRetry is null"));

        return new ParallelJsObjExp(bindings.mapValues(it -> it.retry(predicate,
                                                                      attempts,
                                                                      actionBeforeRetry
                                                                     )));
    }

}
