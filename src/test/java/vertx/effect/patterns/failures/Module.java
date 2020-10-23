package vertx.effect.patterns.failures;

import io.vertx.core.DeploymentOptions;
import io.vertx.core.eventbus.DeliveryOptions;
import vertx.effect.VertxModule;
import vertx.effect.exp.Cons;
import vertx.effect.λ;

import java.util.function.Function;

public class Module extends VertxModule {

    public static Function<Integer, λ<Integer, Integer>> sum100;

    @Override
    protected void initialize() {
        sum100 = timeout -> this.ask("sum100",
                                     new DeliveryOptions().setSendTimeout(timeout)
                                    );

    }

    @Override
    protected void deploy() {

        λ<Integer, Integer> sum100 = n -> {
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
            }
            return Cons.success(n + 100);
        };


        this.deploy("sum100",
                    sum100,
                    new DeploymentOptions().setWorker(true)
                   );
    }
}
