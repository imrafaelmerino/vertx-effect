package vertx.effect;

import io.vertx.core.*;
import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.eventbus.Message;
import io.vertx.core.eventbus.MessageConsumer;
import io.vertx.core.eventbus.ReplyException;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;

import static io.vertx.core.eventbus.ReplyFailure.RECIPIENT_FAILURE;
import static java.util.Objects.requireNonNull;
import static vertx.effect.Failures.INTERNAL_ERROR_CODE;
import static vertx.effect.Failures.UNKNOWN_ERROR_CODE;

/**
 * Wrapper around the vertx instance. There are methods to:
 * <ul>
 * <li>deploy verticles from instances of {@link AbstractVerticle}, {@link Lambda } lambdas and consumers. You get a {@link VerticleRef} wrapped in a Val.
 * A VerticleRef is a wrapper around a verticle to interact with it using functions. </li>
 * <li>spawn lambdas {@link Lambda }.  When a lambda receives a message, it deploys a verticle, do a computation and then
 * undeploy the verticle after returning the response.</li>
 * <li>register a consumer that listen on an address. There is a special address {@link #EVENTS_ADDRESS} where logging
 * events are published.
 * </li>
 * <li>register a publisher on an address to broadcast messages</li>
 * <li>get delays</li>
 * </ul>
 */
public final class VertxRef {
    /**
     * Address where vertx-effect publish different logging events.
     */
    public static final String EVENTS_ADDRESS = "vertx-effect-log-events";
    private static final DeploymentOptions DEFAULT_OPTIONS = new DeploymentOptions();
    private static final AtomicLong processSeq = new AtomicLong(0);
    private final Vertx vertx;
    private final DeploymentOptions deploymentOptions;

    /**
     * @param vertx the vertx instance
     */
    public VertxRef(final Vertx vertx) {
        this(requireNonNull(vertx),
             DEFAULT_OPTIONS
            );
    }

    /**
     * @param vertx             the vertx instance
     * @param deploymentOptions the default deployment options that will be used for deploying and spawning verticles if
     *                          one is not provided
     */
    public VertxRef(final Vertx vertx,
                    final DeploymentOptions deploymentOptions
                   ) {
        this.vertx = requireNonNull(vertx);
        this.deploymentOptions = requireNonNull(deploymentOptions);
    }

    private static DeliveryOptions createDeliveryOpt(MultiMap multiMap) {
        return new DeliveryOptions().setHeaders(multiMap);
    }

    private static String generateProcessAddress(final String address) {
        return String.format("spawned.%s.%s",
                             address,
                             processSeq.getAndIncrement()
                            );
    }

    /**
     * Returns a val that, when executed, deploys a verticle on the specified address. The given consumer processes the
     * messages sent to the verticle. Use this method if the verticle needs some data from the {@link Message} different
     * than the body, like the message headers. Otherwise, use the {@link #deploy(String, Lambda)} method
     *
     * @param address  the address of the verticle
     * @param consumer the consumer that will process the messages sent to the verticle
     * @param <I>      the type of the message sent to the verticle
     * @param <O>      the type of the reply
     * @return an VerticleRef wrapped in a val
     */
    public <I, O> VIO<VerticleRef<I, O>> deployConsumer(final String address,
                                                        final Consumer<Message<I>> consumer
                                                       ) {
        Objects.requireNonNull(address);
        Objects.requireNonNull(consumer);

        return deployConsumer(address,
                              consumer,
                              deploymentOptions
                             );
    }

