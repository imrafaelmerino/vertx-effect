package vertx.effect.api.patterns.useraccount;

import jsonvalues.JsInt;
import jsonvalues.JsObj;
import vertx.effect.Lambda;
import vertx.effect.VIO;

import java.util.Random;

public class UserAccountFunctions {
    private static final Random random = new Random();

    public static Lambda<Integer, Boolean> isLegalAge = age -> VIO.succeed(age > 16);
    public static Lambda<String, Boolean> isValidId = id -> VIO.succeed(!id.isEmpty());

    public static Lambda<String, Boolean> isValidEmail = email -> VIO.succeed(!email.isEmpty());
    public static Lambda<JsObj, JsObj> register = user -> VIO.succeed(user.set("id",
                                                                               JsInt.of(random.nextInt())
                                                                              ));


}
