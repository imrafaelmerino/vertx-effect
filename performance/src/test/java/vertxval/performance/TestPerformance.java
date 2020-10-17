package vertxval.performance;

import io.vertx.core.Vertx;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import jsonvalues.JsObj;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import vertxval.RegisterJsValuesCodecs;
import vertxval.VertxRef;
import vertxval.exp.Pair;

import java.util.Random;
import java.util.function.Supplier;

import static vertxval.performance.Functions.await5segForEnding;
import static vertxval.performance.Module.*;

@ExtendWith(VertxExtension.class)
public class TestPerformance {
    private static final Supplier<JsObj> objSupplier = Functions.generator.apply(new Random());

    private static final Supplier<String> strGen = () -> objSupplier.get()
                                                                    .toString();

    @BeforeAll
    public static void prepare(Vertx vertx,
                               VertxTestContext testContext
                              ) {

        VertxRef deployer = new VertxRef(vertx);

        Pair.of(deployer.deploy(new RegisterJsValuesCodecs()),
                deployer.deploy(new Module())
               )
            .onSuccess(pair -> testContext.completeNow())
            .get();


    }


    @Test
    public void testCountString(final Vertx vertx,
                                final VertxTestContext testContext
                               ) {
        Module.countStringsOneVerticle
                .apply(3)
                .onSuccess(it -> testContext.completeNow())
                .get();
    }

    @Test
    public void testCountStringMultiProcesses(final Vertx vertx,
                                              final VertxTestContext testContext
                                             ) {
        Module.countStringsMultiProcesses
                .apply(3)
                .onSuccess(it -> testContext.completeNow())
                .get();
    }

    @Test
    public void testCountStringMultiVerticle(final Vertx vertx,
                                             final VertxTestContext testContext
                                            ) {
        Module.countStringsMultiVerticles
                .apply(3)
                .onSuccess(it -> testContext.verify(testContext::completeNow))
                .get();
    }

    @Test
    public void testA() {

        await5segForEnding(parser.apply(strGen.get())
                                 .flatMap(id)
                                 .flatMap(id)
                                 .flatMap(id)
                                 .flatMap(id)
                                 .flatMap(id)
                                 .flatMap(id)
                                 .flatMap(id)
                                 .flatMap(id)
                                 .flatMap(id)
                                 .flatMap(id)
                          );
    }

    @Test
    public void testB() {

        await5segForEnding(jacksonParser.apply(strGen.get())
                                        .flatMap(jacksonId)
                                        .flatMap(jacksonId)
                                        .flatMap(jacksonId)
                                        .flatMap(jacksonId)
                                        .flatMap(jacksonId)
                                        .flatMap(jacksonId)
                                        .flatMap(jacksonId)
                                        .flatMap(jacksonId)
                                        .flatMap(jacksonId)
                                        .flatMap(jacksonId)
                          );
    }
}
