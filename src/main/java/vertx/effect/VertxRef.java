package vertx.effect;

import io.vertx.core.*;
import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.eventbus.Message;
import io.vertx.core.eventbus.MessageConsumer;
import io.vertx.core.eventbus.ReplyException;
import io.vertx.ext.shell.ShellService;
import io.vertx.ext.shell.ShellServiceOptions;
import vertx.effect.core.EventPublisher;
import vertx.effect.core.Functions;
import vertx.effect.core.MyVerticle;
import vertx.effect.exp.Cons;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;
import java.util.function.Function;

import static io.vertx.core.eventbus.ReplyFailure.RECIPIENT_FAILURE;
import static java.util.Objects.requireNonNull;
import static vertx.effect.Failures.INTERNAL_ERROR_CODE;
import static vertx.effect.Failures.UNKNOWN_ERROR_CODE;

/**
 Wrapper around the vertx instance. It deploys and spawns functions and tasks as Verticles.
 */
public class VertxRef {
    public static final String EVENTS_ADDRESS = "vertx-values-events";
    private static final DeploymentOptions DEFAULT_OPTIONS = new DeploymentOptions();
    private static final AtomicLong processSeq = new AtomicLong(0);
    private final Vertx vertx;
    private final DeploymentOptions deploymentOptions;
    private static final Function<MultiMap, DeliveryOptions> deliveryOpt =
            multimap -> new DeliveryOptions().setHeaders(multimap);
    private static final String ADDRESS_IS_NULL = "address is null";
    private static final String CONSUMER_IS_NULL = "consumer is null";
    private static final String OPTIONS_IS_NULL = "options is null";
    private static final String LAMBDA_IS_NULL = "λ is null";
    private static final String LAMBDAC_IS_NULL = "λc is null";
    private static final String VERTICLE_IS_NULL = "verticle is null";

    /**
     Creates a factory to deploy and spawn verticles

     @param vertx the vertx instance
     */
    public VertxRef(final Vertx vertx) {
        this(requireNonNull(vertx),
             DEFAULT_OPTIONS
            );
    }

    /**
     Creates a factory to deploy and spawn verticles

     @param vertx             the vertx instance
     @param deploymentOptions the default deployment options that will be used for deploying and spawning
     verticles if one is not provided
     */
    public VertxRef(final Vertx vertx,
                    final DeploymentOptions deploymentOptions
                   ) {
        this.vertx = requireNonNull(vertx);
        this.deploymentOptions = requireNonNull(deploymentOptions);
    }


    /**
     @param address  the address of the verticle
     @param consumer the consumer that will process the messages sent to the verticle
     @param <I>      the type of the message sent to the verticle
     @param <O>      the type of the reply
     @return an VerticleRef wrapped in a val
     */
    public <I, O> Val<VerticleRef<I, O>> deployConsumer(final String address,
                                                        final Consumer<Message<I>> consumer
                                                       ) {
        if (address == null) return Cons.failure(new NullPointerException(ADDRESS_IS_NULL));
        if (consumer == null) return Cons.failure(new NullPointerException(CONSUMER_IS_NULL));

        return deployConsumer(address,
                              consumer,
                              deploymentOptions
                             );
    }

