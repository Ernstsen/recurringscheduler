package dk.esoftware.recurringscheduler.rest;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.parsing.Parser;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;

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
    void testCreateEndpoint(){
        new RecurreCon

        System.out.println(new String(given().when().get("/recurrenceConfiguration").getBody().asByteArray()));
    }
}
