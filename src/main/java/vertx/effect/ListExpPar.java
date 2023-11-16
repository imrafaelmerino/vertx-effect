package vertx.effect;


import io.vertx.core.CompositeFuture;
import io.vertx.core.Future;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static java.util.Objects.requireNonNull;

final class ListExpPar<O> extends ListExp<O> {

    ListExpPar(List<VIO<? extends O>> seq) {
        super(seq);
    }

    ListExpPar() {
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
        return new ListExpPar<>(seq.stream()
                                   .map(it -> it.retry(predicate, policy))
                                   .collect(Collectors.toList())
        );
    }


    @Override
    public Future<List<O>> get() {
        return Future.all(seq.stream()
                             .map(Supplier::get)
                             .collect(Collectors.toList()))
                     .map(CompositeFuture::list);

    }

    @Override
    public ListExp<O> append(final VIO<? extends O> exp) {
        var xs = new ListExpPar<>(seq);
        xs.seq.add(requireNonNull(exp));
        return xs;
    }

    @Override
    public ListExp<O> prepend(final VIO<? extends O> exp) {
        var xs = new ListExpPar<>(seq);
        xs.seq.add(0, exp);
        return xs;
    }


    @Override
    public ListExp<O> tail() {

        return new ListExpPar<>(new ArrayList<>(seq.subList(1, seq.size())));
    }


}
