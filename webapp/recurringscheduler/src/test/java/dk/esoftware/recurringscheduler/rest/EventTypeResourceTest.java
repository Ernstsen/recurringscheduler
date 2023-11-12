package dk.esoftware.recurringscheduler.rest;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import dk.esoftware.recurringscheduler.domain.TimeUnit;
import dk.esoftware.recurringscheduler.persistence.EventType;
import dk.esoftware.recurringscheduler.persistence.RecurrenceConfiguration;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.*;

@QuarkusTest
public class EventTypeResourceTest {

    private List<RecurrenceConfiguration> recurrenceConfigurations;

    @BeforeEach
    void setUp() throws IOException {
        given()
                .when().post("/admin/init")
                .then().statusCode(200)
                .body(is("Ensured proper initialization"));


        final byte[] byteArray = given().when().get("/recurrenceConfiguration").getBody().asByteArray();
        recurrenceConfigurations = new ObjectMapper()
                .readValue(
                        byteArray,
                        new TypeReference<>() {
                        });
    }

    @Test
    void testCreateEndpoint() {
        final EventType creationTestConf = new EventType("CreationTest", recurrenceConfigurations.get(0));

        final Response response = given().contentType(ContentType.JSON)
                .when().body(creationTestConf).post("/eventType")
                .thenReturn();

        response.then().statusCode(201)
                .contentType(ContentType.JSON)
                .body("name", equalTo("CreationTest"))
                .body("recurrenceConfiguration.id", equalTo(recurrenceConfigurations.get(0).getId().toString()));
        System.out.println(new String(given().when().get("/eventType").getBody().asByteArray()));
    }

    @Test
    void testFetchAfterCreateEndpoint() throws IOException {
        final EventType creationTestConf = new EventType(
                "CreationTest2", recurrenceConfigurations.get(1));

        final Response response = given().contentType(ContentType.JSON)
                .when().body(creationTestConf).post("/eventType")
                .thenReturn();

        final EventType createdConf = new ObjectMapper().readValue(response.asByteArray(), EventType.class);

        final Response getResponse = given().contentType(ContentType.JSON)
                .when().body(creationTestConf).get("/eventType/" + createdConf.getId().toString())
                .thenReturn();

        getResponse.then().statusCode(200)
                .contentType(ContentType.JSON)
                .body("name", equalTo("CreationTest2"))
                .body("recurrenceConfiguration.id", equalTo(recurrenceConfigurations.get(1).getId().toString()));

    }

    @Test
    void testModifyAfterCreateEndpoint() throws IOException {
        final EventType creationTestConf = new EventType(
                "ModificationTest", recurrenceConfigurations.get(2));

        final Response response = given().contentType(ContentType.JSON)
                .when().body(creationTestConf).post("/eventType")
                .thenReturn();

        final EventType createdConf = new ObjectMapper().readValue(response.asByteArray(), EventType.class);


        createdConf.setRecurrenceConfiguration(recurrenceConfigurations.get(1));

        final Response modifyResponse = given().contentType(ContentType.JSON)
                .when().body(createdConf).put("/eventType/" + createdConf.getId())
                .thenReturn();

        modifyResponse.then().statusCode(201)
                .contentType(ContentType.JSON)
                .body("name", equalTo("ModificationTest"))
                .body("recurrenceConfiguration.id", equalTo(recurrenceConfigurations.get(1).getId().toString()));


        final Response getResponse = given().contentType(ContentType.JSON)
                .when().body(creationTestConf).get("/eventType/" + createdConf.getId().toString())
                .thenReturn();


        getResponse.then().statusCode(200)
                .body("name", equalTo("ModificationTest"))
                .body("recurrenceConfiguration.id", equalTo(recurrenceConfigurations.get(1).getId().toString()));
    }
}
