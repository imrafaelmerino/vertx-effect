package vertx.effect.httpclient;


import io.vertx.core.CompositeFuture;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpClientOptions;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import jsonvalues.JsObj;
import jsonvalues.JsStr;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import vertx.effect.RegisterJsValuesCodecs;
import vertx.effect.VertxRef;
import vertx.effect.Verifiers;


@ExtendWith(VertxExtension.class)
public class HttpClientMethodsTests {

    private static final int PORT = 1234;
    static HttpClientModule httpClient;

    @BeforeAll
    public static void prepare(final Vertx vertx,
                               final VertxTestContext context
                              ) {
        VertxRef vertxRef = new VertxRef(vertx);
        vertxRef.registerConsumer(VertxRef.EVENTS_ADDRESS, System.out::println);
        httpClient = new HttpExampleModule(new HttpClientOptions());

        CompositeFuture.all(vertx.deployVerticle(new RegisterJsValuesCodecs()),
                            new MyHttpServer(vertx,
                                             PORT,
                                             counter -> req -> body -> JsObj.of("req_method",
                                                                                JsStr.of(req.method()
                                                                                            .name()),
                                                                                "req_body",
                                                                                JsStr.of(body),
                                                                                "req_uri",
                                                                                JsStr.of(req.uri())
                                                                               ),
                                             counter -> req -> body -> 200
                            ).start(),
                            vertx.deployVerticle(httpClient)
                           )
                       .onComplete(Verifiers.pipeTo(context));


    }


    @Test
    public void testGet(VertxTestContext context) {
        Verifiers.<JsObj>verifySuccess(resp -> {
            Integer status  = HttpResp.STATUS_CODE_LENS.get.apply(resp);
            String bodyResp  = HttpResp.STR_BODY_LENS.get.apply(resp);
            JsObj  bodyJsObj = JsObj.parse(bodyResp);
            return bodyJsObj.get("req_method")
                            .equals(JsStr.of("GET"))
                    && bodyJsObj.get("req_uri")
                                .equals(JsStr.of("example"))
                    && bodyJsObj.get("req_body")
                                .equals(JsStr.of("")) && status == 200;
        })
                .accept(httpClient.get.apply(new GetReq().port(PORT)
                                                         .uri("example")
                                            ),
                        context
                       );

    }

    @Test
    public void testPost(VertxTestContext context) {
        Verifiers.<JsObj>verifySuccess(resp -> {
            Integer status  = HttpResp.STATUS_CODE_LENS.get.apply(resp);
            String bodyResp  = HttpResp.STR_BODY_LENS.get.apply(resp);
            JsObj  bodyJsObj = JsObj.parse(bodyResp);
            return bodyJsObj.get("req_method")
                            .equals(JsStr.of("POST"))
                    && bodyJsObj.get("req_uri")
                                .equals(JsStr.of("example"))
                    && bodyJsObj.get("req_body")
                                .equals(JsStr.of("hi")) && status == 200;
        })
                .accept(httpClient.post.apply(new PostReq("hi".getBytes())
                                                      .port(PORT)
                                                      .uri("example")),
                        context
                       );
    }

    @Test
    public void testPut(VertxTestContext context) {
        Verifiers.<JsObj>verifySuccess(resp -> {
            Integer status  = HttpResp.STATUS_CODE_LENS.get.apply(resp);
            String bodyResp  = HttpResp.STR_BODY_LENS.get.apply(resp);
            JsObj  bodyJsObj = JsObj.parse(bodyResp);
            return bodyJsObj.get("req_method")
                            .equals(JsStr.of("PUT"))
                    && bodyJsObj.get("req_uri")
                                .equals(JsStr.of("example"))
                    && bodyJsObj.get("req_body")
                                .equals(JsStr.of("hi")) && status==200;
        })
                .accept(httpClient.put.apply(new PutReq("hi".getBytes())
                                                     .port(PORT)
                                                     .uri("example")),
                        context
                       );
    }

