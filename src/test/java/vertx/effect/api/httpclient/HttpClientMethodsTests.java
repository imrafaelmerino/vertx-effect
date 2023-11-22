package vertx.effect.api.httpclient;

import io.vertx.core.Vertx;
import io.vertx.core.http.HttpClientOptions;
import io.vertx.core.http.HttpHeaders;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import jsonvalues.JsObj;
import jsonvalues.JsStr;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import vertx.effect.*;
import vertx.effect.api.Port;
import vertx.effect.api.Verifiers;
import vertx.effect.stub.http.HttpHeadersRespStub;
import vertx.effect.stub.http.HttpReqHandlerStub;
import vertx.effect.stub.http.HttpRespStub;
import vertx.values.codecs.RegisterJsValuesCodecs;

import static vertx.effect.stub.http.HttpRespStub.ALWAYS;


@ExtendWith(VertxExtension.class)
public class HttpClientMethodsTests {

    private static final int PORT = Port.number.incrementAndGet();
    static HttpClientModule httpClient;

    @BeforeAll
    public static void prepare(final Vertx vertx,
                               final VertxTestContext context
                              ) {
        VertxRef vertxRef = new VertxRef(vertx);
        vertxRef.registerConsumer(VertxRef.EVENTS_ADDRESS,
                                  System.out::println
                                 );
        httpClient = new HttpClientModule(new HttpClientOptions().setDefaultHost("0.0.0.0"), "myhttp-client");

        HttpRespStub mockReqResp =
                HttpRespStub.when(ALWAYS)
                            .setBodyResp(n -> body -> req -> JsObj.of("req_method",
                                                                      JsStr.of(req.method()
                                                                                  .name()
                                                                              ),
                                                                      "req_body",
                                                                      JsStr.of(body.toString()),
                                                                      "req_uri",
                                                                      JsStr.of(req.uri())
                                                                     )
                                                                  .toPrettyString()
                                        )
                            .setHeadersResp(HttpHeadersRespStub.JSON_CONTENT_TYPE);

        MapExp.seq("json-values-codecs", vertxRef.deployVerticle(new RegisterJsValuesCodecs()),
                   "http-server", new HttpServerBuilder(vertx,
                                                        new HttpReqHandlerStub(mockReqResp
                                                        )).create(PORT),
                   "http-client", vertxRef.deployVerticle(httpClient)
                  )
              .get()
              .onComplete(Verifiers.pipeTo(context));


    }


    @Test
    public void testGet(VertxTestContext context) {
        Verifiers.<JsObj>verifySuccess(resp -> {
                     Integer status = HttpResp.STATUS_CODE_LENS.get.apply(resp);
                     String bodyResp = HttpResp.STR_BODY_LENS.get.apply(resp);
                     JsObj bodyJsObj = JsObj.parse(bodyResp);
                     return bodyJsObj.get("req_method")
                                     .equals(JsStr.of("GET"))
                            && bodyJsObj.get("req_uri")
                                        .equals(JsStr.of("example"))
                            && bodyJsObj.get("req_body")
                                        .equals(JsStr.of("")) && status == 200;
                 })
                 .accept(httpClient.get.apply(HttpHeaders.headers()
                                                         .set("method",
                                                              "get"
                                                             ),
                                              new GetReq().port(PORT)
                                                          .uri("example")
                                             ),
                         context
                        );

    }

    @Test
    public void testPost(VertxTestContext context) {
        Verifiers.<JsObj>verifySuccess(resp -> {
                     Integer status = HttpResp.STATUS_CODE_LENS.get.apply(resp);
                     String bodyResp = HttpResp.STR_BODY_LENS.get.apply(resp);
                     JsObj bodyJsObj = JsObj.parse(bodyResp);
                     return bodyJsObj.get("req_method")
                                     .equals(JsStr.of("POST"))
                            && bodyJsObj.get("req_uri")
                                        .equals(JsStr.of("example"))
                            && bodyJsObj.get("req_body")
                                        .equals(JsStr.of("hi")) && status == 200;
                 })
                 .accept(httpClient.post.apply(HttpHeaders.headers()
                                                          .set("method",
                                                               "post"
                                                              ),
                                               new PostReq("hi".getBytes())
                                                       .port(PORT)
                                                       .uri("example")
                                              ),
                         context
                        );
    }

    @Test
    public void testPut(VertxTestContext context) {
        Verifiers.<JsObj>verifySuccess(resp -> {
                     Integer status = HttpResp.STATUS_CODE_LENS.get.apply(resp);
                     String bodyResp = HttpResp.STR_BODY_LENS.get.apply(resp);
                     JsObj bodyJsObj = JsObj.parse(bodyResp);
                     return bodyJsObj.get("req_method")
                                     .equals(JsStr.of("PUT"))
                            && bodyJsObj.get("req_uri")
                                        .equals(JsStr.of("example"))
                            && bodyJsObj.get("req_body")
                                        .equals(JsStr.of("hi")) && status == 200;
                 })
                 .accept(httpClient.put.apply(HttpHeaders.headers()
                                                         .set("method",
                                                              "put"
                                                             ),
                                              new PutReq("hi".getBytes())
                                                      .port(PORT)
                                                      .uri("example")
                                             ),
                         context
                        );
    }

