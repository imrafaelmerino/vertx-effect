package vertx.effect.exp;

import io.vavr.collection.List;
import io.vertx.core.CompositeFuture;
import io.vertx.core.Future;
import jsonvalues.JsArray;
import jsonvalues.JsValue;
import vertx.effect.RetryPolicy;
import vertx.effect.Val;

import java.util.function.Predicate;
import java.util.function.Supplier;


/**
 Represents a supplier of a completable future which result is a json array. It has the same
 recursive structure as a json array. Each index of the array is a completable future that it's
 executed asynchronously. When all the futures are completed, all the results are combined into
 a json array.
 */

final class ParallelJsArrayExp extends JsArrayExp {

    private List<Val<? extends JsValue>> seq = List.empty();

    static final ParallelJsArrayExp EMPTY = new ParallelJsArrayExp();

    ParallelJsArrayExp(List<Val<? extends JsValue>> seq) {
        this.seq = seq;
    }

    ParallelJsArrayExp() {
    }

    @SafeVarargs
    ParallelJsArrayExp(final Val<? extends JsValue> val,
                       final Val<? extends JsValue>... others
                      ) {
        seq = seq.append(val);
        for (Val<? extends JsValue> other : others) {
            seq = seq.append(other);
        }
    }


    /**
     it triggers the execution of all the completable futures, combining the results into a JsArray

     @return a CompletableFuture of a json array
     */
    @Override
    @SuppressWarnings({"unchecked", "rawtypes"})
    public Future<JsArray> get() {
        java.util.List futures = seq.map(Supplier::get)
                                    .toJavaList();
        return CompositeFuture.all(futures)
                              .map(result -> {
                                       java.util.List<Object> list = result.list();
                                       JsArray                acc  = JsArray.empty();
                                       for (final Object o : list) {
                                           acc = acc.append(((JsValue) o));
                                       }
                                       return acc;
                                   }
                                  );


    }

    @Override
    public ParallelJsArrayExp append(final Val<? extends JsValue> future) {

        final ParallelJsArrayExp arrayFuture = new ParallelJsArrayExp();
        arrayFuture.seq = this.seq.append(future);
        return arrayFuture;
    }

    @Override
    public Val<JsValue> race() {
      return Functions.race(seq);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Val<JsValue> head() {
        return (Val<JsValue>) seq.head();
    }

    @Override
    public JsArrayExp tail() {
        return new ParallelJsArrayExp(seq.tail());
    }


    @Override
    public Val<JsArray> retryEach(final RetryPolicy policy) {
        return retryEach(e -> true,policy);
    }

    @Override
    public Val<JsArray> retryEach(final Predicate<Throwable> predicate,
                              final RetryPolicy policy) {
        if (policy == null) return Val.fail(new IllegalArgumentException("Cons.retry: policy is null"));
        if (predicate== null) return Val.fail(new IllegalArgumentException("Cons.retry: predicate is null"));
        return new ParallelJsArrayExp(seq.map(it -> it.retry(predicate,policy)));

    }


}