    /**
     * Returns a val that, when executed, deploys a verticle on the specified address. The given consumer processes the
     * messages sent to the verticle. Use this method if the verticle needs some data from the {@link Message} different
     * than the body, like the message headers. Otherwise, use the {@link #deploy(String, Lambda, DeploymentOptions)}
     * method
     *
     * @param address  the address of the verticle
     * @param consumer the consumer that will process the messages sent to the verticle
     * @param options  options for configuring the verticle deployment
     * @param <I>      the type of the message sent to the verticle
     * @param <O>      the type of the reply
     * @return an VerticleRef wrapped in a val
     */
    public <I, O> VIO<VerticleRef<I, O>> deployConsumer(final String address,
                                                        final Consumer<Message<I>> consumer,
                                                        final DeploymentOptions options
                                                       ) {
        Objects.requireNonNull(address);
        Objects.requireNonNull(consumer);
        Objects.requireNonNull(options);

        final int instances = options.getInstances();
        final Set<String> ids = new HashSet<>();
        final List<Future<?>> futures = new ArrayList<>();
        final MyVerticle<I> verticle = new MyVerticle<>(consumer,
                                                        address
        );

        return VIO.effect(() -> {
                              for (int i = 0; i < instances; i++) {
                                  final Future<String> future = vertx.deployVerticle(verticle,
                                                                                     options.setInstances(1)
                                                                                    );
                                  futures.add(future.onSuccess(id -> {
                                                                   ids.add(id);
                                                                   EventPublisher.PUBLISHER.publishVerticleDeployed(address,
                                                                                                                    id
                                                                                                                   )
                                                                                           .accept(vertx);
                                                               }
                                                              ));
                              }


                              return Future.all(futures)
                                           .flatMap(cf -> Future.succeededFuture(new VerticleRef<>(vertx,
                                                                                                   ids,
                                                                                                   address
                                                                                 )
                                                                                )
                                                   );
                          }
                         );
    }

    /**
     * Returns a val that, when executed, deploys a verticle on the specified address. The given lambda processes the
     * messages sent to the verticle.
     *
     * @param address the address of the verticle
     * @param lambda  the lambda that takes a message of type I and produces an output of type O
     * @param <I>     the type of the message sent to the verticle
     * @param <O>     the type of the reply
     * @return an VerticleRef wrapped in a val
     */
    public <I, O> VIO<VerticleRef<I, O>> deploy(final String address,
                                                final Lambda<I, O> lambda
                                               ) {
        Objects.requireNonNull(address);
        Objects.requireNonNull(lambda);
        return deploy(address,
                      lambda,
                      deploymentOptions
                     );
    }

    /**
     * Returns a val that, when executed, deploys a verticle on the specified address. The given lambda processes the
     * messages sent to the verticle.
     *
     * @param address the address of the verticle
     * @param lambda  the lambda that takes a message of type I and produces an output of type O
     * @param options options for configuring the verticle deployment
     * @param <I>     the type of the message sent to the verticle
     * @param <O>     the type of the reply
     * @return an VerticleRef wrapped in a Val
     */
    public <I, O> VIO<VerticleRef<I, O>> deploy(final String address,
                                                final Lambda<I, O> lambda,
                                                final DeploymentOptions options
                                               ) {
        Objects.requireNonNull(address);
        Objects.requireNonNull(lambda);
        Objects.requireNonNull(options);
        return VIO.effect(() -> {
                              final int instances = options.getInstances();
                              final Set<String> ids = new HashSet<>();
                              final List<Future<?>> futures = new ArrayList<>();
                              final MyVerticle<I> verticle = new MyVerticle<>(message -> wrapLambda(address,
                                                                                                    message,
                                                                                                    lambda
                                                                                                   ),
                                                                              address
                              );

                              for (int i = 0; i < instances; i++) {
                                  final Future<String> future = vertx.deployVerticle(verticle,
                                                                                     options.setInstances(1)
                                                                                    );
                                  futures.add(future.onSuccess(id -> {
                                                                   ids.add(id);
                                                                   EventPublisher.PUBLISHER.publishVerticleDeployed(address,
                                                                                                                    id
                                                                                                                   )
                                                                                           .accept(vertx);
                                                               }
                                                              ));
                              }
                              return Future.all(futures)
                                           .flatMap(cf -> Future.succeededFuture(new VerticleRef<>(vertx,
                                                                                                   ids,
                                                                                                   address
                                                                                 )
                                                                                )
                                                   );
                          }
                         );
    }

