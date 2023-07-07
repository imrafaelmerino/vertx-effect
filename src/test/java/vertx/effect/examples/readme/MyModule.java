package vertx.effect.examples.readme;

import jsonvalues.JsInt;
import jsonvalues.JsObj;
import jsonvalues.JsStr;
import jsonvalues.spec.JsObjSpec;

import static jsonvalues.JsPath.path;
import static jsonvalues.spec.JsSpecs.*;

import vertx.effect.*;

public class MyModule extends VertxModule {

    public static Lambda<String, String> toLowerCase, toUpperCase;
    public static Lambda<Integer, Integer> inc;
    public static Lambda<JsObj, JsObj> validate, validateAndMap;

    @Override
    protected void deploy() {

        this.deploy("toLowerCase",
                    (String str) -> VIO.succeed(str.toLowerCase())
                   );
        this.deploy("toUpperCase",
                    (String str) -> VIO.succeed(str.toUpperCase())
                   );
        this.deploy("inc",
                    (Integer n) -> VIO.succeed(n + 1)
                   );

        // json-values uses specs to define the structure of a Json: {a:int,b:[str,str]} 
        JsObjSpec spec = JsObjSpec.of(
                "a", integer(),
                "b", tuple(str(), str())
                                     );
        this.deploy("validate", Validators.validateJsObj(spec));

        Lambda<JsObj, JsObj> map = obj ->
                JsObjExp.par("a",
                             inc.apply(obj.getInt("a"))
                                .map(JsInt::of),
                             "b",
                             JsArrayExp.par(toLowerCase.apply(obj.getStr(path("/b/0")))
                                                       .map(JsStr::of),
                                            toUpperCase.apply(obj.getStr(path("/b/1")))
                                                       .map(JsStr::of)
                                           )
                            )
                        .retry(RetryPolicies.limitRetries(2));
        this.deploy("validateAnMap",
                    (JsObj obj) -> validate.apply(obj)
                                           .then(map)
                   );

    }

    @Override
    protected void initialize() {

        toUpperCase = this.ask("toUpperCase");
        toLowerCase = this.ask("toLowerCase");
        inc = this.ask("inc");
        validate = this.ask("validate");
        validateAndMap = this.ask("validateAnMap");

    }
}