package dk.esoftware.recurringscheduler.rest;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import dk.esoftware.recurringscheduler.domain.TimeUnit;
import dk.esoftware.recurringscheduler.persistence.EventType;
import dk.esoftware.recurringscheduler.persistence.RecurrenceConfiguration;
import dk.esoftware.recurringscheduler.rest.dto.EventTypeDTO;
import dk.esoftware.recurringscheduler.rest.dto.RecurrenceConfigurationDTO;
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
public class EventTypeResourceTest extends DefaultCRUDResourceTest<EventTypeDTO> {

    private List<RecurrenceConfigurationDTO> recurrenceConfigurations;

    public EventTypeResourceTest() {
        super("eventTypes");
    }

    @BeforeEach
    void setUp() throws IOException {
        given()
                .when().post("/admin/init")
                .then().statusCode(200)
                .body(is("Ensured proper initialization"));


        final byte[] byteArray = given().when().get("/recurrenceConfigurations").getBody().asByteArray();
        recurrenceConfigurations = new ObjectMapper()
                .readValue(
                        byteArray,
                        new TypeReference<>() {
                        });
    }

    @Override
    protected EventTypeDTO createNewEntity() {
        return new EventTypeDTO("CreationTest", null, recurrenceConfigurations.get(0));
    }

    @Override
    protected EventTypeDTO modifyEntity(EventTypeDTO entity) {
        return new EventTypeDTO(entity.name() + "_modified", entity.getId(), recurrenceConfigurations.get(1));
    }
}
