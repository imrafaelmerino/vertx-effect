package vertx.effect;

import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.junit5.Checkpoint;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import jsonvalues.JsObj;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import vertx.effect.exp.Cons;
import vertx.effect.exp.Pair;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(VertxExtension.class)
public class VertxRefTests {

    static VertxRef vertxRef;

    @BeforeAll
    public static void prepare(Vertx vertx,
                               VertxTestContext testContext
                              ) {
        vertxRef = new VertxRef(vertx);
        vertx.deployVerticle(new RegisterJsValuesCodecs())
             .onComplete(Verifiers.pipeTo(testContext));

    }


    @Test
    public void test_number_of_instances(VertxTestContext context) {
        int i = 10;

        final Checkpoint checkpoint = context.checkpoint(i);

        for (int i1 = 0; i1 < i; i1++) {
            vertxRef.deploy("id"+i,
                            λ.<JsObj>identity(),
                            new DeploymentOptions().setWorker(true)
                           )
                    .onComplete(vr -> {
                        vr.result()
                          .undeploy();
                        checkpoint.flag();

                    })
                    .get();
        }
    }




    @Test
    public void test_sending_messages(VertxTestContext context) {

        λ<Integer, Integer> inc = i -> Cons.success(i + 1);
        Val<VerticleRef<Integer, Integer>> addOneExp = vertxRef.deploy("inc",
                                                                       inc
                                                                      );
        λ<Integer, Integer> sum = i -> Cons.success(i + 3);
        Val<VerticleRef<Integer, Integer>> tripleExp = vertxRef.deploy("incBy3",
                                                                       sum
                                                                      );

        Pair.parallel(addOneExp,
                      tripleExp
                     )
            .flatMap(pair -> {
                λ<Integer, Integer> addOne = pair._1.ask();
                λ<Integer, Integer> triple = pair._2.ask();
                return addOne.apply(1)
                             .flatMap(triple)
                             .onSuccess(result -> {
                                 context.verify(() -> assertEquals(5,
                                                                   result
                                                                  )
                                               );
                                 pair._1.undeploy();
                                 pair._2.undeploy();
                                 context.completeNow();
                             });
            })
            .get();


    }

    @Test
    public void test_verticle_consumer(VertxTestContext context) {
        vertxRef.deploy("id",
                        λ.<JsObj>identity()
                       )
                .onComplete(r -> {
                                r.result()
                                 .ask()
                                 .apply(JsObj.empty())
                                 .onComplete(h -> context.verify(() ->
                                                                 {
                                                                     assertEquals(h.result(),
                                                                                  JsObj.empty()
                                                                                 );
                                                                     context.completeNow();
                                                                 }
                                                                )
                                            )
                                 .get();
                                r.result()
                                 .undeploy();
                            }
                           )
                .get();
    }

    @Test
    public void test_verticle_deployment(VertxTestContext context) {
        vertxRef.deploy("id",
                        λ.<JsObj>identity()
                       )
                .onComplete(r ->
                            {
                                context.verify(() -> assertTrue(r.succeeded()));
                                r.result()
                                 .undeploy();
                                context.completeNow();
                            }
                           )
                .get();

    }


    @Test
    public void test_publish_messages(VertxTestContext context) throws InterruptedException {

        CountDownLatch counter = new CountDownLatch(40);
        Consumer<Integer> consumer = n -> {
            for (int i = 0; i < n; i++) {
                counter.countDown();
            }
        };
        vertxRef.registerConsumer("dec",
                                  consumer
                                 );
        vertxRef.registerConsumer("dec",
                                  consumer
                                 );

        Consumer<Integer> publisher = vertxRef.registerPublisher("dec");

        publisher.accept(10);

        publisher.accept(10);

        counter.await(5,
                      TimeUnit.SECONDS
                     );


        context.completeNow();


    }


}
