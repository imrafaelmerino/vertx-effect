package vertx.effect.patterns.useraccount;

import jsonvalues.JsInt;
import jsonvalues.JsObj;
import vertx.effect.Val;
import vertx.effect.λ;

import java.util.Random;

public class UserAccountFunctions {
    private static final Random random = new Random();

    public static λ<Integer, Boolean> isLegalAge = age -> Val.succeed(age > 16);
    public static λ<String, Boolean> isValidId = id -> Val.succeed(!id.isEmpty());

    public static λ<String, Boolean> isValidEmail = email -> Val.succeed(!email.isEmpty());
    public static λ<JsObj, JsObj> register = user -> Val.succeed(user.set("id",
                                                                          JsInt.of(random.nextInt())
                                                                         ));


}
