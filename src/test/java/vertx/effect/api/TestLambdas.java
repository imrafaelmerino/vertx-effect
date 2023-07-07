package vertx.effect.api;

import io.vertx.core.MultiMap;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import vertx.effect.VIO;
import vertx.effect.Lambda;
import vertx.effect.Lambdac;
@SuppressWarnings("ReturnValueIgnored")
@ExtendWith(VertxExtension.class)
public class TestLambdas {


    @Test
    public void test_and_then(final VertxTestContext context) {

        Lambdac<String, Integer> count = (c, str) -> VIO.succeed(str.length());

        Lambdac<Integer, Boolean> isOdd = (c, n) -> VIO.succeed(n % 2 == 1);

        Lambdac<String, Boolean> lambdac = count.then(isOdd);

        lambdac.apply("bye")
          .onSuccess(it -> context.verify(() -> {
              Assertions.assertTrue(it);
              context.completeNow();
          }))
          .get();

    }

    @Test
    public void test_apply_context(final VertxTestContext context) {

        Lambdac<String, Integer> count = (c, str) -> {
            System.out.println(c);
            return VIO.succeed(str.length());
        };

        Lambda<String, Integer> lambda = count.apply(MultiMap.caseInsensitiveMultiMap()
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
