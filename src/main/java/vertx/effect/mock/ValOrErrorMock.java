package vertx.effect.mock;

import io.vertx.core.Future;
import vertx.effect.Val;
import vertx.effect.exp.Cons;

import java.util.function.IntFunction;
import java.util.function.IntPredicate;
import java.util.function.Supplier;

/**
 mock of a val that either returns a val or an error. It keeps a call counter. You can define what val is returned on each call.
 @param <O> the type of val returned
 */
public class ValOrErrorMock<O> implements Supplier<Val<O>> {

    private int counter;
    private final IntFunction<Throwable> getError;
    private final IntPredicate testIfError;
    private final O value;
    private final Val<O> val;


    /**
     Constructor to create a mock that returns the given value after the specified number of attempts. The failures
     returned are specified by the function getError
     @param attempts number of attempts before not returning an error
     @param getError function to specify what failure is returned on each call
     @param value the final successful value returned
     */
    public ValOrErrorMock(final int attempts,
                          final IntFunction<Throwable> getError,
                          final O value) {

        this(counter -> counter <= attempts, getError,value);
    }

    /**
     Constructor to create a mock that returns either a val or an error. It will return an error if
     the given predicate testIfError is tested positive
     @param testIfError function to specify what call returns an error
     @param getError function to specify what failure is returned on each call
     @param value the final successful value returned
     */
    public ValOrErrorMock(final IntPredicate testIfError,
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

    /**
     @return a brand new mock
     */
    @Override
    public Val<O> get() {
        //danger zone, counter is mutable, we have to return a brand new instance
        return new ValOrErrorMock<>(testIfError,
                                    getError,
                                    value).val;
    }
}
