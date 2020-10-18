package vertx.effect.patterns.oauth;

import jsonvalues.JsObj;
import vertx.effect.Failures;
import vertx.effect.exp.Cons;
import vertx.effect.Val;
import vertx.effect.λ;

import static java.util.Objects.requireNonNull;

public class GetTokenVerticle implements λ<JsObj, String> {

    private final Val<String> BAD_MESSAGE = Cons.failure(Failures.GET_BAD_MESSAGE_EXCEPTION.apply("GetTokenMessage or RenewTokenMessage expected"));

    private String token;

    private final λ<JsObj, String> getTokenReq;

    private final λ<JsObj, JsObj> validateGetMessage;

    private final λ<JsObj, JsObj> validateRenewMessage;

    public GetTokenVerticle(final λ<JsObj, String> getTokenReq,
                            final λ<JsObj, JsObj> validateGetMessage,
                            final λ<JsObj, JsObj> validateRenewMessage) {
        this.getTokenReq = requireNonNull(getTokenReq);
        this.validateGetMessage = requireNonNull(validateGetMessage);
        this.validateRenewMessage = requireNonNull(validateRenewMessage);
    }

    @Override
    public Val<String> apply(final JsObj input) {
        return validateGetMessage.apply(input)
                                 .flatMap(getMessage -> {
                                              if (token == null)
                                                  return getTokenReq.apply(getMessage)
                                                                    .onSuccess(newToken -> token = newToken);
                                              return Cons.success(token);
                                          },
                                          $ -> validateRenewMessage.apply(input)
                                                                   .flatMap(renewMessage ->
                                                                                    getTokenReq.apply(renewMessage)
                                                                                               .onSuccess(newToken -> token = newToken),
                                                                            renewMessageError -> BAD_MESSAGE
                                                                           )
                                         );


    }
}
