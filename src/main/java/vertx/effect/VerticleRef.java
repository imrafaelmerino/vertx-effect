package vertx.effect;


import io.vertx.core.Future;
import io.vertx.core.MultiMap;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.eventbus.Message;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static java.util.Objects.requireNonNull;
import static vertx.effect.EventPublisher.PUBLISHER;


/**
 * It represents a reference to a Verticle, the unit of computation. It allows to interact with the verticle it
 * represents using just functions. the method {@link #tell(DeliveryOptions)} returns a consumer to establish an
 * unidirectional conversation (a replay is not expected or it's ignored), and the {@link #tell(DeliveryOptions)} method
 * returns a function to establish a bidirectional conversation.
 *
 * @param <I> the type of the input message sent to this verticle
 * @param <O> the type of the output message returned by this verticle
 */
public final class VerticleRef<I, O> {

    /**
     * the default delivery options that will be used if not specified
     */
    private static final DeliveryOptions DEFAULT = new DeliveryOptions();
    private static final Supplier<MultiMap> EMPTY_HEADERS = MultiMap::caseInsensitiveMultiMap;
    /**
     * address where a verticle is listening on
     */
    public final String address;
    /**
     * the identifiers assigned to the different instances of this verticle after being deployed. To undeploy a
     * verticle, its identifier is needed.
     */
    public final Set<String> ids;
    private final Vertx vertx;

    public VerticleRef(final Vertx vertx,
                       final String address
                      ) {
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
     * returns a lambda to establish a bidirectional conversation with this verticle
     *
     * @param options the delivery options
     * @return a lambda
     */

    public Lambda<I, O> ask(final DeliveryOptions options) {
        requireNonNull(options);
        return body -> VIO.effect(() -> {
                                      try {
                                          PUBLISHER.publishMessageSent(address, body)
                                                   .accept(vertx);
                                          return vertx.eventBus().<O>request(address,
                                                                             body,
                                                                             options
                                                                            )
                                                      .onComplete(event -> {
                                                          if (event.succeeded()) {
                                                              PUBLISHER.publishResponseReceived(address,
                                                                                                EMPTY_HEADERS.get()
                                                                                               )
                                                                       .accept(vertx);
                                                          } else {
                                                              PUBLISHER.publishFailureReceived(
                                                                               address,
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

    /**
     * returns a lambda with context to establish a bidirectional conversation with this verticle. A lambda with context
     * takes two parameters, the message to be sent and the context, which is represented with message headers
     * {@link Message#headers()}
     *
     * @param options the delivery options
     * @return a lambda with context
     */
    public Lambdac<I, O> trace(final DeliveryOptions options) {
        requireNonNull(options);
        return (context, body) -> VIO.effect(() -> {
                                                 try {
                                                     PUBLISHER.publishMessageSent(address,
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
                                                                         PUBLISHER.publishResponseReceived(address,
                                                                                                           context
                                                                                                          )
                                                                                  .accept(vertx);
                                                                     } else {
                                                                         PUBLISHER.publishFailureReceived(address,
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
     * returns a lambda to establish a bidirectional conversation with this verticle
     *
     * @return a lambda
     */

    public Lambda<I, O> ask() {
        return ask(DEFAULT);
    }

    /**
     * returns a lambda with context to establish a bidirectional conversation with this verticle. A lambda with context
     * takes two parameters, the message to be sent and and the context, which is represented with message headers
     * {@link Message#headers()}
     *
     * @return a lambda with context
     */
    public Lambdac<I, O> trace() {
        return trace(DEFAULT);
    }


    /**
     * returns a consumer to send messages to this verticle. Since a consumer is returned, the response is ignored
     *
     * @param options the delivery options
     * @return a consumer that takes an object of type I
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
     * returns a consumer to send messages to this verticle. Since a consumer is returned, the response is ignored
     *
     * @return a consumer that takes an object of type I
     */
    public Consumer<I> tell() {
        return tell(DEFAULT);
    }

    /**
     * Undeploy all the instances of this verticle
     *
     * @return a future that will be completed when all the instances are undeployed
     */
    public Future<Void> undeploy() {
        if (ids.isEmpty()) return Future.succeededFuture();
        List<Future<?>> futures = new ArrayList<>();
        for (final String id : ids)
            futures.add(vertx.undeploy(id));

        return Future
                .all(futures)
                .flatMap(it -> it.succeeded() ?
                                 Future.succeededFuture() :
                                 Future.failedFuture(it.cause())
                        );
    }


    @Override
    public String toString() {
        return address;
    }
}
