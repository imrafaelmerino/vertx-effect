package vertx.effect.core;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.eventbus.Message;
import io.vertx.core.eventbus.ReplyException;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpClientOptions;
import io.vertx.core.http.HttpClientResponse;
import io.vertx.core.http.RequestOptions;
import jsonvalues.JsArray;
import jsonvalues.JsObj;
import jsonvalues.JsStr;
import vertx.effect.VertxModule;
import vertx.effect.λc;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static io.vertx.core.http.HttpMethod.*;
import static java.util.Objects.requireNonNull;
import static vertx.effect.Failures.GET_HTTP_METHOD_NOT_IMPLEMENTED_EXCEPTION;
import static vertx.effect.Failures.GET_HTTP_REPLY_EXCEPTION;
import static vertx.effect.core.ReqEvent.RESULT.FAILURE;
import static vertx.effect.core.ReqEvent.RESULT.SUCCESS;
import static vertx.effect.httpclient.HttpResp.*;

public abstract class AbstractHttpClientModule extends VertxModule {

    public AbstractHttpClientModule(final HttpClientOptions options,
                                    final String address) {
        if (requireNonNull(address).isEmpty())
            throw new IllegalArgumentException("address is empty");

        this.httpClientAddress = requireNonNull(address);
        this.httpOptions = requireNonNull(options);

    }

    protected final HttpClientOptions httpOptions;

    protected final String httpClientAddress;

    protected λc<JsObj, JsObj> httpClient;


    Consumer<Message<JsObj>> consumer(final HttpClient client) {
        return message -> {
            ReqEvent reqEvent = new ReqEvent();


            JsObj req = message.body();
            EventPublisher.PUBLISHER.receivedMessage(httpClientAddress,
                                                     message.headers()
                                                    )
                                    .accept(vertx);

            Integer        type    = HttpReq.TYPE_LENS.get.apply(req);
            RequestOptions options = HttpReq.toReqOptions.apply(req);
            reqEvent.uri = options.getURI();
            switch (type) {
                case 0:
                    reqEvent.method = GET;
                    reqEvent.begin();
                    client.request(options.setMethod(GET))
                          .onComplete(event -> {
                              if (event.succeeded()) {
                                  event.result()
                                       .send(getHandler(message,
                                                        reqEvent
                                                       ));
                              }
                              else {
                                  commitFailure(reqEvent,
                                                event.cause()
                                               );
                                  message.reply(GET_HTTP_REPLY_EXCEPTION.apply(event.cause()));
                              }
                          });

                    break;
                case 1:
                    reqEvent.method = POST;
                    client.request(options.setMethod(POST))
                          .onComplete(event -> {
                              if (event.succeeded()) event.result()
                                                          .send(Buffer.buffer(HttpReq.BYTES_BODY_LENS.get.apply(req)),
                                                                getHandler(message,
                                                                           reqEvent
                                                                          )
                                                               );
                              else {
                                  commitFailure(reqEvent,
                                                event.cause()
                                               );
                                  message.reply(GET_HTTP_REPLY_EXCEPTION.apply(event.cause()));
                              }
                          });

                    break;
                case 2:
                    reqEvent.method = PUT;
                    client.request(options.setMethod(PUT))
                          .onComplete(event -> {
                              if (event.succeeded()) event.result()
                                                          .send(Buffer.buffer(HttpReq.BYTES_BODY_LENS.get.apply(req)),
                                                                getHandler(message,
                                                                           reqEvent
                                                                          )
                                                               );
                              else {
                                  commitFailure(reqEvent,
                                                event.cause()
                                               );
                                  message.reply(GET_HTTP_REPLY_EXCEPTION.apply(event.cause()));
                              }
                          });
                    break;
                case 3:
                    reqEvent.method = DELETE;
                    client.request(options.setMethod(DELETE))
                          .onComplete(event -> {
                              if (event.succeeded()) event.result()
                                                          .send(getHandler(message,
                                                                           reqEvent
                                                                          ));
                              else {
                                  commitFailure(reqEvent,
                                                event.cause()
                                               );
                                  message.reply(GET_HTTP_REPLY_EXCEPTION.apply(event.cause()));
                              }
                          });
                    break;
                case 4:
                    reqEvent.method = OPTIONS;
                    client.request(options.setMethod(OPTIONS))
                          .onComplete(event -> {
                              if (event.succeeded()) event.result()
                                                          .send(getHandler(message,
                                                                           reqEvent
                                                                          ));
                              else {
                                  commitFailure(reqEvent,
                                                event.cause()
                                               );
                                  message.reply(GET_HTTP_REPLY_EXCEPTION.apply(event.cause()));
                              }
                          });
                    break;
                case 5:
                    reqEvent.method = HEAD;
                    client.request(options.setMethod(HEAD))
                          .onComplete(event -> {
                              if (event.succeeded()) event.result()
                                                          .send(getHandler(message,
                                                                           reqEvent
                                                                          ));
                              else {
                                  commitFailure(reqEvent,
                                                event.cause()
                                               );
                                  message.reply(GET_HTTP_REPLY_EXCEPTION.apply(event.cause()));
                              }
                          });
                    break;
                case 6:
                    reqEvent.method = TRACE;
                    client.request(options.setMethod(TRACE))
                          .onComplete(event -> {
                              if (event.succeeded()) event.result()
                                                          .send(getHandler(message,
                                                                           reqEvent
                                                                          ));
                              else {
                                  commitFailure(reqEvent,
                                                event.cause()
                                               );
                                  message.reply(GET_HTTP_REPLY_EXCEPTION.apply(event.cause()));
                              }
                          });
                    break;
                case 7:
                    reqEvent.method = PATCH;
                    client.request(options.setMethod(PATCH))
                          .onComplete(event -> {
                              if (event.succeeded()) event.result()
                                                          .send(Buffer.buffer(HttpReq.BYTES_BODY_LENS.get.apply(req)),
                                                                getHandler(message,
                                                                           reqEvent
                                                                          )
                                                               );
                              else {
                                  commitFailure(reqEvent,
                                                event.cause()
                                               );
                                  message.reply(GET_HTTP_REPLY_EXCEPTION.apply(event.cause()));
                              }
                          });
                    break;
                case 8:
                    reqEvent.method = CONNECT;
                    client.request(options.setMethod(CONNECT))
                          .onComplete(event -> {
                              if (event.succeeded()) event.result()
                                                          .send(getHandler(message,
                                                                           reqEvent
                                                                          ));
                              else {
                                  commitFailure(reqEvent,
                                                event.cause()
                                               );
                                  message.reply(GET_HTTP_REPLY_EXCEPTION.apply(event.cause()));
                              }
                          });
                    break;
                default:
                    message.reply(GET_HTTP_METHOD_NOT_IMPLEMENTED_EXCEPTION.apply(type)
                                 );
            }
        };
    }

