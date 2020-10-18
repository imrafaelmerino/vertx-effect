[![Build Status](https://travis-ci.org/imrafaelmerino/vertx-effect.svg?branch=master)](https://travis-ci.org/imrafaelmerino/vertx-effect)
[![CircleCI](https://circleci.com/gh/imrafaelmerino/vertx-effect/tree/master.svg)](https://circleci.com/gh/imrafaelmerino/vertx-effect/tree/master)
[![codecov](https://codecov.io/gh/imrafaelmerino/vertx-effect/branch/master/graph/badge.svg)](https://codecov.io/gh/imrafaelmerino/vertx-effect)

[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=imrafaelmerino_vertx-effect&metric=alert_status)](https://sonarcloud.io/dashboard?id=imrafaelmerino_vertx-effect)
[![Maintainability Rating](https://sonarcloud.io/api/project_badges/measure?project=imrafaelmerino_vertx-effect&metric=sqale_rating)](https://sonarcloud.io/dashboard?id=imrafaelmerino_vertx-effect)
[![Commitizen friendly](https://img.shields.io/badge/commitizen-friendly-brightgreen.svg)](http://commitizen.github.io/cz-cli/)

[![Javadocs](https://www.javadoc.io/badge/com.github.imrafaelmerino/vertx-effect.svg)](https://www.javadoc.io/doc/com.github.imrafaelmerino/vertx-effect)
[![Maven](https://img.shields.io/maven-central/v/com.github.imrafaelmerino/vertx-effect/0.1)](https://search.maven.org/artifact/com.github.imrafaelmerino/vertx-effect/0.1/jar)
[![](https://jitpack.io/v/imrafaelmerino/vertx-effect.svg)](https://jitpack.io/#imrafaelmerino/vertx-effect)

- [vertx-effect manifesto](#manifesto)
- [Introduction](#introduction)
- [Expressions](#exp)
- [Modules](#modules)
- [Logging](#logging)
    - [Publishing events](#events)
    - [Publishing correlated events](#correlated-events) 
- [Spawning verticles](#spawning-verticles)
- [Reactive http client](#httpclient)
- [Reactive OAuth http client](#oauth-httpclient)
- [What to use _vertx-effect_ for and when to use it](#whatfor)
- [When not to use it](#notwhatfor)
- [Performance](#perf)
- [Requirements](#requirements)
- [Installation](#installation)
- [Related projects](#rp)
- [Release process](#release)

## <a name="manifesto"><a/> vertx-effect manifesto
    . The more verticles, the better.
    . Use persistent data structures.
    . Systems will fail, be prepared.
    . Simplicity matters.
    . Avoid state when possible. 
    . If there is a bug and you can't spot it quickly, then there are two bugs. Fix both of them.
    . Avoid debug-driven development
    
## <a name="introduction"><a/> Introduction

**Functional Programming is all about working with pure functions and values**. That's all. 
Haskell has proven to us how **laziness is an essential property to stay pure**. Since Java 
8, we have suppliers. They are indispensable to do FP in Java. Let's start defining what a value 
is in vertx-effect:
  
```java

import java.util.function.Supplier
import io.vertx.core.Future

public interface Val<O> extends Supplier<Future<O>> {}

```

A **Val** of type **O** is a supplier that will return a Vertx future of type **O**.

What about functions? I always wanted to name **λ** to something, and I finally got the chance!

```java

import java.util.function.Function

public interface λ<I,O> extends Function<I, Val<O>> { }

```
A lambda is a function that returns a **Val** of type **O** given a type **I**. **It models the communication with a Verticle**:
a message is sent, the Verticle receives and processes the message, and replies with a response. The response has to
be of a type that can be sent across the EvenBus; otherwise, you must implement a [MessageCodec](https://vertx.io/docs/apidocs/io/vertx/core/eventbus/MessageCodec.html).

## <a name="exp"><a/> Expressions 

**Using expressions and function composition is how we deal with complexity in functional programming**. 
Let's go over the essential expressions in **vertx-effect**:

- **Cons** (stands for constant). 


```java
import io.vertx.core.Future

public final class Cons<O> implements Val<O>{...}

Val<String> str = Cons.success("hi"); // from a constant

// from an error
Val<Throwable> error = Cons.failure(new RuntimeException("something went wrong :("));

Future<JsObj> getProfile(final String id){...}
Val<JsObj> profile = Cons.of( () -> getProfile(id)); // from a Future
``` 


- **IfElse**. If the predicate is evaluated to **Cons.success(true)**, it executes and returns the consequence
;otherwise, the alternative.


```java


public final class IfElse<O> extends Val<O> {}

IfElse.<O>predicate(Val<Bool>)
         .consequence(Val<O>)
         .alternative(Val<O>)

```
 

- **Cond**. It's a set of branches and a default value. Each branch consists of a predicate and a value to be 
executed and returned if evaluated as **Cons.success(true)**. Suppose no predicate is evaluated to be true. 
In that case, the Cond expression returns the default value, which is the last value of the clause 


```java


Cond.<O>of(Val<Boolean>, Val<O>,
           Val<Boolean>, Val<O>,
           Val<Boolean>, Val<O>,
           Val<O>
          )
                        
``` 
 

- **Case**. The case construct implements multiple predicate-value branches like the Cond construct. 
However, it evaluates a value and allows multiple value clauses based on the evaluation of that value.


```java


new Case<I,O>(Val<I>).of(cons of type I,Val<O>,
                         cons of type I,Val<O>,        
                         cons of type I,Val<O>,
                         Val<O>
                        );       

// reduces to Wednesday
new Case<Integer,String>(Cons.success(3)).of(1, Cons.success("Monday"),
                                             2, Cons.success("Tuesday"),
                                             3, Cons.success("Wednesday"),
                                             4, Cons.success("Thursday"),
                                             5, Cons.success("Friday"),
                                             Cons.success("weekend")
                                            );
```

```java

new Case<I,O>(Val<I>).of(List<I>,Val<O>,
                         List<I>,Val<O>,        
                         List<I>,Val<O>,
                         Val<O>
                        );      
        
// reduces to third week
new Case<Integer,String>(Cons.success(20))
                 .of(asList(1, 2, 3, 4, 5, 6, 7), Cons.success("first week"),
                     asList(8, 9, 10, 11, 12, 13, 14), Cons.success("second week"),
                     asList(15, 16, 17, 18, 19, 20, 10), Cons.success("third week"),
                     asList(21, 12, 23, 24, 25, 26, 27), Cons.success("forth week"),
                     Cons.success("last days of the month")
                    );
```
    

- **And** and **Or**. The current implementation executes all the values of the expression in parallel.


```java


And.of(Val<Boolean>,....)
                         
Or.of(Val<Boolean>,...)

```  
 

- **JsObjVal** and **JsArrayVal**. The current implementation computes all the values of the JsObj 
and JsArray in parallel. The next example shows how JsObjVla and JsArrayVal are data structures that 
look like raw Json. And of course, you can mix expressions and nest them, going as deeper as necessary.  


```java

import jsonval.JsValue

JsObjVal.of("a", IfElse.<String>predicate(Val<Boolean>)
                               .consequence(Val<JsValue>)
                               .alternative(Val<JsValue>),
            "b", JsArrayVal.of(new Case<Integer,String>(Integer).of(1, Val<JsValue>,
                                                                    2, Val<JsValue>,
                                                                    Val<JsValue> 
                                                                   ),
                               Cond.of(Val<Boolean>,Val<JsValue>,
                                       Val<Boolean>,Val<JsValue>,
                                       Val<JsValue>
                                      )
                              ),
            "c", JsObjVal.of("d", Or.of(Val<Boolean>, Val<Boolean>),
                             "e", And.of(Val<Boolean>, Val<Boolean>),
                             "f", JsArrayVal.of(Val<JsValue>,
                                                Val<JsValue> 
                                               ) 
                            )
           );

``` 
 

- **Pair**. A pair is a tuple of two elements. Both elements are computed in parallel.


```java


Val<Tuple2<A,B> = Pair.of(Val<A>,Val<B>);
    
```
  

- **Triple**. A triple is a tuple of three elements. All elements are computed in parallel.


```java


Val<Tuple3<A,B,C> = Triple.of(Val<A>,Val<B>,Val<C>);

```

It's important to notice **that any value of the above expressions can be computed by a Verticle of
any machine of a cluster**. Imagine ten machines collaborating to compute a JsObj, is not this amazing?
 

Find below some of the most critical operations defined in the **Val** interface that will help us make our code
more resilient:


```java
public interface Val<O> extends Supplier<Future<O>> {

    <Q> Val<Q> flatMap(λ<O,Q> fn);

    Val<O> retry(int attempts);

    Val<O> retryIf(Predicate<Throwable> predicate, int attempts);

    Val<O> fallbackTo(λ<Throwable, O> fn);

    Val<O> recoverWith(λ<Throwable, O> fn);
}
 ``` 

## <a name="modules"><a/> Modules 
 
In **vertx-effect**, **a module is a special Verticle whose purpose is to deploy other Verticles and expose lambdas to 
communicate with them**. Let's put an example:

```java
import jsonvalues.JsObj;

public class MyModule extends VertxModule {
  private static final String REMOVE_NULL_ADDRESS = "removeNull";
  private static final String TRIM_ADDRESS = "trim";
 
  public static λ<JsObj, JsObj> removeNull;
  public static λ<JsObj, JsObj> trim;
 
  @Override
  public void deploy() {
     λ<JsObj, JsObj> removeNull = o ->
                 Cons.success(o.filterAllValues(pair -> pair.value.isNotNull()));
     this.deploy(REMOVE_NULL_ADDRESS,
                 removeNull
                );
 
     λ<JsObj, JsObj> trim = o ->
         Cons.success(o.mapAllValues(pair -> JsStr.prism.modify.apply(String::trim)
                                                                       .apply(pair.value)
                                            )
                             );
     this.deploy(TRIM_ADDRESS,
                 trim
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
    
You can still communicate with the module verticles using the Vertx API, but one of the 
points of vertx-effect is to use functions for that. 
  
In our example, **we are using a persistent and immutable Json from the library [json-values](https://imrafaelmerino.github.io/json-values/)**. 
It's vital to work with this kind of data structure in the actor model (and in general, when possible, 
it's considered good practice).

The **ask** method returns a lambda to establish bidirectional communication with a verticle.
In contrast, the **tell** method would return a consumer because either no answer is expected or ignored
Let's deploy our module and do some testing.

```java

 @BeforeAll
 public static void prepare(final Vertx vertx,
                            final VertxTestContext context) 
 {
    VertxRef vertxRef = new VertxRef(vertx);

    // prints out events published by vertx-effect
    vertxRef.registerConsumer(EVENTS_ADDRESS, System.out::println); 

    Pair.of(vertxRef.deploy(new MyModule()),
            vertxRef.deploy(new RegisterJsValuesCodecs())
           )
        .onSuccess(pair -> {
                            System.out.println(String.format("Ids deployed: %s and %s",pair._1,pair._2));
                            context.completeNow();
                           }
                  )
        .get();
 }

 @Test
 public void test_composition(final VertxTestContext context)
 {
    λ<JsObj, JsObj> removeAndNull = MyModule.removeNull.andThen(MyModule.trim);

    JsObj obj = JsObj.of("a", JsStr.of("  hi  "),
                         "b", JsNull.NULL,
                         "c", JsObj.of("d", JsStr.of("  bye  "),
                                       "e", JsNull.NULL
                                      )
                        );

    removeAndNull.apply(obj)
                 .onSuccess(it -> {
                     JsObj expected = JsObj.of("a", JsStr.of("hi"),
                                               "c", JsObj.of("d", JsStr.of("bye"))
                                              );
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
we need to register some codecs. The verticle **RegisterJsValuesCodecs** does this task.

The **VertxRef** class is a wrapper around the Vertx instance to deploy and spawn verticles from lambdas. 
Modules use this class internally.


## <a name="logging"><a/> Logging 
Logging is essential in software. There are many logging libraries. Sometimes it is
not clear what dependencies you have to use because there isn't a standard solution. 
Each library uses its own. I didn't want to be opinionated. At the same time, I 
wanted to provide a simple and decouple solution to know what is going on in any system 
using vertx-effect. That's why I decided to publish remarkable events in a specific address. 
If you want to use your favorite slf4j implementation, just implement it in a consumer.
On the other hand, consuming all those events during testing will give you instant feedback on 
your system and agility spotting bugs. You can disable this future with the Java system 
property**-Dpublish.events=false**. 

### <a name="events"><a/> Publishing events 
**vertx-effect** publishes events to the address **vertx-effect-events**. Find below some of the most important predefined
events:

    - DEPLOYED_VERTICLE 
    - UNDEPLOYED_VERTICLE
    - SENT_MESSAGE: a message is sent to a Verticle
    - RECEIVED_MESSAGE: a Verticle received a message
    - REPLIED_RESP : a Verticle replied with a message
    - REPLIED_FAILURE: a Verticle replied with an error
    - RECEIVED_RESP: a response is received from a Verticle
    - RECEIVED_FAILURE: an error is received from a Verticle
    - INTERNAL_ERROR_XXX: go to github and open and issue. 

A real example from the previous example where we filter and trim the strings of 
a Json would be:

```json

{"event":"DEPLOYED_VERTICLE","address":"removeNull","instant":"2020-10-10T22:44:42.687633Z","id":"3de92ef8-777f-4110-aa45-442fc41900c6","thread":"vert.x-eventloop-thread-1"}
{"event":"DEPLOYED_VERTICLE","class":"vertxval.RegisterJsValuesCodecs","instant":"2020-10-10T22:44:42.682624Z","id":"73181043-ae38-4819-b7de-02f303fcc155","thread":"vert.x-eventloop-thread-3"}
{"event":"DEPLOYED_VERTICLE","address":"trim","instant":"2020-10-10T22:44:42.701293Z","id":"a866ffdc-38c8-4da2-bcc0-c9f4881f5139","thread":"vert.x-eventloop-thread-1"}
{"event":"DEPLOYED_VERTICLE","class":"vertxval.MyModule","instant":"2020-10-10T22:44:42.703410Z","id":"1473dff2-075c-4fd8-be42-cebcf0a890a0","thread":"vert.x-eventloop-thread-6"}

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

In async event-driven systems is extremely difficult to correlate events.
Having this solved is a killer future that saves you from working hours trying to gather all the different events 
associated with a specific transaction. In vertx-effect
is really easy! As always, functions and composition come to the rescue.
Before checking out an example, let's see what a λc is

```java
import io.vertx.core.MultiMap;

public interface λc<I, O> extends BiFunction<MultiMap, I, Val<O>> {}

```

A λc is a function that takes two arguments, a map representing the context in which an operation will be executed, 
and the message of type I sent to the Verticle across the event bus.
You can put the user's email into the context to filter all the events associated with that email
and a random value to distinguish between transactions from the same email. That's only an example.

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
        λ<Integer, Boolean> isLegalAge = age -> Cons.success(age > 16)
        this.deploy(IS_LEGAL_AGE,
                    UserAccountFunctions.isLegalAge
                   );
   
        λ<String, Boolean> isValidId = id -> Cons.success(!id.isEmpty());
        this.deploy(IS_VALID_ID,
                    UserAccountFunctions.isValidId
                   );
       
        λ<String, Boolean> isValidEmail = email -> Cons.success(!email.isEmpty()); 
        this.deploy(IS_VALID_EMAIL,
                    UserAccountFunctions.isValidEmail
                   );
        λc<JsObj, Boolean> isValid = (context, obj) ->
                And.of(isLegalAge.apply(context,
                                        obj.getInt("age")
                                       ),
                       isValidId.apply(context,
                                       obj.getStr("id")
                                      ),
                       isValidEmail.apply(context,
                                          obj.getStr("email")
                                         )
                      );

        this.deploy(IS_VALID,
                    isValid
                   );

    }
}

```

As you can see, we've implemented a module that deploys five verticles and exposes five λc to interact 
with them. The method trace returns a λc (in the previous example, we used the **ask** method that returns a λ). 
The isValid lambda is implemented using the And expression. The context is passed through all the lambdas 
of the And expression.

```java
 Function<JsObj,Multimap> context = user -> MultiMap.caseInsensitiveMultiMap()
                                                    .add("email", user.getStr("email")); 
 
 JsObj user = JsObj.of("email", JsStr.of("imrafaelmerino@gmail.com"),
                       "age", JsInt.of(17), 
                       "id", JsStr.of("03786761>")
                      );
 JsObj user1 = JsObj.of("email", JsStr.of("example@gmail.com"),
                        "age", JsInt.of(10)
                       );

 UserAccountModule.isValid.apply(contex.apply(user), user).get();
 UserAccountModule.isValid.apply(contex.apply(user1), user1).get();
      
```
Let's take a look at the events that are published during the execution of the previous code:

```json
{"event":"SENT_MESSAGE","to":"isValid","context":{"email":["example@gmail.com"]},"message":{"email":"example@gmail.com","age":10},"instant":"2020-10-11T15:09:26.704145Z","thread":"main"}
{"event":"RECEIVED_MESSAGE","address":"isValid","context":{"email":["example@gmail.com"]},"instant":"2020-10-11T15:09:26.708157Z","thread":"vert.x-eventloop-thread-8"}
{"event":"SENT_MESSAGE","to":"isValid","context":{"email":["imrafaelmerino@gmail.com"]},"message":{"email":"imrafaelmerino@gmail.com","age":17,"id":"03786761>"},"instant":"2020-10-11T15:09:26.708597Z","thread":"main"}
{"event":"SENT_MESSAGE","to":"isLegalAge","context":{"email":["example@gmail.com"]},"message":10,"instant":"2020-10-11T15:09:26.709568Z","thread":"vert.x-eventloop-thread-8"}
{"event":"RECEIVED_MESSAGE","address":"isLegalAge","context":{"email":["example@gmail.com"]},"instant":"2020-10-11T15:09:26.710185Z","thread":"vert.x-eventloop-thread-4"}
{"event":"SENT_MESSAGE","to":"isValidId","context":{"email":["example@gmail.com"]},"message":null,"instant":"2020-10-11T15:09:26.710136Z","thread":"vert.x-eventloop-thread-8"}
{"event":"SENT_MESSAGE","to":"isValidEmail","context":{"email":["example@gmail.com"]},"message":"example@gmail.com","instant":"2020-10-11T15:09:26.710672Z","thread":"vert.x-eventloop-thread-8"}
{"event":"RECEIVED_MESSAGE","address":"isValidId","context":{"email":["example@gmail.com"]},"instant":"2020-10-11T15:09:26.710713Z","thread":"vert.x-eventloop-thread-5"}
{"event":"RECEIVED_MESSAGE","address":"isValidEmail","context":{"email":["example@gmail.com"]},"instant":"2020-10-11T15:09:26.711165Z","thread":"vert.x-eventloop-thread-6"}
{"event":"RECEIVED_MESSAGE","address":"isValid","context":{"email":["imrafaelmerino@gmail.com"]},"instant":"2020-10-11T15:09:26.711854Z","thread":"vert.x-eventloop-thread-8"}
{"event":"SENT_MESSAGE","to":"isLegalAge","context":{"email":["imrafaelmerino@gmail.com"]},"message":17,"instant":"2020-10-11T15:09:26.712138Z","thread":"vert.x-eventloop-thread-8"}
```
 
## <a name="spawning-verticles"><a/> Spawning verticles 
With vertx-effect, you can spawn verticles, which means that verticles are deployed and undeployed on the fly. 
Every time something needs to be computed, a new Verticle is deployed. When the computation is done, and the 
verticle replies, it is right away undeployed. 

The point is to heat up the cores! **Erlang taught us how to develop concurrent software that doubles 
in speed if you double the number of cores without changing a code line:  spawning as many verticles as possible**. 
In Erlang jargon, a Verticle is kind of a process. On the 
other hand, if you have a cluster, every computation could be done on different machines.


## <a name="httpclient"><a/> Reactive http client 
vertx-effect implements a reactive HTTP client that exposes a lambda per HTTP method. It's as simple
as extending the class HttpClientModule and defining a constructor to initialize the HTTP options and the internal
address of the verticle that will perform the requests.

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
MyHttpModule httpModule = new MyHttpModule();

vertxRef.deploy(httpModule);

``` 

Find below the types of some of the most relevant lambdas that you can use to make HTTP requests
after deploying the module:

``` java

httpModule.get    ::  λc<GetReq, JsObj>
httpModule.post   ::  λc<PostReq, JsObj>
httpModule.put    ::  λc<PutReq, JsObj>
httpModule.delete ::  λc<DeleteReq, JsObj>
httpModule.patch  ::  λc<PatchReq, JsObj>

```

The response is a Json with the following fields and types:

**body**:String, **status_code**:int, **status_message**:int, **cookies**:JsArray, **headers**:JsObj

All the request events are published into the address "vertx-effect-events. Making the request:

```java

Function<String,JsObj> search =  term -> httpModule.get.apply(new GetReq().uri("/search?q=" + term));
search.apply("vertx").apply()

```

would produce the events:

```json
{"event":"DEPLOYED_VERTICLE","class":"vertxval.RegisterJsValuesCodecs","instant":"2020-10-12T17:29:01.633606Z","id":"15f47d0d-1646-47a8-a458-e2a37a578457","thread":"vert.x-eventloop-thread-3"}
{"event":"DEPLOYED_VERTICLE","address":"myhttp-client-address","instant":"2020-10-12T17:29:01.801718Z","id":"4e0928a8-633b-4a3d-88ae-ef32d4dae06f","thread":"vert.x-eventloop-thread-2"}
{"event":"DEPLOYED_VERTICLE","class":"MyHttpModule","instant":"2020-10-12T17:29:01.803823Z","id":"0395c2ec-9959-4aa5-bd71-84c894c35f0f","thread":"vert.x-eventloop-thread-5"}

{"event":"SENT_MESSAGE","to":"myhttp-client","message":{"type":0,"host":"www.google.com","uri":"/search?q=vertx"},"instant":"2020-10-12T17:29:01.817395Z","thread":"main"}
{"event":"RECEIVED_MESSAGE","address":"myhttp-client","instant":"2020-10-12T17:29:01.819720Z","thread":"vert.x-eventloop-thread-4"}
{"event":"REPLIED_RESP","address":"myhttp-client","message":{"status_code":200,"status_message":"OK","cookies":[], "headers": {},"body":""}
{"event":"RECEIVED_RESP","from":"myhttp-client","instant":"2020-10-12T17:29:03.189085Z","thread":"vert.x-eventloop-thread-7"}

```

As you can see two verticles are deployed: the module and an iternal verticle listening on myhttp-client-adress
that performs the incoming requests. The cookies, headers and body received from Google are ommited for brevity reasons.
Every request message has a type that is an integer for performance reasons. In the example the type 0 means that
it's a GET.

## <a name="oauth-httpclient"><a/> Reactive OAuth http client
in progress
 
## <a name="whatfor"><a/> What to use json-values for and when to use it
in progress

## <a name="notwhatfor"><a/> When not to use it
in progress

## <a name="perf"><a/> Performance 
in progress

## <a name="requirements"><a/> Requirements
Java 11 or greater.

## <a name="installation"><a/> Installation
Add the following dependency to your building tool:
```
<dependency>
  <groupId>com.github.imrafaelmerino</groupId>
  <artifactId>vertx-effect</artifactId>
  <version>0.1</version>
</dependency>
```

## <a name="rp"><a/> Related projects

## <a name="release"><a/> Release process
Every time a tagged commit is pushed into master, a Travis CI build will be triggered automatically and 
start the release process, deploying to Maven repositories and GitHub Releases. See the Travis conf file 
**.travis.yml** for further details. On the other hand, the master branch is read-only, and all the commits 
should be pushed to master through pull requests. 
