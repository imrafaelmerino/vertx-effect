package vertx.effect.patterns.useraccount;

import jsonvalues.JsObj;
import vertx.effect.VertxModule;
import vertx.effect.exp.And;
import vertx.effect.λc;

public class UserAccountModule extends VertxModule {

    public static λc<Integer, Boolean> isLegalAge;
    public static λc<String, Boolean> isValidId;
    public static λc<String, Boolean> isValidEmail;
    public static λc<JsObj, JsObj> register;
    public static λc<JsObj, Boolean> isValid;

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

        λc<JsObj, Boolean> isValid = (context, obj) ->
                And.parallel(isLegalAge.apply(context,
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
