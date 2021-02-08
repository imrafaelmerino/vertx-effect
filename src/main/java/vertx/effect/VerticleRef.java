package vertx.effect;

import io.vertx.core.CompositeFuture;
import io.vertx.core.Future;
import io.vertx.core.MultiMap;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.eventbus.Message;
import vertx.effect.core.EventPublisher;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static java.util.Objects.requireNonNull;


/**
 It represents a reference to a Verticle, the unit of computation. It allows to interact with the verticle it
 represent using just functions. the method {@link #tell(DeliveryOptions)} returns a consumer to establish an
 unidirectional conversation (a replay is not expected or it's ignored), and the {@link #tell(DeliveryOptions)}
 method returns a function to establish a bidirectional conversation.

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
    private static final Supplier<MultiMap> EMPTY_HEADERS = MultiMap::caseInsensitiveMultiMap;

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
     returns a lambda to establish a bidirectional conversation with this verticle

     @param options the delivery options
     @return a lambda
     */

    public λ<I, O> ask(final DeliveryOptions options) {
        requireNonNull(options);
        return body -> Val.effect(() -> {
                                   try {
                                       MessageEvent messageEvent = new MessageEvent();
                                       messageEvent.address = address;
                                       messageEvent.begin();
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
                                                                                             EMPTY_HEADERS.get(),
                                                                                             messageEvent
                                                                                            )
                                                                               .accept(vertx);
                                                   }
                                                   else {
                                                       EventPublisher.PUBLISHER.receivedError(address,
                                                                                              event.cause(),
                                                                                              messageEvent
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
     returns a lambda with context to establish a bidirectional conversation with this verticle.
     A lambda with context takes two parameters, the message to be sent and and the context,
     which is represented with message headers {@link Message#headers()}

     @param options the delivery options
     @return a lambda with context
     */
    public λc<I, O> trace(final DeliveryOptions options) {
        requireNonNull(options);
        return (context, body) -> Val.effect(() -> {
                                              try {
                                                  MessageEvent messageEvent = new MessageEvent();
                                                  messageEvent.address = address;
                                                  messageEvent.begin();
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
                                                                                                        context,
                                                                                                        messageEvent
                                                                                                       )
                                                                                          .accept(vertx);
                                                              }
                                                              else {
                                                                  EventPublisher.PUBLISHER.receivedError(address,
                                                                                                         event.cause(),
                                                                                                         context,
                                                                                                         messageEvent
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
     returns a lambda to establish a bidirectional conversation with this verticle

     @return a lambda
     */

    public λ<I, O> ask() {
        return ask(DEFAULT);
    }

    /**
     returns a lambda with context to establish a bidirectional conversation with this verticle.
     A lambda with context takes two parameters, the message to be sent and and the context,
     which is represented with message headers {@link Message#headers()}

     @return a lambda with context
     */
    public λc<I, O> trace() {
        return trace(DEFAULT);
    }


    /**
     returns a consumer to send messages to this verticle. Since a consumer is returned, the response
     is ignored

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
     returns a consumer to send messages to this verticle. Since a consumer is returned, the response
     is ignored

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
