package vertx.effect.performance;

import jsonvalues.JsObj;
import vertx.effect.Val;
import vertx.effect.λ;

import java.util.Random;
import java.util.function.Supplier;

/**
 Generates a JsObj waiting for the specified time
 */
public class JsGenVerticle implements λ<Integer, JsObj>
{

  private static final Random random = new Random();

  private static final Supplier<JsObj> supplier = Functions.generator.apply(random);


  @Override
  public Val<JsObj> apply(final Integer delay) {
    if(delay==0)return Val.succeed(supplier.get());
    try {
      Thread.sleep(delay);
      return Val.succeed(supplier.get());
    } catch (InterruptedException e) {
       return Val.fail(e);
    }

  }
}
