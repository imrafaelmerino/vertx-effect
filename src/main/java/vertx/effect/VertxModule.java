package vertx.effect;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Promise;
import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.eventbus.Message;
import io.vertx.core.eventbus.ReplyException;

import java.util.Map;
import java.util.function.Consumer;

import static io.vertx.core.eventbus.ReplyFailure.RECIPIENT_FAILURE;
import static java.util.Objects.requireNonNull;
import static vertx.effect.Failures.EXCEPTION_DEPLOYING_MODULE_CODE;


/**
 * A module is a verticle that deploys other verticles and exposes a set of lambdas to communicate with them. The most
 * important methods are:
 * <ul>
 * <li> {@link VertxModule#deploy()}, where the verticles must be deployed using the
 * deploy methods, like {@link #deploy(String, Lambda, DeploymentOptions)}, {@link #deploy(String, Lambdac, DeploymentOptions)}</li>
 * <li> {@link VertxModule#initialize()}, where the public module fields are initialized with lambdas using the functions
 * {@link #ask(String, DeliveryOptions)} and {@link #tell(String, DeliveryOptions)}
 * </li>
 * </ul>
 */
public abstract class VertxModule extends AbstractVerticle {

    private static final DeploymentOptions DEFAULT_DEPLOYMENT_OPTIONS = new DeploymentOptions();
    protected final DeploymentOptions deploymentOptions;
    /**
     * Factory to deploy or spawn verticles
     */
    protected VertxRef vertxRef;
    private ListExp<String> idValSeq;
    private MapExp<VerticleRef<?, ?>> refValMap;
    private Map<String, VerticleRef<?, ?>> refMap;


    @SuppressWarnings({"rawtypes", "unchecked", "squid:S3740"})
    private VertxModule(final MapExp refExp, final DeploymentOptions deploymentOptions) {
        this.refValMap = requireNonNull(refExp);
        this.deploymentOptions = requireNonNull(deploymentOptions);
        idValSeq = ListExp.seq();
    }

    /**
     * Creates an instance of this module
     *
     * @param options deployments options
     */
    @SuppressWarnings("unchecked")
    public VertxModule(final DeploymentOptions options) {
        this(MapExp.seq(), requireNonNull(options));
        idValSeq = ListExp.seq();
    }

    /**
     * Creates an instance of this module.
     */
    @SuppressWarnings("unchecked")
    public VertxModule() {
        this(MapExp.seq(), DEFAULT_DEPLOYMENT_OPTIONS);
        idValSeq = ListExp.seq();

    }

    /**
     * override this method to initialize the instance fields of this class that represent the functions exposed by this
     * module. You can establish a dialogue with the Verticle using the method {@link VerticleRef#ask()}, which returns
     * a function, or just send it messages without waiting for the response with the method {@link VerticleRef#tell()},
     * which returns a consumer. You may be interested in deploying Verticles on the fly to get a greater level of
     * parallelism with the function {@link VertxRef#spawn(String, Lambda)}.
     */
    protected abstract void initialize();

    /**
     * override this method and deploy the Verticles you want to be exposed by your module. using the functions
     * {@link VertxModule#deploy(String, Lambda)} (String, Function)} or
     * {@link VertxModule#deployConsumer(String, Consumer)}. You can also deployed regular Verticles with the method
     * {@link VertxModule#deployVerticle(AbstractVerticle)}
     */
    protected abstract void deploy();

    @Override
    @SuppressWarnings("ReturnValueIgnored")
    public void start(final Promise<Void> start) {

        try {
            vertxRef = new VertxRef(requireNonNull(vertx),
                                    deploymentOptions
            );
            deploy();
            PairExp.par(idValSeq,
                        refValMap
                       )
                   .onComplete(event -> {
                       if (event.failed()) {
                           start.fail(event.cause());
                       } else {
                           try {
                               refMap = event.result().second();
                               initialize();
                               start.complete();
                           } catch (Exception e) {
                               start.fail(new ReplyException(RECIPIENT_FAILURE,
                                                             EXCEPTION_DEPLOYING_MODULE_CODE,
                                                             Functions.getErrorMessage(e)
                               ));
                           }
                       }
                   }).get();

        } catch (Exception e) {
            start.fail(new ReplyException(RECIPIENT_FAILURE,
                                          EXCEPTION_DEPLOYING_MODULE_CODE,
                                          Functions.getErrorMessage(e)
            ));
        }
    }

