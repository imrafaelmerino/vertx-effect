<img src="./logo/package_twitter_swe2n4mg/black/full/coverphoto/black_logo_white_background.png" alt="logo"/>


[![Build Status](https://travis-ci.com/imrafaelmerino/vertx-effect.svg?branch=master)](https://travis-ci.com/imrafaelmerino/vertx-effect)
[![CircleCI](https://circleci.com/gh/imrafaelmerino/vertx-effect/tree/master.svg)](https://circleci.com/gh/imrafaelmerino/vertx-effect/tree/master)
[![codecov](https://codecov.io/gh/imrafaelmerino/vertx-effect/branch/master/graph/badge.svg?token=30SaJ84Ctd)](https://codecov.io/gh/imrafaelmerino/vertx-effect)

[![Javadocs](https://www.javadoc.io/badge/com.github.imrafaelmerino/vertx-effect.svg)](https://www.javadoc.io/doc/com.github.imrafaelmerino/vertx-effect)
[![Maven](https://img.shields.io/maven-central/v/com.github.imrafaelmerino/vertx-effect/3.0.0)](https://search.maven.org/artifact/com.github.imrafaelmerino/vertx-effect/3.0.0/jar)
[![](https://jitpack.io/v/imrafaelmerino/vertx-effect.svg)](https://jitpack.io/#imrafaelmerino/vertx-effect)

[![Maintainability Rating](https://sonarcloud.io/api/project_badges/measure?project=imrafaelmerino_vertx-effect&metric=sqale_rating)](https://sonarcloud.io/dashboard?id=imrafaelmerino_vertx-effect)

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
- [Reactive http client](#httpclient)
- [Reactive OAuth http client](#oauth-httpclient)
    - [Client credentials flow](#clientcredentials)
    - [Authorization flow](#authorizationflow)
- [Java Flight Recorder support](#jfr)    
- [Requirements](#requirements)
- [Installation](#installation)
- [Related projects](#rp)
- [Release process](#release)

## <a name="manifesto"><a/> vertx-effect manifesto
    . The more verticles, the better.
    . A verticle must do only one thing.
    . Use persistent data structures.
    . Systems will fail, be prepared.
    . Simplicity matters.
    . If there is a bug and you can't spot it quickly, then there are two bugs. Fix both of them.

## <a name="persistendata"><a/>How persistent data structures makes a difference working with actors 

**Every message that can be sent across the event bus has an associated MessageCodec**. Go to the package 
_io.vertx.core.eventbus.impl.codecs_ to check out what types Vertx supports. The Json implemented in Vertx with 
**Jackson** has the codec _JsonObjectMessageCodec_.
 
When a verticle sends a message to the event bus, **Vertx intercepts that message and calls its codec's transform method**. 
Since **Jackson** is not immutable at all, the _transform_ method of _JsonObjectMessageCodec_ has to make a copy of the 
message before sending it to the event bus. 

```java
// Vertx impl 
public JsonObject transform(JsonObject message) {
    return message.copy();
}
```

Vertx-effect uses [json-values](https://github.com/imrafaelmerino/json-values). It's a truly immutable Json implemented with 
persistent data structures. Its codec's _transform_ method **returns the same message sent by the verticle without making any copy**.

```java
// vertx-effect impl
public JsObj transform(final JsObj message) {
   return message;
}
```

Using Jackson, the more verticles you have, the more messages have to be copied. It puts a lot of pressure on the garbage 
collector and decreasing performance. Furthermore, the bigger the Jsons are, the longer it takes to copy them. **This is a 
problem since you need to create as many verticles as possible to get the most out of the actor model.**

Find below the result of a benchmark carried out with [jmh](https://openjdk.java.net/projects/code-tools/jmh/), comparing 
the Jsons from Jackson and json-values. The benchmark consists of sending messages to a verticle that just returns them 
back (go to [JacksonVsJsValues](https://github.com/imrafaelmerino/vertx-effect/blob/master/performance/src/main/java/vertx/effect/performance/benchmarks/JacksonVsJsValues.java) for further details on the benchmark).


```text
Benchmark                     Mode    Cnt      Score     Error      Units
JacksonVsJsValues.jackson     thrpt    5    38816.552 ± 8916.894    ops/s
JacksonVsJsValues.jsonValues  thrpt    5    51183.223 ± 10154.660   ops/s
```

## <a name="fewlinesofcode"><a/>vertx-effect in a few lines of code

```java
import jsonvalues.JsInt;
import jsonvalues.JsObj;
import jsonvalues.JsStr;
import jsonvalues.spec.JsObjSpec;

import static jsonvalues.JsPath.path;
import static jsonvalues.spec.JsSpecs.*;

import vertx.effect.Val;
import vertx.effect.Validators;
import vertx.effect.VertxModule;
import vertx.effect.exp.JsArrayExp;
import vertx.effect.exp.JsObjExp;
import vertx.effect.λ;

public class MyModule extends VertxModule {

    public static λ<String, String> toLowerCase, toUpperCase;
    public static λ<Integer, Integer> inc;
    public static λ<JsObj, JsObj> validate, validateAndMap;

    @Override
    protected void deploy() {

        this.deploy("toLowerCase",
                    (String str) -> Val.succeed(str.toLowerCase())
                   );
        this.deploy("toUpperCase",
                    (String str) -> Val.succeed(str.toUpperCase())
                   );
        this.deploy("inc",
                    (Integer n) -> Val.succeed(n + 1)
                   );

        // json-values uses specs to define the structure of a Json: {a:int,b:[str,str]} 
        JsObjSpec spec = JsObjSpec.strict("a",
                                          integer,
                                          "b",
                                          tuple(str,
                                                str
                                               )
                                         );
        this.deploy("validate",
                    Validators.validateJsObj(spec)
                   );

        λ<JsObj, JsObj> map = obj ->
                JsObjExp.parallel("a",
                                  inc.apply(obj.getInt("a"))
                                     .map(JsInt::of),
                                  "b",
                                  JsArrayExp.parallel(toLowerCase.apply(obj.getStr(path("/b/0")))
                                                                 .map(JsStr::of),
                                                      toUpperCase.apply(obj.getStr(path("/b/1")))
                                                                 .map(JsStr::of)
                                                     )
                                 )
                        .retry(RetryPolicies.limitRetries(2));
        this.deploy("validateAnMap",
                    (JsObj obj) -> validate.apply(obj)
                                           .flatMap(map)
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
A lambda is just a function that takes an input and produces an output. In the above example, _MyModule_ deploys 
five verticles. It's worth mentioning how the verticle _ValidateAndMap_ is defined using composition and the expressions 
_JsObjExp_ and _JsArrayExp_. It shows the essence and the goal of vertx-effect. Later on, we'll see more expressions like 
**Cons**, **Cond**, **Case**, **IfElse**, **All**, **Any**, **Pair**, **Triple**, etc.

_ValidateAndMap_ sends a message to _validate_. If the message matches the given spec, _ValidateAndMap_ computes the output 
sending messages to the verticles _inc_, _toLowerCase_, and _toUpperCase_ and composing a Json from their responses in parallel. 
You can operate sequentially instead of in parallel using the constructors _JsObjExp.sequential_ and _JsArrayExp.sequential_. 
Thanks to the _retry_ function, if _any_ verticle failed to compute their value, it would retry the computation up to two times.

It's important to notice that you can still send messages to the module verticles using the Vertx API, but one of the 
points of vertx-effect is to use functions for that.

Let's write some tests. Vertx doesn't support json-values, so we need to register a _MessageCodec_ to send its persistent 
Json across the event bus.

```java
import io.vertx.core.Vertx;
import io.vertx.junit5.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import jsonvalues.JsArray;
import jsonvalues.JsInt;
import jsonvalues.JsObj;
import vertx.effect.RegisterJsValuesCodecs;
import vertx.effect.VertxRef;
import vertx.effect.exp.Pair;

@ExtendWith(VertxExtension.class)
public class TestMyModule {

  @BeforeAll
  // register a MessageCodec for json-values and deploy MyModule
  public static void prepare(final Vertx vertx,
                             final VertxTestContext context) {
    VertxRef ref = new VertxRef(vertx);
    Pair.sequential(ref.deployVerticle(new RegisterJsValuesCodecs()), 
                    ref.deployVerticle(new MyModule())
                   )
        .onSuccess(ids -> context.completeNow())
        .get();
    }
 
    @Test
    public void empty_json_is_sent_and_failure_is_received(VertxTestContext context)  {

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

        JsObj input = JsObj.of("a", JsInt.of(1), "b", JsArray.of("FOO","foo"));
        
        JsObj expected = JsObj.of("a", JsInt.of(2), "b", JsArray.of("foo","FOO"));

        MyModule.validateAndMap.apply(input)
                               .onSuccess(output -> {
                                   context.verify(() -> {
                                       Assertions.assertEquals(expected,output);
                                       context.completeNow();
                                   });
                               })
                               .get();
    }
}
```

**Lambdas are just functions, so you can test them without deploying any verticle!**

```java

 λ<String, String> toLowerCase = str -> Val.succeed(str.toLowerCase());
 
 λ<String, String> toUpperCase = str -> Val.succeed(str.toUpperCase()); 
 
 λ<Integer, Integer> inc = n -> Val.succeed(n+1);
 
 JsObjSpec spec = JsObjSpec.strict("a", integer, "b", tuple(str, str));
 
 λ<JsObj, JsObj> validate = Validators.validateJsObj(spec);
 
 λ<JsObj, JsObj> map = obj-> 
          JsObjExp.parallel("a", inc.apply(obj.getInt("a")).map(JsInt::of),
                            "b", JsArrayExp.parallel(toLowerCase.apply(obj.getStr(path("/b/0")))
                                                                .map(JsStr::of),
                                                     toUpperCase.apply(obj.getStr(path("/b/1")))
                                                                .map(JsStr::of)
                                                     )
                            );   
 
 @Test
 public void valid_json_is_validated_and_mapped(VertxTestContext context) {

    JsObj input = JsObj.of("a", JsInt.of(1), "b", JsArray.of("FOO","foo"));
        
    JsObj expected = JsObj.of("a", JsInt.of(2), "b", JsArray.of("foo","FOO"));

    MyModule.validateAndMap.apply(input)
                           .onSuccess(output -> {
                                   context.verify(() -> {
                                       Assertions.assertEquals(expected,output);
                                       context.completeNow();
                                   });
                               })
                           .get();
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

```java 

Future<Customer> a = insertDb(customer);

Future<Customer> b = insertDb(customer);

```

Both calls can fail, or they can create two different customers, or one of them can fail, who knows. That code is not 
referentially transparent. For obvious reasons, you can't do the following refactoring:

```java

Future<Customer> c = insertDb(customer)

Future<Customer> a = c;

Future<Customer> b = c;

```

A Vertx future represents an asynchronous effect. We don't want to block the event loop because of the latency of a computation. 
**Haskell** has proven to us how laziness is an essential property to stay pure. We need to define an immutable and lazy data structure 
that allows us to control the effect of latency.

Since Java 8, we have suppliers. They are indispensable to do FP in Java. Let's start defining what a value is in vertx-effect:
  
```java

import java.util.function.Supplier
import io.vertx.core.Future

public abstract class Val<O> extends Supplier<Future<O>> {
    //from a constant
    public static Val<O> succeed(O constant);
    
    //from a exception
    public static Val<O> fail(Throwable error);
    
    // from an effect
    public static Val<O> effect(Supplier<Future<O>> effect);
    
}

```

A **Val** of type **O** is a supplier that will return a Vertx future of type **O**. **It describes (and not execute) an asynchronous 
effect that will compute a value of type O**.

If we turn Future into Val in the previous example:

```java 

Val<Customer> a = Val.effect( ()->insertDb(customer) );

Val<Customer> b = Val.effect( ()->insertDb(customer) );

```

The above example is entirely equivalent to:

```java

Val<Customer> c = Val.effect( ()->insertDb(customer) ); 

Val<Customer> a = c;

Val<Customer> b = c;

```

This property is fundamental. Whenever you see the expression _insertDb(customer)_ in your program, you can think of 
it as it was c. Pure FP programming helps us reason about the programs we write. Val is lazy. It's a description 
of an effect. **In FP, we describe programs, and it's at the very last moment when they're executed.**

I always wanted to name **λ** to something, and I finally got the chance!

```java

import java.util.function.Function

public interface λ<I,O> extends Function<I, Val<O>> { }

```

A lambda is a function that returns a **Val** of type **O** given a type **I**. **It models the communication with a verticle**: a message 
is sent, the verticle receives and processes the message, and replies with a response. The message and the answer have to 
be of a type that can be sent across the event bus; otherwise, you must implement a [MessageCodec](https://vertx.io/docs/apidocs/io/vertx/core/eventbus/MessageCodec.html). 
 
## <a name="exp"><a/> Expressions 

**Using expressions and function composition is how we deal with complexity in functional programming**. 
Let's go over the essential expressions in vertx-effect:

- **Val constructors** 

```java
import io.vertx.core.Future

public final class Cons<O> implements Val<O>{...}

Val<String> str = Val.succeed("hi"); // from a constant

// from an error
Val<Throwable> error = Val.fail(new RuntimeException("something went wrong :("));

//from effects
Future<JsObj> getProfile(final String id){...}
Val<JsObj> profile = Val.effect( () -> getProfile(id)); // from a Future

Val<Long> realTime = Val.effect(() -> System.currentTimeMillis() );
``` 


- **IfElse**. If the predicate returns true, it executes and returns the consequence; otherwise, the alternative. 


```java

public final class IfElse<O> extends Val<O> {}

IfElse.<O>predicate(Val<Bool>)
         .consequence(Val<O>)
         .alternative(Val<O>)

```
 
- **Cond**. It's a set of branches and a default value. Each branch consists of a predicate and a value. 
It computes and returns the value of the first branch, which predicate is true. If no predicate is true, it returns the 
default value, which is the last value clause. 


```java


Cond.<O>of(Val<Boolean>, Val<O>,
           Val<Boolean>, Val<O>,
           Val<Boolean>, Val<O>,
           Val<O>
          )
                        
``` 
 

- **Case**. The case construct implements multiple predicate-value branches like the Cond construct. However, it evaluates 
a type I value and allows multiple value clauses based on evaluating that value. 


```java

new Case<I,O>(Val<I>).of(cons of type I, Val<O>,
                         cons of type I, Val<O>,        
                         cons of type I, Val<O>,
                         Val<O>
                        );      

// reduces to Wednesday
new Case<Integer,String>(Val.succeed(3)).of(1, Val.succeed("Monday"),
                                             2, Val.succeed("Tuesday"),
                                             3, Val.succeed("Wednesday"),
                                             4, Val.succeed("Thursday"),
                                             5, Val.succeed("Friday"),
                                             Val.succeed("weekend")
                                            );
```

The same as before but using lists instead of constants.

```java

new Case<I,O>(Val<I>).of(List<I>, Val<O>,
                         List<I>, Val<O>,        
                         List<I>, Val<O>,
                         Val<O>
                        );      
        
// reduces to third week
new Case<Integer,String>(Val.succeed(20))
                 .of(asList(1, 2, 3, 4, 5, 6, 7), Val.succeed("first week"),
                     asList(8, 9, 10, 11, 12, 13, 14), Val.succeed("second week"),
                     asList(15, 16, 17, 18, 19, 20, 10), Val.succeed("third week"),
                     asList(21, 12, 23, 24, 25, 26, 27), Val.succeed("forth week"),
                     Val.succeed("last days of the month")
                    );
```
    

- **All** and **Any**. You can compute all the values either in parallel or sequentially.


```java

All.parallel(Val<Boolean>,....)
All.sequential(Val<Boolean>,....)
                         
Any.parallel(Val<Boolean>,...)
Any.sequential(Val<Boolean>,...)

```  

- **Pair**. A pair is a tuple of two elements. Each element can be computed either in parallel or sequentially.


```java

Val<Tuple2<A,B>  pair = Pair.parallel(Val<A>, Val<B>);

Val<Tuple2<A,B>  pair = Pair.sequential(Val<A>, Val<B>);

```
  
- **Triple**. A triple is a tuple of three elements. Each element can be computed either in parallel or sequentially. 


```java

Val<Tuple3<A,B,C> triple = Triple.parallel(Val<A>, Val<B>, Val<C>);

Val<Tuple3<A,B,C> triple = Triple.sequential(Val<A>, Val<B>, Val<C>);

```
 
- **JsObjExp** and **JsArrayExp**. 
_JsObjExp_ and _JsArrayExp_ are data structures that look like raw Json. You can compute all the values either in parallel
or sequentially. You can mix all the expressions we've seen so far and nest them, going as deep as necessary,
like in the following example:


```java

IfElse<JsStr> a = IfElse.<JsStr>predicate(Val<Boolean>)
                        .consequence(Val<JsStr>)
                        .alternative(Val<JsStr>); 

JsArrayExp b = JsArrayExp.sequential(new Case<Integer,JsValue>(n).of(1, Val<JsValue>,
                                                                     2, Val<JsValue>,
                                                                     Val<JsValue> 
                                                                    ),
                                     Cond.of(Val<Boolean>, Val<JsValue>,
                                             Val<Boolean>, Val<JsValue>,
                                             Val<JsValue>
                                            )
                                     );

JsObjExp c = JsObjExp.parallel("d", Any.sequential(Val<Boolean>, Val<Boolean>).map(JsBool::of),
                               "e", All.parallel(Val<Boolean>, Val<Boolean>).map(JsBool::of),
                               "f", JsArrayExp.parallel(Val<JsValue>,
                                                        Val<JsValue> 
                                                       ) 
                              )

JsObjExp obj = JsObjExp.parallel("a",a,
                                 "b",b,
                                 "c",c 
                                );
```

It's important to notice that any value of the above expressions can be computed by a different cluster machine's verticle. 
Imagine ten machines collaborating to compute a JsObj. Is not this amazing?

- **ListExp and MapExp**

They represent sequences and maps. **Modules use them internally**. For example, the deploy method uses a MapExp to put the 
deployed verticles using their addresses as keys. They also use a ListExp when more than a verticle instance is deployed. 
As with the other expressions, you can compute their values either in parallel or sequentially.

```

MapExp<String> map = MapExp.parallel("a",Val<String>,
                                     "b",Val<String>,
                                     "c",Val<String>
                                     );

ListExp<Integer> seq = ListExp.parallel(Val<Integer>,Val<Integer>);

Val<Integer> firstFinishing = seq.race();

```

The _race_ function returns the value that finishes first. You can race a _JsArrayExp_ as well.

## <a name="reactive"><a/> Being reactive 

Find below some of the most critical operations defined in the _Val_ interface that will help us make our code more resilient:

```java
import vertx.effect.RetryPolicy;

public interface Val<O> extends Supplier<Future<O>> {
    Val<O> retry(RetryPolicy policy);

    Val<O> retry(Predicate<Throwable>,
                 RetryPolicy policy);

    Val<O> repeat(Predicate<O> predicate,
                  RetryPolicy policy);

    Val<O> recoverWith(λ<Throwable, O> fn);

    Val<O> fallbackTo(λ<Throwable, O> fn);

    Val<O> recoverWith(λ<Throwable, O> fn);
}
 ``` 

**recoverWith**:  it switches to an alternative lambda when a failure happens. 

**fallbackTo**: It's like recoverWith, but if the second lambda fails too, it returns the first one error. 

**recover**: returns a constant if the computation fails. 

**retry**: retries the computation if an error happens. You can define a predicate to retry only the specified errors.
Retry policies are created in a very declarative and composable way, for example: 

```java
import static vertx.effect.RetryPolicies.*

Delay oneHundredMillis = vertxRef.sleep(Duration.ofMillis(100));
Delay oneSec = vertxRef.sleep(Duration.ofSeconds(1));

// up to five retries waiting 100 ms 
constantDelay(oneHundredMillis).append(limitRetries(5))

//during 3 seconds up to 10 times     
limitRetries(10).limitRetriesByCumulativeDelay(Duration.ofSeconds(3))    

//5 times without delay and then, if it keeps failing, an incremental delay from 100 ms up to 1 second
limiteRetries(5).followedBy(incrementalDelay(oneHundredMillis).capDelay(oneSec))

```

There are very interesting policies implemented based on [this article](https://aws.amazon.com/blogs/architecture/exponential-backoff-and-jitter/):
exponential backoff, full jitter, equal jitter, decorrelated jitter etc

**repeat**: When you get a not expected value (a failure) and want to repeat the computation. A predicate is
specified to catch the failures. You can define any imaginable policy as well. Imagine you make a http request
and you get a 500. That's not an error, it's a server failure. You can repeat the request according to a 
policy.

For expressions like **Cond**, **Case**, **IfElse**, **All**, **Any**, **Pair**, **Triple**, you can retry 
each value of the expression instead of the overall expresion with the methods:

```java
    Val<O> retryEach(RetryPolicy policy);

    Val<O> retryEach(Predicate<Throwable>,
                     RetryPolicy policy);

```

## <a name="modules"><a/> Modules
 
In vertx-effect, **a module is a special verticle whose purpose is to deploy other verticles and expose lambdas 
to communicate with them**. Let's put an example.

```java
import jsonvalues.JsObj;
import jsonvalues.JsStr;
import vertx.effect.Val;
import vertx.effect.VertxModule;
import vertx.effect.λ;

public class MyModule extends VertxModule {

    private static final String REMOVE_NULL_ADDRESS = "removeNull";
    private static final String TRIM_ADDRESS = "trim";

    public static λ<JsObj, JsObj> removeNull;
    public static λ<JsObj, JsObj> trim;

    @Override
    public void deploy() {

        this.deploy(REMOVE_NULL_ADDRESS,
                    (JsObj o) -> Val.succeed(o.filterAllValues(pair -> pair.value.isNotNull()))
                   );

        Function<JsValue, JsValue> trim = JsStr.prism.modify.apply(String::trim);
        this.deploy(TRIM_ADDRESS,
                    (JsObj o) -> Val.succeed(o.mapAllValues(pair -> trim.apply(pair.value)))
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
    . The deploy method, where the module deploys the verticles.
    . The initialize method, where the module initializes the lambdas.

In our example, we are using the persistent and immutable Json from json-values.
The **ask** method returns a lambda to establish bidirectional communication with a verticle. In contrast, the **tell** 
method would return a consumer because a response is either not expected or ignored. Let's deploy our module and do some testing.
We usually divide modules into four main blocks:


```java
 @BeforeAll
 public static void prepare(final Vertx vertx,
                            final VertxTestContext context) 
 {
    VertxRef vertxRef = new VertxRef(vertx);

    // prints out events published by vertx-effect
    vertxRef.registerConsumer(EVENTS_ADDRESS, System.out::println); 

    Pair.sequential(vertxRef.deployVerticle(new RegisterJsValuesCodecs()),
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
    λ<JsObj, JsObj> removeAndTrim = MyModule.removeNull.andThen(MyModule.trim);

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
To send the persistent objects from [json-values](https://imrafaelmerino.github.io/json-values/) across the event bus, 
we need to register some codecs. The verticle RegisterJsValuesCodecs does this task.
The VertxRef class is a wrapper around the Vertx instance to deploy and spawn verticles from lambdas. Modules use this class internally.

The _VertxRef_ class is a wrapper around the Vertx instance to deploy and spawn verticles from lambdas. Modules use this class internally.


## <a name="logging"><a/> Logging 
Logging is essential in software. There are many logging libraries. Sometimes it is not clear what dependencies you have 
to use because there isn't a standard solution. Each library uses its own. I didn't want to be opinionated. At the same 
time, I wanted to provide a simple and decouple solution to know what is going on in any system using vertx-effect. 
That's why I decided to publish remarkable events in a specific address. If you want to use your favorite slf4j implementation,
 just implement it in a consumer. On the other hand, consuming all those events during testing will give you instant feedback 
 on your system and agility spotting bugs. You can disable this future with the Java system property **-D"vertx.effect.enable.log.events"=false**.

### <a name="events"><a/> Publishing events 
**vertx-effect** publishes events to the address **vertx-effect-events**. Find below some of the most important predefined
events:

    - DEPLOYED_VERTICLE 
    - UNDEPLOYED_VERTICLE
    - SENT_MESSAGE: a message is sent to a verticle
    - RECEIVED_MESSAGE: a verticle received a message
    - REPLIED_RESP: a verticle replied with a message
    - REPLIED_FAILURE: a verticle replied with an error
    - RECEIVED_RESP: a response is received from a verticle
    - RECEIVED_FAILURE: an error is received from a verticle
    - INTERNAL_ERROR_XXX: go to github and open and issue. 

An example from the previous example would be:

```json

{"event":"DEPLOYED_VERTICLE","address":"removeNull","instant":"2020-10-10T22:44:42.687633Z","id":"3de92ef8-777f-4110-aa45-442fc41900c6","thread":"vert.x-eventloop-thread-1"}
{"event":"DEPLOYED_VERTICLE","class":"vertx.effect.RegisterJsValuesCodecs","instant":"2020-10-10T22:44:42.682624Z","id":"73181043-ae38-4819-b7de-02f303fcc155","thread":"vert.x-eventloop-thread-3"}
{"event":"DEPLOYED_VERTICLE","address":"trim","instant":"2020-10-10T22:44:42.701293Z","id":"a866ffdc-38c8-4da2-bcc0-c9f4881f5139","thread":"vert.x-eventloop-thread-1"}
{"event":"DEPLOYED_VERTICLE","class":"vertx.effect.MyModule","instant":"2020-10-10T22:44:42.703410Z","id":"1473dff2-075c-4fd8-be42-cebcf0a890a0","thread":"vert.x-eventloop-thread-6"}

{"event":"SENT_MESSAGE","to":"removeNull","message":{"a":"  hi  ","b":null,"c":{"d":"  bye  ","e":null}},"instant":"2020-10-10T22:44:42.710447Z","thread":"main"}
{"event":"RECEIVED_MESSAGE","address":"removeNull","instant":"2020-10-10T22:44:42.713981Z","thread":"vert.x-eventloop-thread-4"}
{"event":"REPLIED_RESP","address":"removeNull","message":{"c":{"d":"  bye  "},"a":"  hi  "},"instant":"2020-10-10T22:44:42.723013Z","thread":"vert.x-eventloop-thread-4"}
{"event":"RECEIVED_RESP","from":"removeNull","instant":"2020-10-10T22:44:42.723225Z","thread":"vert.x-eventloop-thread-8"}

{"event":"SENT_MESSAGE","to":"trim","message":{"c":{"d":"  bye  "},"a":"  hi  "},"instant":"2020-10-10T22:44:42.723635Z","thread":"vert.x-eventloop-thread-8"}
{"event":"RECEIVED_MESSAGE","address":"trim","instant":"2020-10-10T22:44:42.724047Z","thread":"vert.x-eventloop-thread-5"}
{"event":"REPLIED_RESP","address":"trim","message":{"a":"hi","c":{"d":"bye"}},"instant":"2020-10-10T22:44:42.728636Z","thread":"vert.x-eventloop-thread-5"}
{"event":"RECEIVED_RESP","from":"trim","instant":"2020-10-10T22:44:42.728902Z","thread":"vert.x-eventloop-thread-8"}

``` 

### <a name="correlated-events"><a/> Publishing correlated events  
In async event-driven systems is extremely difficult to correlate events. Having this solved is a killer future that 
saves you from working hours trying to gather all the different events associated with a specific transaction. 
In vertx-effect is really easy! As always, functions and composition come to the rescue. Before checking out an example, 
let's see what a λc is:

```java
import io.vertx.core.MultiMap;

public interface λc<I, O> extends BiFunction<MultiMap, I, Val<O>> {}

```

A λc is a function that takes two arguments, a map representing the context in which an operation will be executed, 
and the message of type I sent to the verticle across the event bus. You can put the user's email into the context 
to filter all the events associated with that email and a random value to distinguish between transactions from the 
same email. That's only an example.

```java
    public static λc<Integer, Boolean> isLegalAge;
    public static λc<String, Boolean> isValidId;
    public static λc<JsObj, Boolean> isValidAddress;
    public static λc<String, Boolean> isValidEmail;
    public static λc<JsObj, Boolean> isValid;

    private static final String IS_VALID_ID = "isValidId";
    private static final String IS_LEGAL_AGE = "isLegalAge";
    private static final String IS_VALID_ADDRESS = "isValidAddress";
    private static final String IS_VALID_EMAIL = "isValidEmail";
    private static final String IS_VALID = "isValid";

    @Override
    protected void initialize() {
        isLegalAge = this.trace(IS_LEGAL_AGE);
  
        isValidId = this.trace(IS_VALID_ID);
  
        isValidEmail = this.trace(IS_VALID_EMAIL);
  
        isValid = this.trace(IS_VALID);
    }

    @Override
    protected void deploy() {

        this.deploy(IS_LEGAL_AGE, (Integer age) -> Val.succeed(age > 16));

        this.deploy(IS_VALID_ID, (String id) -> Val.succeed(!id.isEmpty()));
        
        this.deploy(IS_VALID_EMAIL, (String email) -> Val.succeed(!email.isEmpty()));

        λc<JsObj, Boolean> isValid = (context, obj) ->
                All.parallel(isLegalAge.apply(context,
                                              obj.getInt("age")
                                             ),
                             isValidId.apply(context,
                                             obj.getStr("id")
                                            ),
                             isValidEmail.apply(context,
                                                obj.getStr("email")
                                               )
                            );
        this.deploy(IS_VALID, isValid);
    }
}
```

As you can see, we've implemented a module that deploys five verticles and exposes five λc to interact with them. 
The method trace returns a λc (in the previous example, we used the ask method that returns a λ). The isValid lambda 
is implemented using the _All_ expression. The context is passed through all the lambdas of the And expression.

```java
 Function<JsObj,Multimap> context = user -> MultiMap.caseInsensitiveMultiMap()
                                                    .add("email", user.getStr("email")); 
 
 JsObj user = JsObj.of("email", JsStr.of("imrafaelmerino@gmail.com"),
                       "age", JsInt.of(17), 
                       "id", JsStr.of("03786761")
                      );
 JsObj user1 = JsObj.of("email", JsStr.of("example@gmail.com"),
                        "age", JsInt.of(10),
                        "id", JsStr.of("03486761")
                       );

 UserAccountModule.isValid.apply(contex.apply(user), user).get();
 UserAccountModule.isValid.apply(contex.apply(user1), user1).get();
      
```

Let's take a look at the events that are published during the execution of the previous code:

```json
{"event":"SENT_MESSAGE","to":"isValid","context":{"email":["example@gmail.com"]},"message":{"email":"example@gmail.com","age":10, "id": "03486761"},"instant":"2020-10-11T15:09:26.704145Z","thread":"main"}
{"event":"RECEIVED_MESSAGE","address":"isValid","context":{"email":["example@gmail.com"]},"instant":"2020-10-11T15:09:26.708157Z","thread":"vert.x-eventloop-thread-8"}
{"event":"SENT_MESSAGE","to":"isValid","context":{"email":["imrafaelmerino@gmail.com"]},"message":{"email":"imrafaelmerino@gmail.com","age":17,"id":"03786761>"},"instant":"2020-10-11T15:09:26.708597Z","thread":"main"}
{"event":"SENT_MESSAGE","to":"isLegalAge","context":{"email":["example@gmail.com"]},"message":10,"instant":"2020-10-11T15:09:26.709568Z","thread":"vert.x-eventloop-thread-8"}
{"event":"RECEIVED_MESSAGE","address":"isLegalAge","context":{"email":["example@gmail.com"]},"instant":"2020-10-11T15:09:26.710185Z","thread":"vert.x-eventloop-thread-4"}
{"event":"SENT_MESSAGE","to":"isValidId","context":{"email":["example@gmail.com"]},"message": "03486761","instant":"2020-10-11T15:09:26.710136Z","thread":"vert.x-eventloop-thread-8"}
{"event":"SENT_MESSAGE","to":"isValidEmail","context":{"email":["example@gmail.com"]},"message":"example@gmail.com","instant":"2020-10-11T15:09:26.710672Z","thread":"vert.x-eventloop-thread-8"}
{"event":"RECEIVED_MESSAGE","address":"isValidId","context":{"email":["example@gmail.com"]},"instant":"2020-10-11T15:09:26.710713Z","thread":"vert.x-eventloop-thread-5"}
{"event":"RECEIVED_MESSAGE","address":"isValidEmail","context":{"email":["example@gmail.com"]},"instant":"2020-10-11T15:09:26.711165Z","thread":"vert.x-eventloop-thread-6"}
{"event":"RECEIVED_MESSAGE","address":"isValid","context":{"email":["imrafaelmerino@gmail.com"]},"instant":"2020-10-11T15:09:26.711854Z","thread":"vert.x-eventloop-thread-8"}
{"event":"SENT_MESSAGE","to":"isLegalAge","context":{"email":["imrafaelmerino@gmail.com"]},"message":17,"instant":"2020-10-11T15:09:26.712138Z","thread":"vert.x-eventloop-thread-8"}
```
 
## <a name="spawning-verticles"><a/> Spawning verticles 
With vertx-effect, you can spawn verticles, which means that verticles are deployed and undeployed on the fly. 
Every time something needs to be computed, a new verticle is deployed. When the computation is done and the 
verticle replies, it is undeployed right away. The goal is to get the most out of the cores! Erlang taught us
how to develop concurrent software that doubles in speed if you double the number of cores without changing a 
code line: spawning as many verticles as possible. In Erlang jargon, a verticle is kind of a process.

Will deploy and undeploy verticles continuously slow down the system? It depends, like everything related to performance. 
There are times when the cost of reaching a greater level of parallelization is worth it. Other times it's not. 
Let's see how long it takes to deploy and undeploy one million verticles:

```java
    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    public void deploy_undeploy() throws InterruptedException {
        int processes = 1000000; 
        CountDownLatch latch = new CountDownLatch(processes);

        for (int i = 0; i < processes; i++) {
            vertxRef.deploy("id" + i,
                            λ.<JsObj>identity()
                           )
                    .onComplete(vr -> {
                        vr.result()
                          .undeploy()
                          .onSuccess(it -> latch.countDown());
                    })
                    .get();
        }

        latch.await(10,
                    SECONDS 
                   );

    }
```

It takes almost three seconds, 3 microseconds per verticle:

```text
Benchmark                  Mode  Cnt  Score   Error  Units
Processes.deploy_undeploy  avgt   10  2.907 ± 0.658   s/op
```

## <a name="httpclient"><a/> Reactive http client 
vertx-effect implements a reactive HTTP client that exposes a lambda per HTTP method. It's as simple as extending the class 
HttpClientModule and defining a constructor to initialize the HTTP options and the verticle's internal address that will perform the requests.

```java
import vertx.effect.httpclient.HttpClientModule
import io.vertx.core.http.HttpClientOptions;

public class MyHttpModule extends HttpClientModule {

    public MyHttpModule(final HttpClientOptions options) {
        super(options,
              "myhttp-client-address"
             );
    }
}

HttpClientOptions options = new HttpClientOptions().setDefaultHost("www.google.com")
                                                   .setDefaultPort(80);
MyHttpModule httpModule = new MyHttpModule(options);

Pair.sequential(vertxRef.deployVerticle(new RegisterJsValuesCodecs()),
                vertxRef.deployVerticle(httpModule)
               )
    .get()
```

Find below the types of some of the most relevant lambdas that you can use to make HTTP requests after deploying the module.

``` java

httpModule.get    ::  λc<GetReq, JsObj>
httpModule.post   ::  λc<PostReq, JsObj>
httpModule.put    ::  λc<PutReq, JsObj>
httpModule.delete ::  λc<DeleteReq, JsObj>
httpModule.patch  ::  λc<PatchReq, JsObj>

```

The response is a Json with the following fields and types:

   - **body** :: String
   - **status_code** :: int
   - **status_message** :: int
   - **cookies** :: JsArray
   - **headers** :: JsObj 

Let's create a function that takes two arguments; the number of retries in case of a timeout occurs or the TCP connection 
is closed and a search term. We wait one second before making the first attempt, two seconds before making the second one, 
and so forth. If another error happens, or the function uses up the number of attempts, the function returns an empty json.

```java 
import static vertx.effect.Failures.HTTP_CONNECT_TIMEOUT_PRISM;
import static vertx.effect.Failures.HTTP_REQUEST_TIMEOUT_PRISM; 
import static vertx.effect.Failures.TCP_CONNECTION_CLOSED_PRISM;

BiFunction<Integer, String, Val<JsObj>> search =
      (attempts, term) ->
            httpModule.get.apply(new GetReq().uri("/search?q=" + term))
                          .retry(Failures.any(HTTP_CONNECT_TIMEOUT_PRISM,
                                              HTTP_REQUEST_TIMEOUT_PRISM,
                                              TCP_CONNECTION_CLOSED_PRISM 
                                             ),
                                 attempts,
                                 (error, remainingAttempts) ->
                                             vertxRef.delay(attempts - remainingAttempts + 1,
                                                            SECONDS
                                                           )
                                 )
                          .recoverWith(e -> Val.succeed(JsObj.EMPTY));

search.apply(3, "vertx").get();

``` 

All the request events are also published into the address **vertx-effect-events**.

```json
{"event":"DEPLOYED_VERTICLE","class":"vertx.effect.RegisterJsValuesCodecs","instant":"2020-10-12T17:29:01.633606Z","id":"15f47d0d-1646-47a8-a458-e2a37a578457","thread":"vert.x-eventloop-thread-3"}
{"event":"DEPLOYED_VERTICLE","address":"myhttp-client-address","instant":"2020-10-12T17:29:01.801718Z","id":"4e0928a8-633b-4a3d-88ae-ef32d4dae06f","thread":"vert.x-eventloop-thread-2"}
{"event":"DEPLOYED_VERTICLE","class":"MyHttpModule","instant":"2020-10-12T17:29:01.803823Z","id":"0395c2ec-9959-4aa5-bd71-84c894c35f0f","thread":"vert.x-eventloop-thread-5"}

{"event":"SENT_MESSAGE","to":"myhttp-client","message":{"type":0,"host":"www.google.com","uri":"/search?q=vertx"},"instant":"2020-10-12T17:29:01.817395Z","thread":"main"}
{"event":"RECEIVED_MESSAGE","address":"myhttp-client-address","instant":"2020-10-12T17:29:01.819720Z","thread":"vert.x-eventloop-thread-4"}
{"event":"REPLIED_RESP","address":"myhttp-client-address","message":{"status_code":200,"status_message":"OK","cookies":[], "headers": {},"body":""}
{"event":"RECEIVED_RESP","from":"myhttp-client-address","instant":"2020-10-12T17:29:03.189085Z","thread":"vert.x-eventloop-thread-7"}

```

As you can see, two verticles were deployed: the module and an internal verticle listening on the specified address myhttp-client-address.
This verticle performs the requests. The cookies, headers, and body received from Google are omitted.

## <a name="oauth-httpclient"><a/> Reactive OAuth http client

Following the philosophy we've seen so far, the OAuth Http clients implemented in vertx-effect are verticles that expose 
lambdas to make Http request. Getting and refreshing the access token is something you don't have to worry about. It's all 
handled for you. 

### <a name="clientcredentials"><a/> Client credentials flow 

```java
import vertx.effect.httpclient.oauth.GetAccessTokenRequest;
import vertx.effect.httpclient.oauth.ClientCredentialsModule;
import vertx.effect.httpclient.oauth.ClientCredentialsFlowBuilder;
import io.vertx.core.http.HttpClientOptions;

ClientCredentialsFlowBuilder builder  = 
            new ClientCredentialsFlowBuilder(new HttpClientOptions().setDefaultPort(port)
                                                                    .setDefaultHost("localhost"),
                                             "my-httpclient-address",
                                             new GetAccessTokenRequest(clientId,clientSecret)
                                            );

ClientCredentialsModule httpClient = builder.createModule();

vertxRef.deployVerticle(httpClient);

```

After deploying the Http client module, you can use the lambdas it exposes to make any requests:

```java

public final λc<GetReq, JsObj> getOauth;
public final λc<PostReq, JsObj> postOauth;
public final λc<PutReq, JsObj> putOauth;
public final λc<DeleteReq, JsObj> deleteOauth;
public final λc<PatchReq, JsObj> patchOauth;
public final λc<HeadReq, JsObj> headOauth;
public final λc<ConnectReq, JsObj> connectOauth;
public final λc<OptionsReq, JsObj> optionsOauth;
public final λc<TraceReq, JsObj> traceOauth;

```

You can customize anything using the builder:

```java

// by default Authorization
builder.authorizationHeaderName(String authorizationHeaderName) 

// by default token -> "Bearer "+token
builder.authorizationHeaderValue(Function<String, String> authorizationHeaderValue) 

// predicate to check if we need to refresh the token
// by default resp -> resp.getInt("status_code") == 401
builder.refreshTokenPredicate(Predicate<JsObj> refreshTokenPredicate) 

// lambda to get the access token from the resp
// by default parse the body into a Json a get the access_token field 
builder.readAccessTokenAfterRefresh(λ<JsObj, String> readNewAccessTokenAfterRefresh) 

// predicate to check if retrying in case of an error making the request to get the token
// by default connection timeout, unknown host or access_token is not found in the response
builder.setRetryAccessTokenReqPredicate(Predicate<Throwable> retryGetTokenPredicate)

// default number of attempts in case of retryGetTokenPredicate is tested true 
builder.setAccessTokenReqAttempts(int accessTokenReqAttempts) 

// predicate to check if retrying in case of an error making the request
// by default connection timeout or unknown host 
builder.setRetryReqPredicate(Predicate<Throwable> retryReqPredicate) 

// default number of attempts in case of setRetryReqPredicate is tested true 
builder.setReqAttempts(int reqAttempts) 

```

The default request to get the token is:

```text
POST https://{{host}}:{{port}}/token
Authorization: Basic Base64(clientId:clientSecret) 
Content-Type: application/x-www-form-urlencoded

grant_type=client_credentials
```

Since _GetAccessTokenRequest_ is just a function, it can also be customized:

```java
GetAccessTokenRequest accessTokenReq = 
                (context,httpclient) -> { 
                                          PostReq postReq = ???;
                                          return httpclient.post
                                                           .apply(context,postReq);
                                        };

new ClientCredentialsFlowBuilder(httpOptions,   
                                 "address",
                                 accessTokenReq 
                                );
```


### <a name="authorizationflow"><a/> Authorization flow


You need to get the access and refresh token, making an authentication request. You will typically need a code, a redirect_uri, etc.

```java

HttpClientOptions options = new HttpClientOptions().setDefaultPort(port)
                                                   .setDefaultHost("localhost");


// authenticateReq :: (module,inputs) -> http response
// inputs will be passed in when calling the authenticate method
BiFunction<HttpClientModule, JsObj, Val<JsObj>> authenticateReq = ???;


AuthorizationCodeModule httpClient =
           new AuthorizationCodeFlowBuilder(options,
                                            "oauth-http-client",
                                            new RefreshAccessTokenReq("client_id",
                                                                      "client_secret")
                                           ).createFromAuthReq(authenticateReq);

vertxRef.deployVerticle(httpClient);

// data needed to do the authentication
JsObj inputs = JsObj.of("code",???,"redirect_uri",???);
        
Val<Tuple2<String, String>> tokens = httpClient.authenticate(inputs);

tokens.get()
      .onSuccess(tuple -> {
                            String accessToken = tuple._1;
                            String refreshToken = tuple._2;
                                
                            // after authentication you can make any request
                            GetReq getReq = ???;
                            httpClient.getOauth.apply(getReq);
                                  
                            PostReq postReq = ???;
                            httpClient.postOauth.apply(postReq)
                          }
                );
```

You already have the refresh token. You don't need to make any authorization request:

```java
String refreshToken = ???;

AuthorizationCodeModule httpClient = 
                new AuthorizationCodeFlowBuilder(options,
                                                 "oauth-http-client",
                                                 new RefreshAccessTokenReq("client_id",
                                                                           "client_secret"
                                                ).createFromRefreshToken(refreshToken);
               
vertxRef.deployVerticle(httpClient);


```

There is an implementation of the requests to get the tokens and authenticate your app for Spotify. 
_Go to vertx.effect.httpclient.oauth.Spotify_ for further details. More implementations will be added 
little by little. As shown in the previous section, you can customize everything: retries under certain 
errors, number of attempts, function to extract the tokens from the authentication request etc.

### <a name="jfr"><a/> JFR support
Every http request and verticle call has an associated event that is recorded using Java Flight Recorded

Fields of a verticle message event:

    - address: Address of the Verticle where the message is sent to
    - result: SUCCESS OR FAILURE, dependening on what the caller receives
    - failure code: In case the failure is a ReplyException, it's the failure code
    - failure type: In case the failure is a ReplyException, it's the failure type
    - failure message: In case the failure is a ReplyException, it's the failure message
    - exception class: In case the failure is not a ReplyException, it's the exception class name
    - exception message: In case the failure is not a ReplyException, it's the exception message
    - duration: time since the message is sent until the response is received
    
Fields of a http request event:

    - status code: The http status code response
    - host: the host of the request
    - uri: the uri of the request
    - method: the method of the request: GET, POST ...
    - duration: the method since the request is sent until the response is received
    - exception class: If an exception takes place and no http response is received, the exception class
    - exception message: If an exception takes place and no http response is received, the exception message

## <a name="requirements"><a/> Requirements
Java 11 or greater

Vertx version 4.0.0.CR1 

## <a name="installation"><a/> Installation

```xml
<dependency>
  <groupId>com.github.imrafaelmerino</groupId>
  <artifactId>vertx-effect</artifactId>
  <version>3.0.0</version>
</dependency>
```

## <a name="rp"><a/> Related projects
I've developed some real examples with vertx-effect in [vertx-effect-examples](https://github.com/imrafaelmerino/vertx-effect-examples).
vertx-effect uses the persistent Json from [json-values](https://github.com/imrafaelmerino/json-scala-values).
[vertx-mongodb-effect](https://github.com/imrafaelmerino/vertx-mongodb-effect) uses vertx-effect to work with 
MongoDB using lambdas.

## <a name="release"><a/> Release process
Every time a tagged commit is pushed into master, a Travis CI build will be triggered automatically and 
start the release process, deploying to Maven repositories and GitHub Releases. See the Travis conf file 
**.travis.yml** for further details. On the other hand, the master branch is read-only, and all the commits 
should be pushed to master through pull requests. 
