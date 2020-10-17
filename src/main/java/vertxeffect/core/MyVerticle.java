package vertxeffect.core;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.eventbus.Message;
import io.vertx.core.eventbus.MessageConsumer;

import java.util.function.Consumer;

import static java.util.Objects.requireNonNull;
import static vertxeffect.Event.INTERNAL_ERROR_PROCESSING_MESSAGE;
import static vertxeffect.Event.INTERNAL_ERROR_STARTING_VERTICLE;
import static vertxeffect.Failures.*;

/**
 Represents a Verticle. It's the unit of computation.

 @param <I> type of the message sent to the Verticle */
public class MyVerticle<I> extends AbstractVerticle {
    private final Consumer<Message<I>> consumer;
    private final String address;
    private MessageConsumer<Object> messageConsumer;

    /**
     Creates a verticle instance that when deployed will process the messages sent to the given
     address

     @param consumer consumer that takes and processes the messages sent to this verticle
     @param address  address where the verticle is listening on
     */
    public MyVerticle(final Consumer<Message<I>> consumer,
                      final String address
                     ) {
        this.consumer = requireNonNull(consumer);
        this.address = requireNonNull(address);
    }

    /**
     Register the given consumer listening on the given address. When it's done, this
     actor is ready to receive messages on that address.

     @param promise promise to be completed when the consumer is registered
     */
    @Override
    @SuppressWarnings("unchecked")
    //if you interact with this actor via its ActorRef object, there's no
    //way you can send it messages of type different than I
    public void start(final Promise<Void> promise) {
        try {
            messageConsumer = vertx.eventBus()
                                   .consumer(address,
                                             message ->
                                             {
                                                 try {
                                                     this.consumer.accept((Message<I>) message);
                                                 } catch (Exception exc) {
                                                     message.reply(GET_INTERNAL_ERROR_EXCEPTION.apply(exc));
                                                     promise.fail(exc);
                                                     EventPublisher.PUBLISHER.internalError(INTERNAL_ERROR_PROCESSING_MESSAGE,
                                                                                            address,
                                                                                            exc,
                                                                                            message.headers()
                                                                                           ).accept(vertx);
                                                 }
                                             }
                                            );
            messageConsumer.completionHandler(promise);
        } catch (Exception exc) {
            promise.fail(GET_EXCEPTION_DEPLOYING_VERTICLE.apply(exc));
            EventPublisher.PUBLISHER.internalError(INTERNAL_ERROR_STARTING_VERTICLE,
                                                   address,
                                                   exc
                                                  ).accept(vertx);
        }
    }

    /**
     Unregister the consumer listening on the give address that was created during the
     {@link #start(Promise)} method.

     @param promise promise to be completed when the consumer is unregistered
     */
    @Override
    public void stop(final Promise<Void> promise) {
        try {
            if (messageConsumer.isRegistered())
                messageConsumer.unregister(promise);
            else promise.complete();
        } catch (Exception e) {
            promise.fail(GET_EXCEPTION_STOPPING_VERTICLE.apply(e));

        }
    }
}
