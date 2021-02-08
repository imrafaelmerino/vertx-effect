package vertx.effect.performance;

import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import jsonvalues.JsInt;
import jsonvalues.JsObj;
import vertx.effect.*;
import vertx.effect.exp.Cons;
import vertx.effect.exp.Pair;

import static vertx.effect.performance.benchmarks.Inputs.VERTICLE_INSTANCES;


public class MyModule extends VertxModule {

    public static λ<Integer, Integer> countStringsLengthMultiProcesses;
    public static λ<Integer, Integer> countStringsLengthMultiVerticles;
    public static λ<JsObj, JsObj> filter;
    public static λ<JsObj, JsObj> filterProcess;
    public static λ<Integer, JsObj> generator;
    public static λ<Integer, JsObj> generatorProcess;
    public static λ<JsObj, JsObj> map;
    public static λ<JsObj, JsObj> mapProcess;
    public static λ<JsObj, Integer> reduce;
    public static λ<JsObj, Integer> reduceProcess;
    public static λ<JsObj, JsObj> id;
    public static λ<String, JsObj> parser;
    public static λ<JsonObject, JsonObject> jacksonId;
    public static λ<String, JsonObject> jacksonParser;
    static final DeploymentOptions WORKER = new DeploymentOptions().setWorker(true);

    private static final String FILTER_ADDRESS = "filter";
    private static final String MAP_ADDRESS = "map";
    private static final String REDUCE_ADDRESS = "reduce";
    private static final String GENERATOR_ADDRESS = "generator";
    private static final String PARSER_ADDRESS = "parser";
    private static final String JACKSON_PARSER_ADDRESS = "jacksonParser";
    private static final String COUNT_STRING_LENGTH_MULTIVERTICLE_ADDRESS = "countStringsLengthMultiVerticle";
    private static final String ID_ADDRESS = "id";
    private static final String JACKSONID_ADDRESS = "jacksonId";
    private static final String COUNT_STRING_LENGTH_MULTIPROCESSES_ADDRESS = "countStringsLengthMultiProcesses";

    final λ<JsObj, JsObj> mapFn = obj ->
            Val.succeed(obj.mapAllValues(pair -> JsInt.of(pair.value.toJsStr().value.length())));

    final λ<JsObj, JsObj> filterFn = obj ->
            Val.succeed(obj.filterAllValues(pair -> pair.value.isStr()));

    final λ<JsObj, Integer> reduceFn = json ->
            Val.succeed(json.reduceAll(Integer::sum,
                                        pair -> pair.value.toJsInt().value,
                                        pair -> true
                                       )
                             .orElse(0));
    @Override
    protected void initialize() {
        filter = this.ask(FILTER_ADDRESS);

        map = this.ask(MAP_ADDRESS);

        reduce = this.ask(REDUCE_ADDRESS);

        generator = this.ask(GENERATOR_ADDRESS);

        countStringsLengthMultiVerticles = this.ask(COUNT_STRING_LENGTH_MULTIVERTICLE_ADDRESS);

        countStringsLengthMultiProcesses = this.ask(COUNT_STRING_LENGTH_MULTIPROCESSES_ADDRESS);

        id = this.ask(ID_ADDRESS);

        jacksonId = this.ask(JACKSONID_ADDRESS);

        parser = this.ask(PARSER_ADDRESS);

        jacksonParser = this.ask(JACKSON_PARSER_ADDRESS);

        filterProcess = vertxRef.spawn(FILTER_ADDRESS,
                                       filterFn
                                      );

        mapProcess = vertxRef.spawn(MAP_ADDRESS,
                                    mapFn
                                   );

        reduceProcess = vertxRef.spawn(REDUCE_ADDRESS,
                                       reduceFn
                                      );

        generatorProcess = vertxRef.spawn(GENERATOR_ADDRESS,
                                          new JsGenVerticle(),
                                          WORKER
                                         );
    }




    @Override
    protected void deploy() {

        this.deploy(FILTER_ADDRESS,
                    filterFn,
                    new DeploymentOptions().setInstances(VERTICLE_INSTANCES)
                   );


        this.deploy(MAP_ADDRESS,
                    mapFn,
                    new DeploymentOptions().setInstances(VERTICLE_INSTANCES)

                   );


        this.deploy(REDUCE_ADDRESS,
                    reduceFn,
                    new DeploymentOptions().setInstances(VERTICLE_INSTANCES)
                   );

        this.deploy(GENERATOR_ADDRESS,
                    new JsGenVerticle(),
                    WORKER.setInstances(VERTICLE_INSTANCES)
                   );


        this.deploy(COUNT_STRING_LENGTH_MULTIVERTICLE_ADDRESS,
                    new SumJsonStringLengthWithVerticles(),
                    WORKER
                   );

        this.deploy(COUNT_STRING_LENGTH_MULTIPROCESSES_ADDRESS,
                    new SumJsonStringLengthWithProcesses(),
                    WORKER
                   );

        this.deploy(ID_ADDRESS,
                    λ.<JsObj>identity()
                   );
        this.deploy(JACKSONID_ADDRESS,
                    λ.<JsonObject>identity()
                   );

        final λ<String, JsObj> parser = str -> Val.succeed(JsObj.parse(str));

        this.deploy(PARSER_ADDRESS,
                    parser
                   );
        final λ<String, JsonObject> jacksonParser = str -> Val.succeed(new JsonObject(str));

        this.deploy(JACKSON_PARSER_ADDRESS,
                    jacksonParser
                   );


    }

}
