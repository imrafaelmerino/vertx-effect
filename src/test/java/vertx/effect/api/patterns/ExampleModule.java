package vertx.effect.api.patterns;

import io.vertx.core.eventbus.Message;
import jsonvalues.JsInt;
import jsonvalues.JsObj;
import vertx.effect.Lambda;
import vertx.effect.VIO;
import vertx.effect.VertxModule;

import java.util.function.Consumer;

public class ExampleModule extends VertxModule {


    public static Lambda<Integer, Integer> triple;
    public static Lambda<Integer, Integer> quadruple;
    public static Lambda<Integer, Integer> addOne;
    public static Consumer<Integer> printNumber;

    private final String TRIPLE_ADDRESS = "triple";
    private final String ADD_ONE_ADDRESS = "addOne";
    private final String PRINT_ADDRESS = "print";

    {
        quadruple = vertxRef.spawn("mulByFour",
                                   i -> VIO.succeed(i * 4)
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
        final Lambda<Integer, Integer> triple = i -> VIO.succeed(i * 3);
        final Lambda<Integer, Integer> addOne = i -> VIO.succeed(i + 1);
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

    public static void main(String[] args) {
        JsObj.of("a", JsInt.of(1)).stream().forEach(p -> System.out.println(p.value()));
    }
}
