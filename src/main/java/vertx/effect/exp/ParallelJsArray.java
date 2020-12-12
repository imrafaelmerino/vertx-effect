package vertx.effect.exp;

import io.vavr.collection.List;
import io.vertx.core.CompositeFuture;
import io.vertx.core.Future;
import jsonvalues.JsArray;
import jsonvalues.JsValue;
import vertx.effect.Val;

import java.util.function.BiFunction;
import java.util.function.Predicate;
import java.util.function.Supplier;


/**
 Represents a supplier of a completable future which result is a json array. It has the same
 recursive structure as a json array. Each index of the array is a completable future that it's
 executed asynchronously. When all the futures are completed, all the results are combined into
 a json array.
 */

final class ParallelJsArray extends JsArrayExp {

    private static final String ATTEMPTS_LOWER_THAN_ONE_ERROR = "attempts < 1";

    private List<Val<? extends JsValue>> seq = List.empty();

    static final ParallelJsArray EMPTY = new ParallelJsArray();

    ParallelJsArray(List<Val<? extends JsValue>> seq) {
        this.seq = seq;
    }

    ParallelJsArray() {
    }

    @SafeVarargs
    ParallelJsArray(final Val<? extends JsValue> val,
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
    public ParallelJsArray append(final Val<? extends JsValue> future) {

        final ParallelJsArray arrayFuture = new ParallelJsArray();
        arrayFuture.seq = this.seq.append(future);
        return arrayFuture;
    }

    @Override
    public Val<JsValue> race() {
      return Functions.race(seq);
    }

    @Override
    public Val<JsValue> head() {
        return (Val<JsValue>) seq.head();
    }

    @Override
    public JsArrayExp tail() {
        return new ParallelJsArray(seq.tail());
    }


    @Override
    public Val<JsArray> retry(final int attempts) {

        if (attempts < 1)
            return Cons.failure(new IllegalArgumentException(ATTEMPTS_LOWER_THAN_ONE_ERROR));

        return new ParallelJsArray(seq.map(it -> it.retry(attempts)));
    }


    @Override
    public Val<JsArray> retry(final int attempts,
                              final BiFunction<Throwable, Integer, Val<Void>> actionBeforeRetry) {
        if (attempts < 1)
            return Cons.failure(new IllegalArgumentException(ATTEMPTS_LOWER_THAN_ONE_ERROR));
        if (actionBeforeRetry == null)
            return Cons.failure(new NullPointerException("actionBeforeRetry is null"));
        return new ParallelJsArray(seq.map(it -> it.retry(attempts,
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

        return new ParallelJsArray(seq.map(it -> it.retryIf(predicate,
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
        return new ParallelJsArray(seq.map(it -> it.retryIf(predicate,
                                                            attempts,
                                                            actionBeforeRetry
                                                           ))
        );
    }


}
