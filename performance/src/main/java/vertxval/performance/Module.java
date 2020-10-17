package vertxval.performance;

import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import jsonvalues.JsInt;
import jsonvalues.JsObj;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vertxval.VertxModule;
import vertxval.exp.Cons;
import vertxval.exp.λ;


public class Module extends VertxModule {

    public static λ<Integer, Integer> countStringsOneVerticle;
    public static λ<Integer, Integer> countStringsMultiProcesses;
    public static λ<Integer, Integer> countStringsMultiVerticles;
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
    final static DeploymentOptions WORKER = new DeploymentOptions().setWorker(true);

    private static final String FILTER_ADDRESS = "filter";
    private static final String MAP_ADDRESS = "map";
    private static final String REDUCE_ADDRESS = "reduce";
    private static final String GENERATOR_ADDRESS = "generator";
    private static final String PARSER_ADDRESS = "parser";
    private static final String JACKSON_PARSER_ADDRESS = "jacksonParser";
    private static final String GET_LENGTH_ADDRESS = "getLengthString";
    private static final String GET_LENGTH_MULTIVERTICLE_ADDRESS = "getLengthStringMultiVerticle";
    private static final String ID_ADDRESS = "id";
    private static final String JACKSONID_ADDRESS = "jacksonId";
    private static final String GET_LENGTH_MULTIPROCESSES_ADDRESS = "getLengthStringMultiProcesses";

    @Override
    protected void initialize() {
        filter = this.ask(FILTER_ADDRESS);

        map = this.ask(MAP_ADDRESS);

        reduce = this.ask(REDUCE_ADDRESS);

        generator = this.ask(GENERATOR_ADDRESS);

        countStringsOneVerticle = this.ask(GET_LENGTH_ADDRESS);

        countStringsMultiVerticles = this.ask(GET_LENGTH_MULTIVERTICLE_ADDRESS);

        countStringsMultiProcesses = this.ask(GET_LENGTH_MULTIPROCESSES_ADDRESS);

        id = this.ask(ID_ADDRESS);

        jacksonId = this.ask(JACKSONID_ADDRESS);

        parser = this.ask(PARSER_ADDRESS);

        jacksonParser = this.ask(JACKSON_PARSER_ADDRESS);

        filterProcess = vertxRef.spawn(FILTER_ADDRESS,
                                       filter
                                      );

        mapProcess = vertxRef.spawn(MAP_ADDRESS,
                                    map
                                   );

        reduceProcess = vertxRef.spawn(REDUCE_ADDRESS,
                                       reduce
                                      );

        generatorProcess = vertxRef.spawn(GENERATOR_ADDRESS,
                                          new JsGenVerticle(),
                                          WORKER
                                         );


    }


    @Override
    protected void deploy() {
        final λ<JsObj, JsObj> filter = obj ->
                Cons.success(obj.filterAllValues(pair -> pair.value.isStr()));
        this.deploy(FILTER_ADDRESS,
                    filter
                   );

        final λ<JsObj, JsObj> map = obj ->
                Cons.success(obj.mapAllValues(pair -> JsInt.of(pair.value.toJsStr().value.length())));
        this.deploy(MAP_ADDRESS,
                    map
                   );

        final λ<JsObj, Integer> reduce = json ->
                Cons.success(json.reduceAll(Integer::sum,
                                            pair -> pair.value.toJsInt().value,
                                            pair -> true
                                           )
                                 .orElse(0));
        this.deploy(REDUCE_ADDRESS,
                    reduce
                   );

        this.deploy(GENERATOR_ADDRESS,
                    new JsGenVerticle(),
                    WORKER.setInstances(8)
                   );

        this.deploy(GET_LENGTH_ADDRESS,
                    new CountStringOneVerticle(),
                    WORKER
                   );

        this.deploy(GET_LENGTH_MULTIVERTICLE_ADDRESS,
                    new CountStringMultiVerticle(),
                    WORKER
                   );

        this.deploy(GET_LENGTH_MULTIPROCESSES_ADDRESS,
                    new CountStringProcesses(),
                    WORKER
                   );

        this.deploy(ID_ADDRESS,
                    λ.<JsObj>identity()
                   );
        this.deploy(JACKSONID_ADDRESS,
                    λ.<JsonObject>identity()
                   );

        final λ<String, JsObj> parser = str -> Cons.success(JsObj.parse(str));

        this.deploy(PARSER_ADDRESS,
                    parser
                   );
        final λ<String, JsonObject> jacksonParser = str -> Cons.success(new JsonObject(str));

        this.deploy(JACKSON_PARSER_ADDRESS,
                    jacksonParser
                   );


    }

    public static void main(String[] args) throws InterruptedException {
        Logger LOGGER = LoggerFactory.getLogger(Module.class);

        final Vertx vertx = Vertx.vertx();


        vertx.deployVerticle(new Module(),
                             h -> Module.countStringsMultiVerticles.apply(10)
                            );

        Thread.sleep(10000);

        LOGGER.info("The end.");


    }


}
