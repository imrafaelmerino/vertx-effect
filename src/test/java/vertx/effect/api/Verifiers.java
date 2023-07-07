package vertx.effect.api;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.junit5.VertxTestContext;
import org.junit.jupiter.api.Assertions;
import vertx.effect.VIO;

import java.util.function.BiConsumer;
import java.util.function.Predicate;

import static java.util.Objects.requireNonNull;
@SuppressWarnings("ReturnValueIgnored")
public final class Verifiers {

    private Verifiers() {
    }

    public static <T> Handler<AsyncResult<T>> pipeTo(final VertxTestContext context) {
        requireNonNull(context);
        return result -> {
            if (result == null) context.failNow(new NullPointerException("result is null"));
            if (result.succeeded()) {
                context.completeNow();
            } else {
                context.failNow(result.cause());
            }
        };
    }

    public static <T> BiConsumer<VIO<T>, VertxTestContext> verifySuccess() {
        return verifySuccess(it -> true);
    }

    public static <T> BiConsumer<VIO<T>, VertxTestContext> verifySuccess(final Predicate<T> predicate) {
        return verifySuccess(requireNonNull(predicate),
                             ""
                            );
    }

    public static <T> BiConsumer<VIO<T>, VertxTestContext> verifySuccess(final Predicate<T> predicate,
                                                                         final String errorMessage
                                                                        ) {
        requireNonNull(predicate);
        requireNonNull(errorMessage);
        return (val, context) -> {
            if (val == null) context.failNow(new NullPointerException("val is null"));
            val.onComplete(r -> {
                   if (r.failed()) {
                       context.failNow(r.cause());
                   } else {
                       context.verify(() -> Assertions.assertTrue(predicate.test(r.result()),
                                                                  errorMessage
                                                                 ));
                       context.completeNow();
                   }
               })
               .get();
        };
    }

    public static <T> BiConsumer<VIO<T>, VertxTestContext> verifyFailure(final Predicate<Throwable> predicate,
                                                                         final String errorMessage
                                                                        ) {
        requireNonNull(predicate);
        requireNonNull(errorMessage);
        return (val, context) -> {
            if (val == null) context.failNow(new NullPointerException("val is null"));
            val.onComplete(r -> {
                   if (r.succeeded()) {
                       context.failNow(new RuntimeException(String.format("The val was supposed to fail, and it succeeded returning %s",
                                                                          r.result()
                                                                         )
                                       )
                                      );
                   } else {
                       context.verify(() -> Assertions.assertTrue(predicate.test(r.cause()),
                                                                  errorMessage
                                                                 )
                                     );
                       context.completeNow();
                   }
               })
               .get();
        };
    }

    public static <T> BiConsumer<VIO<T>, VertxTestContext> verifyFailure(final Predicate<Throwable> predicate) {
        requireNonNull(predicate);
        return verifyFailure(predicate,
                             ""
                            );
    }

    public static <T> BiConsumer<VIO<T>, VertxTestContext> verifyFailure() {
        return Verifiers.verifyFailure(it -> true,
                                       ""
                                      );
    }
}
