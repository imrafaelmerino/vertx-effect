package vertx.effect.exp;

import io.vavr.collection.List;
import io.vertx.core.Future;
import jsonvalues.JsArray;
import jsonvalues.JsValue;
import vertx.effect.Val;

import javax.naming.OperationNotSupportedException;
import java.util.function.BiFunction;
import java.util.function.Predicate;


/**
 Represents a supplier of a completable future which result is a json array. It has the same
 recursive structure as a json array. Each index of the array is a completable future that it's
 executed asynchronously. When all the futures are completed, all the results are combined into
 a json array.
 */

final class SequentialJsArray extends JsArrayExp {
    private static final String ATTEMPTS_LOWER_THAN_ONE_ERROR = "attempts < 1";

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
    public Val<JsArray> retry(final int attempts) {

        if (attempts < 1)
            return Cons.failure(new IllegalArgumentException(ATTEMPTS_LOWER_THAN_ONE_ERROR));

        return new SequentialJsArray(seq.map(it -> it.retry(attempts)));
    }


    @Override
    public Val<JsArray> retry(final int attempts,
                              final BiFunction<Throwable, Integer, Val<Void>> actionBeforeRetry) {
        if (attempts < 1)
            return Cons.failure(new IllegalArgumentException(ATTEMPTS_LOWER_THAN_ONE_ERROR));
        if (actionBeforeRetry == null)
            return Cons.failure(new NullPointerException("actionBeforeRetry is null"));
        return new SequentialJsArray(seq.map(it -> it.retry(attempts,
                                                            actionBeforeRetry
                                                           )));
    }

    @Override
    public Val<JsArray> retryIf(final Predicate<Throwable> predicate,
                                final int attempts) {
        if (attempts < 1)
            return Cons.failure(new IllegalArgumentException(ATTEMPTS_LOWER_THAN_ONE_ERROR));
        if (predicate == null)
            return Cons.failure(new NullPointerException("predicate is null"));

        return new SequentialJsArray(seq.map(it -> it.retryIf(predicate,
                                                              attempts
                                                             )));

    }

    @Override
    public Val<JsArray> retryIf(final Predicate<Throwable> predicate,
                                final int attempts,
                                final BiFunction<Throwable, Integer, Val<Void>> actionBeforeRetry) {
        if (predicate == null)
            return Cons.failure(new NullPointerException("predicate is null"));
        if (attempts < 1)
            return Cons.failure(new IllegalArgumentException(ATTEMPTS_LOWER_THAN_ONE_ERROR));
        if (actionBeforeRetry == null)
            return Cons.failure(new NullPointerException("actionBeforeRetry is null"));
        return new SequentialJsArray(seq.map(it -> it.retryIf(predicate,
                                                              attempts,
                                                              actionBeforeRetry
                                                             ))
        );
    }

    @Override
    public JsArrayExp append(final Val<? extends JsValue> future) {

        final SequentialJsArray arrayFuture = new SequentialJsArray();
        arrayFuture.seq = this.seq.append(future);
        return arrayFuture;
    }

    @Override
    public Val<JsValue> race() {
        return Cons.failure(new OperationNotSupportedException("race doesn't make any sense in a sequential execution"));
    }

    @Override
    public Val<JsValue> head() {
        return (Val<JsValue>) seq.head();
    }

    @Override
    public JsArrayExp tail() {
        return new SequentialJsArray(seq.tail());
    }

}