    /**
     * Returns a val that, when executed, deploys a verticle on the specified address. The given lambda processes the
     * messages sent to the verticle.
     *
     * @param address the address of the verticle
     * @param lambda  the lambda that takes a message of type I and the context of the computation and produces an
     *                output of type O
     * @param <I>     the type of the message sent to the verticle
     * @param <O>     the type of the reply
     * @return an VerticleRef wrapped in a Val
     */
    public <I, O> VIO<VerticleRef<I, O>> deploy(final String address,
                                                final Lambdac<I, O> lambda
                                               ) {
        Objects.requireNonNull(address);
        Objects.requireNonNull(lambda);
        return deploy(address,
                      lambda,
                      deploymentOptions
                     );
    }

    /**
     * Returns a val that, when executed, deploys a verticle on the specified address. The given lambda processes the
     * messages sent to the verticle.
     *
     * @param address the address of the verticle
     * @param lambda  the lambda that takes a message of type I and the context of the computation and produces an
     *                output of type O
     * @param options options for configuring the verticle deployment
     * @param <I>     the type of the message sent to the verticle
     * @param <O>     the type of the reply
     * @return an VerticleRef wrapped in a Val
     */
    public <I, O> VIO<VerticleRef<I, O>> deploy(final String address,
                                                final Lambdac<I, O> lambda,
                                                final DeploymentOptions options
                                               ) {
        Objects.requireNonNull(address);
        Objects.requireNonNull(lambda);
        Objects.requireNonNull(options);

        return VIO.effect(() -> {
                              final int instances = options.getInstances();
                              final Set<String> ids = new HashSet<>();
                              final List<Future<?>> futures = new ArrayList<>();
                              final MyVerticle<I> verticle = new MyVerticle<>(message -> wrapLambda(address,
                                                                                                    message,
                                                                                                    lambda
                                                                                                   ),
                                                                              address
                              );

                              for (int i = 0; i < instances; i++) {
                                  final Future<String> future = vertx.deployVerticle(verticle,
                                                                                     options.setInstances(1)
                                                                                    );
                                  futures.add(future.onSuccess(id -> {
                                                                   ids.add(id);
                                                                   EventPublisher.PUBLISHER.publishVerticleDeployed(address,
                                                                                                                    id
                                                                                                                   )
                                                                                           .accept(vertx);
                                                               }
                                                              ));
                              }
                              return Future.all(futures)
                                           .flatMap(cf -> Future.succeededFuture(new VerticleRef<>(vertx,
                                                                                                   ids,
                                                                                                   address
                                                                                 )
                                                                                )
                                                   );
                          }
                         );
    }

    /**
     * Returns a lambda that given an input, it deploys a verticle, does the computation, replies with the response and
     * finally undeploys the verticle. The address is generated according to the following formula: spawned.prefix.n,
     * where prefix is the specified one, and n an internal counter. Examples: spawned.get_client.1,
     * spawned.get_client.2 etc
     *
     * @param lambda        the function that takes a message of type I and and produces an output of type O
     * @param <I>           the type of the message sent to the verticle
     * @param <O>           the type of the reply
     * @param addressPrefix the prefix of the auto generated address
     * @return an VerticleRef wrapped in a Val
     */
    public <I, O> Lambda<I, O> spawn(final String addressPrefix,
                                     final Lambda<I, O> lambda
                                    ) {
        return spawn(addressPrefix,
                     lambda,
                     deploymentOptions
                    );
    }

    /**
     * Returns a lambda that given an input, it deploys a verticle, does the computation, replies with the response and
     * finally undeploys the verticle.The address is generated according to the following formula: spawned.prefix.n,
     * where prefix is the specified one, and n an internal counter. Examples: spawned.get_client.1,
     * spawned.get_client.2 etc
     *
     * @param lambda  the lambda that takes a message of type I and the context of the computation and produces an
     *                output of type O
     * @param <I>     the type of the message sent to the verticle
     * @param <O>     the type of the reply
     * @param address the prefix of the auto generated address
     * @return an VerticleRef wrapped in a Val
     */
    public <I, O> Lambdac<I, O> spawn(final String address,
                                      final Lambdac<I, O> lambda
                                     ) {
        return spawn(address,
                     lambda,
                     deploymentOptions
                    );
    }

