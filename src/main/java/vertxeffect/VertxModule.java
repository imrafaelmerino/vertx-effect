package vertxeffect;

import io.vavr.collection.Map;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Promise;
import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.eventbus.Message;
import vertxeffect.exp.*;

import java.util.function.Consumer;

import static java.util.Objects.requireNonNull;


/**
 A module it's a Verticle that when deployed exposes a set of functions that represents other Verticles. Verticles are deployed in the
 {@link VertxModule#deploy()} method and then the module functions are initialized in the
 {@link VertxModule#initialize()} method.
 */
public abstract class VertxModule extends AbstractVerticle {

    private static final DeploymentOptions DEFAULT_DEPLOYMENT_OPTIONS = new DeploymentOptions();
    protected final DeploymentOptions deploymentOptions;
    private SeqVal<String> idValSeq;
    private MapVal<VerticleRef<?, ?>> refValMap;
    private Map<String, VerticleRef<?, ?>> refMap;


    @SuppressWarnings({"rawtypes", "unchecked", "squid:S3740"})
    private VertxModule(final MapVal refExp,
                        final DeploymentOptions deploymentOptions) {
        this.refValMap = requireNonNull(refExp);
        this.deploymentOptions = requireNonNull(deploymentOptions);
        idValSeq = SeqVal.empty();
    }


    /**
     Creates an instance of this module

     @param options deployments options
     */
    public VertxModule(final DeploymentOptions options) {
        this(MapVal.EMPTY,
             requireNonNull(options)
            );
        idValSeq = SeqVal.empty();
    }

    /**
     Creates an instance of this module.
     <pre>{@code
    public class MyModule extends VertxModule{...}
    MyModule module = new MyModule();
    vertx.deployVerticle(module);
    }
     </pre>
     */
    public VertxModule() {
        this(MapVal.EMPTY,
             DEFAULT_DEPLOYMENT_OPTIONS
            );
        idValSeq = SeqVal.empty();

    }

    /**
     override this method to initialize the instance fields of this class that represent the functions exposed by
     this module. You can establish a dialogue with the Verticle using
     the method {@link VerticleRef#ask()}, which returns a function, or just send it messages without waiting for the
     response with the method {@link VerticleRef#tell()}, which returns a consumer. You may be interested in deploying
     Verticles on the fly to get a greater level of parallelism with the function {@link VertxRef#spawn(String, λ)}.
     */
    protected abstract void initialize();

    /**
     override this method and deploy the Verticles you want to be exposed by your module.
     using the functions {@link VertxModule#deploy(String, λ)} (String, Function)} or
     {@link VertxModule#deploy(String, Consumer)}.
     You can also deployed regular Verticles with the method {@link VertxModule#deploy(AbstractVerticle)}
     */
    protected abstract void deploy();

    /**
     Factory to deploy or spawn verticles
     */
    protected VertxRef vertxRef;

    @Override
    public void start(final Promise<Void> start) {

        try {
            vertxRef = new VertxRef(requireNonNull(vertx),
                                    deploymentOptions
            );
            deploy();
            Pair.of(idValSeq,
                    refValMap
                   )
                .onComplete(event -> {
                    if (event.failed()) {
                        start.fail(event.cause());
                    }
                    else {
                        try {
                            refMap = event.result()
                                          ._2();
                            initialize();
                            start.complete();
                        } catch (Exception e) {
                            start.fail(Failures.GET_EXCEPTION_DEPLOYING_MODULE.apply(e));
                        }
                    }
                })
                .get();

        } catch (Exception e) {
            start.fail(Failures.GET_EXCEPTION_DEPLOYING_MODULE.apply(e));
        }
    }

    @SuppressWarnings({"unchecked"})
    protected <I, O> λ<I, O> ask(final String address) {
        return ((VerticleRef<I, O>) refMap.get(requireNonNull(address))
                                          .get()).ask();
    }

