package vertx.effect.exp;

import io.vertx.core.Future;
import vertx.effect.RetryPolicy;
import vertx.effect.Val;

import javax.naming.OperationNotSupportedException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import static java.util.Objects.requireNonNull;

class SequentialSeq<O> extends ListExp<O>  {

    @SuppressWarnings("rawtypes")
    protected static final ListExp EMPTY = new SequentialSeq<>(io.vavr.collection.List.empty());

    SequentialSeq(final io.vavr.collection.List<Val<? extends O>> seq) {
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
        if (policy == null) return Val.fail(new IllegalArgumentException("Cons.retry: policy is null"));
        if (predicate == null) return Val.fail(new IllegalArgumentException("Cons.retry: predicate is null"));

        return new SequentialSeq<>(seq.map(it -> it.retry(predicate,
                                                          policy)));
    }

    @Override
    @SuppressWarnings({"unchecked", "rawtypes"})
    public Future<List<O>> get() {
        Future<List<O>> acc = Future.succeededFuture(new ArrayList<>());


        for (final Val<? extends O> val : seq)
            acc = acc.flatMap(l -> val.get()
                                      .map(it -> {
                                          l.add(it);
                                          return l;
                                      }));

        return acc;

    }

    @Override
    public ListExp<O> append(final Val<? extends O> exp) {
        return new SequentialSeq<>(seq.append(requireNonNull(exp)));
    }

    @Override
    public ListExp<O> prepend(final Val<? extends O> exp) {
        return new SequentialSeq<>(seq.prepend(requireNonNull(exp)));
    }

    @Override
    public Val<O> race() {
        return Val.fail(new OperationNotSupportedException("race doesn't make any sense in a sequential execution"));
    }


    @Override
    public ListExp<O> tail() {
        return new SequentialSeq<>(seq.tail());
    }


}
