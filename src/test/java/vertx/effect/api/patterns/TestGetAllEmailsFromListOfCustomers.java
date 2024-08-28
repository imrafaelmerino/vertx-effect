package vertx.effect.api.patterns;

import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import jsonvalues.JsArray;
import jsonvalues.JsObj;
import jsonvalues.JsStr;
import jsonvalues.spec.JsSpecs;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import vertx.effect.Lambda;
import vertx.effect.VIO;
import vertx.effect.Validators;
import vertx.effect.JsArrayExp;
import vertx.effect.JsObjExp;


@SuppressWarnings("ReturnValueIgnored")
@ExtendWith(VertxExtension.class)
public class TestGetAllEmailsFromListOfCustomers {

    public static Lambda<String, JsArray> getCustomerEmails =
            id -> JsArrayExp.seq()
                            .append(VIO.succeed(JsStr.of(id + "_a")))
                            .append(VIO.succeed(JsStr.of(id + "_b")));


    public static Lambda<JsArray, JsArray> getCustomersEmails =
            ids -> Validators.validateJsArray(JsSpecs.arrayOfStr())
                             .apply(ids)
                             .then($ -> ids.stream()
                                           .map(pair -> pair.value().toJsStr().value)
                                           .reduce(VIO.succeed(JsArray.empty()),
                                                   (acc, id) -> acc.then(emails -> {
                                                                             VIO<JsArray> a = getCustomerEmails.apply(id);
                                                                             VIO<JsArray> arr = a
                                                                                                                 .map(emails::appendAll);
                                                                             return arr;
                                                                         }
                                                                        ),
                                                   (arr1, arr2) -> arr1.then(a -> arr2.map(a::appendAll))
                                                  ));


    public static final Lambda<JsObj, JsArray> getCustomerEmailsRec =
            input -> {
                JsArray ids = input.getArray("ids");
                if (ids.isEmpty()) return VIO.succeed(input.getArray("acc"));
                String head = ids.head()
                                 .toJsStr().value;
                VIO<JsArray> headEmailsVal = getCustomerEmails.apply(head);
                return headEmailsVal.then(emails ->
                                                  TestGetAllEmailsFromListOfCustomers.getCustomerEmailsRec.apply(JsObj.of("ids",
                                                                                                                          ids.tail(),
                                                                                                                          "acc",
                                                                                                                          input.getArray("acc")
                                                                                                                               .appendAll(emails)
                                                                                                                         )
                                                                                                                )

                                         );

            };


    public static final Lambda<JsObj, JsArray> getCustomerEmailsRecI =
            input -> {
                JsArray ids = input.getArray("ids");
                if (ids.isEmpty()) return VIO.succeed(input.getArray("acc"));
                String head = ids.head()
                                 .toJsStr().value;

                return JsObjExp.seq("ids",
                                    VIO.succeed(ids.tail()),
                                    "acc",
                                    getCustomerEmails.apply(head)
                                   )
                               .then(obj ->
                                             TestGetAllEmailsFromListOfCustomers.getCustomerEmailsRec.apply(obj.set("acc",
                                                                                                                    input.getArray("acc")
                                                                                                                         .appendAll(obj.getArray("acc"))
                                                                                                                   )
                                                                                                           )
                                    );


            };


    public static Lambda<JsArray, JsArray> getCustomersEmailsA =
            ids -> Validators.validateJsArray(JsSpecs.arrayOfStr())
                             .apply(ids)
                             .then($ -> getCustomerEmailsRec.apply(JsObj.of("ids",
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


}
