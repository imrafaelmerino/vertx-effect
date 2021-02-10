package vertx.effect.exp;

import io.vertx.core.CompositeFuture;
import io.vertx.core.Future;
import vertx.effect.RetryPolicy;
import vertx.effect.Val;

import java.util.List;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static java.util.Objects.requireNonNull;

class ParallelSeq<O> extends ListExp<O>  {

    @SuppressWarnings("rawtypes")
    protected static final ListExp EMPTY = new ParallelSeq<>(io.vavr.collection.List.empty());

    ParallelSeq(final io.vavr.collection.List<Val<? extends O>> seq) {
        super(seq);
    }

    @Override
    public Val<List<O>> retryEach(final RetryPolicy policy) {
        return retryEach(e -> true,
                         policy);

    }

    @Override
    public Val<List<O>> retryEach(final Predicate<Throwable> predicate,
                                  final RetryPolicy policy) {
        if (policy == null) return Val.fail(new IllegalArgumentException("Seq.retryEach: policy is null"));
        if (predicate == null) return Val.fail(new IllegalArgumentException("Seq.retryEach: predicate is null"));
        return new ParallelSeq<>(seq.map(it -> it.retry(predicate,
                                                        policy)));
    }


    @Override
    @SuppressWarnings({"unchecked", "rawtypes"})
    public Future<List<O>> get() {
        java.util.List futures = seq.map(Supplier::get)
                                    .toJavaList();
        return CompositeFuture.all(futures)
                              .map(CompositeFuture::list);

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
    public ListExp<O> tail() {
        return new ParallelSeq<>(seq.tail());
    }


}
