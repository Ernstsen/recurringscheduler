package dk.esoftware.recurringscheduler.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import dk.esoftware.recurringscheduler.domain.TimeUnit;
import dk.esoftware.recurringscheduler.persistence.RecurrenceConfiguration;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.*;

@QuarkusTest
public class RecurrenceConfigurationResourceTest {

    @Test
    void testOverviewEndpointPostInitialization() {
        given()
                .when().post("/admin/init")
                .then().statusCode(200)
                .body(is("Ensured proper initialization"));

        given()
                .when().get("/recurrenceConfiguration")
                .then()
                .body(
                        containsString("Once a week"),
                        containsString("Once a month"),
                        containsString("Twice a year")
                );
    }

    @Test
    void testCreateEndpoint() {
        final RecurrenceConfiguration creationTestConf = new RecurrenceConfiguration(
                "CreationTest", TimeUnit.WEEK, 4);

        final Response response = given().contentType(ContentType.JSON)
                .when().body(creationTestConf).post("/recurrenceConfiguration")
                .thenReturn();

        response.then().statusCode(201)
                .contentType(ContentType.JSON)
                .body("name", equalTo("CreationTest"))
                .body("timeUnit", equalTo(TimeUnit.WEEK.toString()))
                .body("occurrencesPerTimePeriod", equalTo(4));

        System.out.println(new String(given().when().get("/recurrenceConfiguration").getBody().asByteArray()));
    }

    @Test
    void testFetchAfterCreateEndpoint() throws IOException {
        final RecurrenceConfiguration creationTestConf = new RecurrenceConfiguration(
                "CreationTest2", TimeUnit.MONTH, 3);

        final Response response = given().contentType(ContentType.JSON)
                .when().body(creationTestConf).post("/recurrenceConfiguration")
                .thenReturn();

        final RecurrenceConfiguration createdConf = new ObjectMapper().readValue(response.asByteArray(), RecurrenceConfiguration.class);

        final Response getResponse = given().contentType(ContentType.JSON)
                .when().body(creationTestConf).get("/recurrenceConfiguration/" + createdConf.getId().toString())
                .thenReturn();


        getResponse.then().statusCode(200)
                .contentType(ContentType.JSON)
                .assertThat()
                .body("name", equalTo("CreationTest2"))
                .body("timeUnit", equalTo(TimeUnit.MONTH.toString()))
                .body("occurrencesPerTimePeriod", equalTo(3));
    }

    @Test
    void testModifyAfterCreateEndpoint() throws IOException {
        final RecurrenceConfiguration creationTestConf = new RecurrenceConfiguration(
                "ModificationTest", TimeUnit.WEEK, 1);

        final Response response = given().contentType(ContentType.JSON)
                .when().body(creationTestConf).post("/recurrenceConfiguration")
                .thenReturn();

        final RecurrenceConfiguration createdConf = new ObjectMapper().readValue(response.asByteArray(), RecurrenceConfiguration.class);


        createdConf.setTimeUnit(TimeUnit.YEAR);
        createdConf.setOccurrencesPerTimePeriod(9);

        final Response modifyResponse = given().contentType(ContentType.JSON)
                .when().body(createdConf).put("/recurrenceConfiguration/" + createdConf.getId())
                .thenReturn();

        modifyResponse.then().statusCode(201)
                .contentType(ContentType.JSON)
                .body("name", equalTo("ModificationTest"))
                .body("timeUnit", equalTo(TimeUnit.YEAR.toString()))
                .body("occurrencesPerTimePeriod", equalTo(9));


        final Response getResponse = given().contentType(ContentType.JSON)
                .when().body(creationTestConf).get("/recurrenceConfiguration/" + createdConf.getId().toString())
                .thenReturn();


        getResponse.then().statusCode(200)
                .body("name", equalTo("ModificationTest"))
                .body("timeUnit", equalTo(TimeUnit.YEAR.toString()))
                .body("occurrencesPerTimePeriod", equalTo(9));
    }
}
