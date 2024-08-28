package vertx.effect.stub;

import fun.gen.Gen;
import vertx.effect.Lambda;
import vertx.effect.VIO;

import java.time.Duration;

import static java.util.Objects.requireNonNull;

/**
 * A builder for building `IO` stubs instances using generators of IO effects. class, where delays and failures can be
 * produced based on the call number to the generator. Generators are composable using the `Combinators` class.
 * <p>
 * Example of generating `IO` instances with generators and composition:
 * <pre>
 * {@code
 * var gen1 = Gen.seq(IO::succeed);
 * var gen2 = Gen.cons(IO.fail(new RuntimeException("bad luck!")));
 * var gen = Combinators.oneOf(gen1, gen2);
 * }
 * </pre>
 * <p>
 * This class allows you to generate `VIO` instances with various generators and customize their behavior.
 *
 * @param <O> The type of value generated by the stub.
 * @see Gen
 * @see fun.gen.Combinators
 */
public final class StubBuilder<O> {

    private final Gen<VIO<O>> gen;

    private Gen<Duration> delayGen;

    private StubBuilder(final Gen<VIO<O>> gen) {
        this.gen = gen;
    }

    /**
     * Creates a new stub using the provided generator of IO effects.
     *
     * @param gen The generator for creating `IO` instances.
     * @param <O> The type of value to generate.
     * @return A new stub instance.
     */
    public static <O> StubBuilder<O> ofGen(final Gen<VIO<O>> gen) {
        return new StubBuilder<>(requireNonNull(gen));
    }


    /**
     * Creates a new stub using the provided generator of values.
     *
     * @param gen The generator for creating values of type O
     * @param <O> The type of value to generate.
     * @return A new stub instance.
     */
    public static <O> StubBuilder<O> ofSucGen(final Gen<O> gen) {
        return new StubBuilder<>(requireNonNull(gen).map(VIO::succeed));
    }


    /**
     * Sets the generator of delays
     *
     * @param delaysGen the generator of delays
     * @return this stub builder with a delay generator
     */
    public StubBuilder<O> withDelays(final Gen<Duration> delaysGen) {
        this.delayGen = requireNonNull(delaysGen);
        return this;
    }


    /**
     * Generates an `IO` stub using the specified generator and settings.
     *
     * @return The generated `IO` stub instance.
     */

    public VIO<O> build() {
        Lambda<VIO<O>, O> delay = it -> delayGen == null ? it : VIO.lazy(delayGen.sample())
                                                                   .then(it::sleep);
        return VIO.lazy(gen.sample()).then(delay);
    }
}