package vertx.effect.mock;

import io.vertx.core.Future;
import vertx.effect.Val;
import vertx.effect.exp.Cons;

import java.util.Objects;
import java.util.function.IntFunction;
import java.util.function.Supplier;

public class ValMock<O> implements Supplier<Val<O>> {

    private int counter;
    private final IntFunction<O> getValue;
    private final Val<O> val;


    public ValMock(final IntFunction<O> getValue) {

        this.getValue = Objects.requireNonNull(getValue);
        this.val = Cons.of(() -> {
                               counter += 1;
                               return Future.succeededFuture(getValue.apply(counter));
                           }
                          );
    }


    @Override
    public Val<O> get() {
        //danger zone, counter is mutable, we have to return a brand new instance
        return new ValMock<>(getValue).val;
    }
}
