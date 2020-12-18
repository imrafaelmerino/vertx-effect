package vertx.effect.mock;

import io.vertx.core.Handler;
import io.vertx.core.MultiMap;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

class HttpServerHandler implements Handler<HttpServerRequest> {

    protected AtomicInteger counter = new AtomicInteger(0);

    final List<MockReqResp> mockReqResps;

    HttpServerHandler(final List<MockReqResp> mockReqResps) {
        this.mockReqResps = Objects.requireNonNull(mockReqResps);
    }

    @Override
    public void handle(final HttpServerRequest req) {

        int counter = this.counter.incrementAndGet();
        mockReqResps.stream()
                    .filter(it -> it.predicate.test(counter,
                                                   req
                                                  )
                          )
                    .findFirst()
                    .ifPresentOrElse(it -> req.body(event ->
                                                   {
                                                       if (event.succeeded()) {
                                                           Buffer buffer = event.result();
                                                           HttpServerResponse response = req.response()
                                                                                            .setStatusCode(it.code.apply(counter)
                                                                                                                  .apply(buffer)
                                                                                                                  .apply(req)
                                                                                                          );
                                                           MultiMap respHeaders = it.headers.apply(counter)
                                                                                            .apply(buffer)
                                                                                            .apply(req);

                                                           respHeaders.forEach(header -> response.putHeader(header.getKey(),
                                                                                                            header.getValue()
                                                                                                           )
                                                                              );
                                                           response
                                                                   .end(it.body.apply(counter)
                                                                               .apply(buffer)
                                                                               .apply(req)
                                                                       );
                                                       }
                                                       else {
                                                           req.response()
                                                              .setStatusCode(500)
                                                              .end(Arrays.toString(event.cause()
                                                                                        .getStackTrace()));
                                                       }
                                                   }
                                                  )

                           ,
                                    () -> req.response()
                                             .setStatusCode(404)
                                             .end("No handler matched the req")
                                   );

    }

    public void resetCounter() {
        counter.set(0);
    }

}
