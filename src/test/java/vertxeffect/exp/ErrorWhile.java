package vertxeffect.exp;

import io.vertx.core.Future;
import vertxeffect.Val;

import java.util.function.IntFunction;
import java.util.function.IntPredicate;
import java.util.function.Supplier;

public class ErrorWhile<O> implements Supplier<Val<O>> {

    private int counter;
    private final IntFunction<Throwable> getError;
    private final IntPredicate testIfError;
    private final O value;
    private final Val<O> val;


    public ErrorWhile(final int attempts,
                      final IntFunction<Throwable> getError,
                      final O value) {

        this(counter -> counter <= attempts, getError,value);
    }

    public ErrorWhile(final IntPredicate testIfError,
                      final IntFunction<Throwable> getError,
                      final O value) {
        this.getError = getError;
        this.testIfError = testIfError;
        this.value = value;
        this.val = Cons.of(() -> {
                               counter += 1;
                               if (testIfError.test(counter))
                                   return Future.failedFuture(getError.apply(counter));
                               return Future.succeededFuture(value);
                           }
                          );
    }

    @Override
    public Val<O> get() {
        //danger zone, counter is mutable, we have to return a brand new instance
        return new ErrorWhile<>(testIfError,
                                getError,
                                value).val;
    }
}
