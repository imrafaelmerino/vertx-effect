package vertx.effect.patterns.oauth;

import jsonvalues.JsObj;
import vertx.effect.exp.Cons;
import vertx.effect.Val;
import vertx.effect.λ;

public class GetTokenReqVerticle implements λ<JsObj, String> {

    int count = 0;

    @Override
    public Val<String> apply(final JsObj input) {
        count += 1;

        return Cons.success(count +"");

    }
}
