package vertx.effect;

import io.vertx.core.MultiMap;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import vertx.effect.exp.Cons;

@ExtendWith(VertxExtension.class)
public class TestLambdas {


    @Test
    public void test_and_then(final VertxTestContext context) {

        λc<String, Integer> count = (c, str) -> Cons.success(str.length());

        λc<Integer, Boolean> isOdd = (c, n) -> Cons.success(n % 2 == 1);

        λc<String, Boolean> λc = count.andThen(isOdd);

        λc.apply("bye")
          .onSuccess(it -> context.verify(() -> {
              Assertions.assertTrue(it);
              context.completeNow();
          }))
          .get();

    }

    @Test
    public void test_apply_context(final VertxTestContext context) {

        λc<String, Integer> count = (c, str) -> {
            System.out.println(c);
            return Cons.success(str.length());
        };

        λ<String, Integer> lambda = count.apply(MultiMap.caseInsensitiveMultiMap()
                                                        .add("status",
                                                             "happy"
                                                            )
                                               );

        lambda.apply("hi")
              .onSuccess(n -> context.verify(() -> {
                  Assertions.assertEquals(2,
                                          n
                                         );
                  context.completeNow();
              }))
              .get();

    }
}
