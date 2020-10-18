package vertx.effect.exp;

import io.vavr.collection.List;
import io.vertx.core.CompositeFuture;
import io.vertx.core.Future;
import vertx.effect.Val;
import vertx.effect.core.AbstractVal;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static java.util.Objects.requireNonNull;

public class SeqVal<O> extends AbstractVal<List<O>> {

    @SuppressWarnings("rawtypes")
    private static final SeqVal EMPTY = new SeqVal<>(List.empty());

    private final List<Val<? extends O>> seq;
    private static final String ATTEMPTS_LOWER_THAN_ONE_ERROR = "attempts < 1";

    @SuppressWarnings("unchecked")
    public static <O> SeqVal<O> empty() {
        return EMPTY;
    }

    private SeqVal(final List<Val<? extends O>> seq) {
        this.seq = requireNonNull(seq);
    }

    @Override
    public <P> Val<P> map(final Function<List<O>, P> fn) {
        if(fn==null)
            return Cons.failure(new NullPointerException("fn is null"));
        return Cons.of(() -> get().map(fn));
    }

    @Override
    public Val<List<O>> retry(final int attempts) {
        if (attempts < 1)
            return Cons.failure(new IllegalArgumentException(ATTEMPTS_LOWER_THAN_ONE_ERROR));
        return new SeqVal<>(seq.map(it -> it.retry(attempts)));
    }



    @Override
    public Val<List<O>> retry(final int attempts,
                              final BiFunction<Throwable, Integer, Val<Void>> actionBeforeRetry) {
        if (attempts < 1)
            return Cons.failure(new IllegalArgumentException(ATTEMPTS_LOWER_THAN_ONE_ERROR));
        if (actionBeforeRetry == null)
            return Cons.failure(new NullPointerException("actionBeforeRetry is null"));
        return new SeqVal<>(seq.map(it -> it.retry(attempts,
                                                   actionBeforeRetry
                                                  )));
    }

    @Override
    public Val<List<O>> retryIf(final Predicate<Throwable> predicate,
                             final int attempts) {
        if (attempts < 1)
            return Cons.failure(new IllegalArgumentException(ATTEMPTS_LOWER_THAN_ONE_ERROR));
        if(predicate==null)
            return Cons.failure(new NullPointerException("predicate is null"));


        return new SeqVal<>(seq.map(it -> it.retryIf(predicate,
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
        if(predicate==null)
            return Cons.failure(new NullPointerException("predicate is null"));
        return new SeqVal<>(seq.map(it -> it.retryIf(predicate,
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

    public SeqVal<O> append(final Val<? extends O> exp) {
        return new SeqVal<>(seq.append(requireNonNull(exp)));
    }

    public SeqVal<O> prepend(final Val<? extends O> exp) {
        return new SeqVal<>(seq.prepend(requireNonNull(exp)));
    }

    @SuppressWarnings("unchecked")
    public Val<O> head() {
        return (Val<O>) seq.head();
    }

    public SeqVal<O> tail() {
        return new SeqVal<>(seq.tail());
    }

    public int size() {
        return seq.size();
    }

    public boolean isEmpty() {
        return seq.isEmpty();
    }
}
