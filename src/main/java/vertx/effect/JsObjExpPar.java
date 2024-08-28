package vertx.effect;


import io.vertx.core.Future;
import jsonvalues.JsObj;
import jsonvalues.JsValue;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static java.util.Objects.requireNonNull;

/**
 * Represents a supplier of a vertx future which result is a json object. It has the same recursive structure as a json
 * object. Each key has a future associated that it's executed asynchronously. When all the futures are completed, all
 * the results are combined into a json object.
 */
final class JsObjExpPar extends JsObjExp {


    JsObjExpPar() {
    }

    JsObjExpPar(final Map<String, VIO<? extends JsValue>> bindings) {
        this.bindings = bindings;
    }

    /**
     * returns a new object future inserting the given future at the given key
     *
     * @param key    the given key
     * @param future the given future
     * @return a new JsObjFuture
     */
    @Override
    public JsObjExpPar set(final String key,
                           final VIO<? extends JsValue> future
                          ) {
        Map<String, VIO<? extends JsValue>> map = new LinkedHashMap<>(bindings);
        map.put(requireNonNull(key),
                requireNonNull(future)
               );
        return new JsObjExpPar(map);
    }


    /**
     * it triggers the execution of all the completable futures, combining the results into a JsObj
     *
     * @return a Future of a json object
     */
    @Override
    public Future<JsObj> get() {

        String[] keys = bindings.keySet()
                                .toArray(String[]::new);
        List<Future<?>> xs = new ArrayList<>();
        for (String key : keys) {
            xs.add(bindings.get(key).get());
        }


        return Future.all(xs)
                     .map(r -> {
                         JsObj result = JsObj.empty();
                         java.util.List<?> list = r.result()
                                                   .list();
                         for (int i = 0; i < list.size(); i++) {
                             result = result.set(keys[i],
                                                 ((JsValue) list.get(i))
                                                );
                         }

                         return result;

                     });
    }

    @Override
    public VIO<JsObj> retryEach(final RetryPolicy policy) {
        return retryEach(e -> true,
                         policy
                        );
    }

    @Override
    public VIO<JsObj> retryEach(final Predicate<Throwable> predicate,
                                final RetryPolicy policy
                               ) {
        Objects.requireNonNull(policy);
        Objects.requireNonNull(predicate);
        return new JsObjExpPar(bindings.entrySet()
                                       .stream()
                                       .collect(Collectors.toMap(Map.Entry::getKey,
                                                                 e -> e.getValue()
                                                                       .retry(predicate, policy)
                                                                )
                                               )
        );

    }

}
