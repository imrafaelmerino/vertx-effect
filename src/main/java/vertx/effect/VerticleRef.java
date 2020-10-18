package vertx.effect;

import io.vertx.core.CompositeFuture;
import io.vertx.core.Future;
import io.vertx.core.MultiMap;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.eventbus.Message;
import vertx.effect.core.EventPublisher;
import vertx.effect.exp.Cons;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

import static java.util.Objects.requireNonNull;


/**
 It represents a reference to a Verticle, the unit of computation. It allows to send messages
 to the Verticle with the method {@link #tell(DeliveryOptions)}, or establish conversations with the
 method {@link #ask(DeliveryOptions)}.

 @param <I> the type of the input message sent to this verticle
 @param <O> the type of the output message returned by this verticle */
public class VerticleRef<I, O> {

    private final Vertx vertx;
    /**
     the default delivery options that will be used if not specified
     */
    private static final DeliveryOptions DEFAULT = new DeliveryOptions();
    /**
     address where a verticle is listening on
     */
    public final String address;
    /**
     the identifiers assigned to the different instances of this verticle after being deployed.
     To undeploy a verticle, its identifier is needed.
     */
    public final Set<String> ids;
    private static final MultiMap EMPTY_HEADERS = MultiMap.caseInsensitiveMultiMap();

    public VerticleRef(final Vertx vertx,
                       final String address) {
        this.vertx = requireNonNull(vertx);
        this.address = requireNonNull(address);
        ids = Set.of();
    }

    VerticleRef(final Vertx vertx,
                final Set<String> ids,
                final String address
               ) {
        this.vertx = requireNonNull(vertx);
        this.ids = requireNonNull(ids);
        this.address = requireNonNull(address);
        if (ids.isEmpty()) throw new IllegalArgumentException("ids is empty");
    }

    /**
     Method to establish a conversation with this verticle: a message is sent and then a message is
     received.

     @param options the delivery options
     @return a function that takes an object of type I and returns an object of type O wrapped in a
     future
     */

    public λ<I, O> ask(final DeliveryOptions options) {
        requireNonNull(options);
        return body -> Cons.of(() -> {
                                   try {
                                       EventPublisher.PUBLISHER.sentMessage(address,
                                                                            body
                                                                           )
                                                               .accept(vertx);
                                       return vertx.eventBus().<O>request(address,
                                                                          body,
                                                                          options
                                                                         )
                                               .onComplete(event -> {
                                                   if (event.succeeded()) {
                                                       EventPublisher.PUBLISHER.receivedResp(address,
                                                                                             EMPTY_HEADERS
                                                                                            )
                                                                               .accept(vertx);
                                                   }
                                                   else {
                                                       EventPublisher.PUBLISHER.receivedError(address,
                                                                                              event.cause()
                                                                                             )
                                                                               .accept(vertx);
                                                   }
                                               })
                                               .map(Message::body);

                                   } catch (Exception e) {
                                       return Future.failedFuture(e);
                                   }
                               }
                              );
    }

    public λc<I, O> trace(final DeliveryOptions options) {
        requireNonNull(options);
        return (context, body) -> Cons.of(() -> {
                                              try {
                                                  EventPublisher.PUBLISHER.sentMessage(address,
                                                                                       body,
                                                                                       context
                                                                                      )
                                                                          .accept(vertx);
                                                  return vertx.eventBus().<O>request(address,
                                                                                     body,
                                                                                     options.setHeaders(context)
                                                                                    )
                                                          .onComplete(event -> {
                                                              if (event.succeeded()) {
                                                                  EventPublisher.PUBLISHER.receivedResp(address,
                                                                                                        context
                                                                                                       )
                                                                                          .accept(vertx);
                                                              }
                                                              else {
                                                                  EventPublisher.PUBLISHER.receivedError(address,
                                                                                                         event.cause(),
                                                                                                         context
                                                                                                        )
                                                                                          .accept(vertx);
                                                              }
                                                          })
                                                          .map(Message::body);

                                              } catch (Exception e) {
                                                  return Future.failedFuture(e);
                                              }
                                          }
                                         );
    }

    /**
     Method to establish a conversation with this verticle: a message is sent and then a message is
     received.

     @return a function that takes an object of type I and returns an object of type O wrapped in a
     future
     */
    public λ<I, O> ask() {
        return ask(DEFAULT);
    }


    public λc<I, O> trace() {
        return trace(DEFAULT);
    }


    /**
     Method to send a message to this verticle.

     @param options the delivery options
     @return a consumer that takes an object of type I
     */
    public Consumer<I> tell(final DeliveryOptions options) {
        requireNonNull(options);
        return body -> vertx.eventBus()
                            .send(address,
                                  body,
                                  options
                                 );
    }

    /**
     Method to send a message to this verticle.

     @return a consumer that takes an object of type I
     */
    public Consumer<I> tell() {
        return tell(DEFAULT);
    }

    /**
     Undeploy all the instances of this verticle

     @return a future that will be completed when all the instances are undeployed
     */
    @SuppressWarnings({"rawtypes", "squid:S3740"})//vertx api doesn't use type parameter
    public Future<Void> undeploy() {
        if (ids.isEmpty()) return Future.succeededFuture();
        List<Future> futures = new ArrayList<>();
        for (final String id : ids) {
            final Future<Void> future = vertx.undeploy(id);
            futures.add(future);
        }
        return CompositeFuture.all(futures)
                              .flatMap(it ->
                                       {
                                           if (it.succeeded()) return Future.succeededFuture();
                                           else return Future.failedFuture(it.cause());
                                       });
    }

}