    @SuppressWarnings({"unchecked"})
    protected <I, O> λ<I, O> ask(final String address,
                                 final DeliveryOptions options) {
        return ((VerticleRef<I, O>) refMap.get(requireNonNull(address))
                                          .get()).ask(options);
    }

    @SuppressWarnings({"unchecked"})
    protected <I, O> λc<I, O> trace(final String address) {
        return ((VerticleRef<I, O>) refMap.get(requireNonNull(address))
                                          .get()).trace();
    }

    @SuppressWarnings({"unchecked"})
    protected <I, O> λc<I, O> trace(final String address,
                                    final DeliveryOptions options) {
        return ((VerticleRef<I, O>) refMap.get(requireNonNull(address))
                                          .get()).trace(options);
    }

    @SuppressWarnings({"unchecked"})
    protected <I, O> Consumer<I> tell(final String address) {
        return ((VerticleRef<I, O>) refMap.get(requireNonNull(address))
                                          .get()).tell();
    }

    @SuppressWarnings({"unchecked"})
    protected <I, O> Consumer<I> tell(final String address,
                                      final DeliveryOptions options) {
        return ((VerticleRef<I, O>) refMap.get(requireNonNull(address))
                                          .get()).tell(options);
    }

    protected void deploy(final AbstractVerticle verticle) {
        idValSeq = idValSeq.append(vertxRef.deploy(requireNonNull(verticle)));
    }

    protected void deploy(final AbstractVerticle verticle,
                          final DeploymentOptions options) {
        idValSeq = idValSeq.append(vertxRef.deploy(requireNonNull(verticle),
                                                   requireNonNull(options)
                                                  ));
    }

    protected <I, O> void deploy(final String address,
                                 final Consumer<Message<I>> consumer) {

        Val<VerticleRef<I, O>> exp = vertxRef.deploy(requireNonNull(address),
                                                     requireNonNull(consumer),
                                                     deploymentOptions
                                                    );
        refValMap = refValMap.set(address,
                                  requireNonNull(exp)
                                 );
    }

    protected <I, O> void deploy(final String address,
                                 final Consumer<Message<I>> consumer,
                                 final DeploymentOptions options) {

        Val<VerticleRef<I, O>> exp = vertxRef.deploy(requireNonNull(address),
                                                     requireNonNull(consumer),
                                                     requireNonNull(options)
                                                    );
        refValMap = refValMap.set(address,
                                  exp
                                 );
    }


    protected <I, O> void deploy(final String address,
                                 final λ<I, O> lambda) {
        Val<VerticleRef<I, O>> exp = vertxRef.deploy(requireNonNull(address),
                                                     lambda,
                                                     deploymentOptions
                                                    );

        refValMap = refValMap.set(requireNonNull(address),
                                  exp
                                 );

    }

    protected <I, O> void deploy(final String address,
                                 final λc<I, O> lambda) {
        Val<VerticleRef<I, O>> exp = vertxRef.deploy(requireNonNull(address),
                                                     lambda,
                                                     deploymentOptions
                                                    );

        refValMap = refValMap.set(requireNonNull(address),
                                  exp
                                 );

    }

    protected <I, O> void deploy(final String address,
                                 final λ<I, O> lambda,
                                 final DeploymentOptions options) {
        Val<VerticleRef<I, O>> exp = vertxRef.deploy(requireNonNull(address),
                                                     lambda,
                                                     options
                                                    );
        refValMap = refValMap.set(requireNonNull(address),
                                  exp
                                 );

    }

    protected <I, O> void deploy(final String address,
                                 final λc<I, O> lambda,
                                 final DeploymentOptions options) {
        Val<VerticleRef<I, O>> exp = vertxRef.deploy(requireNonNull(address),
                                                     lambda,
                                                     options
                                                    );
        refValMap = refValMap.set(requireNonNull(address),
                                  exp
                                 );

    }


}
