package dk.esoftware.recurringscheduler.rest;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import dk.esoftware.recurringscheduler.domain.TimeUnit;
import dk.esoftware.recurringscheduler.persistence.EventType;
import dk.esoftware.recurringscheduler.persistence.RecurrenceConfiguration;
import dk.esoftware.recurringscheduler.rest.dto.EventDTO;
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
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

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

    @Test
    public void testCreateEventFromType() throws IOException {
        // Create eventType to create event from
        final EventTypeDTO creationTestEntity = createNewEntity();

        final Response response = given().contentType(ContentType.JSON)
                .when().body(creationTestEntity).post("eventTypes")
                .thenReturn();

        final EventTypeDTO createdEventType = mapper.readValue(response.asByteArray(), EventTypeDTO.class);

        // Create event from type
        final Response creationResponse = given()
                .when().post("eventTypes/" + createdEventType.getId() + "/createEvent")
                .thenReturn();

        creationResponse.then().statusCode(200);

        final EventDTO createdEvent = mapper.readValue(creationResponse.asByteArray(), EventDTO.class);
        assertNotNull(createdEvent);
        assertNotNull(createdEvent.id());
        assertNotNull(createdEvent.name());
        assertNull(createdEvent.chosenTime());
    }
}
