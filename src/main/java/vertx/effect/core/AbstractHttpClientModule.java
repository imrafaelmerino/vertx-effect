package vertx.effect.core;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.VertxException;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.eventbus.Message;
import io.vertx.core.eventbus.ReplyException;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpClientOptions;
import io.vertx.core.http.HttpClientResponse;
import io.vertx.core.http.RequestOptions;
import io.vertx.core.http.impl.HttpClientImpl;
import jsonvalues.JsArray;
import jsonvalues.JsObj;
import jsonvalues.JsStr;
import vertx.effect.VertxModule;
import vertx.effect.λc;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

import static io.vertx.core.eventbus.ReplyFailure.RECIPIENT_FAILURE;
import static io.vertx.core.http.HttpMethod.*;
import static io.vertx.core.net.impl.ConnectionBase.CLOSED_EXCEPTION;
import static java.util.Objects.requireNonNull;
import static vertx.effect.Failures.*;
import static vertx.effect.core.Functions.getErrorMessage;
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
            reqEvent.begin();
            JsObj req = message.body();
            EventPublisher.PUBLISHER.receivedMessage(httpClientAddress,
                                                     message.headers()
                                                    )
                                    .accept(vertx);

            Integer        type    = HttpReq.TYPE_LENS.get.apply(req);
            RequestOptions options = HttpReq.toReqOptions.apply(req);
            reqEvent.uri = options.getURI();
            String defaultHost = ((HttpClientImpl) client).getOptions()
                                                          .getDefaultHost();
            reqEvent.host = (defaultHost == null) ? options.getHost() : defaultHost;

            switch (type) {
                case 0:
                    reqEvent.method = GET.name();
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
                    reqEvent.method = POST.name();
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
                    reqEvent.method = PUT.name();
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
                    reqEvent.method = DELETE.name();
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
                    reqEvent.method = OPTIONS.name();
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
                    reqEvent.method = HEAD.name();
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
                    reqEvent.method = TRACE.name();
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
                    reqEvent.method = PATCH.name();
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
                    reqEvent.method = CONNECT.name();
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
                    message.reply(new ReplyException(RECIPIENT_FAILURE,
                                                     HTTP_METHOD_NOT_IMPLEMENTED_CODE,
                                                     "The method type " + type + " is not supported. Supported types are in enum HttpReqBuilder.TYPE."
                                  )
                                 );
            }
        };
    }

    private void commitFailure(final ReqEvent reqEvent,
                               final Throwable failure) {
        reqEvent.exceptionClass = failure.getClass()
                                         .getCanonicalName();
        reqEvent.exceptionMessage = failure.getMessage();
        reqEvent.commit();
        reqEvent.end();
    }


    private Handler<AsyncResult<HttpClientResponse>> getHandler(final Message<JsObj> message,
                                                                final ReqEvent event) {
        return r -> {
            if (r.succeeded()) {
                HttpClientResponse resp = r.result();
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
    protected final void initialize() {
        this.httpClient = trace(httpClientAddress);
    }


    @Override
    protected final void deploy() {
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

    private static final Function<Throwable, ReplyException> GET_HTTP_REPLY_EXCEPTION =
            exc -> {
                switch (exc.getClass()
                           .getSimpleName()) {

                    case "ConnectTimeoutException":
                        return new ReplyException(RECIPIENT_FAILURE,
                                                  HTTP_CONNECT_TIMEOUT_CODE,
                                                  exc.getMessage()
                        );
                    case "UnknownHostException":
                        return new ReplyException(RECIPIENT_FAILURE,
                                                  HTTP_UNKNOWN_HOST_CODE,
                                                  exc.getMessage()
                        );
                    case "NoStackTraceTimeoutException":
                        return new ReplyException(RECIPIENT_FAILURE,
                                                  HTTP_REQUEST_TIMEOUT_CODE,
                                                  exc.getMessage()
                        );
                    case "VertxException": {
                        VertxException vertxException = (VertxException) exc;
                        if (vertxException == CLOSED_EXCEPTION) {
                            return new ReplyException(RECIPIENT_FAILURE,
                                                      HTTP_CONNECTION_WAS_CLOSED_CODE,
                                                      CLOSED_EXCEPTION.getMessage()
                            );
                        }
                        else return new ReplyException(RECIPIENT_FAILURE,
                                                       HTTP_FAILURE_CODE,
                                                       getErrorMessage(exc)
                        );

                    }

                    default:
                        return new ReplyException(RECIPIENT_FAILURE,
                                                  HTTP_FAILURE_CODE,
                                                  getErrorMessage(exc)
                        );
                }
            };
}
