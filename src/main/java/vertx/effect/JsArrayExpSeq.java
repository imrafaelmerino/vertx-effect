package vertx.effect;

import io.vertx.core.Future;
import jsonvalues.JsArray;
import jsonvalues.JsValue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;


/**
 * Represents a supplier of a completable future which result is a json array. It has the same recursive structure as a
 * json array. Each index of the array is a completable future that it's executed asynchronously. When all the futures
 * are completed, all the results are combined into a json array.
 */

final class JsArrayExpSeq extends JsArrayExp {


    JsArrayExpSeq(List<VIO<? extends JsValue>> seq) {
        this.seq = seq;
    }

    JsArrayExpSeq() {
    }

    @SafeVarargs
    @SuppressWarnings("varargs")
    JsArrayExpSeq(final VIO<? extends JsValue> val,
                  final VIO<? extends JsValue>... others
                 ) {
        seq.add(val);
        Collections.addAll(seq, others);
    }


    /**
     * it triggers the execution of all the completable futures, combining the results into a JsArray
     *
     * @return a CompletableFuture of a json array
     */
    @Override
    public Future<JsArray> get() {
        Future<JsArray> result = Future.succeededFuture(JsArray.empty());

        for (final VIO<? extends JsValue> future : seq) {
            result = result.flatMap(arr -> future.get()
                                                 .map(arr::append));
        }
        return result;
    }

    @Override
    public VIO<JsArray> retryEach(final RetryPolicy policy) {
        return retryEach(e -> true,
                         policy
                        );
    }

    @Override
    public VIO<JsArray> retryEach(final Predicate<Throwable> predicate,
                                  final RetryPolicy policy
                                 ) {
        Objects.requireNonNull(policy);
        Objects.requireNonNull(predicate);
        return new JsArrayExpSeq(seq.stream()
                                    .map(it -> it.retry(predicate, policy))
                                    .collect(Collectors.toList())
        );

    }

    @Override
    public JsArrayExp append(final VIO<? extends JsValue> future) {
        List<VIO<? extends JsValue>> arr = new ArrayList<>(this.seq);
        arr.add(future);
        return new JsArrayExpSeq(arr);
    }

    @Override
    @SuppressWarnings("unchecked")
    public VIO<JsValue> head() {
        return (VIO<JsValue>) seq.get(0);
    }

    @Override
    public JsArrayExp tail() {
        return new JsArrayExpSeq(new ArrayList<>(seq.subList(1, seq.size())));
    }

}
