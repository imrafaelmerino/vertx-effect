package vertx.effect.exp;

import io.vavr.collection.List;
import io.vertx.core.Future;
import jsonvalues.JsArray;
import jsonvalues.JsValue;
import vertx.effect.RetryPolicy;
import vertx.effect.Val;

import java.util.function.Predicate;


/**
 Represents a supplier of a completable future which result is a json array. It has the same
 recursive structure as a json array. Each index of the array is a completable future that it's
 executed asynchronously. When all the futures are completed, all the results are combined into
 a json array.
 */

final class SequentialJsArray extends JsArrayExp {

    private List<Val<? extends JsValue>> seq = List.empty();

    static final SequentialJsArray EMPTY = new SequentialJsArray();

    SequentialJsArray(List<Val<? extends JsValue>> seq) {
        this.seq = seq;
    }

    SequentialJsArray() {
    }

    @SafeVarargs
    SequentialJsArray(final Val<? extends JsValue> val,
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
    public Future<JsArray> get() {
        Future<JsArray> result = Future.succeededFuture(JsArray.empty());

        for (final Val<? extends JsValue> future : seq) {
            result = result.flatMap(arr -> future.get()
                                                 .map(arr::append));
        }
        return result;
    }

    @Override
    public Val<JsArray> retryEach(final RetryPolicy policy) {
        return retryEach(e -> true,
                         policy
                        );
    }

    @Override
    public Val<JsArray> retryEach(final Predicate<Throwable> predicate,
                                  final RetryPolicy policy) {
        if (policy == null) return Val.fail(new IllegalArgumentException("Val.retry: policy is null"));
        if (predicate == null) return Val.fail(new IllegalArgumentException("Val.retry: predicate is null"));
        return new SequentialJsArray(seq.map(it -> it.retry(predicate,
                                                            policy
                                                           )));

    }

    @Override
    public JsArrayExp append(final Val<? extends JsValue> future) {

        final SequentialJsArray arrayFuture = new SequentialJsArray();
        arrayFuture.seq = this.seq.append(future);
        return arrayFuture;
    }

    @Override
    public Val<JsValue> race() {
        return Val.fail(new IllegalCallerException("race doesn't make any sense in a sequential execution"));
    }

    @Override
    @SuppressWarnings("unchecked")
    public Val<JsValue> head() {
        return (Val<JsValue>) seq.head();
    }

    @Override
    public JsArrayExp tail() {
        return new SequentialJsArray(seq.tail());
    }

}