    private void commitFailure(final ReqEvent reqEvent,
                               final Throwable failure) {
        reqEvent.result = FAILURE;
        reqEvent.failure = failure;
        reqEvent.commit();
        reqEvent.end();
    }


    private Handler<AsyncResult<HttpClientResponse>> getHandler(final Message<JsObj> message,
                                                                final ReqEvent event) {
        return r -> {
            if (r.succeeded()) {
                HttpClientResponse resp = r.result();
                event.result = SUCCESS;
                event.statusCode = resp.statusCode();
                event.commit();
                event.end();
                resp.body()
                    .onComplete(it -> {
                                    if (it.succeeded()) {
                                        JsObj output = toJsObj.apply(it.result(),
                                                                     resp
                                                                    );
                                        message.reply(output);
                                        EventPublisher.PUBLISHER.repliedResp(httpClientAddress,
                                                                             output,
                                                                             message.headers()
                                                                            )
                                                                .accept(vertx);
                                    }
                                    else {
                                        ReplyException replyException = GET_HTTP_REPLY_EXCEPTION.apply(it.cause());
                                        message.reply(replyException);
                                        EventPublisher.PUBLISHER.repliedError(httpClientAddress,
                                                                              replyException,
                                                                              message.headers()
                                                                             )
                                                                .accept(vertx);
                                    }
                                }

                               );

            }
            else {
                ReplyException replyException = GET_HTTP_REPLY_EXCEPTION.apply(r.cause());
                message.reply(replyException);

                EventPublisher.PUBLISHER.repliedError(httpClientAddress,
                                                      replyException,
                                                      message.headers()
                                                     )
                                        .accept(vertx);

            }

        };
    }

    @Override
    protected void initialize() {
        this.httpClient = trace(httpClientAddress);
    }


    @Override
    protected void deploy() {
        this.deployConsumer(httpClientAddress,
                            consumer(vertx.createHttpClient(httpOptions))
                           );
    }

    private static JsArray cookies2JsArray(final List<String> cookies) {
        if (cookies == null || cookies.isEmpty()) return JsArray.empty();
        return JsArray.ofIterable(cookies.stream()
                                         .map(JsStr::of)
                                         .collect(Collectors.toList()));
    }


    private static final BiFunction<Buffer, HttpClientResponse, JsObj> toJsObj =
            (buffer, httpResp) ->
                    STATUS_CODE_LENS.set.apply(httpResp.statusCode())
                                        .andThen(STATUS_MESSAGE_OPT.set.apply(httpResp.statusMessage()))
                                        .andThen(STR_BODY_LENS.set.apply(new String(buffer.getBytes())))
                                        .andThen(HEADERS_OPT.set.apply(Functions.headers2JsObj.apply(httpResp.headers())))
                                        .andThen(COOKIES_LENS.set.apply(cookies2JsArray(httpResp.cookies())))
                                        .apply(JsObj.empty());

}
