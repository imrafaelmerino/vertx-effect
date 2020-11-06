package vertx.effect.performance;

import jsonvalues.JsObj;
import vertx.effect.Val;
import vertx.effect.exp.Cons;
import vertx.effect.λ;

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
  public Val<JsObj> apply(final Integer delay) {
    if(delay==0)return Cons.success(supplier.get());
    try {
      Thread.sleep(delay);
      return Cons.success(supplier.get());
    } catch (InterruptedException e) {
       return Cons.failure(e);
    }

  }
}
