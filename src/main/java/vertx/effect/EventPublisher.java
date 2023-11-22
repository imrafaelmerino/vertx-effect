package vertx.effect;

import io.vertx.core.MultiMap;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.ReplyException;
import jsonvalues.*;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.Instant;
import java.util.Arrays;
import java.util.Optional;
import java.util.function.Consumer;

import static jsonvalues.JsNothing.NOTHING;
import static vertx.effect.Event.*;
import static vertx.effect.Failures.REPLY_EXCEPTION_PRISM;
import static vertx.effect.Functions.headers2JsObj;

class EventPublisher {
    public static final EventPublisher PUBLISHER =
            new EventPublisher(Boolean.parseBoolean(System.getProperty("vertx.effect.enable.log.events", "true")));
    protected final boolean enabled;

    private EventPublisher(boolean enabled) {
        this.enabled = enabled;
    }

    public Consumer<Vertx> publishFailureReceived(final String from,
                                                  final Throwable exc,
                                                  final MultiMap context
                                                 ) {
        return vertx -> {
            if (enabled) {
                Optional<ReplyException> opt = REPLY_EXCEPTION_PRISM.getOptional.apply(exc);

                opt.ifPresentOrElse(error -> vertx.eventBus()
                                                  .publish(VertxRef.EVENTS_ADDRESS,
                                                           eventLens.set.apply(RECEIVED_FAILURE_EVENT)
                                                                        .andThen(fromOpt.set.apply(from))
                                                                        .andThen(contextLens.set.apply(context.isEmpty() ? NOTHING : headers2JsObj.apply(context)))
                                                                        .andThen(failureTypeLens.set.apply(error.failureType().name()))
                                                                        .andThen(failureCodeLens.set.apply(error.failureCode()))
                                                                        .andThen(failureMessageLens.set.apply(error.getMessage()))
                                                                        .andThen(Event.instantLens.set.apply(Instant.now()))
                                                                        .andThen(Event.threadNameLens.set.apply(Thread.currentThread().getName()))
                                                                        .apply(JsObj.empty())

                                                          ),
                                    () -> vertx
                                            .eventBus()
                                            .publish(VertxRef.EVENTS_ADDRESS,
                                                     eventLens.set.apply(RECEIVED_FAILURE_EVENT)
                                                                  .andThen(fromOpt.set.apply(from))
                                                                  .andThen(contextLens.set.apply(context.isEmpty() ? NOTHING : headers2JsObj.apply(context)))
                                                                  .andThen(exceptionOpt.set.apply(exc.getClass().getCanonicalName()))
                                                                  .andThen(exceptionMessageOpt.set.apply(exc.getMessage()))
                                                                  .andThen(exceptionStackOpt.set.apply(Arrays.toString(exc.getStackTrace())))
                                                                  .andThen(Event.instantLens.set.apply(Instant.now()))
                                                                  .andThen(Event.threadNameLens.set.apply(Thread.currentThread().getName()))
                                                                  .apply(JsObj.empty())

                                                    )
                                   );
            }


        };
    }


    public Consumer<Vertx> publishFailureReceived(final String from,
                                                  final Throwable exc
                                                 ) {
        return vertx -> {
            if (enabled) {
                Optional<ReplyException> opt = REPLY_EXCEPTION_PRISM.getOptional.apply(exc);
                opt.ifPresentOrElse(error -> vertx
                                            .eventBus()
                                            .publish(VertxRef.EVENTS_ADDRESS,
                                                     eventLens.set.apply(RECEIVED_FAILURE_EVENT)
                                                                  .andThen(fromOpt.set.apply(from))
                                                                  .andThen(failureTypeLens.set.apply(error.failureType().name()))
                                                                  .andThen(failureCodeLens.set.apply(error.failureCode()))
                                                                  .andThen(failureMessageLens.set.apply(error.getMessage()))
                                                                  .andThen(Event.instantLens.set.apply(Instant.now()))
                                                                  .andThen(Event.threadNameLens.set.apply(Thread.currentThread().getName()))
                                                                  .apply(JsObj.empty())

                                                    ),
                                    () -> vertx
                                            .eventBus()
                                            .publish(VertxRef.EVENTS_ADDRESS,
                                                     eventLens.set.apply(RECEIVED_FAILURE_EVENT)
                                                                  .andThen(fromOpt.set.apply(from))
                                                                  .andThen(exceptionOpt.set.apply(exc.getClass().getCanonicalName()))
                                                                  .andThen(exceptionMessageOpt.set.apply(exc.getMessage()))
                                                                  .andThen(exceptionStackOpt.set.apply(Arrays.toString(exc.getStackTrace())))
                                                                  .andThen(Event.instantLens.set.apply(Instant.now()))
                                                                  .andThen(Event.threadNameLens.set.apply(Thread.currentThread().getName()))
                                                                  .apply(JsObj.empty())
                                                    )
                                   );
            }

        };

    }