    @SuppressWarnings({"unchecked"})
    protected <I, O> Lambda<I, O> ask(final String address) {
        return ((VerticleRef<I, O>) refMap.get(requireNonNull(address))).ask();
    }

    @SuppressWarnings({"unchecked"})
    protected <I, O> Lambda<I, O> ask(final String address, final DeliveryOptions options) {
        return ((VerticleRef<I, O>) refMap.get(requireNonNull(address))).ask(options);
    }

    @SuppressWarnings({"unchecked"})
    protected <I, O> Lambdac<I, O> trace(final String address) {
        return ((VerticleRef<I, O>) refMap.get(requireNonNull(address))).trace();
    }

    @SuppressWarnings({"unchecked"})
    protected <I, O> Lambdac<I, O> trace(final String address, final DeliveryOptions options) {
        return ((VerticleRef<I, O>) refMap.get(requireNonNull(address))).trace(options);
    }

    @SuppressWarnings({"unchecked"})
    protected <I, O> Consumer<I> tell(final String address) {
        return ((VerticleRef<I, O>) refMap.get(requireNonNull(address))).tell();
    }

    @SuppressWarnings({"unchecked"})
    protected <I, O> Consumer<I> tell(final String address, final DeliveryOptions options) {
        return ((VerticleRef<I, O>) refMap.get(requireNonNull(address))).tell(options);
    }

    protected VIO<String> deployVerticle(final AbstractVerticle verticle) {
        VIO<String> val = vertxRef.deployVerticle(requireNonNull(verticle));
        idValSeq = idValSeq.append(val);
        return val;
    }

    protected VIO<String> deployVerticle(final AbstractVerticle verticle, final DeploymentOptions options) {
        VIO<String> val = vertxRef.deployVerticle(requireNonNull(verticle), requireNonNull(options));
        idValSeq = idValSeq.append(val);

        return val;
    }

    protected <I, O> VIO<VerticleRef<I, O>> deployConsumer(final String address, final Consumer<Message<I>> consumer) {

        VIO<VerticleRef<I, O>> exp = vertxRef.deployConsumer(requireNonNull(address),
                                                             requireNonNull(consumer),
                                                             deploymentOptions
                                                            );
        refValMap = refValMap.set(address,
                                  requireNonNull(exp)
                                 );
        return exp;
    }

    protected <I, O> VIO<VerticleRef<I, O>> deployConsumer(final String address,
                                                           final Consumer<Message<I>> consumer,
                                                           final DeploymentOptions options
                                                          ) {

        VIO<VerticleRef<I, O>> exp = vertxRef.deployConsumer(requireNonNull(address),
                                                             requireNonNull(consumer),
                                                             requireNonNull(options)
                                                            );
        refValMap = refValMap.set(address,
                                  exp
                                 );
        return exp;
    }


    protected <I, O> VIO<VerticleRef<I, O>> deploy(final String address,
                                                   final Lambda<I, O> lambda
                                                  ) {
        VIO<VerticleRef<I, O>> exp = vertxRef.deploy(requireNonNull(address),
                                                     lambda,
                                                     deploymentOptions
                                                    );

        refValMap = refValMap.set(requireNonNull(address),
                                  exp
                                 );

        return exp;

    }

    protected <I, O> VIO<VerticleRef<I, O>> deploy(final String address,
                                                   final Lambdac<I, O> lambda
                                                  ) {
        VIO<VerticleRef<I, O>> exp = vertxRef.deploy(requireNonNull(address),
                                                     lambda,
                                                     deploymentOptions
                                                    );

        refValMap = refValMap.set(requireNonNull(address),
                                  exp
                                 );

        return exp;

    }


    protected <I, O> VIO<VerticleRef<I, O>> deploy(
            final String address, final Lambda<I, O> lambda, final DeploymentOptions options
                                                  ) {
        VIO<VerticleRef<I, O>> exp = vertxRef.deploy(requireNonNull(address),
                                                     lambda,
                                                     options
                                                    );
        refValMap = refValMap.set(requireNonNull(address), exp);

        return exp;

    }

    protected <I, O> VIO<VerticleRef<I, O>> deploy(final String address, final Lambdac<I, O> lambda, final DeploymentOptions options) {
        VIO<VerticleRef<I, O>> exp = vertxRef.deploy(requireNonNull(address),
                                                     lambda,
                                                     options
                                                    );
        refValMap = refValMap.set(requireNonNull(address),
                                  exp
                                 );

        return exp;

    }

}
