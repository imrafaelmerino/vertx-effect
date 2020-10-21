package vertx.effect.exp;

import io.vavr.collection.List;
import io.vertx.core.Future;
import jsonvalues.JsArray;
import jsonvalues.JsValue;
import vertx.effect.Val;
import vertx.effect.core.AbstractVal;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;

import static java.util.Objects.requireNonNull;


/**
 Represents a supplier of a completable future which result is a json array. It has the same
 recursive structure as a json array. Each index of the array is a completable future that it's
 executed asynchronously. When all the futures are completed, all the results are combined into
 a json array.
 */

public final class SequentialJsArray extends AbstractVal<JsArray> {
    private static final String ATTEMPTS_LOWER_THAN_ONE_ERROR = "attempts < 1";

    private List<Val<? extends JsValue>> seq = List.empty();

    private static final SequentialJsArray EMPTY = new SequentialJsArray();

    private SequentialJsArray(List<Val<? extends JsValue>> seq) {
        this.seq = seq;
    }

    private SequentialJsArray() {
    }


    public static SequentialJsArray empty() {
        return EMPTY;
    }

    @SafeVarargs
    private SequentialJsArray(final Val<? extends JsValue> fut,
                              final Val<? extends JsValue>... others
                             ) {
        seq = seq.append(fut);
        for (Val<? extends JsValue> other : others) {
            seq = seq.append(other);
        }
    }


    /**
     returns a JsArrayFuture from the given head and the tail

     @param head the head
     @param tail the tail
     @return a new JsArrayFuture
     */
    @SafeVarargs
    public static SequentialJsArray of(final Val<? extends JsValue> head,
                                       final Val<? extends JsValue>... tail
                                      ) {
        return new SequentialJsArray(requireNonNull(head),
                                     requireNonNull(tail)
        );
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

    public SequentialJsArray append(final Val<? extends JsValue> future) {

        final SequentialJsArray arrayFuture = new SequentialJsArray();
        arrayFuture.seq = this.seq.append(future);
        return arrayFuture;
    }

    @Override
    public <P> Val<P> map(final Function<JsArray, P> fn) {
        if (fn == null)
            return Cons.failure(new NullPointerException("fn is null"));
        return Cons.of(() -> get().map(fn));
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

    public SequentialJsArray appendAll(final SequentialJsArray others) {
        return new SequentialJsArray(seq.appendAll(others.seq));
    }

}
