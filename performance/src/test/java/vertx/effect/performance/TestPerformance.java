package vertx.effect.performance;

import io.vertx.core.Vertx;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import jsonvalues.JsObj;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import vertx.effect.RegisterJsValuesCodecs;
import vertx.effect.VertxRef;
import vertx.effect.exp.Pair;

import java.util.Random;
import java.util.function.Supplier;


@ExtendWith(VertxExtension.class)
public class TestPerformance {
    private static final Supplier<JsObj> objSupplier = Functions.generator.apply(new Random());

    @BeforeAll
    public static void prepare(Vertx vertx,
                               VertxTestContext testContext
                              ) {

        VertxRef vertxRef = new VertxRef(vertx);

        vertxRef.registerConsumer(VertxRef.EVENTS_ADDRESS,
                                  System.out::println
                                 );

        Pair.sequential(vertxRef.deployVerticle(new RegisterJsValuesCodecs()),
                        vertxRef.deployVerticle(new MyModule())
               )
            .onSuccess(pair -> testContext.completeNow())
            .get();


    }


    @Test
    public void testCountStringMultiProcesses(final VertxTestContext testContext) {
        MyModule.countStringsLengthMultiProcesses
                .apply(10)
                .onSuccess(it -> testContext.completeNow())
                .get();
    }

    @Test
    @Disabled
    public void testCountStringMultiVerticle(final VertxTestContext testContext) {
        MyModule.countStringsLengthMultiVerticles
                .apply(100)
                .onSuccess(it -> testContext.completeNow())
                .get();
    }

    @Test
    public void testCountStringMultiVerticle(final Vertx vertx,
                                             final VertxTestContext testContext
                                            ) {
        MyModule.countStringsLengthMultiVerticles
                .apply(3)
                .onSuccess(it -> testContext.verify(testContext::completeNow))
                .get();
    }

}
