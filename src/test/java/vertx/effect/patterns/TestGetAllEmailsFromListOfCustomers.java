package vertx.effect.patterns;

import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import jsonvalues.JsArray;
import jsonvalues.JsObj;
import jsonvalues.JsStr;
import jsonvalues.spec.JsObjSpec;
import jsonvalues.spec.JsSpecs;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import vertx.effect.Validators;
import vertx.effect.exp.Cons;
import vertx.effect.exp.JsArrayVal;
import vertx.effect.exp.JsObjVal;
import vertx.effect.patterns.oauth.GetTokenReqVerticle;
import vertx.effect.patterns.oauth.GetTokenVerticle;
import vertx.effect.Val;
import vertx.effect.exp.*;
import vertx.effect.λ;


@ExtendWith(VertxExtension.class)
public class TestGetAllEmailsFromListOfCustomers {

    public static λ<String, JsArray> getCustomerEmails =
            id -> JsArrayVal.empty()
                            .append(Cons.success(JsStr.of(id + "_a")))
                            .append(Cons.success(JsStr.of(id + "_b")));


    public static λ<JsArray, JsArray> getCustomersEmails =
            ids -> Validators.validateJsArray(JsSpecs.arrayOfStr)
                             .apply(ids)
                             .flatMap($ -> ids.stream()
                                     .map(pair -> pair.value.toJsStr().value)
                                     .reduce(Cons.success(JsArray.empty()),
                                             (acc, id) -> acc.flatMap(emails -> getCustomerEmails.apply(id)
                                                                                                 .map(emails::appendAll)
                                                                     ),
                                             (arr1, arr2) -> arr1.flatMap(a -> arr2.map(a::appendAll))
                                            ));


    public static final λ<JsObj, JsArray> getCustomerEmailsRec =
            input -> {
                JsArray ids = input.getArray("ids");
                if (ids.isEmpty()) return Cons.success(input.getArray("acc"));
                String head = ids.head()
                                 .toJsStr().value;
                Val<JsArray> headEmailsVal = getCustomerEmails.apply(head);
                return headEmailsVal.flatMap(emails ->
                                                     TestGetAllEmailsFromListOfCustomers.getCustomerEmailsRec.apply(JsObj.of("ids",
                                                                                                                             ids.tail(),
                                                                                                                             "acc",
                                                                                                                             input.getArray("acc")
                                                                                                                                  .appendAll(emails)
                                                                                                                            )
                                                                                                                   )

                                            );

            };


    public static final λ<JsObj, JsArray> getCustomerEmailsRecI =
            input -> {
                JsArray ids = input.getArray("ids");
                if (ids.isEmpty()) return Cons.success(input.getArray("acc"));
                String head = ids.head()
                                 .toJsStr().value;

                return JsObjVal.of("ids",
                                   Cons.success(ids.tail()),
                                   "acc",
                                   getCustomerEmails.apply(head)
                                  )
                               .flatMap(obj ->
                                                TestGetAllEmailsFromListOfCustomers.getCustomerEmailsRec.apply(obj.set("acc",
                                                                                                                       input.getArray("acc")
                                                                                                                            .appendAll(obj.getArray("acc"))
                                                                                                                      )
                                                                                                              )
                                       );


            };


    public static λ<JsArray, JsArray> getCustomersEmailsA =
            ids -> Validators.validateJsArray(JsSpecs.arrayOfStr)
                             .apply(ids)
                             .flatMap($ -> getCustomerEmailsRec.apply(JsObj.of("ids",
                                                                      ids,
                                                                      "acc",
                                                                      JsArray.empty()
                                                                     )
                                                            )
                            );

    @Test
    public void test_Get_All_Emails_From_A_List_Of_Ids(VertxTestContext context) {

        getCustomersEmailsA.apply(JsArray.of("1",
                                             "2"
                                            )
                                 )
                           .onSuccess(arr -> {
                               context.verify(() -> Assertions.assertEquals(JsArray.of("1_a",
                                                                                       "1_b",
                                                                                       "2_a",
                                                                                       "2_b"
                                                                                      ),
                                                                            arr
                                                                           )
                                             );
                               context.completeNow();
                           })
                           .get();
    }


    @Test
    public void test_Get_TokenVerticle(VertxTestContext context) {
        JsObj GET_MESSAGE = JsObj.of("op",
                                     JsStr.of("get")
                                    );

        JsObj RENEW_MESSAGE = JsObj.of("op",
                                       JsStr.of("renew")
                                      );

        GetTokenVerticle getTokenVerticle =
                new GetTokenVerticle(new GetTokenReqVerticle(),
                                     Validators.validateJsObj(JsObjSpec.strict("op",
                                                                               JsSpecs.cons(JsStr.of("get"))
                                                                              )
                                                             ),
                                     Validators.validateJsObj(JsObjSpec.strict("op",
                                                                               JsSpecs.cons(JsStr.of("renew"))
                                                                              )
                                                             )

                );

        getTokenVerticle.apply(GET_MESSAGE)
                        .onSuccess(token -> context.verify(() -> {
                                                               Assertions.assertEquals("1",
                                                                                       token
                                                                                      );
                                                               context.completeNow();
                                                           }
                                                          )
                                  )
                        .get();

        getTokenVerticle.apply(GET_MESSAGE)
                        .onSuccess(token -> context.verify(() -> {
                                                               Assertions.assertEquals("1",
                                                                                       token
                                                                                      );
                                                               context.completeNow();
                                                           }
                                                          )
                                  )
                        .get();

        getTokenVerticle.apply(RENEW_MESSAGE)
                        .onSuccess(token -> context.verify(() -> {
                                                               Assertions.assertEquals("2",
                                                                                       token
                                                                                      );
                                                               context.completeNow();
                                                           }
                                                          )
                                  )
                        .get();

        getTokenVerticle.apply(GET_MESSAGE)
                        .onSuccess(token -> context.verify(() -> {
                                                               Assertions.assertEquals("2",
                                                                                       token
                                                                                      );
                                                               context.completeNow();
                                                           }
                                                          )
                                  )
                        .get();

        getTokenVerticle.apply(RENEW_MESSAGE)
                        .onSuccess(token -> context.verify(() -> {
                                                               Assertions.assertEquals("3",
                                                                                       token
                                                                                      );
                                                               context.completeNow();
                                                           }
                                                          )
                                  )
                        .get();

        getTokenVerticle.apply(RENEW_MESSAGE)
                        .onSuccess(token -> context.verify(() -> {
                                                               Assertions.assertEquals("4",
                                                                                       token
                                                                                      );
                                                               context.completeNow();
                                                           }
                                                          )
                                  )
                        .get();
    }


}