    /**
     * Returns a lambda that given an input, it deploys a verticle, does the computation, replies with the response and
     * finally undeploys the verticle.The address is generated according to the following formula: spawned.prefix.n,
     * where prefix is the specified one, and n an internal counter. Examples: spawned.get_client.1,
     * spawned.get_client.2 etc
     *
     * @param lambda  the lambda that takes a message of type I and the context of the computation and produces an
     *                output of type O
     * @param <I>     the type of the message sent to the verticle
     * @param <O>     the type of the reply
     * @param address the prefix of the auto generated address
     * @param options the deployment options
     * @return an VerticleRef wrapped in a Val
     */
    public <I, O> Lambdac<I, O> spawn(final String address,
                                      final Lambdac<I, O> lambda,
                                      final DeploymentOptions options
                                     ) {

        requireNonNull(address);
        requireNonNull(options);
        requireNonNull(lambda);

        return (context, input) ->
        {
            String generatedAddress = generateProcessAddress(address);

            Consumer<Message<I>> consumer = message -> wrapLambda(generatedAddress,
                                                                  message,
                                                                  lambda
                                                                 );
            VIO<VerticleRef<I, O>> future = deployConsumer(generatedAddress,
                                                           consumer,
                                                           options
                                                          );

            return future.then(r -> r.trace()
                                     .apply(context,
                                            input
                                           )
                                     .onComplete(__ -> r.undeploy()
                                                        .onComplete(event -> {
                                                            if (event.succeeded())
                                                                EventPublisher.PUBLISHER.publishVerticleUndeployed(generatedAddress)
                                                                                        .accept(vertx);
                                                            else
                                                                EventPublisher.PUBLISHER.publishException(Event.EXCEPTION_UNDEPLOYING_VERTICLE,
                                                                                                          generatedAddress,
                                                                                                          event.cause()
                                                                                                         )
                                                                                        .accept(vertx);
                                                        })
                                                )
                              );
        };


    }

    /**
     * Returns a lambda that given an input, it deploys a verticle, does the computation, replies with the response and
     * finally undeploys the verticle.The address is generated according to the following formula: spawned.prefix.n,
     * where prefix is the specified one, and n an internal counter. Examples: spawned.get_client.1,
     * spawned.get_client.2 etc
     *
     * @param lambda  the function that takes a message of type I and produces an output of type O
     * @param <I>     the type of the message sent to the verticle
     * @param <O>     the type of the reply
     * @param address the prefix of the auto generated address
     * @param options the deployment options
     * @return an VerticleRef wrapped in a Val
     */
    public <I, O> Lambda<I, O> spawn(final String address,
                                     final Lambda<I, O> lambda,
                                     final DeploymentOptions options
                                    ) {

        requireNonNull(address);
        requireNonNull(options);
        requireNonNull(lambda);

        return input ->
        {
            String generatedAddress = generateProcessAddress(address);
            Consumer<Message<I>> consumer = message -> wrapLambda(generatedAddress,
                                                                  message,
                                                                  lambda
                                                                 );
            VIO<VerticleRef<I, O>> future = deployConsumer(generatedAddress,
                                                           consumer,
                                                           options
                                                          );

            return future.then(r -> r.ask()
                                     .apply(input)
                                     .onComplete(__ -> r.undeploy()
                                                        .onComplete(event -> {
                                                            if (event.succeeded())
                                                                EventPublisher.PUBLISHER.publishVerticleUndeployed(generatedAddress)
                                                                                        .accept(vertx);
                                                            else
                                                                EventPublisher.PUBLISHER.publishException(Event.EXCEPTION_UNDEPLOYING_VERTICLE,
                                                                                                          generatedAddress,
                                                                                                          event.cause()
                                                                                                         )
                                                                                        .accept(vertx);
                                                        })
                                                )
                              );
        };


    }

