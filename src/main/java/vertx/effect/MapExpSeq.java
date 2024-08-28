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
final class MapExpSeq<O> extends MapExp<O> {

    @SuppressWarnings({"rawtypes"})
    public static final MapExpSeq EMPTY = new MapExpSeq<>();


    MapExpSeq() {
    }

    MapExpSeq(final Map<String, VIO<? extends O>> bindings) {
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
    public MapExp<O> set(final String key,
                         final VIO<? extends O> exp
                        ) {
        Map<String, VIO<? extends O>> map = new HashMap<>(bindings);

        map.put(requireNonNull(key),
                requireNonNull(exp)
               );
        return new MapExpSeq<>(map);
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
        Objects.requireNonNull(policy);
        Objects.requireNonNull(predicate);
        return new MapExpSeq<>(bindings.entrySet()
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
        List<VIO<? extends O>> xs = new ArrayList<>();
        for (String key : keys) {
            xs.add(bindings.get(key));
        }

        Future<Map<String, O>> acc = Future.succeededFuture(new LinkedHashMap<>());

        for (int i = 0; i < keys.length; i++) {
            String k = keys[i];
            VIO<? extends O> val = xs.get(i);
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
