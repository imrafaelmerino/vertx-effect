package vertxeffect.patterns.oauth;

import jsonvalues.JsObj;
import vertxeffect.exp.Cons;
import vertxeffect.Val;
import vertxeffect.λ;

public class GetTokenReqVerticle implements λ<JsObj, String> {

    int count = 0;

    @Override
    public Val<String> apply(final JsObj input) {
        count += 1;

        return Cons.success(count +"");

    }
}
