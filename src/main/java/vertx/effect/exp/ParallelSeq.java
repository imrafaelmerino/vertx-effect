package vertx.effect.exp;

import io.vavr.collection.List;
import io.vertx.core.CompositeFuture;
import io.vertx.core.Future;
import vertx.effect.Val;

import java.util.function.BiFunction;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static java.util.Objects.requireNonNull;

class ParallelSeq<O> extends ListExp<O> {

    @SuppressWarnings("rawtypes")
    protected static final ListExp EMPTY = new ParallelSeq<>(List.empty());

    ParallelSeq(final List<Val<? extends O>> seq) {
        super(seq);
    }

    @Override
    public Val<List<O>> retry(final int attempts) {
        if (attempts < 1)
            return Cons.failure(new IllegalArgumentException(ATTEMPTS_LOWER_THAN_ONE_ERROR));
        return new ParallelSeq<>(seq.map(it -> it.retry(attempts)));
    }


    @Override
    public Val<List<O>> retry(final int attempts,
                              final BiFunction<Throwable, Integer, Val<Void>> actionBeforeRetry) {
        if (attempts < 1)
            return Cons.failure(new IllegalArgumentException(ATTEMPTS_LOWER_THAN_ONE_ERROR));
        if (actionBeforeRetry == null)
            return Cons.failure(new NullPointerException("actionBeforeRetry is null"));
        return new ParallelSeq<>(seq.map(it -> it.retry(attempts,
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


        return new ParallelSeq<>(seq.map(it -> it.retryIf(predicate,
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
        return new ParallelSeq<>(seq.map(it -> it.retryIf(predicate,
                                                          attempts,
                                                          actionBeforeRetry
                                                         )));
    }

    @Override
    @SuppressWarnings({"unchecked", "rawtypes"})
    public Future<List<O>> get() {
        java.util.List futures = seq.map(Supplier::get)
                                    .toJavaList();
        return CompositeFuture.all(futures)
                              .map(c -> {
                                  List<O>                result    = List.empty();
                                  java.util.List<Object> completed = c.list();
                                  for (Object o : completed) {
                                      O element = (O) o;
                                      result = result.append(element);
                                  }
                                  return result;
                              });

    }

    @Override
    public ListExp<O> append(final Val<? extends O> exp) {
        return new ParallelSeq<>(seq.append(requireNonNull(exp)));
    }

    @Override
    public ListExp<O> prepend(final Val<? extends O> exp) {
        return new ParallelSeq<>(seq.prepend(requireNonNull(exp)));
    }

    @Override
    public Val<O> race() {
        return Functions.race(seq);
    }

    @Override
    public Val<O> raceFirst() {
        return Functions.raceFirst(seq);
    }

    @Override
    public ListExp<O> tail() {
        return new ParallelSeq<>(seq.tail());
    }


}
