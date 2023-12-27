package dk.esoftware.recurringscheduler.rest;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import dk.esoftware.recurringscheduler.rest.dto.EventDTO;
import dk.esoftware.recurringscheduler.rest.dto.EventTypeDTO;
import dk.esoftware.recurringscheduler.rest.dto.RecurrenceConfigurationDTO;
import dk.esoftware.recurringscheduler.rest.dto.UserDTO;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@QuarkusTest
public class EventTypeResourceTest extends DefaultCRUDResourceTest<EventTypeDTO> {

    private List<RecurrenceConfigurationDTO> recurrenceConfigurations;
    private UserDTO userDTO;

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

        final Response getUser = given().contentType(ContentType.JSON)
                .when().get("users")
                .thenReturn();

        userDTO = mapper.readValue(getUser.asByteArray(), new TypeReference<List<UserDTO>>() {})
                .stream().filter(user -> "testUserForEventType".equals(user.name())).findFirst()
                .orElse(null);

        if (userDTO == null) {
            // Create User
            final UserDTO userDTO = new UserDTO(null, "testUserForEventType", "test@EventTypeResourceTest.java", new HashSet<>());

            final Response createUserResponse = given().contentType(ContentType.JSON)
                    .when().body(userDTO).post("users")
                    .thenReturn();

            this.userDTO = mapper.readValue(createUserResponse.asByteArray(), UserDTO.class);
        }

    }

    @Override
    protected EventTypeDTO createNewEntity() {
        return new EventTypeDTO("CreationTest", null, recurrenceConfigurations.get(0), new ArrayList<>());
    }

    @Override
    protected EventTypeDTO modifyEntity(EventTypeDTO entity) {
        return new EventTypeDTO(entity.name() + "_modified", entity.getId(), recurrenceConfigurations.get(1), Collections.singletonList(userDTO));
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