    /**
     * returns a val that, when executes, deploys the given verticle and returns the deployment id
     *
     * @param verticle the verticle to be deployed
     * @param options  the deployment options
     * @return the deployment id wrapped in a val
     */
    public VIO<String> deployVerticle(final AbstractVerticle verticle,
                                      final DeploymentOptions options
                                     ) {
        Objects.requireNonNull(verticle);
        Objects.requireNonNull(options);

        return VIO.effect(() -> vertx.deployVerticle(verticle,
                                                     options
                                                    )
                                     .onComplete(event -> {
                                                     if (event.succeeded())
                                                         EventPublisher.PUBLISHER.publishVerticleDeployed(verticle.getClass(),
                                                                                                          event.result()
                                                                                                         )
                                                                                 .accept(vertx);
                                                     else
                                                         EventPublisher.PUBLISHER.publishException(Event.EXCEPTION_DEPLOYING_VERTICLE,
                                                                                                   verticle.getClass(),
                                                                                                   event.cause()
                                                                                                  )
                                                                                 .accept(vertx);
                                                 }
                                                )
                         );
    }

    /**
     * returns a val that, when executes, deploys the given verticle and returns the deployment id
     *
     * @param verticle the verticle to be deployed
     * @return the deployment id wrapped in a val
     */
    public VIO<String> deployVerticle(final AbstractVerticle verticle) {
        return deployVerticle(verticle,
                              deploymentOptions
                             );
    }

    /**
     * returns a consumer that takes messages of type O and published them (broadcasting) to the given address
     *
     * @param address the address where messages are published
     * @param <O>     the type of the messages published
     * @return a consumer that takes messages and publishes them
     */
    public <O> Consumer<O> registerPublisher(final String address) {
        requireNonNull(address);
        if (Objects.equals(address,
                           EVENTS_ADDRESS
                          ))
            return message -> vertx.eventBus()
                                   .publish(address,
                                            message
                                           );
        else
            return message -> {
                EventPublisher.PUBLISHER.publishMessageSent(address,
                                                            message
                                                           )
                                        .accept(vertx);
                vertx.eventBus()
                     .publish(address,
                              message
                             );
            };

    }

    /**
     * register the given consumer to listen to the messages published to the given address. If the consumer can't keep
     * up with the publishers, messages will be buffered and eventually discarded. The size of the buffer can be
     * configured with the returned {@link MessageConsumer}
     *
     * @param address  the address from which the messages are consumed
     * @param consumer the consumer that processes the messages
     * @param <O>      the type of the messages
     * @return a MessageConsumer
     * @see MessageConsumer to see the size of the buffer and other methods
     */
    public <O> MessageConsumer<O> registerConsumer(final String address,
                                                   final Consumer<O> consumer
                                                  ) {

        requireNonNull(address);
        requireNonNull(consumer);

        if (!Objects.equals(address, EVENTS_ADDRESS)) {
            return vertx
                    .eventBus()
                    .consumer(address,
                              message -> {
                                  try {
                                      EventPublisher.PUBLISHER.publishMessageReceived(address, message.headers())
                                                              .accept(vertx);
                                      O body = message.body();
                                      consumer.accept(body);
                                      message.reply(null);
                                  } catch (Exception exc) {
                                      EventPublisher.PUBLISHER.publishException(
                                              Event.EXCEPTION_PROCESSING_MESSAGE,
                                              address,
                                              exc,
                                              message.headers()
                                                                               ).accept(vertx);
                                      message.reply(new ReplyException(RECIPIENT_FAILURE,
                                                                       INTERNAL_ERROR_CODE,
                                                                       Functions.getErrorMessage(exc)
                                      ));
                                  }
                              }
                             );
        } else return vertx.eventBus().consumer(address, message -> {
                                                    try {
                                                        O body = message.body();
                                                        consumer.accept(body);
                                                        message.reply(null);
                                                    } catch (Exception exc) {
                                                        message.reply(new ReplyException(RECIPIENT_FAILURE,
                                                                                         INTERNAL_ERROR_CODE,
                                                                                         Functions.getErrorMessage(exc)
                                                        ));
                                                    }
                                                }
                                               );
    }