    @Test
    public void testPatch(VertxTestContext context) {
        Verifiers.<JsObj>verifySuccess(resp -> {
            Integer status  = HttpResp.STATUS_CODE_LENS.get.apply(resp);
            String bodyResp  = HttpResp.STR_BODY_LENS.get.apply(resp);
            JsObj  bodyJsObj = JsObj.parse(bodyResp);
            return bodyJsObj.get("req_method")
                            .equals(JsStr.of("PATCH"))
                    && bodyJsObj.get("req_uri")
                                .equals(JsStr.of("example"))
                    && bodyJsObj.get("req_body")
                                .equals(JsStr.of("hi")) && status==200;
        })
                .accept(httpClient.patch.apply(new PatchReq("hi".getBytes())
                                                       .port(PORT)
                                                       .uri("example")),
                        context
                       );
    }

    @Test
    public void testDelete(VertxTestContext context) {
        Verifiers.<JsObj>verifySuccess(resp -> {
            Integer status  = HttpResp.STATUS_CODE_LENS.get.apply(resp);
            String bodyResp  = HttpResp.STR_BODY_LENS.get.apply(resp);
            JsObj  bodyJsObj = JsObj.parse(bodyResp);
            return bodyJsObj.get("req_method")
                            .equals(JsStr.of("DELETE"))
                    && bodyJsObj.get("req_uri")
                                .equals(JsStr.of("example")) && status == 200;

        })
                .accept(httpClient.delete.apply(new DeleteReq()
                                                        .port(PORT)
                                                        .uri("example")
                                               ),
                        context
                       );
    }

    @Test
    public void testConnect(VertxTestContext context) {
        Verifiers.<JsObj>verifySuccess(resp -> {
            Integer status  = HttpResp.STATUS_CODE_LENS.get.apply(resp);
            return status == 200;

        })
                .accept(httpClient.connect.apply(new ConnectReq()
                                                        .port(PORT)
                                                        .uri("example")
                                                ),
                        context
                       );
    }

    @Test
    public void testHead(VertxTestContext context) {
        Verifiers.<JsObj>verifySuccess(resp -> {
            Integer status  = HttpResp.STATUS_CODE_LENS.get.apply(resp);
            return status == 200;

        })
                .accept(httpClient.head.apply(new HeadReq()
                                                         .port(PORT)
                                                         .uri("example")
                                                ),
                        context
                       );
    }

    @Test
    public void testOptions(VertxTestContext context) {
        Verifiers.<JsObj>verifySuccess(resp -> {
            Integer status  = HttpResp.STATUS_CODE_LENS.get.apply(resp);
            String bodyResp  = HttpResp.STR_BODY_LENS.get.apply(resp);
            JsObj  bodyJsObj = JsObj.parse(bodyResp);
            return bodyJsObj.get("req_method")
                            .equals(JsStr.of("OPTIONS"))
                    && bodyJsObj.get("req_uri")
                                .equals(JsStr.of("example")) &&  status == 200;

        })
                .accept(httpClient.options.apply(new OptionsReq()
                                                      .port(PORT)
                                                      .uri("example")
                                             ),
                        context
                       );
    }

    @Test
    public void testTrace(VertxTestContext context) {
        Verifiers.<JsObj>verifySuccess(resp -> {
            Integer status  = HttpResp.STATUS_CODE_LENS.get.apply(resp);
            String bodyResp  = HttpResp.STR_BODY_LENS.get.apply(resp);
            JsObj  bodyJsObj = JsObj.parse(bodyResp);
            return bodyJsObj.get("req_method")
                            .equals(JsStr.of("TRACE"))
                    && bodyJsObj.get("req_uri")
                                .equals(JsStr.of("example")) &&  status == 200;

        })
                .accept(httpClient.trace.apply(new TraceReq()
                                                         .port(PORT)
                                                         .uri("example")
                                                ),
                        context
                       );
    }
}
