package vertx.effect.exp;

import io.vertx.core.CompositeFuture;
import io.vertx.core.Future;
import vertx.effect.RetryPolicy;
import vertx.effect.Val;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static java.util.Objects.requireNonNull;

class ParallelSeq<O> extends ListExp<O> {

    @SuppressWarnings("rawtypes")
    protected static final ListExp EMPTY = new ParallelSeq<>(io.vavr.collection.List.empty());

    ParallelSeq(final io.vavr.collection.List<Val<? extends O>> seq) {
        super(seq);
    }

    @Override
    public Val<List<O>> retry(final RetryPolicy policy) {

        return new ParallelSeq<>(seq.map(it -> it.retry(policy)));
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