    /**
     * returns a Delay, which a lazy value that when executed, will wait asynchronously for the specified amount of
     * time
     *
     * @param duration the amount of time
     * @return a Delay
     */
    public Delay delay(final Duration duration) {
        return new Delay(duration,
                         VIO.effect(() -> {
                             EventPublisher.PUBLISHER.publishTimerStarted().accept(vertx);
                             Promise<Long> promise = Promise.promise();
                             vertx.setTimer(
                                     duration.toMillis(),
                                     id -> {
                                         promise.complete(id);
                                         EventPublisher.PUBLISHER.publishTimerEnded().accept(vertx);
                                     }
                                           );
                             return promise.future();
                         })
        );
    }

    @SuppressWarnings("ReturnValueIgnored")
    private <I, O> void wrapLambda(final String address,
                                   final Message<I> message,
                                   final Lambda<I, O> fn
                                  ) {
        MultiMap headers = message.headers();
        try {
            EventPublisher.PUBLISHER.publishMessageReceived(address,
                                                            headers
                                                           )
                                    .accept(vertx);
            fn.apply(message.body())
              .onComplete(event -> {
                  if (event.succeeded()) {
                      message.reply(event.result(),
                                    createDeliveryOpt(headers)
                                   );
                      EventPublisher.PUBLISHER.publishMessageReplied(address,
                                                                     event.result(),
                                                                     headers
                                                                    )
                                              .accept(vertx);
                  } else {

                      ReplyException error = Failures.REPLY_EXCEPTION_PRISM
                              .getOptional.apply(event.cause())
                                          .orElse(new ReplyException(RECIPIENT_FAILURE,
                                                                     UNKNOWN_ERROR_CODE,
                                                                     Functions.getErrorMessage(event.cause())
                                          ));
                      message.reply(error,
                                    createDeliveryOpt(headers)
                                   );
                      EventPublisher.PUBLISHER.publishFailureReplied(address,
                                                                     error,
                                                                     headers
                                                                    )
                                              .accept(vertx);
                  }


              })
              .get();
        } catch (Exception exc) {
            message.reply(new ReplyException(RECIPIENT_FAILURE,
                                             INTERNAL_ERROR_CODE,
                                             Functions.getErrorMessage(exc)
                          ),
                          createDeliveryOpt(headers)
                         );
            EventPublisher.PUBLISHER.publishException(Event.EXCEPTION_PROCESSING_MESSAGE,
                                                      address,
                                                      exc,
                                                      headers
                                                     )
                                    .accept(vertx);
        }
    }

    @SuppressWarnings("ReturnValueIgnored")
    private <I, O> void wrapLambda(final String address,
                                   final Message<I> message,
                                   final Lambdac<I, O> fn
                                  ) {
        MultiMap headers = message.headers();
        try {
            EventPublisher.PUBLISHER.publishMessageReceived(address,
                                                            message.headers()
                                                           )
                                    .accept(vertx);

            fn.apply(headers,
                     message.body()
                    )
              .onComplete(event -> {
                  if (event.succeeded()) {
                      message.reply(event.result(),
                                    createDeliveryOpt(headers)
                                   );
                      EventPublisher.PUBLISHER.publishMessageReplied(address,
                                                                     event.result(),
                                                                     headers
                                                                    )
                                              .accept(vertx);
                  } else {

                      ReplyException error = Failures
                              .REPLY_EXCEPTION_PRISM
                              .getOptional.apply(event.cause())
                                          .orElse(new ReplyException(RECIPIENT_FAILURE,
                                                                     UNKNOWN_ERROR_CODE,
                                                                     Functions.getErrorMessage(event.cause())
                                          ));
                      message.reply(error,
                                    createDeliveryOpt(headers)
                                   );
                      EventPublisher.PUBLISHER.publishFailureReplied(address,
                                                                     error,
                                                                     headers
                                                                    )
                                              .accept(vertx);
                  }


              })
              .get();

        } catch (Exception exc) {
            message.reply(new ReplyException(RECIPIENT_FAILURE,
                                             INTERNAL_ERROR_CODE,
                                             Functions.getErrorMessage(exc)
                          ),
                          createDeliveryOpt(headers)
                         );
            EventPublisher.PUBLISHER.publishException(Event.EXCEPTION_PROCESSING_MESSAGE,
                                                      address,
                                                      exc,
                                                      message.headers()

                                                     )
                                    .accept(vertx);
        }
    }


}
