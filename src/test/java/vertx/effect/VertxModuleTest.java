package vertx.effect;


import io.vertx.core.CompositeFuture;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.Message;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import vertx.effect.exp.Cons;

import java.util.function.Consumer;


@ExtendWith(VertxExtension.class)
public class VertxModuleTest extends VertxModule {

    private static VertxModuleTest module;
    private static int counter = 0;

    private λ<Integer, Integer> mulByTwo;
    private λ<Integer, Integer> mulByThree;
    private λ<Integer, Void> incCounter;
    private Val<Integer> getCounter;
    private λ<Integer, Integer> mulBy1000;
    private λ<Integer, Integer> mulBy10000;


    VertxModuleTest() {
        super(new DeploymentOptions().setWorker(true));
    }


    @BeforeAll
    public static void prepare(final Vertx vertx,
                               final VertxTestContext testContext
                              ) {
        module = new VertxModuleTest();
        VertxRef vertxRef = new VertxRef(vertx);
        vertxRef.registerConsumer(VertxRef.EVENTS_ADDRESS,
                                  System.out::println
                                 );


        CompositeFuture.all(vertx.deployVerticle(module),
                            vertx.deployVerticle(new RegisterJsValuesCodecs())
                           )
                       .onComplete(event -> {
                           if (event.failed()) testContext.failNow(event.cause());
                           else testContext.completeNow();
                       });

    }

    @Test
    public void test_mul_by_two(VertxTestContext context) {

        Verifiers.<Integer>verifySuccess(it -> it.equals(6))
                .accept(module.mulByTwo.apply(3),
                        context
                       );

    }


    @Test
    public void test_mul_by_three(VertxTestContext context) {


        Verifiers.<Integer>verifySuccess(it -> it.equals(15))
                .accept(module.mulByThree.apply(5),
                        context
                       );

    }

    @Test
    public void test_get_counter(VertxTestContext context) {
        Val<Integer> counter = module.incCounter.apply(10)
                                                .flatMap($ -> module.getCounter);
        Verifiers.<Integer>verifySuccess(n -> n == 10)
                .accept(counter,
                        context
                       );
    }

    @Test
    public void test_mul_by_1000(VertxTestContext context) {

        Verifiers.<Integer>verifySuccess(n -> n.equals(10000))
                .accept(module.mulBy1000.apply(10),
                        context
                       );
    }

    @Test
    public void test_mul_by_10000(VertxTestContext context) {

        Verifiers.<Integer>verifySuccess(n -> n.equals(100000))
                .accept(module.mulBy10000.apply(10),
                        context
                       );
    }

    @Override
    protected void initialize() {
        mulByTwo = this.ask("double");
        mulByThree = this.ask("triple");
        incCounter = this.ask("incCounter");
        getCounter = this.<Integer, Integer>ask("getCounter").apply(null);
        mulBy1000 = vertxRef.spawn("mulBy1000",
                                  n -> Cons.success(1000 * n)
                                  );
        mulBy10000 = vertxRef.spawn("mulBy10000",
                                   n -> Cons.success(10000 * n),
                                    new DeploymentOptions().setWorker(true)
                                   );

    }

    @Override
    protected void deploy() {
        λ<Integer, Integer> mulByTwo = i -> Cons.success(i * 2);
        deploy("double",
               mulByTwo
              );

        λ<Integer, Integer> mulByThree = i -> Cons.success(i * 3);
        deploy("triple",
               mulByThree,
               new DeploymentOptions().setInstances(3)
              );

        this.<Integer, Void>deploy("incCounter",
                                           message -> {
                                               counter = counter + message.body();
                                               message.reply(null);
                                           }
                                  );
        Consumer<Message<Void>> consumer = message -> message.reply(counter);
        this.<Void, Integer>deploy("getCounter",
                                   consumer,
                                   new DeploymentOptions().setInstances(3)
                                  );


    }

}
