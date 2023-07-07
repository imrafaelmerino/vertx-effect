package vertx.effect.api.patterns.failures;

import io.vertx.core.Vertx;
import io.vertx.core.eventbus.ReplyException;
import io.vertx.core.eventbus.ReplyFailure;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import vertx.effect.*;
import vertx.effect.PairExp;
import vertx.values.codecs.RegisterJsValuesCodecs;

import static vertx.effect.Failures.*;

@SuppressWarnings("ReturnValueIgnored")
@ExtendWith(VertxExtension.class)
public class TestFailures {

    @BeforeAll
    public static void prepare(VertxTestContext context,
                               Vertx vertx
                              ) {

        VertxRef ref = new VertxRef(vertx);

        ref.registerConsumer(VertxRef.EVENTS_ADDRESS,
                             System.out::println
                            );


        PairExp.seq(ref.deployVerticle(new RegisterJsValuesCodecs()),
                    ref.deployVerticle(new Module())
                   )
               .onComplete(tuple -> context.completeNow())
               .get();


    }

    @Test
    public void test_verticle_timeout(VertxTestContext context) {

        Module.sum100.apply(100)
                     .apply(10)
                     .retry(it -> VERTICLE_TIMEOUT_PRISM.getOptional.apply(it)
                                                                    .isPresent(),
                            RetryPolicies.limitRetries(2)
                           )
                     .onComplete(it -> context.verify(() -> {
                         Assertions.assertTrue(it.failed());
                         Assertions.assertEquals(ReplyFailure.TIMEOUT,
                                                 ((ReplyException) it.cause()).failureType()
                                                );
                         context.completeNow();
                     }))
                     .get();
    }
}