    public Consumer<Vertx> publishException(final String event,
                                            final String address,
                                            final Throwable exc,
                                            final MultiMap context
                                           ) {
        return vertx -> {
            if (enabled) vertx
                    .eventBus()
                    .publish(VertxRef.EVENTS_ADDRESS,
                             eventLens.set.apply(event)
                                          .andThen(addressOpt.set.apply(address))
                                          .andThen(contextLens.set.apply(context.isEmpty() ? NOTHING : headers2JsObj.apply(context)))
                                          .andThen(exceptionOpt.set.apply(exc.getClass().getCanonicalName()))
                                          .andThen(exceptionMessageOpt.set.apply(exc.getMessage()))
                                          .andThen(exceptionStackOpt.set.apply(Arrays.toString(exc.getStackTrace())))
                                          .andThen(Event.instantLens.set.apply(Instant.now()))
                                          .andThen(Event.threadNameLens.set.apply(Thread.currentThread().getName()))
                                          .apply(JsObj.empty())

                            );
        };
    }

    public Consumer<Vertx> publishException(final String event,
                                            final String address,
                                            final Throwable exception
                                           ) {
        return vertx -> {
            if (enabled) vertx
                    .eventBus()
                    .publish(VertxRef.EVENTS_ADDRESS,
                             eventLens.set.apply(event)
                                          .andThen(addressOpt.set.apply(address))
                                          .andThen(Event.exceptionOpt.set.apply(exception.getClass().getCanonicalName()))
                                          .andThen(exceptionMessageOpt.set.apply(exception.getMessage()))
                                          .andThen(exceptionStackOpt.set.apply(Arrays.toString(exception.getStackTrace())))
                                          .andThen(Event.instantLens.set.apply(Instant.now()))
                                          .andThen(Event.threadNameLens.set.apply(Thread.currentThread().getName()))
                                          .apply(JsObj.empty())

                            );
        };
    }


    public Consumer<Vertx> publishException(final String event,
                                            final Class<?> verticle,
                                            final Throwable exception
                                           ) {
        return vertx -> {
            if (enabled) vertx
                    .eventBus()
                    .publish(VertxRef.EVENTS_ADDRESS,
                             eventLens.set.apply(event)
                                          .andThen(classOpt.set.apply(verticle.getCanonicalName()))
                                          .andThen(Event.exceptionOpt.set.apply(exception.getClass()
                                                                                         .getCanonicalName()))
                                          .andThen(exceptionMessageOpt.set.apply(exception.getMessage()))
                                          .andThen(exceptionStackOpt.set.apply(Arrays.toString(exception.getStackTrace())))
                                          .andThen(Event.instantLens.set.apply(Instant.now()
                                                                              )
                                                  )
                                          .andThen(Event.threadNameLens.set.apply(Thread.currentThread()
                                                                                        .getName()
                                                                                 )
                                                  )
                                          .apply(JsObj.empty())

                            );
        };
    }



    public Consumer<Vertx> publishMessageReceived(final String address,
                                                  final MultiMap headers
                                                 ) {
        return vertx -> {
            if (enabled) {
                vertx
                        .eventBus()
                        .publish(VertxRef.EVENTS_ADDRESS,
                                 eventLens.set.apply(RECEIVED_MESSAGE_EVENT)
                                              .andThen(addressOpt.set.apply(address))
                                              .andThen(contextLens.set.apply(headers.isEmpty() ? NOTHING : headers2JsObj.apply(headers)))
                                              .andThen(Event.instantLens.set.apply(Instant.now()))
                                              .andThen(Event.threadNameLens.set.apply(Thread.currentThread().getName()))
                                              .apply(JsObj.empty())
                                );
            }
        };
    }

