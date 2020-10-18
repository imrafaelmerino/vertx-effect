package vertx.effect.patterns.useraccount;

import jsonvalues.JsInt;
import jsonvalues.JsObj;
import vertx.effect.exp.Cons;
import vertx.effect.λ;

import java.util.Random;

public class UserAccountFunctions {
    private static final Random random = new Random();

    public static λ<Integer, Boolean> isLegalAge = age -> Cons.success(age > 16);
    public static λ<String, Boolean> isValidId = id -> Cons.success(!id.isEmpty());

    public static λ<String, Boolean> isValidEmail = email -> Cons.success(!email.isEmpty());
    public static λ<JsObj, JsObj> register = user -> Cons.success(user.set("id",
                                                                           JsInt.of(random.nextInt())
                                                                          ));


}
