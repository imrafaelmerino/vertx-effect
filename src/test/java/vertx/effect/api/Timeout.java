package vertx.effect.api;

import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import vertx.effect.*;
import vertx.effect.PairExp;
import vertx.values.codecs.RegisterJsValuesCodecs;

import static vertx.effect.VertxRef.EVENTS_ADDRESS;
@SuppressWarnings("ReturnValueIgnored")
@ExtendWith(VertxExtension.class)
public class Timeout {

    public static class MyModule extends VertxModule {
        public static Lambda<Integer, Integer> inc;

        @Override
        protected void initialize() {

            inc = this.ask("inc",
                           new DeliveryOptions().setSendTimeout(1000)
                          );
        }

        @Override
        protected void deploy() {
            Lambda<Integer, Integer> inc = i -> {
                try {
                    Thread.sleep(10000);
                    return VIO.succeed(i + 1);
                } catch (InterruptedException e) {
                    return VIO.fail(e);
                }

            };

            this.deploy("inc",
                        inc,
                        new DeploymentOptions().setWorker(true)
                       );

        }
    }

    @BeforeAll
    public static void prepare(final Vertx vertx,
                               final VertxTestContext context
                              ) {

        VertxRef vertxRef = new VertxRef(vertx);

        vertxRef.registerConsumer(EVENTS_ADDRESS,
                                  System.out::println
                                 );

        PairExp.seq(vertxRef.deployVerticle(new RegisterJsValuesCodecs()),
                    vertxRef.deployVerticle(new MyModule())
                   )
               .onComplete(it -> context.completeNow())
               .get();
    }

    @Test
    public void test_timeout(final Vertx vertx,
                             final VertxTestContext context
                            ) {

        MyModule.inc.apply(1)
                    .onComplete(it -> {
                        System.out.println(it);
                        context.completeNow();
                    })
                    .get();
    }


}
