package vertx.effect.exp;

import io.vavr.collection.List;
import io.vertx.core.Future;
import vertx.effect.Val;
import vertx.effect.core.AbstractVal;

import javax.naming.OperationNotSupportedException;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;

import static java.util.Objects.requireNonNull;

class SequentialSeq<O> extends SeqVal<O> {

    @SuppressWarnings("rawtypes")
    protected static final SeqVal EMPTY = new SequentialSeq<>(List.empty());

     SequentialSeq(final List<Val<? extends O>> seq) {
       super(seq);
    }


    @Override
    public Val<List<O>> retry(final int attempts) {
        if (attempts < 1)
            return Cons.failure(new IllegalArgumentException(ATTEMPTS_LOWER_THAN_ONE_ERROR));
        return new SequentialSeq<>(seq.map(it -> it.retry(attempts)));
    }


    @Override
    public Val<List<O>> retry(final int attempts,
                              final BiFunction<Throwable, Integer, Val<Void>> actionBeforeRetry) {
        if (attempts < 1)
            return Cons.failure(new IllegalArgumentException(ATTEMPTS_LOWER_THAN_ONE_ERROR));
        if (actionBeforeRetry == null)
            return Cons.failure(new NullPointerException("actionBeforeRetry is null"));
        return new SequentialSeq<>(seq.map(it -> it.retry(attempts,
                                                          actionBeforeRetry
                                                         )));
    }

    @Override
    public Val<List<O>> retryIf(final Predicate<Throwable> predicate,
                                final int attempts) {
        if (attempts < 1)
            return Cons.failure(new IllegalArgumentException(ATTEMPTS_LOWER_THAN_ONE_ERROR));
        if (predicate == null)
            return Cons.failure(new NullPointerException("predicate is null"));


        return new SequentialSeq<>(seq.map(it -> it.retryIf(predicate,
                                                            attempts
                                                           ))
        );
    }


    @Override
    public Val<List<O>> retryIf(final Predicate<Throwable> predicate,
                                final int attempts,
                                final BiFunction<Throwable, Integer, Val<Void>> actionBeforeRetry) {
        if (attempts < 1)
            return Cons.failure(new IllegalArgumentException(ATTEMPTS_LOWER_THAN_ONE_ERROR));
        if (predicate == null)
            return Cons.failure(new NullPointerException("predicate is null"));
        return new SequentialSeq<>(seq.map(it -> it.retryIf(predicate,
                                                            attempts,
                                                            actionBeforeRetry
                                                           )));
    }

    @Override
    @SuppressWarnings({"unchecked", "rawtypes"})
    public Future<List<O>> get() {
        Future<List<O>> acc = Future.succeededFuture(List.empty());

        for (final Val<? extends O> val : seq)
            acc = acc.flatMap(l -> val.get()
                                      .map(l::append));

        return acc;

    }

    @Override
    public SeqVal<O> append(final Val<? extends O> exp) {
        return new SequentialSeq<>(seq.append(requireNonNull(exp)));
    }

    @Override
    public SeqVal<O> prepend(final Val<? extends O> exp) {
        return new SequentialSeq<>(seq.prepend(requireNonNull(exp)));
    }

    @Override
    public Val<O> race() {
        return Cons.failure(new OperationNotSupportedException("race doesn't make any sense in a sequential execution"));

    }

    @Override
    public SeqVal<O> tail() {
        return new SequentialSeq<>(seq.tail());
    }





}
