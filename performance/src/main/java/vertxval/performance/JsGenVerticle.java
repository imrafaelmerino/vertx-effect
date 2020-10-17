package vertxval.performance;

import jsonvalues.JsObj;
import vertxval.exp.Cons;
import vertxval.exp.Val;
import vertxval.exp.λ;
import java.util.Random;
import java.util.function.Supplier;

/**
 Generates a JsObj waiting for the specified time
 */
public class JsGenVerticle implements λ<Integer, JsObj>
{

  private final Random random = new Random();

  private final Supplier<JsObj> supplier = Functions.generator.apply(random);


  @Override
  public Val<JsObj> apply(final Integer time) {
    try {
      Thread.sleep(time);
      return Cons.success(supplier.get());
    } catch (InterruptedException e) {
       return Cons.failure(e);
    }

  }
}
