package vertx.effect.patterns;

import io.vertx.core.eventbus.Message;
import vertx.effect.Val;
import vertx.effect.VertxModule;
import vertx.effect.λ;

import java.util.function.Consumer;

public class ExampleModule extends VertxModule {


    public static λ<Integer, Integer> triple;
    public static λ<Integer, Integer> quadruple;
    public static λ<Integer, Integer> addOne;
    public static Consumer<Integer> printNumber;

    private final String TRIPLE_ADDRESS = "triple";
    private final String ADD_ONE_ADDRESS = "addOne";
    private final String PRINT_ADDRESS = "print";

    {
        quadruple = vertxRef.spawn("mulByFour",
                                           i -> Val.succeed(i * 4)
                                  );
    }


    @Override
    protected void initialize() {
        triple = this.ask(TRIPLE_ADDRESS);
        addOne = this.ask(ADD_ONE_ADDRESS);
        printNumber = this.tell(PRINT_ADDRESS);
    }

    @Override
    protected void deploy() {
        final λ<Integer, Integer>        triple      = i -> Val.succeed(i * 3);
        final λ<Integer, Integer>        addOne      = i -> Val.succeed(i + 1);
        final Consumer<Message<Integer>> printNumber = m -> System.out.println(m.body());

        this.deploy(TRIPLE_ADDRESS,
                    triple
                   );

        this.deploy(ADD_ONE_ADDRESS,
                    addOne
                   );

        this.deployConsumer(PRINT_ADDRESS,
                            printNumber
                           );


    }
}