    @Test
    public void testPatch(VertxTestContext context) {
        Verifiers.<JsObj>verifySuccess(resp -> {
                     Integer status = HttpResp.STATUS_CODE_LENS.get.apply(resp);
                     String bodyResp = HttpResp.STR_BODY_LENS.get.apply(resp);
                     JsObj bodyJsObj = JsObj.parse(bodyResp);
                     return bodyJsObj.get("req_method")
                                     .equals(JsStr.of("PATCH"))
                            && bodyJsObj.get("req_uri")
                                        .equals(JsStr.of("example"))
                            && bodyJsObj.get("req_body")
                                        .equals(JsStr.of("hi")) && status == 200;
                 })
                 .accept(httpClient.patch.apply(HttpHeaders.headers()
                                                           .set("method",
                                                                "patch"
                                                               ),
                                                new PatchReq("hi".getBytes())
                                                        .port(PORT)
                                                        .uri("example")
                                               ),
                         context
                        );
    }

    @Test
    public void testDelete(VertxTestContext context) {
        Verifiers.<JsObj>verifySuccess(resp -> {
                     Integer status = HttpResp.STATUS_CODE_LENS.get.apply(resp);
                     String bodyResp = HttpResp.STR_BODY_LENS.get.apply(resp);
                     JsObj bodyJsObj = JsObj.parse(bodyResp);
                     return bodyJsObj.get("req_method")
                                     .equals(JsStr.of("DELETE"))
                            && bodyJsObj.get("req_uri")
                                        .equals(JsStr.of("example")) && status == 200;

                 })
                 .accept(httpClient.delete.apply(HttpHeaders.headers()
                                                            .set("method",
                                                                 "delete"
                                                                ),
                                                 new DeleteReq()
                                                         .port(PORT)
                                                         .uri("example")
                                                ),
                         context
                        );
    }

    /*@Test
    public void testConnect(VertxTestContext context) {
        Verifiers.<JsObj>verifySuccess(resp -> {
            Integer status = HttpResp.STATUS_CODE_LENS.get.apply(resp);
            return status == 200;

        })
                .accept(httpClient.connect.apply(HttpHeaders.headers()
                                                            .set("method",
                                                                 "connect1"
                                                                ),
                                                 new ConnectReq()
                                                         .port(PORT)
                                                         .uri("example")
                                                ),
                        context
                       );
    }
*/
    @Test
    public void testHead(VertxTestContext context) {
        Verifiers.<JsObj>verifySuccess(resp -> {
                     Integer status = HttpResp.STATUS_CODE_LENS.get.apply(resp);
                     return status == 200;

                 })
                 .accept(httpClient.head.apply(HttpHeaders.headers()
                                                          .set("method",
                                                               "head"
                                                              ),
                                               new HeadReq()
                                                       .port(PORT)
                                                       .uri("example")
                                              ),
                         context
                        );
    }

    @Test
    public void testOptions(VertxTestContext context) {
        Verifiers.<JsObj>verifySuccess(resp -> {
            Integer status = HttpResp.STATUS_CODE_LENS.get.apply(resp);
            String bodyResp = HttpResp.STR_BODY_LENS.get.apply(resp);
            JsObj bodyJsObj = JsObj.parse(bodyResp);
            return bodyJsObj.get("req_method")
                            .equals(JsStr.of("OPTIONS"))
                   && bodyJsObj.get("req_uri")
                               .equals(JsStr.of("example")) && status == 200;

        }).accept(httpClient.options.apply(HttpHeaders.headers()
                                                      .set("method",
                                                           "options"
                                                          ),
                                           new OptionsReq()
                                                   .port(PORT)
                                                   .uri("example")
                                          ),
                  context
                 );
    }

    @Test
    public void testTrace(VertxTestContext context) {
        Verifiers.<JsObj>verifySuccess(resp -> {
                     Integer status = HttpResp.STATUS_CODE_LENS.get.apply(resp);
                     String bodyResp = HttpResp.STR_BODY_LENS.get.apply(resp);
                     JsObj bodyJsObj = JsObj.parse(bodyResp);
                     return bodyJsObj.get("req_method")
                                     .equals(JsStr.of("TRACE"))
                            && bodyJsObj.get("req_uri")
                                        .equals(JsStr.of("example")) && status == 200;

                 })
                 .accept(httpClient.trace.apply(new TraceReq()
                                                        .port(PORT)
                                                        .uri("example")
                                               ),
                         context
                        );
    }
}