    public Consumer<Vertx> publishResponseReceived(final String address,
                                                   final MultiMap headers
                                                  ) {
        return vertx -> {
            if (enabled) {
                vertx
                        .eventBus()
                        .publish(VertxRef.EVENTS_ADDRESS,
                                 eventLens.set.apply(RECEIVED_RESP_EVENT)
                                              .andThen(fromOpt.set.apply(address))
                                              .andThen(contextLens.set.apply(headers.isEmpty() ? NOTHING : headers2JsObj.apply(headers)))
                                              .andThen(Event.instantLens.set.apply(Instant.now()))
                                              .andThen(Event.threadNameLens.set.apply(Thread.currentThread().getName()))
                                              .apply(JsObj.empty())
                                );
            }
        };
    }


    public Consumer<Vertx> publishMessageSent(final String address,
                                              final Object message
                                             ) {
        return vertx -> {
            if (enabled) {
                vertx
                        .eventBus()
                        .publish(VertxRef.EVENTS_ADDRESS,
                                 eventLens.set.apply(SENT_MESSAGE_EVENT)
                                              .andThen(toOpt.set.apply(address))
                                              .andThen(messageLens.set.apply(toJsValue(message)))
                                              .andThen(Event.instantLens.set.apply(Instant.now()))
                                              .andThen(Event.threadNameLens.set.apply(Thread.currentThread().getName()))
                                              .apply(JsObj.empty())
                                );
            }
        };
    }

    public Consumer<Vertx> publishMessageSent(final String address,
                                              final Object message,
                                              final MultiMap headers
                                             ) {
        return vertx -> {
            if (enabled) {
                vertx
                        .eventBus()
                        .publish(VertxRef.EVENTS_ADDRESS,
                                 eventLens.set.apply(SENT_MESSAGE_EVENT)
                                              .andThen(toOpt.set.apply(address))
                                              .andThen(contextLens.set.apply(headers.isEmpty() ? NOTHING : headers2JsObj.apply(headers)))
                                              .andThen(messageLens.set.apply(toJsValue(message)))
                                              .andThen(Event.instantLens.set.apply(Instant.now()))
                                              .andThen(Event.threadNameLens.set.apply(Thread.currentThread()
                                                                                            .getName())
                                                      )
                                              .apply(JsObj.empty())
                                );
            }
        };
    }

    public Consumer<Vertx> publishMessageReplied(final String address,
                                                 final Object result,
                                                 final MultiMap context
                                                ) {
        return vertx -> {
            if (enabled) vertx
                    .eventBus()
                    .publish(VertxRef.EVENTS_ADDRESS,
                             eventLens.set.apply(REPLIED_RESP_EVENT)
                                          .andThen(addressOpt.set.apply(address))
                                          .andThen(contextLens.set.apply(context.isEmpty() ? NOTHING : headers2JsObj.apply(context)))
                                          .andThen(messageLens.set.apply(toJsValue(result)))
                                          .andThen(Event.instantLens.set.apply(Instant.now()))
                                          .andThen(Event.threadNameLens.set.apply(Thread.currentThread()
                                                                                        .getName())
                                                  )
                                          .apply(JsObj.empty())
                            );
        };
    }


    public Consumer<Vertx> publishFailureReplied(final String address,
                                                 final ReplyException exc,
                                                 final MultiMap context
                                                ) {
        return vertx -> {
            if (enabled) vertx.eventBus()
                              .publish(VertxRef.EVENTS_ADDRESS,
                                       eventLens.set.apply(REPLIED_FAILURE_EVENT)
                                                    .andThen(addressOpt.set.apply(address))
                                                    .andThen(contextLens.set.apply(context.isEmpty() ?
                                                                                           NOTHING : headers2JsObj.apply(context)))
                                                    .andThen(failureTypeLens.set.apply(exc.failureType()
                                                                                          .name()))
                                                    .andThen(failureCodeLens.set.apply(exc.failureCode()))
                                                    .andThen(failureMessageLens.set.apply(exc.getMessage()))
                                                    .andThen(Event.instantLens.set.apply(Instant.now()))
                                                    .andThen(Event.threadNameLens.set.apply(Thread.currentThread()
                                                                                                  .getName())
                                                            )
                                                    .apply(JsObj.empty())
                                      );
        };
    }


