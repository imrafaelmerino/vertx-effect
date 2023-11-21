package vertx.effect;

import io.vertx.core.Future;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static java.util.Objects.requireNonNull;

final class ListExpSeq<O> extends ListExp<O> {

    @SuppressWarnings("rawtypes")
    ListExpSeq(final List<VIO<? extends O>> seq) {
        super(seq);
    }

    ListExpSeq() {
        super(new ArrayList<>());
    }


    @Override
    public VIO<List<O>> retryEach(final RetryPolicy policy) {
        return retryEach(e -> true,
                         policy
                        );

    }

    @Override
    public VIO<List<O>> retryEach(final Predicate<Throwable> predicate,
                                  final RetryPolicy policy
                                 ) {
        Objects.requireNonNull(policy);
        Objects.requireNonNull(predicate);

        return new ListExpSeq<>(seq.stream()
                                   .map(it -> it.retry(predicate,
                                                       policy
                                                      ))
                                   .collect(Collectors.toList()));
    }

    @Override
    @SuppressWarnings({"unchecked", "rawtypes"})
    public Future<List<O>> get() {
        Future<List<O>> acc = Future.succeededFuture(new ArrayList<>());

        for (final VIO<? extends O> val : seq)
            acc = acc.flatMap(l -> val.get()
                                      .map(it -> {
                                          l.add(it);
                                          return l;
                                      }));

        return acc;

    }

    @Override
    public ListExp<O> append(final VIO<? extends O> exp) {
        var xs = new ListExpSeq<>(seq);
        xs.seq.add(requireNonNull(exp));
        return xs;
    }

    @Override
    public ListExp<O> prepend(final VIO<? extends O> exp) {
        var xs = new ListExpSeq<>(seq);
        xs.seq.add(0, requireNonNull(exp));
        return xs;
    }


    @Override
    public ListExp<O> tail() {

        return new ListExpSeq<>(new ArrayList<>(seq.subList(1, seq.size())));
    }


}
