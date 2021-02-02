package vertx.effect.exp;

import io.vavr.Tuple2;
import io.vavr.collection.List;
import io.vertx.core.CompositeFuture;
import io.vertx.core.Future;
import vertx.effect.RetryPolicy;
import vertx.effect.Val;
import java.util.LinkedHashMap;
import java.util.Map;


import static java.util.Objects.requireNonNull;

/**
 Represents a supplier of a vertx future which result is a json object. It has the same
 recursive structure as a json object. Each key has a future associated that it's
 executed asynchronously. When all the futures are completed, all the results are combined into
 a json object.
 */
final class ParallelMapExp<O> extends MapExp<O> {

    @SuppressWarnings({"rawtypes"})
    public static final ParallelMapExp EMPTY = new ParallelMapExp<>();


    ParallelMapExp() {
    }

    ParallelMapExp(final io.vavr.collection.Map<String, Val<? extends O>> bindings) {
        this.bindings = requireNonNull(bindings);
    }


    /**
     returns a new object future inserting the given future at the given key

     @param key the given key
     @param exp the given future
     @return a new JsObjFuture
     */
    @Override
    public ParallelMapExp<O> set(final String key,
                                 final Val<? extends O> exp
                                ) {
        return new ParallelMapExp<>(bindings.put(requireNonNull(key),
                                                 requireNonNull(exp)
                                                ));
    }

    @Override
    public Val<Map<String, O>> retry(final RetryPolicy policy) {

        return new ParallelMapExp<>(bindings.mapValues(it -> it.retry(policy)));
    }


    @Override
    public Future<Map<String, O>> get() {

        List<String> keys = bindings.keysIterator()
                                    .toList();
        @SuppressWarnings({"rawtypes"})
        io.vavr.collection.Map<String, Future> futures = bindings.map((k, v) -> new Tuple2<>(k,
                                                                                             v.get()
                                                                      )
                                                                     );
        return CompositeFuture.all(futures.values()
                                          .toJavaList()
                                  )
                              .map(r -> {
                                  Map<String, O> result = new LinkedHashMap<>();
                                  java.util.List<O> list = r.result()
                                                            .list();
                                  for (int i = 0; i < list.size(); i++) {
                                      result.put(keys.get(i),
                                                 list.get(i)
                                                );
                                  }

                                  return result;
                              });
    }


}
