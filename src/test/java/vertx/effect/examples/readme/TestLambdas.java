package vertx.effect.examples.readme;

import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import jsonvalues.JsArray;
import jsonvalues.JsInt;
import jsonvalues.JsObj;
import jsonvalues.JsStr;
import jsonvalues.spec.JsObjSpec;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import vertx.effect.*;

import static jsonvalues.JsPath.path;
import static jsonvalues.spec.JsSpecs.*;
@SuppressWarnings("ReturnValueIgnored")
@ExtendWith(VertxExtension.class)
public class TestLambdas {

    Lambda<String, String> toLowerCase = str -> VIO.succeed(str.toLowerCase());

    Lambda<String, String> toUpperCase = str -> VIO.succeed(str.toUpperCase());

    Lambda<Integer, Integer> inc = n -> VIO.succeed(n + 1);

    JsObjSpec spec = JsObjSpec.of("a", integer(), "b", tuple(str(), str()));

    Lambda<JsObj, JsObj> validate = Validators.validateJsObj(spec);

    Lambda<JsObj, JsObj> map = obj ->
            JsObjExp.par("a", inc.apply(obj.getInt("a")).map(JsInt::of),
                         "b", JsArrayExp.par(toLowerCase.apply(obj.getStr(path("/b/0")))
                                                        .map(JsStr::of),
                                             toUpperCase.apply(obj.getStr(path("/b/1")))
                                                        .map(JsStr::of)
                                            )
                        );

    @Test
    public void valid_json_is_validated_and_mapped(VertxTestContext context) {

        JsObj input = JsObj.of("a", JsInt.of(1), "b", JsArray.of("FOO", "foo"));

        JsObj expected = JsObj.of("a", JsInt.of(2), "b", JsArray.of("foo", "FOO"));

        validate.then(map).apply(input).get()
                .onComplete(result -> {
                    context.verify(() -> Assertions.assertTrue(result.succeeded() && result.result().equals(expected)));
                    context.completeNow();
                });


    }
}
