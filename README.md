<img src="./logo/package_twitter_swe2n4mg/black/full/coverphoto/black_logo_white_background.png" alt="logo"/>

- [vertx-effect manifesto](#manifesto)
- [How persistent data structures makes a difference working with actors](#persistendata)
- [vertx-effect in a few lines of code](#fewlinesofcode)
- [Effects](#effects)
- [Expressions](#exp)
- [Being reactive](#reactive)
- [Modules](#modules)
- [Logging](#logging)
    - [Publishing events](#events)
    - [Publishing correlated events](#correlated-events)
- [Spawning verticles](#spawning-verticles)
- [Http client](#http-client)
- [Oauth Http client](#oauth-client)
- [Http server](#http-server)
- [Testing](#testing)
    - [VIO stubs](#vio-stubs)
    - [Http server stubs](#http-stubs)
- [Requirements](#requirements)
- [Installation](#installation)
- [Related projects](#rp)

[![Maven](https://img.shields.io/maven-central/v/com.github.imrafaelmerino/vertx-effect/5.0.0)](https://search.maven.org/artifact/com.github.imrafaelmerino/vertx-effect/5.0.0/jar)

## <a name="manifesto"><a/> vertx-effect manifesto

    . The more verticles, the better.
    . A verticle must do only one thing.
    . Use persistent data structures.
    . Systems will fail, be prepared.
    . Simplicity matters.
    . If there is a bug and you can't spot it quickly, then there are two bugs. Fix both of them.

## <a name="persistendata"><a/>How persistent data structures makes a difference working with actors

Vertx-effect embraces immutability and persistent data structures.
That's why it uses [vertx-values](https://github.com/imrafaelmerino/vertx-values) instead of the
default Vert.x Json which is very inefficient.

## <a name="fewlinesofcode"><a/>vertx-effect in a few lines of code

```code  
public class MyModule extends VertxModule {

    public static Lambda<String, String> toLowerCase, toUpperCase;
    public static Lambda<Integer, Integer> inc;
    public static Lambda<JsObj, JsObj> validate, validateAndMap;

    @Override
    protected void deploy() {

        this.deploy("toLowerCase",
                    (String str) -> VIO.succeed(str.toLowerCase())
                   );
        this.deploy("toUpperCase",
                    (String str) -> VIO.succeed(str.toUpperCase())
                   );
        this.deploy("inc",
                    (Integer n) -> VIO.succeed(n + 1)
                   );

        // json-values uses specs to define the structure of a Json: {a:int,b:[str,str]} 
        JsObjSpec spec = JsObjSpec.of("a", integer(),
                                      "b", tuple(str(), str())
                                     );
        this.deploy("validate", Validators.validateJsObj(spec));

        Lambda<JsObj, JsObj> map = obj ->
                JsObjExp.par("a",
                             inc.apply(obj.getInt("a"))
                                .map(JsInt::of),
                             "b",
                             JsArrayExp.par(toLowerCase.apply(obj.getStr(path("/b/0")))
                                                       .map(JsStr::of),
                                            toUpperCase.apply(obj.getStr(path("/b/1")))
                                                       .map(JsStr::of)
                                           )
                            )
                        .retry(RetryPolicies.limitRetries(2));
        this.deploy("validateAnMap",
                    (JsObj obj) -> validate.apply(obj).then(map)
                   );

    }

    @Override
    protected void initialize() {

        toUpperCase = this.ask("toUpperCase");
        toLowerCase = this.ask("toLowerCase");
        inc = this.ask("inc");
        validate = this.ask("validate");
        validateAndMap = this.ask("validateAnMap");

    }
}
```

**A module is a regular verticle that deploys other verticles and exposes lambdas to communicate with them**.
A lambda is just a function that takes an input and produces an output. In the above example, `MyModule` deploys
five verticles. It's worth mentioning how the verticle `ValidateAndMap` is defined using composition and the expressions
`JsObjExp` and `JsArrayExp`. It shows the essence and the goal of vertx-effect. Later on, we'll see more expressions
like
`CondExp`, `SwitchExp`, `IfElseExp`, `AllExp`, `AnyExp`, `PairExp`, `TripleExp`, `ListExp`,
`MapExp` etc.

`ValidateAndMap` sends a message to `validate`. If the message matches the given spec, `ValidateAndMap` computes the
output
sending messages to the verticles `inc`, `toLowerCase`, and `toUpperCase` and composing a Json from their responses in
parallel.
You can operate sequentially instead of in parallel using the constructors `JsObjExp.seq` and `JsArrayExp.sequential`.
Thanks to the `retry` function, if `any` verticle failed to compute their value, it would retry the computation up to
two times.

It's important to notice that you can still send messages to the module verticles using the Vertx API, but one of the
points of vertx-effect is to use functions for that.

Let's write some tests. Vertx doesn't support json-values, so we need to register a `MessageCodec` to send its
persistent
Json across the event bus.

```java   
import io.vertx.core.Vertx;
import io.vertx.junit5.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import jsonvalues.JsArray;
import jsonvalues.JsInt;
import jsonvalues.JsObj;
import vertx.effect.PairExp;
import vertx.effect.VertxRef;
import vertx.values.codecs.RegisterJsValuesCodecs;

@ExtendWith(VertxExtension.class)
public class TestMyModule {

    @BeforeAll
    // register a MessageCodec for json-values and deploy MyModule
    public static void prepare(final Vertx vertx,
                               final VertxTestContext context
                              ) {
        VertxRef ref = new VertxRef(vertx);
        PairExp.seq(ref.deployVerticle(new RegisterJsValuesCodecs()),
                    ref.deployVerticle(new MyModule())
                   )
               .onSuccess(ids -> context.completeNow())
               .get();
    }

    @Test
    public void empty_json_is_sent_and_failure_is_received(VertxTestContext context) {

        MyModule.validateAndMap.apply(JsObj.EMPTY)
                               .onComplete(result ->
                                                   context.verify(() -> {
                                                       Assertions.assertTrue(result.failed());
                                                       System.out.println(result.cause());
                                                       context.completeNow();
                                                   })
                                          )
                               .get();
    }

    @Test
    public void valid_json_is_sent_and_is_mapped_successfully(VertxTestContext context) {

        JsObj input = JsObj.of("a", JsInt.of(1), "b", JsArray.of("FOO", "foo"));

        JsObj expected = JsObj.of("a", JsInt.of(2), "b", JsArray.of("foo", "FOO"));

        MyModule.validateAndMap.apply(input)
                               .onSuccess(output -> {
                                   context.verify(() -> {
                                       Assertions.assertEquals(expected, output);
                                       context.completeNow();
                                   });
                               })
                               .get();
    }
}
```

**Lambdas are just functions, so you can test them without deploying any verticle!**

```code  

 Lambda<String, String> toLowerCase = str -> VIO.succeed(str.toLowerCase());

 Lambda<String, String> toUpperCase = str -> VIO.succeed(str.toUpperCase());

 Lambda<Integer, Integer> inc = n -> VIO.succeed(n+1);

 JsObjSpec spec = JsObjSpec.of("a", integer(), "b", tuple(str(), str()));

 Lambda<JsObj, JsObj> validate = Validators.validateJsObj(spec);

 Lambda<JsObj, JsObj> map = obj->
            JsObjExp.par("a", inc.apply(obj.getInt("a")).map(JsInt::of),
                         "b", JsArrayExp.par(toLowerCase.apply(obj.getStr(path("/b/0"))).map(JsStr::of),
                                             toUpperCase.apply(obj.getStr(path("/b/1"))).map(JsStr::of)
                                            )
                        );
 
 @Test
 public void valid_json_is_validated_and_mapped(VertxTestContext context) {

    JsObj input = JsObj.of("a", JsInt.of(1), "b", JsArray.of("FOO","foo"));
        
    JsObj expected = JsObj.of("a", JsInt.of(2), "b", JsArray.of("foo","FOO"));

    validate.then(map)
            .apply(input).get()
            .onComplete(result->{
                    context.verify(
                        () -> Assertions.assertTrue(result.succeeded() && result.result().equals(expected))
                    );
                    context.completeNow();
                });
    }

```

**This is extremely convenient and productive to do your testing. You don't need to mock anything.
Passing around functions that produce outputs given some inputs is enough to check that your verticles
will do their job.**

**The takeaway from this section is how using function composition and different expressions, you'll
be able to handle complexity and implement and test any imaginable flow very quickly.**

## <a name="effects"><a/> Effects

Functional Programming is all about working with pure functions and values. That's all. **One of the points where FP
especially shines is dealing with effects**. An effect is something you can't call twice unless you intended to:

```code 

Future<Customer> a = insertDb(customer);

Future<Customer> b = insertDb(customer);

```

Both calls can fail, or they can create two different customers, or one of them can fail, who knows. That code is not
referentially transparent. For obvious reasons, you can't do the following refactoring:

```code  

Future<Customer> c = insertDb(customer)

Future<Customer> a = c;

Future<Customer> b = c;

```

A Vertx future represents an asynchronous effect. We don't want to block the event loop because
of the latency of a computation.
**Haskell** has proven to us how laziness is an essential property to stay pure. We need to define
an immutable and lazy data structure that allows us to control the effect of latency.

Since Java 8, we have suppliers. They are indispensable to do FP in Java. Let's start defining what
an effect is in vertx-effect:

```code   

import java.util.function.Supplier
import io.vertx.core.Future

public abstract class VIO<O> extends Supplier<Future<O>> {
    //from a constant
    public static VIO<O> succeed(O constant);
    
    //from a exception
    public static VIO<O> fail(Throwable error);
    
    // from an effect
    public static VIO<O> effect(Supplier<Future<O>> effect);
    
}

```

A **VIO** of type **O** is a supplier that will return a Vertx future of type **O**.
**It describes (and not execute) an asynchronous effect that will compute a value of type O**.

If we turn Future into VIO in the previous example:

```code 

VIO<Customer> a = VIO.effect( () -> insertDb(customer) );

VIO<Customer> b = VIO.effect( () -> insertDb(customer) );

```

The above example is entirely equivalent to:

```code  

VIO<Customer> c = VIO.effect( ()->insertDb(customer) ); 

VIO<Customer> a = c;

VIO<Customer> b = c;

```

This property is fundamental. Whenever you see the expression `insertDb(customer)`
in your program, you can think of it as it was c. Pure FP programming helps us reason
about the programs we write. VIO is lazy. It's a description
of an effect. **In FP, we describe programs, and it's at the very last moment when
they're executed.**

I always wanted to name **Lambda** to something, and I finally got the chance!

```code  

import java.util.function.Function

public interface Lambda<I,O> extends Function<I, VIO<O>> { }

```

A lambda is a function that returns a **VIO** of type **O** given a type **I**.
**It models the communication with a verticle**: a message is sent, the verticle
receives and processes the message, and replies with a response.
The message and the answer have to be of a type that can be sent across the event bus;
otherwise, you must implement a [MessageCodec](https://vertx.io/docs/apidocs/io/vertx/core/eventbus/MessageCodec.html).

## <a name="exp"><a/> Expressions

**Using expressions and function composition is how we deal with complexity in functional programming**.
Let's go over the essential expressions in vertx-effect:

- **VIO constructors**

```code   
VIO<String> str = VIO.succeed("hi"); 

VIO<Throwable> error = VIO.fail(new RuntimeException("something went wrong :("));

Future<JsObj> getProfile(final String id){...}
VIO<JsObj> profile = VIO.effect( () -> getProfile(id)); 

VIO<Long> realTime = VIO.effect(() -> System.currentTimeMillis() );
``` 

- **IfElseExp**. If the condition is evaluated to true, it computes and returns the consequence;
  otherwise, the alternative.

```code   

import jio.IfElseExp;

 VIO<O> exp = IfElseExp.<O>predicate(VIO<Boolean> condition)
                       .consequence(Supplier<VIO<O>> consequence)
                       .alternative(Supplier<VIO<O>> alternative);

 VIO<O> exp = IfElseExp.<O>predicate(boolean condition)
                       .consequence(Supplier<VIO<O>> consequence)
                       .alternative(Supplier<VIO<O>> alternative);

```

The alternative and the consequence are lazy computations of IO effects.

- **SwitchExp**. The switch construct implements multiple pattern-value branches.
  It evaluates an effect or value of type I and allows multiple clauses based on
  evaluating that value.

```code   

// matches a value of type I

 VIO<O> exp = 
  SwitchExp<I,O>.match(I value)
                .patterns(I pattern1, Lambda<I,O> lambda1,
                          I pattern2, Lambda<I,O> lambda2,           
                          I pattern3, Lambda<I,O> lambda3,
                          Lambda<I,O> otherwise
                          );      

// matches an effect of type I
                          
 VIO<O> exp = 
  SwitchExp<I,O>.match(VIO<I> value)
                .patterns(I pattern1, Lambda<I,O> lambda1,
                          I pattern2, Lambda<I,O> lambda2,           
                          I pattern3, Lambda<I,O> lambda3,
                          Lambda<I,O> otherwise
                          );                               
                          

// For example, the following expression reduces to "Wednesday"

 VIO<O> exp = 
  SwitchExp<Integer,String>.match(3)
                           .patterns(1, _ -> VIO.succeed("Monday"),
                                     2, _ -> VIO.succeed("Tuesday"),
                                     3, _ -> VIO.succeed("Wednesday"),
                                     4, _ -> VIO.succeed("Thursday"),
                                     5, _ -> VIO.succeed("Friday"),
                                     _ -> VIO.succeed("weekend")
                                     );
```

The same as before but using lists instead of constants as patterns.

```code   

 VIO<O> exp = SwitchExp<I,O>.match(I value)
                          .patterns(List<I> pattern1, Lambda<I,O> lambda1,
                                    List<I> pattern2, Lambda<I,O> lambda2,        
                                    List<I> pattern3, Lambda<I,O> lambda3,
                                    Lambda<I,O> otherwise
                                   );      
        
// For example, the following expression reduces to "third week"
 VIO<O> exp = 
  SwitchExp<Integer,String>.match(20)
                           .patterns(List.of(1, 2, 3, 4, 5, 6, 7), _ -> VIO.succeed("first week"),
                                     List.of(8, 9, 10, 11, 12, 13, 14), _ -> VIO.succeed("second week"),
                                     List.of(15, 16, 17, 18, 19, 20, 10), _ -> VIO.succeed("third week"),
                                     List.of(21, 12, 23, 24, 25, 26, 27), _ -> VIO.succeed("forth week"),
                                     _ -> VIO.succeed("last days of the month")
                                    );
```

Last but not least, you can use predicates as patterns instead of values or list of values:

```code   

 VIO<O> exp = 
  SwitchExp<I,O>.match(VIO<I> value)
                .patterns(Predicate<I> pattern1, Lambda<I,O> lambda1,
                          Predicate<I> pattern2, Lambda<I,O> lambda2,        
                          Predicate<I> pattern3, Lambda<I,O> lambda3,
                          Lambda<I,O> otherwise
                          );      
        
// For example, the following expression reduces to the default value, "greater or equal to twenty"

 VIO<O> exp = 
  SwitchExp<Integer,String>.match(IO.succeed(20))
                           .patterns(i -> i < 5 , _ -> VIO.succeed("lower than five"),
                                     i -> i < 10 , _ -> VIO.succeed("lower than ten"),
                                     i -> i < 20 , _ -> VIO.succeed("lower than twenty"),
                                     _ -> VIO.succeed("greater or equal to twenty")
                                    );
```

- **CondExp**. It's a set of branches, and a default value. Each branch consists of an
  effect that computes a boolean (the condition) and its associated effect. The effect is
  computed and the expression reduced to its value if its condition is the first one in the
  list to be true. This means the order you place branches matters.
  If no condition is true, it computes the default effect, which is the last clause.
  You can compute all the conditions values either in parallel or sequentially.

```code   

 VIO<O> exp = CondExp.<O>seq(VIO<Boolean> cond1, Supplier<VIO<O>> effect1,
                             VIO<Boolean> cond2, Supplier<VIO<O>> effect2,
                             VIO<Boolean> cond3, Supplier<VIO<O>> effect3,
                             Supplier<VIO<O>> otherwise
                          );
                          
                          
 VIO<O> exp = CondExp.<O>par(VIO<Boolean> cond1, Supplier<VIO<O>> effect1,
                             VIO<Boolean> cond2, Supplier<VIO<O>> effect2,
                             VIO<Boolean> cond3, Supplier<VIO<O>> effect3,
                             Supplier<VIO<O>> otherwise
                          );                          
                        
``` 

- `AllExp` and `AnyExp`. They are just idiomatic names for the boolean expressions
  And and Or. You can compute all the boolean effects either in parallel or sequentially.

```code  

 VIO<Boolean> all = AllExp.par(VIO<Boolean> cond1, VIO<Boolean> cond2, ....);
 VIO<Boolean> all = AllExp.seq(VIO<Boolean> cond1, VIO<Boolean> cond2, ....);

 VIO<Boolean> any = AnyExp.par(VIO<Boolean> cond1, VIO<Boolean> cond2, ...);
 VIO<Boolean> any = AnyExp.seq(VIO<Boolean> cond1, VIO<Boolean> cond2, ...);

```  

- **PairExp**. A pair is a tuple of two elements. Each element can be computed
  either in parallel or sequentially.

```code   

 VIO<Pair<A,B> pair = PairExp.par(VIO<A> val1, VIO<B> val2);

 VIO<Pair<A,B> pair = PairExp.seq(VIO<A> val1, VIO<B> val2);

```

You can race pairs when A and B have the same type, returning the first value
that is computed:

```code   

 VIO<Pair<A,A> pair = PairExp.par(VIO<A> val1, VIO<A> val2);

 VIO<A> thefastest = PairExp.race(par);

```

- **TripleExp**. A triple is a tuple of three elements. Each element can be
  computed either in parallel or sequentially.

```code   

 VIO<Triple<A,B,C> triple = TripleExp.par(VIO<A> val1, VIO<B> val2, VIO<C> val3);

 VIO<Triple<A,B,C> triple = TripleExp.seq(VIO<A> val1, VIO<B> val2, VIO<C> val3);

```

You can also race triples.

- **JsObjExp** and **JsArrayExp**.

`JsObjExp` and `JsArrayExp` are data structures that look like raw Json.
You can compute all the values either in parallel or sequentially. You can mix
all the expressions we've seen so far and nest them, going as deep as
necessary, like in the following example:

```code   

IfElseExp<JsStr> a = IfElseExp.<JsStr>predicate(VIO<Boolean> condition)
                                     .consequence(VIO<JsStr> consequence)
                                     .alternative(VIO<JsStr> alternative); 

JsArrayExp b = 
        JsArrayExp.seq(SwitchExp<Integer,JsValue>.match(n)
                                                 .patthers(1, Lambda<Insteger,JsValue> lambda1,
                                                           2, Lambda<Insteger,JsValue> lambda2,
                                                           Lambda<Insteger,JsValue> defaultLambda
                                                          ),
                      CondExp.par(VIO<Boolean> cond1, Supplier<IO<JsValue>> effect1,
                                  VIO<Boolean> cond2, Supplier<IO<JsValue>> effect2,
                                  Supplier<IO<JsValue>> defaultValue
                                )
                      );

JsObjExp c = 
       JsObjExp.par("d", AnyExp.seq(VIO<Boolean> cond3, VIO<Boolean> cond4)
                               .map(JsBool::of),
                    "e", AllExp.par(VIO<Boolean> cond5, VIO<Boolean> cond6)
                               .map(JsBool::of),
                    "f", JsArrayExp.par(VIO<JsValue> value1, VIO<JsValue> value2) 
                   )

JsObjExp exp = JsObjExp.par("a", a,
                            "b", b,
                            "c", c 
                           );
                           
JsObj json = exp.result();                          
```

It's important to notice that any value of the above expressions can be computed
by different verticles deployed on different machine's of a cluster.
Imagine ten machines collaborating to compute a JsObj. Isn't this amazing?

- **ListExp and MapExp**

They represent sequences and maps. **Modules use them internally**. For example,
the `deploy` method uses a MapExp to put the deployed verticles using their addresses
as keys. They also use a ListExp when more than a verticle instance is deployed.
As with the other expressions, you can compute their values either in parallel or
sequentially.

```

MapExp<String> map = MapExp.par("a", VIO<String> value1,
                                "b", VIO<String> value2,
                                "c", VIO<String> value3
                                );

ListExp<Integer> seq = ListExp.par(VIO<Integer>, VIO<Integer>);
VIO<Integer> firstFinishing = seq.race();

```

The `race` function returns the value that finishes first. You can race a `JsArrayExp` as well.

## <a name="reactive"><a/> Being reactive

Find below some of the most critical operations defined in the `Val` interface that will help us
make our code more resilient:

```code   
import vertx.effect.RetryPolicy;

public interface VIO<O> extends Supplier<Future<O>> {
    VIO<O> retry(RetryPolicy policy);

    VIO<O> retry(Predicate<Throwable>,
                 RetryPolicy policy);

    VIO<O> repeat(Predicate<O> predicate,
                  RetryPolicy policy);

    VIO<O> recoverWith(Lambda<Throwable, O> fn);

    VIO<O> fallbackTo(Lambda<Throwable, O> fn);

    VIO<O> recoverWith(Lambda<Throwable, O> fn);
}
 ``` 

**recoverWith**:  it switches to an alternative lambda when a failure happens.

**fallbackTo**: It's like recoverWith, but if the second lambda fails too, it returns the first one error.

**recover**: returns a constant if the computation fails.

**retry**: retries the computation if an error happens. You can define a predicate to retry only the
specified errors. Retry policies are created in a very declarative and composable way, for example:

```code   
import static vertx.effect.RetryPolicies.*

Delay oneHundredMillis = vertxRef.sleep(Duration.ofMillis(100));
Delay oneSec = vertxRef.sleep(Duration.ofSeconds(1));

// up to five retries waiting 100 ms 
constantDelay(oneHundredMillis).append(limitRetries(5))

//during 3 seconds up to 10 times     
limitRetries(10).limitRetriesByCumulativeDelay(Duration.ofSeconds(3))    

//5 times without timer and then, if it keeps failing, an incremental timer from 100 ms up to 1 second
limiteRetries(5).followedBy(incrementalDelay(oneHundredMillis).capDelay(oneSec))

```

There are very interesting policies implemented based
on [this article](https://aws.amazon.com/blogs/architecture/exponential-backoff-and-jitter/):
exponential backoff, full jitter, equal jitter, decorrelated jitter etc

**repeat**: When you get a not expected value (a failure) and want to repeat the computation.
A predicate is specified to catch the failures. You can define any imaginable policy as well.
Imagine you make a http request and you get a 500. That's not an error, it's a server failure.
You can repeat the request according to a policy.

For expressions like **Cond**, **Case**, **IfElse**, **All**, **Any**, **Pair**, **Triple**, you can retry
each value of the expression instead of the overall expresion with the methods:

```code   
 VIO<O> retryEach(RetryPolicy policy);

 VIO<O> retryEach(Predicate<Throwable>,
                  RetryPolicy policy);

```

## <a name="modules"><a/> Modules

In vertx-effect, **a module is a special verticle whose purpose is to deploy other verticles and expose lambdas
to communicate with them**. Let's put an example.

```code   
import jsonvalues.JsObj;
import jsonvalues.JsStr;
import vertx.effect.VIO;
import vertx.effect.VertxModule;
import vertx.effect.Lambda;

public class MyModule extends VertxModule {

    private static final String REMOVE_NULL_ADDRESS = "removeNull";
    private static final String TRIM_ADDRESS = "trim";

    public static Lambda<JsObj, JsObj> removeNull;
    public static Lambda<JsObj, JsObj> trim;

    @Override
    public void deploy() {

        this.deploy(REMOVE_NULL_ADDRESS,
                    (JsObj o) -> VIO.succeed(o.filterAllValues(pair -> pair.value.isNotNull()))
                   );

        Function<JsValue, JsValue> trim = JsStr.prism.modify.apply(String::trim);
        this.deploy(TRIM_ADDRESS,
                    (JsObj o) -> VIO.succeed(o.mapAllValues(pair -> trim.apply(pair.value)))
                   );
    }

    @Override
    protected void initialize() {
        removeNull = this.ask(REMOVE_NULL_ADDRESS);
        trim = this.ask(TRIM_ADDRESS);
    }
}
```

We usually divide modules into four main blocks:

    . The addresses where the module verticles will be listening on.
    . The lambdas that are exposed to the outside world to communicate with the deployed verticles.
    . The `deploy` method, where the module deploys the verticles.
    . The `initialize` method, where the module initializes the lambdas.

In our example, we are using the persistent and immutable Json from json-values.
The **ask** method returns a lambda to establish bidirectional communication with a verticle.
In contrast, the **tell** method would return a consumer because a response is either not expected
or ignored. Let's deploy our module and do some testing. We usually divide modules into four main blocks:

```code   
 @BeforeAll
 public static void prepare(final Vertx vertx,
                            final VertxTestContext context) 
 {
    VertxRef vertxRef = new VertxRef(vertx);

    // prints out events published by vertx-effect
    vertxRef.registerConsumer(EVENTS_ADDRESS, System.out::println); 

    Pair.seq(vertxRef.deployVerticle(new RegisterJsValuesCodecs()),
                    vertxRef.deployVerticle(new MyModule()) 
                   )
        .onSuccess(pair -> {
                            System.out.println(String.format("Ids deployed: %s and %s",
                                                             pair._1,
                                                             pair._2
                                                            )
                                              );
                            context.completeNow();
                           }
                  )
        .get();
 }

 @Test
 public void test_remove_and_then_trim(final VertxTestContext context)
 {
    Lambda<JsObj, JsObj> removeAndTrim = MyModule.removeNull.andThen(MyModule.trim);

    JsObj input = JsObj.of("a", JsStr.of("  hi  "),
                           "b", JsNull.NULL,
                           "c", JsObj.of("d", JsStr.of("  bye  "),
                                         "e", JsNull.NULL
                                        )
                          );

    JsObj expected = JsObj.of("a", JsStr.of("hi"),
                              "c", JsObj.of("d", JsStr.of("bye"))
                             );

    removeAndTrim.apply(input)
                 .onSuccess(it -> {
                     context.verify(()-> {
                                          Assertions.assertEquals(expected,it);
                                          context.completeNow();
                                         }
                                   );
                                  }
                           )
                .get();
 }

```

To send the persistent objects from [json-values](https://imrafaelmerino.github.io/json-values/)
across the event bus, we need to register some codecs. The verticle RegisterJsValuesCodecs does
this task. The VertxRef class is a wrapper around the Vertx instance to deploy and spawn verticles
from lambdas. Modules use this class internally.

The `VertxRef` class is a wrapper around the Vertx instance to deploy and spawn verticles from lambdas.
Modules use this class internally.

## <a name="logging"><a/> Logging

Logging is essential in software. There are many logging libraries. Sometimes it is not clear
what dependencies you have to use because there isn't a standard solution. Each library uses
its own. I didn't want to be opinionated. At the same time, I wanted to provide a simple and
decouple solution to know what is going on in any system using vertx-effect.
That's why I decided to publish remarkable events in a specific address. If you want to use your
favorite slf4j implementation, just implement it in a consumer. On the other hand, consuming all
those events during testing will give you instant feedback
on your system and agility spotting bugs. You can disable this future with the Java system
property **-D"vertx.effect.enable.log.events"=false**.

### <a name="events"><a/> Publishing events

**vertx-effect** publishes events to the address **vertx-effect-events**. Find below some of the most important
predefined
events:

    - VERTICLE_DEPLOYED
    - VERTICLE_UNDEPLOYED
    - MESSAGE_SENT
    - MESSAGE_RECEIVED
    - RESPONSE_REPLIED
    - FAILURE_REPLIED
    - RESPONSE_RECEIVED
    - FAILURE_RECEIVED
    - EXCEPTION_STARTING_VERTICLE
    - EXCEPTION_STARTING_SHELL
    - EXCEPTION_PROCESSING_MESSAGE
    - EXCEPTION_UNDEPLOYING_VERTICLE
    - EXCEPTION_DEPLOYING_VERTICLE
    - TIMER_STARTED
    - TIMER_ENDED

An example from the previous example would be:

```text   

{"event":"VERTICLE_DEPLOYED","address":"removeNull","instant":"2020-10-10T22:44:42.687633Z","id":"3de92ef8-777f-4110-aa45-442fc41900c6","thread":"vert.x-eventloop-thread-1"}
{"event":"VERTICLE_DEPLOYED","class":"vertx.effect.RegisterJsValuesCodecs","instant":"2020-10-10T22:44:42.682624Z","id":"73181043-ae38-4819-b7de-02f303fcc155","thread":"vert.x-eventloop-thread-3"}
{"event":"VERTICLE_DEPLOYED","address":"trim","instant":"2020-10-10T22:44:42.701293Z","id":"a866ffdc-38c8-4da2-bcc0-c9f4881f5139","thread":"vert.x-eventloop-thread-1"}
{"event":"VERTICLE_DEPLOYED","class":"vertx.effect.MyModule","instant":"2020-10-10T22:44:42.703410Z","id":"1473dff2-075c-4fd8-be42-cebcf0a890a0","thread":"vert.x-eventloop-thread-6"}

{"event":"MESSAGE_SENT","to":"removeNull","message":{"a":"  hi  ","b":null,"c":{"d":"  bye  ","e":null}},"instant":"2020-10-10T22:44:42.710447Z","thread":"main"}
{"event":"MESSAGE_RECEIVED","address":"removeNull","instant":"2020-10-10T22:44:42.713981Z","thread":"vert.x-eventloop-thread-4"}
{"event":"RESPONSE_REPLIED","address":"removeNull","message":{"c":{"d":"  bye  "},"a":"  hi  "},"instant":"2020-10-10T22:44:42.723013Z","thread":"vert.x-eventloop-thread-4"}
{"event":"RESPONSE_RECEIVED","from":"removeNull","instant":"2020-10-10T22:44:42.723225Z","thread":"vert.x-eventloop-thread-8"}

{"event":"MESSAGE_SENT","to":"trim","message":{"c":{"d":"  bye  "},"a":"  hi  "},"instant":"2020-10-10T22:44:42.723635Z","thread":"vert.x-eventloop-thread-8"}
{"event":"MESSAGE_RECEIVED","address":"trim","instant":"2020-10-10T22:44:42.724047Z","thread":"vert.x-eventloop-thread-5"}
{"event":"RESPONSE_REPLIED","address":"trim","message":{"a":"hi","c":{"d":"bye"}},"instant":"2020-10-10T22:44:42.728636Z","thread":"vert.x-eventloop-thread-5"}
{"event":"RESPONSE_RECEIVED","from":"trim","instant":"2020-10-10T22:44:42.728902Z","thread":"vert.x-eventloop-thread-8"}

``` 

### <a name="correlated-events"><a/> Publishing correlated events

In async event-driven systems is extremely difficult to correlate events. Having this solved is a killer future that
saves you from working hours trying to gather all the different events associated with a specific transaction.
In vertx-effect is really easy! As always, functions and composition come to the rescue. Before checking out an example,
let's see what a `Lambdac` is:

```code   
import io.vertx.core.MultiMap;

public interface Lambdac<I, O> extends BiFunction<MultiMap, I, VIO<O>> {}

```

A `Lambdac` is a function that takes two arguments, a map representing the context in which an operation will be
executed,
and the message of type I sent to the verticle across the event bus. You can put the user's email into the context
to filter all the events associated with that email and a random value to distinguish between transactions from the
same email. That's only an example.

```code   
public class UserAccountModule extends VertxModule {

    public static Lambdac<Integer, Boolean> isLegalAge;
    public static Lambdac<String, Boolean> isValidId;
    public static Lambdac<String, Boolean> isValidEmail;
    public static Lambdac<JsObj, Boolean> isValid;

    private static final String IS_VALID_ID = "isValidId";
    private static final String IS_LEGAL_AGE = "isLegalAge";
    private static final String IS_VALID_EMAIL = "isValidEmail";
    private static final String IS_VALID = "isValid";


    @Override
    protected void deploy() {

        this.deploy(IS_LEGAL_AGE, (Integer age) -> VIO.succeed(age > 16));

        this.deploy(IS_VALID_ID, (String id) -> VIO.succeed(!id.isEmpty()));

        this.deploy(IS_VALID_EMAIL, (String email) -> VIO.succeed(!email.isEmpty()));

        Lambdac<JsObj, Boolean> isValid = (context, obj) ->
                AllExp.par(isLegalAge.apply(context, obj.getInt("age")),
                           isValidId.apply(context, obj.getStr("id")),
                           isValidEmail.apply(context, obj.getStr("email"))
                          );
        this.deploy(IS_VALID, isValid);
    }

    @Override
    protected void initialize() {

        isLegalAge = this.trace(IS_LEGAL_AGE);

        isValidId = this.trace(IS_VALID_ID);

        isValidEmail = this.trace(IS_VALID_EMAIL);

        isValid = this.trace(IS_VALID);
    }
}
```

As you can see, we've implemented a module that deploys five verticles and exposes five Lambdac
to interact with them.
The method `trace` returns a `Lambdac` (in the previous example, we used the `ask` method that returns a `Lambda`).
The `isValid` lambda is implemented using the `AllExp` expression. The context is passed through all the lambdas of
the `AllExp` expression.

```code   

Function<JsObj, MultiMap> context=
        user -> MultiMap.caseInsensitiveMultiMap()
                        .add("email",user.getStr("email"));

        JsObj user = JsObj.of("email",JsStr.of("imrafaelmerino@gmail.com"),
                              "age",JsInt.of(17),
                              "id",JsStr.of("03786761")
                              );

        JsObj user1=JsObj.of("email",JsStr.of("example@gmail.com"),
                             "age",JsInt.of(10),
                             "id",JsStr.of("03486761")
                            );

        UserAccountModule.isValid
                         .apply(context.apply(user),
                                user)
                         .get();
        
        UserAccountModule.isValid
                         .apply(context.apply(user1),
                                user1)
                         .get();

```

Let's take a look at the events that are published during the execution of the previous code:

```json  
[
  {
    "event": "MESSAGE_SENT",
    "to": "isValid",
    "context": {
      "email": [
        "example@gmail.com"
      ]
    },
    "message": {
      "email": "example@gmail.com",
      "age": 10,
      "id": "03486761"
    },
    "instant": "2020-10-11T15:09:26.704145Z",
    "thread": "main"
  },
  {
    "event": "MESSAGE_RECEIVED",
    "address": "isValid",
    "context": {
      "email": [
        "example@gmail.com"
      ]
    },
    "instant": "2020-10-11T15:09:26.708157Z",
    "thread": "vert.x-eventloop-thread-8"
  },
  {
    "event": "MESSAGE_SENT",
    "to": "isValid",
    "context": {
      "email": [
        "imrafaelmerino@gmail.com"
      ]
    },
    "message": {
      "email": "imrafaelmerino@gmail.com",
      "age": 17,
      "id": "03786761>"
    },
    "instant": "2020-10-11T15:09:26.708597Z",
    "thread": "main"
  },
  {
    "event": "MESSAGE_SENT",
    "to": "isLegalAge",
    "context": {
      "email": [
        "example@gmail.com"
      ]
    },
    "message": 10,
    "instant": "2020-10-11T15:09:26.709568Z",
    "thread": "vert.x-eventloop-thread-8"
  },
  {
    "event": "MESSAGE_RECEIVED",
    "address": "isLegalAge",
    "context": {
      "email": [
        "example@gmail.com"
      ]
    },
    "instant": "2020-10-11T15:09:26.710185Z",
    "thread": "vert.x-eventloop-thread-4"
  },
  {
    "event": "MESSAGE_SENT",
    "to": "isValidId",
    "context": {
      "email": [
        "example@gmail.com"
      ]
    },
    "message": "03486761",
    "instant": "2020-10-11T15:09:26.710136Z",
    "thread": "vert.x-eventloop-thread-8"
  },
  {
    "event": "MESSAGE_SENT",
    "to": "isValidEmail",
    "context": {
      "email": [
        "example@gmail.com"
      ]
    },
    "message": "example@gmail.com",
    "instant": "2020-10-11T15:09:26.710672Z",
    "thread": "vert.x-eventloop-thread-8"
  },
  {
    "event": "MESSAGE_RECEIVED",
    "address": "isValidId",
    "context": {
      "email": [
        "example@gmail.com"
      ]
    },
    "instant": "2020-10-11T15:09:26.710713Z",
    "thread": "vert.x-eventloop-thread-5"
  },
  {
    "event": "MESSAGE_RECEIVED",
    "address": "isValidEmail",
    "context": {
      "email": [
        "example@gmail.com"
      ]
    },
    "instant": "2020-10-11T15:09:26.711165Z",
    "thread": "vert.x-eventloop-thread-6"
  },
  {
    "event": "MESSAGE_RECEIVED",
    "address": "isValid",
    "context": {
      "email": [
        "imrafaelmerino@gmail.com"
      ]
    },
    "instant": "2020-10-11T15:09:26.711854Z",
    "thread": "vert.x-eventloop-thread-8"
  }
  {
    "event": "MESSAGE_SENT",
    "to": "isLegalAge",
    "context": {
      "email": [
        "imrafaelmerino@gmail.com"
      ]
    },
    "message": 17,
    "instant": "2020-10-11T15:09:26.712138Z",
    "thread": "vert.x-eventloop-thread-8"
  }
]
```

## <a name="spawning-verticles"><a/> Spawning verticles

With vertx-effect, you can spawn verticles, which means that verticles are deployed and
undeployed on the fly. Every time something needs to be computed, a new verticle is deployed.
When the computation is done and the verticle replies, it is undeployed right away. The
goal is to get the most out of the cores! Erlang taught us how to develop concurrent software
that doubles in speed if you double the number of cores without changing a code line: spawning
as many verticles as possible. In Erlang jargon, a verticle is kind of a process.

Will deploy and undeploy verticles continuously slow down the system? It depends, like everything related
to performance. There are times when the cost of reaching a greater level of parallelization is worth it.
Other times it's not. Let's see how long it takes to deploy and undeploy one million verticles:

```code   

@Benchmark
@BenchmarkMode(Mode.AverageTime)
public void deploy_undeploy()throws InterruptedException{
        
        int processes = 1000000;
        CountDownLatch latch = new CountDownLatch(processes);

        for(int i=0; i<processes; i++) {
          vertxRef.deploy("id"+i,
                           Lambda.<JsObj>identity()
                         )
                  .onComplete( vr -> {
                                    vr.result()
                                      .undeploy()
                                      .onSuccess(it->latch.countDown());
                                     }
                             )
                  .get();
        }

        latch.await(10,SECONDS);

        }
```

It takes almost three seconds, 3 microseconds per verticle:

```text
Benchmark                  Mode  Cnt  Score   Error  Units
Processes.deploy_undeploy  avgt   10  2.907 ± 0.658   s/op
```

## <a name="http-client"><a/> Http Client

Here's a comprehensive example demonstrating the effortless creation of an HTTP server, deployment of a handler using
stubs, creation of an HTTP client module, and the sending of both GET and POST requests. Requesting data is simplified
to the extent that it merely involves invoking lambdas that return HTTP responses encapsulated in JsObj.

```java

@ExtendWith(VertxExtension.class)
public class HttpClientMethodsTests {

    private static final int PORT = Port.number.incrementAndGet();
    static HttpClientModule httpClient;

    @BeforeAll
    public static void prepare(final Vertx vertx,
                               final VertxTestContext context
                              ) {
        VertxRef vertxRef = new VertxRef(vertx);
        vertxRef.registerConsumer(VertxRef.EVENTS_ADDRESS,
                                  System.out::println
                                 );
        httpClient = new HttpClientModule(new HttpClientOptions().setDefaultHost("0.0.0.0"),
                                          "myhttp-client");

        HttpRespStub mockReqResp =
                HttpRespStub.when(ALWAYS)
                            .setBodyResp(n -> body -> req -> JsObj.of("req_method",
                                                                      JsStr.of(req.method()
                                                                                  .name()
                                                                              ),
                                                                      "req_body",
                                                                      JsStr.of(body.toString()),
                                                                      "req_uri",
                                                                      JsStr.of(req.uri())
                                                                     )
                                                                  .toPrettyString()
                                        )
                            .setHeadersResp(HttpHeadersRespStub.JSON_CONTENT_TYPE);

        MapExp.seq("json-values-codecs",
                   vertxRef.deployVerticle(new RegisterJsValuesCodecs()),
                   "http-server",
                   new HttpServerBuilder(vertx,
                                         new HttpReqHandlerStub(mockReqResp)
                   ).create(PORT),
                   "http-client",
                   vertxRef.deployVerticle(httpClient)
                  )
              .get()
              .onComplete(Verifiers.pipeTo(context));


    }


    @Test
    public void testGet(VertxTestContext context) {
        VIO<JsObj> getReq = httpClient.get.apply(HttpHeaders.headers()
                                                            .set("method",
                                                                 "get"
                                                                ),
                                                 new GetReq().port(PORT)
                                                             .uri("example")
                                                );
        Verifiers.<JsObj>verifySuccess(resp -> {
                     int status = HttpResp.STATUS_CODE_LENS.get.apply(resp);
                     String bodyResp = HttpResp.STR_BODY_LENS.get.apply(resp);
                     JsObj bodyJsObj = JsObj.parse(bodyResp);
                     return bodyJsObj.getStr("req_method")
                                     .equals("GET")
                            && bodyJsObj.getStr("req_uri")
                                        .equals("example")
                            && bodyJsObj.getStr("req_body").isEmpty()
                            && status == 200;
                 })
                 .accept(getReq,
                         context
                        );

    }

    @Test
    public void testPost(VertxTestContext context) {
        VIO<JsObj> postReq = httpClient.post.apply(HttpHeaders.headers()
                                                              .set("method",
                                                                   "post"
                                                                  ),
                                                   new PostReq("hi".getBytes())
                                                           .port(PORT)
                                                           .uri("example")
                                                  );
        Verifiers.<JsObj>verifySuccess(resp -> {
                     Integer status = HttpResp.STATUS_CODE_LENS.get.apply(resp);
                     String bodyResp = HttpResp.STR_BODY_LENS.get.apply(resp);
                     JsObj bodyJsObj = JsObj.parse(bodyResp);
                     return bodyJsObj.getStr("req_method")
                                     .equals("POST")
                            && bodyJsObj.getStr("req_uri")
                                        .equals("example")
                            && bodyJsObj.getStr("req_body")
                                        .equals("hi")
                            && status == 200;
                 })
                 .accept(postReq,
                         context
                        );
    }


}


```

You can harness the power of the `VIO` API to make requests with retry policies, as illustrated in the following
example:

```java

@ExtendWith(VertxExtension.class)
public class HttpClientTestRetryOnFailure {

    private static final int PORT = Port.number.incrementAndGet();

    static HttpClientModule httpClient;
    static VertxRef vertxRef;
    static HttpReqHandlerStub httpReqHandlerStub;

    @BeforeAll
    public static void prepare(final Vertx vertx,
                               final VertxTestContext context
                              ) {
        vertxRef = new VertxRef(vertx);

        vertxRef.registerConsumer(VertxRef.EVENTS_ADDRESS,
                                  System.out::println
                                 );
        httpClient = new HttpClientModule(new HttpClientOptions().setDefaultHost("0.0.0.0"),
                                          "myhttp-client");

        HttpRespStub mockReqErrorResp =
                HttpRespStub.when((n, req) -> n <= 3)
                            .setStatusCodeResp(n -> body -> req -> 500)
                            .setBodyResp(n -> body -> req -> "{}")
                            .setHeadersResp(HttpHeadersRespStub.JSON_CONTENT_TYPE);

        HttpRespStub mockReqErrorSuccess =
                HttpRespStub.when((n, req) -> n > 3)
                            .setStatusCodeResp(n -> body -> req -> 200)
                            .setBodyResp(n -> body -> req -> "{}")
                            .setHeadersResp(HttpHeadersRespStub.JSON_CONTENT_TYPE);

        httpReqHandlerStub = new HttpReqHandlerStub(mockReqErrorResp,
                                                    mockReqErrorSuccess
        );
        TripleExp.seq(vertxRef.deployVerticle(new RegisterJsValuesCodecs()),
                      new HttpServerBuilder(vertx,
                                            httpReqHandlerStub
                      ).create(PORT),
                      vertxRef.deployVerticle(httpClient)
                     )
                 .get()
                 .onComplete(Verifiers.pipeTo(context));
    }


    @Test
    public void test_retries(VertxTestContext context) {
        Verifiers.<JsObj>verifySuccess(resp -> {
                     Integer status = HttpResp.STATUS_CODE_LENS.get.apply(resp);
                     return status == 200;
                 })
                 .accept(httpClient.get.apply(new GetReq().port(PORT)
                                                          .uri("example")
                                             )
                                       .repeat(resp -> HttpResp.STATUS_CODE_LENS.get.apply(resp) == 500,
                                               limitRetries(3)
                                              ),
                         context
                        );

    }


    @Test
    public void test_retries_constant_delay(VertxTestContext context) {
        httpReqHandlerStub.resetCounter();

        long tic = Instant.now()
                          .toEpochMilli();

        VIO<JsObj> getReq =
                httpClient.get.apply(new GetReq().port(PORT)
                                                 .uri("example")
                                    )
                              .repeat(resp -> HttpResp.STATUS_CODE_LENS.get.apply(resp) == 500,
                                      limitRetries(3).append(constantDelay(vertxRef.delay(Duration.ofMillis(100))))
                                     );

        Verifiers.<JsObj>verifySuccess(resp -> {
            long elapsed = Instant.now()
                                  .toEpochMilli() - tic;
            int status = HttpResp.STATUS_CODE_LENS.get.apply(resp);
            System.out.println(elapsed);
            return status == 200 && elapsed >= 300;
        }).accept(getReq,
                  context
                 );
    }

}


```

## <a name="oauth-client"><a/> Oauth Http client

Creating an HTTP client with OAuth client credentials support is made exceptionally straightforward, liberating you from
the intricacies of obtaining and refreshing tokens. The provided code showcases how to easily set up a resilient HTTP
client using the VIO API, complete with token retrieval and automatic refresh. This not only streamlines the process but
also enables you to seamlessly integrate retry policies and other reliability features.

```java

@ExtendWith(VertxExtension.class)
public class ClientCredentialsModuleTest {

    static ClientCredentialsModule httpClient;
    static ClientCredentialsModuleBuilder builder;
    static VertxRef vertxRef;
    static int port = Port.number.incrementAndGet();


    @BeforeAll
    public static void prepare(final Vertx vertx,
                               final VertxTestContext context
                              ) {
        builder =
                new ClientCredentialsModuleBuilder(new HttpClientOptions().setDefaultPort(port)
                                                                          .setDefaultHost("0.0.0.0"),
                                                   "my-httpclient",
                                                   new AccessTokenRequest("client_id",
                                                                          "client_secret"
                                                   ));

        httpClient = builder.createModule();

        vertxRef = new VertxRef(vertx);

        vertxRef.registerConsumer(VertxRef.EVENTS_ADDRESS,
                                  System.out::println
                                 );

        PairExp.seq(vertxRef.deployVerticle(new RegisterJsValuesCodecs()),
                    vertxRef.deployVerticle(httpClient)
                   )
               .onComplete(Verifiers.pipeTo(context))
               .get();

    }


    @Test
    public void test_get_success_after_three_retries_getting_token(Vertx vertx,
                                                                   VertxTestContext context
                                                                  ) {


        builder.setAccessTokenReqRetryPolicy(e -> true,
                                             RetryPolicies.limitRetries(3)
                                            );

        List<HttpRespStub> httpRespStubs =
                List.of(when(REQ_LET.apply(3))
                                .setBodyResp(HttpBodyRespStub.cons(JsObj.of("token_found",
                                                                            FALSE
                                                                           )
                                                                  )
                                            )
                                .setStatusCodeResp(HttpStatusCodeRespStub._401)
                                .setHeadersResp(HttpHeadersRespStub.JSON_CONTENT_TYPE),
                        when(FORTH_REQ)
                                .setBodyResp(HttpBodyRespStub.cons(JsObj.of("token_found",
                                                                            TRUE,
                                                                            "access_token",
                                                                            JsStr.of("foooo")
                                                                           )
                                                                  )
                                            )
                                .setStatusCodeResp(HttpStatusCodeRespStub._200)
                                .setHeadersResp(HttpHeadersRespStub.JSON_CONTENT_TYPE),
                        when(REQ_GT.apply(4))
                                .setBodyResp(HttpBodyRespStub.cons(JsObj.of("name",
                                                                            JsStr.of("Rafael")
                                                                           ))
                                            )
                                .setStatusCodeResp(HttpStatusCodeRespStub._200)
                                .setHeadersResp(HttpHeadersRespStub.JSON_CONTENT_TYPE));

        VIO<JsObj> getReq = httpClient.getOauth.apply(new GetReq().uri("/name"));

        new HttpServerBuilder(vertx,
                              new HttpReqHandlerStub(httpRespStubs

                              )
        ).create(port)
         .get()
         .onSuccess(server -> {
             Verifiers.<JsObj>verifySuccess(resp -> HttpResp.STATUS_CODE_LENS.get.apply(resp) == 200)
                      .accept(getReq,
                              context
                             );
         });

    }

  @Test
  public void test_get_success_after_three_retries(Vertx vertx,
                                                   VertxTestContext context
                                                  ) {


    List<HttpRespStub> httpRespStubs =
            List.of(when(REQ_LET.apply(3))
                            .setBodyResp(HttpBodyRespStub.cons(JsObj.of("token_found",
                                                                        FALSE
                                                                       )
                                                              )
                                        )
                            .setStatusCodeResp(HttpStatusCodeRespStub._401),
                    when(FORTH_REQ)
                            .setBodyResp(HttpBodyRespStub.cons(JsObj.of("token_found",
                                                                        TRUE,
                                                                        "access_token",
                                                                        JsStr.of("foooo")
                                                                       )
                                                              )
                                        )
                            .setStatusCodeResp(HttpStatusCodeRespStub._200),
                    when(REQ_LET.apply(7))
                            .setBodyResp(c -> body -> req -> {
                                           req.response().close();
                                           return "{}";
                                         }
                                        )
                            .setStatusCodeResp(HttpStatusCodeRespStub._500),
                    when(REQ_GT.apply(7))
                            .setBodyResp(HttpBodyRespStub.cons(JsObj.of("name",
                                                                        JsStr.of("Rafael")
                                                                       ))
                                        )
                            .setStatusCodeResp(HttpStatusCodeRespStub._200));

    VIO<JsObj> getReq = httpClient.getOauth.apply(new GetReq().uri("/name"))
                                           .retry(RetryPolicies.limitRetries(3));

    new HttpServerBuilder(vertx,
                          new HttpReqHandlerStub(httpRespStubs)
    ).create(port)
     .get()
     .onSuccess(server -> {
       Verifiers.<JsObj>verifySuccess(resp -> HttpResp.STATUS_CODE_LENS.get.apply(resp) == 200)
                .accept(getReq,
                        context
                       );
     });


  }
}

```

## <a name="http-server"><a/> Http server

TO be documented but implemented!

## <a name="testing"><a/> Testing

### <a name="vio-stubs"><a/> VIO stubs

TO be documented but implemented!

### <a name="http-stubs"><a/> Http server stubs

TO be documented but implemented!

## <a name="requirements"><a/> Requirements

Java 17 or greater

## <a name="installation"><a/> Installation

```xml

<dependency>
    <groupId>com.github.imrafaelmerino</groupId>
    <artifactId>vertx-effect</artifactId>
    <version>4.0.0</version>
</dependency>

```

Java 21 or greater

## <a name="installation"><a/> Installation

```xml

<dependency>
    <groupId>com.github.imrafaelmerino</groupId>
    <artifactId>vertx-effect</artifactId>
    <version>5.0.0</version>
</dependency>

```

## <a name="rp"><a/> Related projects

