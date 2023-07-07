package vertx.effect.api.patterns.useraccount;

import jsonvalues.JsObj;
import vertx.effect.VertxModule;
import vertx.effect.AllExp;
import vertx.effect.Lambdac;

public class UserAccountModule extends VertxModule {

    public static Lambdac<Integer, Boolean> isLegalAge;
    public static Lambdac<String, Boolean> isValidId;
    public static Lambdac<String, Boolean> isValidEmail;
    public static Lambdac<JsObj, JsObj> register;
    public static Lambdac<JsObj, Boolean> isValid;

    private static final String IS_VALID_ID = "isValidId";
    private static final String IS_LEGAL_AGE = "isLegalAge";
    private static final String IS_VALID_EMAIL = "isValidEmail";
    private static final String REGISTER = "register";
    private static final String IS_VALID = "isValid";

    @Override
    protected void initialize() {
        isLegalAge = this.trace(IS_LEGAL_AGE);
        isValidId = this.trace(IS_VALID_ID);
        isValidEmail = this.trace(IS_VALID_EMAIL);
        register = this.trace(REGISTER);
        isValid = this.trace(IS_VALID);
    }

    @Override
    protected void deploy() {
        this.deploy(IS_LEGAL_AGE,
                    UserAccountFunctions.isLegalAge
                   );
        this.deploy(IS_VALID_ID,
                    UserAccountFunctions.isValidId
                   );
        this.deploy(IS_VALID_EMAIL,
                    UserAccountFunctions.isValidEmail
                   );
        this.deploy(REGISTER,
                    UserAccountFunctions.register
                   );

        Lambdac<JsObj, Boolean> isValid = (context, obj) ->
                AllExp.par(isLegalAge.apply(context,
                                            obj.getInt("age")
                                           ),
                           isValidId.apply(context,
                                           obj.getStr("id")
                                          ),
                           isValidEmail.apply(context,
                                              obj.getStr("email")
                                             )
                          );

        this.deploy(IS_VALID,
                    isValid
                   );

    }
}