    /**
     @param address  the address of the verticle
     @param consumer the consumer that will process the messages sent to the verticle
     @param options  options for configuring the verticle deployment
     @param <I>      the type of the message sent to the verticle
     @param <O>      the type of the reply
     @return an VerticleRef wrapped in a val
     */
    public <I, O> Val<VerticleRef<I, O>> deployConsumer(final String address,
                                                        final Consumer<Message<I>> consumer,
                                                        final DeploymentOptions options
                                                       ) {
        if (address == null) return Cons.failure(new NullPointerException(ADDRESS_IS_NULL));
        if (consumer == null) return Cons.failure(new NullPointerException(CONSUMER_IS_NULL));
        if (options == null) return Cons.failure(new NullPointerException(OPTIONS_IS_NULL));
        final int                                                         instances = options.getInstances();
        final Set<String>                                                 ids       = new HashSet<>();
        @SuppressWarnings({"rawtypes", "squid:S3740"}) final List<Future> futures   = new ArrayList<>();
        final MyVerticle<I> verticle = new MyVerticle<>(consumer,
                                                        address
        );

        return Cons.of(() -> {
                           for (int i = 0; i < instances; i++) {
                               final Future<String> future = vertx.deployVerticle(verticle,
                                                                                  options.setInstances(1)
                                                                                 );
                               futures.add(future.onSuccess(id -> {
                                                                ids.add(id);
                                                                EventPublisher.PUBLISHER.deployedVerticle(address,
                                                                                                          id
                                                                                                         )
                                                                                        .accept(vertx);
                                                            }
                                                           ));
                           }


                           return CompositeFuture.all(futures)
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
     @param address the address of the verticle
     @param lambda  the function that takes a message of type I and produces an output of type O
     @param <I>     the type of the message sent to the verticle
     @param <O>     the type of the reply
     @return an VerticleRef wrapped in a val
     */
    public <I, O> Val<VerticleRef<I, O>> deploy(final String address,
                                                final λ<I, O> lambda
                                               ) {
        if (address == null) return Cons.failure(new NullPointerException(ADDRESS_IS_NULL));
        if (lambda == null) return Cons.failure(new NullPointerException(LAMBDA_IS_NULL));
        return deploy(address,
                      lambda,
                      deploymentOptions
                     );
    }

    /**
     @param address the address of the verticle
     @param lambda  the function that takes a message of type I and produces an output of type O
     @param options options for configuring the verticle deployment
     @param <I>     the type of the message sent to the verticle
     @param <O>     the type of the reply
     @return an VerticleRef wrapped in a Val
     */
    public <I, O> Val<VerticleRef<I, O>> deploy(final String address,
                                                final λ<I, O> lambda,
                                                final DeploymentOptions options
                                               ) {
        if (address == null) return Cons.failure(new NullPointerException(ADDRESS_IS_NULL));
        if (lambda == null) return Cons.failure(new NullPointerException(LAMBDA_IS_NULL));
        if (options == null) return Cons.failure(new NullPointerException(OPTIONS_IS_NULL));
        return Cons.of(() -> {
                           final int                                                         instances = options.getInstances();
                           final Set<String>                                                 ids       = new HashSet<>();
                           @SuppressWarnings({"rawtypes", "squid:S3740"}) final List<Future> futures   = new ArrayList<>();
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
                                                                EventPublisher.PUBLISHER.deployedVerticle(address,
                                                                                                          id
                                                                                                         )
                                                                                        .accept(vertx);
                                                            }
                                                           ));
                           }
                           return CompositeFuture.all(futures)
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
     @param address the address of the verticle
     @param lambda  the function that takes a message of type I and produces an output of type O
     @param <I>     the type of the message sent to the verticle
     @param <O>     the type of the reply
     @return an VerticleRef wrapped in a Val
     */
    public <I, O> Val<VerticleRef<I, O>> deploy(final String address,
                                                final λc<I, O> lambda
                                               ) {
        if (address == null) return Cons.failure(new NullPointerException(ADDRESS_IS_NULL));
        if (lambda == null) return Cons.failure(new NullPointerException(LAMBDA_IS_NULL));
        return deploy(address,
                      lambda,
                      deploymentOptions
                     );
    }

    /**
     @param address the address of the verticle
     @param lambda  the function that takes a message of type I and produces an output of type O
     @param options options for configuring the verticle deployment
     @param <I>     the type of the message sent to the verticle
     @param <O>     the type of the reply
     @return an VerticleRef wrapped in a Val
     */
    public <I, O> Val<VerticleRef<I, O>> deploy(final String address,
                                                final λc<I, O> lambda,
                                                final DeploymentOptions options
                                               ) {

        if (address == null) return Cons.failure(new NullPointerException(ADDRESS_IS_NULL));
        if (lambda == null) return Cons.failure(new NullPointerException(LAMBDAC_IS_NULL));
        if (options == null) return Cons.failure(new NullPointerException(OPTIONS_IS_NULL));

        return Cons.of(() -> {
                           final int                                                         instances = options.getInstances();
                           final Set<String>                                                 ids       = new HashSet<>();
                           @SuppressWarnings({"rawtypes", "squid:S3740"}) final List<Future> futures   = new ArrayList<>();
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
                                                                EventPublisher.PUBLISHER.deployedVerticle(address,
                                                                                                          id
                                                                                                         )
                                                                                        .accept(vertx);
                                                            }
                                                           ));
                           }
                           return CompositeFuture.all(futures)
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
     @param lambda  the function that takes a message of type I and produces an output of type O
     @param <I>     the type of the message sent to the verticle
     @param <O>     the type of the reply
     @param address the prefix of the auto generated address
     @return an VerticleRef wrapped in a Val
     */
    public <I, O> λ<I, O> spawn(final String address,
                                final λ<I, O> lambda) {
        return spawn(address,
                     lambda,
                     deploymentOptions
                    );
    }


    /**
     @param lambda  the function that takes a message of type I and produces an output of type O
     @param <I>     the type of the message sent to the verticle
     @param <O>     the type of the reply
     @param address the prefix of the auto generated address
     @return an VerticleRef wrapped in a Val
     */
    public <I, O> λc<I, O> spawn(final String address,
                                 final λc<I, O> lambda) {
        return spawn(address,
                     lambda,
                     deploymentOptions
                    );
    }


    /**
     @param lambda  the function that takes a message of type I and produces an output of type O
     @param <I>     the type of the message sent to the verticle
     @param <O>     the type of the reply
     @param address the prefix of the auto generated address
     @param options the deployment options
     @return an VerticleRef wrapped in a Val
     */
    public <I, O> λc<I, O> spawn(final String address,
                                 final λc<I, O> lambda,
                                 final DeploymentOptions options) {

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
            Val<VerticleRef<I, O>> future = deployConsumer(generatedAddress,
                                                           consumer,
                                                           options
                                                          );

            return future.flatMap(r -> r.trace()
                                        .apply(context,
                                               input
                                              )
                                        .onComplete(__ -> r.undeploy()
                                                           .onComplete(event -> {
                                                               if (event.succeeded())
                                                                   EventPublisher.PUBLISHER.undeployedVerticle(generatedAddress)
                                                                                           .accept(vertx);
                                                               else
                                                                   EventPublisher.PUBLISHER.internalError(Event.INTERNAL_ERROR_UNDEPLOYING_VERTICLE,
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
     @param lambda  the function that takes a message of type I and produces an output of type O
     @param <I>     the type of the message sent to the verticle
     @param <O>     the type of the reply
     @param address the prefix of the auto generated address
     @param options the deployment options
     @return an VerticleRef wrapped in a Val
     */
    public <I, O> λ<I, O> spawn(final String address,
                                final λ<I, O> lambda,
                                final DeploymentOptions options) {

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
            Val<VerticleRef<I, O>> future = deployConsumer(generatedAddress,
                                                           consumer,
                                                           options
                                                          );

            return future.flatMap(r -> r.ask()
                                        .apply(input)
                                        .onComplete(__ -> r.undeploy()
                                                           .onComplete(event -> {
                                                               if (event.succeeded())
                                                                   EventPublisher.PUBLISHER.undeployedVerticle(generatedAddress)
                                                                                           .accept(vertx);
                                                               else
                                                                   EventPublisher.PUBLISHER.internalError(Event.INTERNAL_ERROR_UNDEPLOYING_VERTICLE,
                                                                                                          generatedAddress,
                                                                                                          event.cause()
                                                                                                         )
                                                                                           .accept(vertx);
                                                           })
                                                   )
                                 );
        };


    }


    public Val<String> deployVerticle(final AbstractVerticle verticle,
                                      final DeploymentOptions options) {
        if (verticle == null) return Cons.failure(new NullPointerException(VERTICLE_IS_NULL));
        if (options == null) return Cons.failure(new NullPointerException(OPTIONS_IS_NULL));

        return Cons.of(() -> vertx.deployVerticle(verticle,
                                                  options
                                                 )
                                  .onComplete(event -> {
                                                  if (event.succeeded())
                                                      EventPublisher.PUBLISHER.deployedVerticle(verticle.getClass(),
                                                                                                event.result()
                                                                                               )
                                                                              .accept(vertx);
                                                  else
                                                      EventPublisher.PUBLISHER.internalError(Event.INTERNAL_ERROR_DEPLOYING_VERTICLE,
                                                                                             verticle.getClass(),
                                                                                             event.cause()
                                                                                            )
                                                                              .accept(vertx);
                                              }
                                             )
                      );
    }

    public Val<String> deployVerticle(final AbstractVerticle verticle) {
        return deployVerticle(verticle,
                              deploymentOptions
                             );
    }


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
                EventPublisher.PUBLISHER.sentMessage(address,
                                                     message
                                                    )
                                        .accept(vertx);
                vertx.eventBus()
                     .publish(address,
                              message
                             );
            };

    }


    public <O> MessageConsumer<O> registerConsumer(final String address,
                                                   final Consumer<O> consumer) {

        requireNonNull(address);
        requireNonNull(consumer);

        if (!Objects.equals(address,
                            EVENTS_ADDRESS
                           )) {
            return vertx.eventBus()
                        .consumer(address,
                                  message -> {
                                      try {
                                          EventPublisher.PUBLISHER.receivedMessage(address,
                                                                                   message.headers()
                                                                                  )
                                                                  .accept(vertx);

                                          O body = message.body();
                                          consumer.accept(body);
                                          message.reply(null);
                                      } catch (Exception exc) {
                                          EventPublisher.PUBLISHER.internalError(Event.INTERNAL_ERROR_PROCESSING_MESSAGE,
                                                                                 address,
                                                                                 exc,
                                                                                 message.headers()
                                                                                )
                                                                  .accept(vertx);
                                          message.reply(new ReplyException(RECIPIENT_FAILURE,
                                                                           INTERNAL_ERROR_CODE,
                                                                           Functions.getErrorMessage(exc)
                                          ));
                                      }
                                  }
                                 );
        }
        else return vertx.eventBus()
                         .consumer(address,
                                   message -> {
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


    public Val<Void> delay(final int time,
                           final TimeUnit unit) {
        if (time < 0) return Cons.failure(new IllegalArgumentException("time < 0"));
        if (unit == null) return Cons.failure(new NullPointerException("unit is null"));
        return Cons.of(() -> {
            EventPublisher.PUBLISHER.timer(Event.TIMER_STARTS_EVENT)
                                    .accept(vertx);
            Promise<Void> promise = Promise.promise();
            vertx.setTimer(requireNonNull(unit).toMillis(time),
                           result -> {
                               promise.complete(null);
                               EventPublisher.PUBLISHER.timer(Event.TIMER_ENDS_EVENT)
                                                       .accept(vertx);
                           }
                          );
            return promise.future();
        });
    }


    private static String generateProcessAddress(final String address) {
        return String.format("spawned.%s.%s",
                             address,
                             processSeq.getAndIncrement()
                            );
    }


    private <I, O> void wrapLambda(final String address,
                                   final Message<I> message,
                                   final λ<I, O> fn) {
        MultiMap headers = message.headers();
        try {
            EventPublisher.PUBLISHER.receivedMessage(address,
                                                     headers
                                                    )
                                    .accept(vertx);
            fn.apply(message.body())
              .onComplete(event -> {
                  if (event.succeeded()) {
                      message.reply(event.result(),
                                    deliveryOpt.apply(headers)
                                   );
                      EventPublisher.PUBLISHER.repliedResp(address,
                                                           event.result(),
                                                           headers
                                                          )
                                              .accept(vertx);
                  }
                  else {

                      ReplyException error = Failures.REPLY_EXCEPTION_PRISM
                              .getOptional.apply(event.cause())
                                          .orElse(new ReplyException(RECIPIENT_FAILURE,
                                                                     UNKNOWN_ERROR_CODE,
                                                                     Functions.getErrorMessage(event.cause())
                                          ));
                      message.reply(error,
                                    deliveryOpt.apply(headers)
                                   );
                      EventPublisher.PUBLISHER.repliedError(address,
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
                          deliveryOpt.apply(headers)
                         );
            EventPublisher.PUBLISHER.internalError(Event.INTERNAL_ERROR_PROCESSING_MESSAGE,
                                                   address,
                                                   exc,
                                                   headers
                                                  )
                                    .accept(vertx);
        }
    }

    private <I, O> void wrapLambda(final String address,
                                   final Message<I> message,
                                   final λc<I, O> fn) {
        MultiMap headers = message.headers();
        try {
            EventPublisher.PUBLISHER.receivedMessage(address,
                                                     message.headers()
                                                    )
                                    .accept(vertx);

            fn.apply(headers,
                     message.body()
                    )
              .onComplete(event -> {
                  if (event.succeeded()) {
                      message.reply(event.result(),
                                    deliveryOpt.apply(headers)
                                   );
                      EventPublisher.PUBLISHER.repliedResp(address,
                                                           event.result(),
                                                           headers
                                                          )
                                              .accept(vertx);
                  }
                  else {

                      ReplyException error = Failures
                              .REPLY_EXCEPTION_PRISM
                              .getOptional.apply(event.cause())
                                          .orElse(new ReplyException(RECIPIENT_FAILURE,
                                                                     UNKNOWN_ERROR_CODE,
                                                                     Functions.getErrorMessage(event.cause())
                                          ));
                      message.reply(error,
                                    deliveryOpt.apply(headers)
                                   );
                      EventPublisher.PUBLISHER.repliedError(address,
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
                          deliveryOpt.apply(headers)
                         );
            EventPublisher.PUBLISHER.internalError(Event.INTERNAL_ERROR_PROCESSING_MESSAGE,
                                                   address,
                                                   exc,
                                                   message.headers()

                                                  )
                                    .accept(vertx);
        }
    }

    public Val<ShellService> startShellService(final ShellServiceOptions options) {
        requireNonNull(options);
        return Cons.of(() -> {
            ShellService service = ShellService.create(vertx,
                                                       options
                                                      );
            return service.start()
                          .map(it -> service)
                          .onComplete(event -> {
                              if (event.succeeded()) {
                                  EventPublisher.PUBLISHER.startedShell()
                                                          .accept(vertx);
                              }
                              else {
                                  EventPublisher.PUBLISHER.internalError(Event.INTERNAL_ERROR_STARTING_SHELL_SERVICE,
                                                                         event.cause()
                                                                        )
                                                          .accept(vertx);
                              }
                          });

        });
    }


}
