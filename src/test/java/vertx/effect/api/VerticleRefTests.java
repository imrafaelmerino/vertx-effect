package vertx.effect.api;

import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import vertx.effect.VIO;
import vertx.effect.Lambda;
import vertx.effect.VertxRef;
import vertx.values.codecs.RegisterJsValuesCodecs;

import java.util.Set;
@SuppressWarnings("ReturnValueIgnored")
@ExtendWith(VertxExtension.class)
public class VerticleRefTests {

    public static VertxRef vertxRef;

    @BeforeAll
    public static void prepare(final Vertx vertx,
                               final VertxTestContext testContext
                              ) {

        vertxRef = new VertxRef(vertx);
        vertxRef.registerConsumer(VertxRef.EVENTS_ADDRESS,
                                  System.out::println
                                 );
        vertxRef.deployVerticle(new RegisterJsValuesCodecs())
                .onComplete(it -> testContext.completeNow())
                .get();

    }

    @Test
    public void _1(final VertxTestContext context) {
        Lambda<Integer, Integer> doubleFn = i -> VIO.succeed(i * 2);


        //nothing is executing, a val is a lazy data structure

        vertxRef.deploy("double",
                        doubleFn,
                        new DeploymentOptions().setInstances(4)
                       )
                .map(verRef -> {
                    // a VerticleRef is a wrapper around a Verticle

                    // an address is generated if it was not provided
                    System.out.println(verRef.address);
                    // a set of ids, once per instance
                    Set<String> ids = verRef.ids;
                    System.out.println(ids);
                    // ask method returns  a lambda, which is an alias for Function<Integer,Val<Integer>>
                    // a Val<O> is an alias for Supplier<Future<O>>


                    return verRef.ask();


                })
                .then(it -> it.apply(1))
                .onSuccess(result -> {
                    context.verify(() -> Assertions.assertEquals(2,
                                                                 result
                                                                ));
                    context.completeNow();
                })
                .get();

    }


}
