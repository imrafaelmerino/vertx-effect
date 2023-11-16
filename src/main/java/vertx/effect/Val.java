package vertx.effect;

import io.vertx.core.Future;

import java.util.function.Supplier;


final class Val<O> implements VIO<O> {
    private final Supplier<Future<O>> futureSupplier;

    Val(final Supplier<Future<O>> futureSupplier) {
        this.futureSupplier = futureSupplier;
    }


    @Override
    public Future<O> get() {
        return futureSupplier.get();
    }


}
