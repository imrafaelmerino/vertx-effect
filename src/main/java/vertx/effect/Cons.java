package vertx.effect;

import io.vertx.core.Future;
import vertx.effect.core.AbstractVal;

import java.util.function.Supplier;


final class Cons<O> extends AbstractVal<O> {
    private final Supplier<Future<O>> futureSupplier;

    Cons(final Supplier<Future<O>> futureSupplier) {
        this.futureSupplier = futureSupplier;
    }


    @Override
    public Future<O> get() {
        return futureSupplier.get();
    }



}
