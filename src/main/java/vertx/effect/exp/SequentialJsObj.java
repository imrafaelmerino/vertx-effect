package vertx.effect.exp;

import io.vavr.collection.Map;
import io.vavr.collection.Set;
import io.vavr.collection.TreeMap;
import io.vertx.core.Future;
import jsonvalues.JsObj;
import jsonvalues.JsValue;
import vertx.effect.RetryPolicy;
import vertx.effect.Val;

import java.util.function.BiFunction;
import java.util.function.Predicate;

import static java.util.Objects.requireNonNull;

/**
 Represents a supplier of a vertx future which result is a json object. It has the same
 recursive structure as a json object. Each key has a future associated that it's
 executed asynchronously. When all the futures are completed, all the results are combined into
 a json object.
 */
final class SequentialJsObj extends JsObjExp {
    Map<String, Val<? extends JsValue>> bindings = TreeMap.empty();

    SequentialJsObj(){}

    SequentialJsObj(final Map<String, Val<? extends JsValue>> bindings) {
        this.bindings = bindings;
    }


    /**
     returns a new object future inserting the given future at the given key

     @param key    the given key
     @param future the given future
     @return a new JsObjFuture
     */
    @Override
    public SequentialJsObj set(final String key,
                               final Val<? extends JsValue> future
                              ) {
        final Map<String, Val<? extends JsValue>> a = bindings.put(requireNonNull(key),
                                                                   requireNonNull(future)
                                                                  );
        return new SequentialJsObj(a);
    }


    /**
     it triggers the execution of all the completable futures, combining the results into a JsObj

     @return a Future of a json object
     */
    @Override
    public Future<JsObj> get() {
        Set<String>   keySet = bindings.keySet();
        Future<JsObj> result = Future.succeededFuture(JsObj.empty());
        for (final String key : keySet) {
            result = result.flatMap(acc -> bindings.get(key)
                                                   .getOrElseThrow(IllegalStateException::new)
                                                   .get()
                                                   .flatMap(val -> Future.succeededFuture(acc.set(key,
                                                                                                  val
                                                                                                 )
                                                                                         )
                                                           )
                                   );
        }
        return result;
    }

    @Override
    public Val<JsObj> retry(final RetryPolicy policy) {
        return new SequentialJsObj(bindings.mapValues(it -> it.retry(policy)));
    }


}
