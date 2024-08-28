package vertx.effect;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.eventbus.Message;
import io.vertx.core.eventbus.MessageConsumer;
import io.vertx.core.eventbus.ReplyException;

import java.util.function.Consumer;

import static io.vertx.core.eventbus.ReplyFailure.RECIPIENT_FAILURE;
import static java.util.Objects.requireNonNull;
import static vertx.effect.Event.EXCEPTION_PROCESSING_MESSAGE;
import static vertx.effect.Event.EXCEPTION_STARTING_VERTICLE;
import static vertx.effect.Failures.*;

/**
 * Represents a Verticle. It's the unit of computation.
 *
 * @param <I> type of the message sent to the Verticle
 */
class MyVerticle<I> extends AbstractVerticle {
    private final Consumer<Message<I>> consumer;
    private final String address;
    private MessageConsumer<Object> messageConsumer;

    /**
     * Creates a verticle instance that when deployed will process the messages sent to the given address
     *
     * @param consumer consumer that takes and processes the messages sent to this verticle
     * @param address  address where the verticle is listening on
     */
    public MyVerticle(final Consumer<Message<I>> consumer,
                      final String address
                     ) {
        this.consumer = requireNonNull(consumer);
        this.address = requireNonNull(address);
    }

    /**
     * Register the given consumer listening on the given address. When it's done, this actor is ready to receive
     * messages on that address.
     *
     * @param promise promise to be completed when the consumer is registered
     */
    @Override
    @SuppressWarnings("unchecked")
    //if you interact with this actor via its ActorRef object, there's no
    //way you can send it messages of type different from I
    public void start(final Promise<Void> promise) {
        try {
            messageConsumer = vertx.eventBus()
                                   .consumer(address,
                                             message ->
                                             {
                                                 try {
                                                     this.consumer.accept((Message<I>) message);
                                                 } catch (Exception exc) {
                                                     message.reply(new ReplyException(RECIPIENT_FAILURE,
                                                                                      INTERNAL_ERROR_CODE,
                                                                                      Functions.getErrorMessage(exc)
                                                     ));
                                                     promise.fail(exc);
                                                     EventPublisher.PUBLISHER.publishException(EXCEPTION_PROCESSING_MESSAGE,
                                                                                               address,
                                                                                               exc,
                                                                                               message.headers()
                                                                                              ).accept(vertx);
                                                 }
                                             }
                                            );
            messageConsumer.completionHandler(promise);
        } catch (Exception exc) {
            promise.fail(new ReplyException(RECIPIENT_FAILURE,
                                            EXCEPTION_DEPLOYING_VERTICLE_CODE,
                                            Functions.getErrorMessage(exc)
                         )
                        );
            EventPublisher.PUBLISHER.publishException(EXCEPTION_STARTING_VERTICLE,
                                                      address,
                                                      exc
                                                     ).accept(vertx);
        }
    }

    /**
     * Unregister the consumer listening on the give address that was created during the {@link #start(Promise)}
     * method.
     *
     * @param promise promise to be completed when the consumer is unregistered
     */
    @Override
    public void stop(final Promise<Void> promise) {
        try {
            if (messageConsumer.isRegistered())
                messageConsumer.unregister(promise);
            else promise.complete();
        } catch (Exception e) {
            promise.fail(new ReplyException(RECIPIENT_FAILURE,
                                            EXCEPTION_UNDEPLOYING_VERTICLE_CODE,
                                            Functions.getErrorMessage(e)
                         )
                        );

        }
    }
}
