package vertx.effect;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.VertxException;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.eventbus.Message;
import io.vertx.core.eventbus.ReplyException;
import io.vertx.core.http.*;
import jsonvalues.JsArray;
import jsonvalues.JsObj;
import jsonvalues.JsStr;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static io.vertx.core.eventbus.ReplyFailure.RECIPIENT_FAILURE;
import static io.vertx.core.http.HttpMethod.*;
import static io.vertx.core.net.impl.ConnectionBase.CLOSED_EXCEPTION;
import static java.util.Objects.requireNonNull;
import static vertx.effect.EventPublisher.PUBLISHER;
import static vertx.effect.Failures.*;
import static vertx.effect.Functions.getErrorMessage;
import static vertx.effect.HttpResp.*;

abstract class AbstractHttpClientModule extends VertxModule {

    protected final HttpClientOptions httpOptions;
    protected final String httpClientAddress;
    protected Lambdac<JsObj, JsObj> httpClient;

    public AbstractHttpClientModule(final HttpClientOptions options,
                                    final String address
                                   ) {
        if (requireNonNull(address).isBlank())
            throw new IllegalArgumentException("address is empty");

        this.httpClientAddress = address;
        this.httpOptions = requireNonNull(options);

    }

    private static JsObj toJsObj(Buffer buffer, HttpClientResponse httpResp) {
        return STATUS_CODE_LENS.set.apply(httpResp.statusCode())
                                   .andThen(STATUS_MESSAGE_OPT.set.apply(httpResp.statusMessage()))
                                   .andThen(STR_BODY_LENS.set.apply(new String(buffer.getBytes(), StandardCharsets.UTF_8)))
                                   .andThen(HEADERS_OPT.set.apply(Functions.headers2JsObj.apply(httpResp.headers())))
                                   .andThen(COOKIES_LENS.set.apply(cookies2JsArray(httpResp.cookies())))
                                   .apply(JsObj.empty());
    }

    private static ReplyException getHttpReplyException(Throwable exc) {
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
                } else return new ReplyException(RECIPIENT_FAILURE,
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
    }

    private static JsArray cookies2JsArray(final List<String> cookies) {
        if (cookies == null || cookies.isEmpty()) return JsArray.empty();
        return JsArray.ofIterable(cookies.stream()
                                         .map(JsStr::of)
                                         .collect(Collectors.toList()));
    }

    Consumer<Message<JsObj>> consumer(final HttpClient client) {
        return message -> {

            HttpReqEvent reqEvent = new HttpReqEvent();
            reqEvent.begin();
            JsObj req = message.body();
            PUBLISHER.publishMessageReceived(httpClientAddress,
                                             message.headers()
                                            )
                     .accept(vertx);

            Integer type = HttpReq.TYPE_LENS.get.apply(req);
            RequestOptions options = HttpReq.toReqOptions.apply(req);
            reqEvent.uri = options.getURI();
            reqEvent.host = options.getHost();

            switch (type) {
                case 0 -> execReq(client, message, reqEvent, options, GET);
                case 1 -> execBodyReq(client, message, reqEvent, HttpReq.BYTES_BODY_LENS.get.apply(req), options, POST);
                case 2 -> execBodyReq(client, message, reqEvent, HttpReq.BYTES_BODY_LENS.get.apply(req), options, PUT);
                case 3 -> execReq(client, message, reqEvent, options, DELETE);
                case 4 -> execReq(client, message, reqEvent, options, OPTIONS);
                case 5 -> execReq(client, message, reqEvent, options, HEAD);
                case 6 -> execReq(client, message, reqEvent, options, TRACE);
                case 7 ->
                        execBodyReq(client, message, reqEvent, HttpReq.BYTES_BODY_LENS.get.apply(req), options, PATCH);
                default -> message.reply(new ReplyException(RECIPIENT_FAILURE,
                                                            HTTP_METHOD_NOT_IMPLEMENTED_CODE,
                                                            "The method type " + type + " is not supported. Supported types are in enum HttpReqBuilder.TYPE."
                                         )
                                        );
            }
        };
    }

    private void execReq(HttpClient client,
                         Message<JsObj> message,
                         HttpReqEvent reqEvent,
                         RequestOptions options,
                         HttpMethod method
                        ) {
        client.request(options.setMethod(method))
              .onComplete(event -> {
                  if (event.succeeded())
                      event.result()
                           .send(getHandler(message,
                                            reqEvent
                                           ));
                  else {
                      commitFailure(reqEvent,
                                    event.cause()
                                   );
                      message.reply(getHttpReplyException(event.cause()));
                  }
              });
    }

    private void execBodyReq(HttpClient client,
                             Message<JsObj> message,
                             HttpReqEvent reqEvent,
                             byte[] body,
                             RequestOptions options,
                             HttpMethod method
                            ) {
        reqEvent.method = method.name();
        client.request(options.setMethod(method))
              .onComplete(event -> {
                  if (event.succeeded()) event.result()
                                              .send(Buffer.buffer(body),
                                                    getHandler(message,
                                                               reqEvent
                                                              )
                                                   );
                  else {
                      commitFailure(reqEvent,
                                    event.cause()
                                   );
                      message.reply(getHttpReplyException(event.cause()));
                  }
              });
    }

    private void commitFailure(final HttpReqEvent reqEvent,
                               final Throwable failure
                              ) {
        reqEvent.exceptionClass = failure.getClass()
                                         .getCanonicalName();
        reqEvent.exceptionMessage = failure.getMessage();
        reqEvent.tac();
        reqEvent.commit();
        reqEvent.end();
    }

    private Handler<AsyncResult<HttpClientResponse>> getHandler(final Message<JsObj> message,
                                                                final HttpReqEvent event
                                                               ) {
        return r -> {
            if (r.succeeded()) {
                HttpClientResponse resp = r.result();
                event.statusCode = resp.statusCode();
                event.tac();
                event.commit();
                event.end();
                resp.body()
                    .onComplete(it -> {
                                    if (it.succeeded()) {
                                        JsObj output = toJsObj(it.result(),
                                                               resp
                                                              );
                                        message.reply(output);
                                        PUBLISHER.publishMessageReplied(httpClientAddress,
                                                                        output,
                                                                        message.headers()
                                                                       )
                                                 .accept(vertx);
                                    } else {
                                        ReplyException replyException =
                                                getHttpReplyException(it.cause());
                                        message.reply(replyException);
                                        PUBLISHER
                                                .publishFailureReplied(httpClientAddress,
                                                                       replyException,
                                                                       message.headers()
                                                                      )
                                                .accept(vertx);
                                    }
                                }

                               );

            } else {
                ReplyException replyException = getHttpReplyException(r.cause());
                message.reply(replyException);

                PUBLISHER.publishFailureReplied(httpClientAddress,
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
                            consumer(vertx.createHttpClient(httpOptions)));
    }
}
