package dk.esoftware.recurringscheduler.rest;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import dk.esoftware.recurringscheduler.rest.dto.EventTypeDTO;
import dk.esoftware.recurringscheduler.rest.dto.RecurrenceConfigurationDTO;
import dk.esoftware.recurringscheduler.rest.dto.UserDTO;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
public class UserResourceTest extends DefaultCRUDResourceTest<UserDTO> {

    private HashSet<EventTypeDTO> eventTypes;

    @BeforeEach
    void setUp() throws IOException {
        given()
                .when().post("/admin/init")
                .then().statusCode(200)
                .body(is("Ensured proper initialization"));


        final byte[] byteArray = given().when().get("/recurrenceConfigurations").getBody().asByteArray();
        List<RecurrenceConfigurationDTO> recurrenceConfigurations = new ObjectMapper()
                .readValue(
                        byteArray,
                        new TypeReference<>() {
                        });

        // Create entity
        final EventTypeDTO creationTestEntity = new EventTypeDTO("eventType", null, recurrenceConfigurations.get(1));
        final EventTypeDTO creationTestEntity2 = new EventTypeDTO("eventType2", null, recurrenceConfigurations.get(2));

        final Response response = given().contentType(ContentType.JSON)
                .when().body(creationTestEntity).post("/eventTypes")
                .thenReturn();

        final Response response2 = given().contentType(ContentType.JSON)
                .when().body(creationTestEntity2).post("/eventTypes")
                .thenReturn();

        eventTypes = new HashSet<>();
        eventTypes.add(mapper.readValue(response.asByteArray(), EventTypeDTO.class));
        eventTypes.add(mapper.readValue(response2.asByteArray(), EventTypeDTO.class));
    }

    public UserResourceTest() {
        super("/users");
    }

    @Override
    protected UserDTO createNewEntity() {
        return new UserDTO( null, "User name", UUID.randomUUID() + "@mail.com",  eventTypes);
    }

    @Override
    protected UserDTO modifyEntity(UserDTO entity) {
        return new UserDTO(
                entity.id(),
                entity.name() + "changed",
                entity.email(),
                new HashSet<>()
        );
    }
}