    public Consumer<Vertx> publishTimerStarted() {
        return timer(TIMER_STARTED);
    }

    public Consumer<Vertx> publishTimerEnded() {
        return timer(TIMER_ENDED);
    }


    Consumer<Vertx> timer(final String event) {
        return vertx -> {
            if (enabled) vertx
                    .eventBus()
                    .publish(VertxRef.EVENTS_ADDRESS,
                             Event.eventLens.set.apply(event)
                                                .andThen(Event.instantLens.set.apply(Instant.now()))
                                                .andThen(Event.threadNameLens.set.apply(Thread.currentThread()
                                                                                              .getName())
                                                        )
                                                .apply(JsObj.empty())
                            );
        };
    }

    public Consumer<Vertx> publishVerticleDeployed(final String address,
                                                   final String id
                                                  ) {
        return vertx -> {
            if (enabled) vertx
                    .eventBus()
                    .publish(VertxRef.EVENTS_ADDRESS,
                             eventLens.set.apply(DEPLOYED_VERTICLE)
                                          .andThen(addressOpt.set.apply(address))
                                          .andThen(instantLens.set.apply(Instant.now()))
                                          .andThen(Event.idOption.set.apply(id))
                                          .andThen(threadNameLens.set.apply(Thread.currentThread()
                                                                                  .getName())
                                                  )
                                          .apply(JsObj.empty())
                            );
        };
    }


    public Consumer<Vertx> publishVerticleDeployed(final Class<?> verticle,
                                                   final String id
                                                  ) {
        return vertx -> {
            if (enabled) vertx
                    .eventBus()
                    .publish(VertxRef.EVENTS_ADDRESS,
                             eventLens.set.apply(DEPLOYED_VERTICLE)
                                          .andThen(classOpt.set.apply(verticle.getCanonicalName()))
                                          .andThen(instantLens.set.apply(Instant.now()))
                                          .andThen(Event.idOption.set.apply(id))
                                          .andThen(threadNameLens.set.apply(Thread.currentThread()
                                                                                  .getName())
                                                  )
                                          .apply(JsObj.empty())
                            );
        };
    }

    public Consumer<Vertx> publishVerticleUndeployed(final String address) {
        return vertx -> {
            if (enabled) vertx
                    .eventBus()
                    .publish(VertxRef.EVENTS_ADDRESS,
                             eventLens.set.apply(UNDEPLOYED_VERTICLE)
                                          .andThen(addressOpt.set.apply(address))
                                          .andThen(instantLens.set.apply(Instant.now()))
                                          .andThen(threadNameLens.set.apply(Thread.currentThread()
                                                                                  .getName())
                                                  )
                                          .apply(JsObj.empty())
                            );
        };
    }

    private JsValue toJsValue(Object obj) {
        if (obj == null) return JsNull.NULL;
        if (obj instanceof String) return JsStr.of(((String) obj));
        if (obj instanceof Character) return JsStr.of(((Character) obj).toString());
        if (obj instanceof Byte) return JsInt.of(((Byte) obj));
        if (obj instanceof Short) return JsInt.of(((Short) obj));
        if (obj instanceof Integer) return JsInt.of(((Integer) obj));
        if (obj instanceof Long) return JsLong.of(((Long) obj));
        if (obj instanceof Float) return JsDouble.of(((Float) obj));
        if (obj instanceof Double) return JsDouble.of(((Double) obj));
        if (obj instanceof BigInteger) return JsBigInt.of(((BigInteger) obj));
        if (obj instanceof BigDecimal) return JsBigDec.of(((BigDecimal) obj));
        if (obj instanceof JsArray) return ((JsArray) obj);
        if (obj instanceof JsObj) return ((JsObj) obj);
        if (obj instanceof Boolean) return JsBool.of(((Boolean) obj));
        if (obj instanceof Instant) return JsInstant.of(((Instant) obj));
        if (obj instanceof byte[]) return JsBinary.of((byte[]) obj);
        return JsStr.of(obj.toString());
    }
}
