package vertx.effect.examples.readme;


import io.vertx.core.MultiMap;
import jsonvalues.JsInt;
import jsonvalues.JsObj;
import jsonvalues.JsStr;
import vertx.effect.*;

import java.util.function.Function;

@SuppressWarnings("ReturnValueIgnored")
public class UserAccountModule extends VertxModule {

    public static Lambdac<Integer, Boolean> isLegalAge;
    public static Lambdac<String, Boolean> isValidId;
    public static Lambdac<String, Boolean> isValidEmail;
    public static Lambdac<JsObj, Boolean> isValid;

    private static final String IS_VALID_ID = "isValidId";
    private static final String IS_LEGAL_AGE = "isLegalAge";
    private static final String IS_VALID_EMAIL = "isValidEmail";
    private static final String IS_VALID = "isValid";

    @Override
    protected void initialize() {
        isLegalAge = this.trace(IS_LEGAL_AGE);

        isValidId = this.trace(IS_VALID_ID);

        isValidEmail = this.trace(IS_VALID_EMAIL);

        isValid = this.trace(IS_VALID);
    }

    @Override
    protected void deploy() {

        this.deploy(IS_LEGAL_AGE, (Integer age) -> VIO.succeed(age > 16));

        this.deploy(IS_VALID_ID, (String id) -> VIO.succeed(!id.isEmpty()));

        this.deploy(IS_VALID_EMAIL, (String email) -> VIO.succeed(!email.isEmpty()));

        Lambdac<JsObj, Boolean> isValid = (context, obj) ->
                AllExp.par(isLegalAge.apply(context, obj.getInt("age")),
                           isValidId.apply(context, obj.getStr("id")),
                           isValidEmail.apply(context, obj.getStr("email"))
                          );
        this.deploy(IS_VALID, isValid);
    }

    public static void main(String[] args) {
        Function<JsObj, MultiMap> context = user -> MultiMap.caseInsensitiveMultiMap()
                                                            .add("email", user.getStr("email"));

        JsObj user = JsObj.of("email", JsStr.of("imrafaelmerino@gmail.com"),
                              "age", JsInt.of(17),
                              "id", JsStr.of("03786761")
                             );
        JsObj user1 = JsObj.of("email", JsStr.of("example@gmail.com"),
                               "age", JsInt.of(10),
                               "id", JsStr.of("03486761")
                              );

        UserAccountModule.isValid.apply(context.apply(user), user).get();
        UserAccountModule.isValid.apply(context.apply(user1), user1).get();
    }
}