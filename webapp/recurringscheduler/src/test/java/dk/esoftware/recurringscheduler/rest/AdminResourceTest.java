package dk.esoftware.recurringscheduler.rest;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
public class AdminResourceTest {

    @Test
    void testOverviewEndpointPostInitialization() {
        given()
                .when().get("/admin/overview")
                .then()
                .body(
                        containsString("Once a week"),
                        containsString("Once a month"),
                        containsString("Twice a year")
                );
    }
}
