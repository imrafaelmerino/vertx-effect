package vertx.effect;


import io.vertx.core.Future;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static java.util.Objects.requireNonNull;

/**
 * Represents a supplier of a vertx future which result is a json object. It has the same recursive structure as a json
 * object. Each key has a future associated that it's executed asynchronously. When all the futures are completed, all
 * the results are combined into a json object.
 */
final class MapExpPar<O> extends MapExp<O> {

    @SuppressWarnings({"rawtypes"})
    public static final MapExpPar EMPTY = new MapExpPar<>();


    MapExpPar() {
    }

    MapExpPar(final Map<String, VIO<? extends O>> bindings) {
        this.bindings = requireNonNull(bindings);
    }


    /**
     * returns a new object future inserting the given future at the given key
     *
     * @param key the given key
     * @param exp the given future
     * @return a new JsObjFuture
     */
    @Override
    public MapExpPar<O> set(final String key,
                            final VIO<? extends O> exp
                           ) {
        Map<String, VIO<? extends O>> map = new HashMap<>(bindings);

        map.put(requireNonNull(key),
                requireNonNull(exp)
               );

        return new MapExpPar<>(map);
    }

    @Override
    public VIO<Map<String, O>> retryEach(final RetryPolicy policy) {
        return retryEach(e -> true,
                         policy
                        );
    }

    @Override
    public VIO<Map<String, O>> retryEach(final Predicate<Throwable> predicate,
                                         final RetryPolicy policy
                                        ) {
        Objects.requireNonNull(predicate);
        Objects.requireNonNull(policy);
        return new MapExpPar<>(bindings.entrySet()
                                       .stream()
                                       .collect(Collectors.toMap(Map.Entry::getKey,
                                                                 e -> e.getValue()
                                                                       .retry(predicate, policy)
                                                                )
                                               )
        );


    }


    @Override
    public Future<Map<String, O>> get() {

        String[] keys = bindings.keySet()
                                .toArray(String[]::new);
        List<Future<?>> xs = new ArrayList<>();
        for (String key : keys) {
            xs.add(bindings.get(key).get());
        }

        return Future.all(xs)
                     .map(r -> {
                         Map<String, O> result = new LinkedHashMap<>();
                         List<O> list = r.result().list();
                         for (int i = 0; i < list.size(); i++) {
                             result.put(keys[i],
                                        list.get(i)
                                       );
                         }

                         return result;
                     });
    }


}
