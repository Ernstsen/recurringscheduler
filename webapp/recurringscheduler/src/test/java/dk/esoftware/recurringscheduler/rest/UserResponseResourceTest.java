package dk.esoftware.recurringscheduler.rest;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import dk.esoftware.recurringscheduler.rest.dto.*;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.LocalDate;
import java.time.Month;
import java.util.*;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@QuarkusTest
public class UserResponseResourceTest extends DefaultCRUDResourceTest<UserResponseDTO> {

    private static final Random rand = new Random();
    private UserDTO user;
    private EventDTO event;

    public UserResponseResourceTest() {
        super(
                "userResponse",
                Map.of("userResponse", "GET;PUT")
        );
    }

    @BeforeEach
    void setUp() throws IOException {
        final AuthenticationResponse login = login();
        final byte[] byteArray = given().when().header("Authorization", "Bearer " + login.token())
                .get("/recurrenceConfigurations").getBody().asByteArray();
        List<RecurrenceConfigurationDTO> recurrenceConfigurations = new ObjectMapper()
                .readValue(
                        byteArray,
                        new TypeReference<>() {
                        });

        // Create User
        UserDTO createUser1 = new UserDTO(null, "User1" + rand.nextInt(), "EventTestUser1" + rand.nextInt() + "@mail.com", null);

        final Response userResponse1 = given().contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + login.token())
                .when().body(createUser1).post("/users")
                .thenReturn();
        user = mapper.readValue(userResponse1.asByteArray(), UserDTO.class);

        // Create Entity
        final EventTypeDTO creationTestEntity = new EventTypeDTO("eventType", null, recurrenceConfigurations.get(1), Collections.singletonList(user));

        final Response response = given().contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + login.token())
                .when().body(creationTestEntity).post("/eventTypes")
                .thenReturn();


        List<EventTypeDTO> eventTypes = new ArrayList<>();
        eventTypes.add(mapper.readValue(response.asByteArray(), EventTypeDTO.class));


        // Create Event

        final List<LocalDate> possibleTimes = Arrays.asList(LocalDate.of(2023, Month.DECEMBER, 3), LocalDate.of(2023, Month.DECEMBER, 10), LocalDate.of(2023, Month.DECEMBER, 17));
        final EventDTO creationTestEntityEvent = new EventDTO(null, "event", eventTypes.get(0), user, possibleTimes, null);

        final Response responseEvent = given().contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + login.token())
                .when().body(creationTestEntityEvent).post("/events")
                .thenReturn();

        event = mapper.readValue(responseEvent.asByteArray(), EventDTO.class);
    }

    @Test
    void testCreateFromType() throws IOException {
        final Response response = given().contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + login().token())
                .when().post("/userResponse/events/" + event.id())
                .thenReturn();

        assertEquals(201, response.getStatusCode());

        final Response getFromEvent = given().contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + login().token())
                .when().get("/userResponse/events/" + event.id())
                .thenReturn();

        final List<UserResponseDTO> userResponses = mapper.readValue(getFromEvent.asByteArray(), new TypeReference<>() {
        });

        assertEquals(1, userResponses.size());
    }

    @Test
    void testCreateFromTypeIdempotent() throws IOException {
        final Response response = given().contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + login().token())
                .when().post("/userResponse/events/" + event.id())
                .thenReturn();

        assertEquals(201, response.getStatusCode());

        final Response response2 = given().contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + login().token())
                .when().post("/userResponse/events/" + event.id())
                .thenReturn();

        assertEquals(201, response2.getStatusCode());

        final Response getFromEvent = given().contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + login().token())
                .when().get("/userResponse/events/" + event.id())
                .thenReturn();

        final List<UserResponseDTO> userResponses = mapper.readValue(getFromEvent.asByteArray(), new TypeReference<>() {
        });

        assertEquals(1, userResponses.size());
    }

    @Test
    void testGetFromEvent() throws IOException {
        final UserResponseDTO newEntity = createNewEntity();

        final Response response = given().contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + login().token())
                .when().body(newEntity).post("/userResponse")
                .thenReturn();

        final UserResponseDTO createdEntity = mapper.readValue(response.asByteArray(), UserResponseDTO.class);

        final Response getFromEvent = given().contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + login().token())
                .when().get("/userResponse/events/" + event.id())
                .thenReturn();

        final List<UserResponseDTO> userResponses = mapper.readValue(getFromEvent.asByteArray(), new TypeReference<>() {
        });

        assertEquals(1, userResponses.size());
        assertTrue(userResponses.contains(createdEntity));
    }

    @Override
    protected UserResponseDTO createNewEntity() {
        return new UserResponseDTO(null,
                event, user.id(),
                event.possibleTimes()
        );
    }

    @Override
    protected UserResponseDTO modifyEntity(UserResponseDTO entity) {
        final List<LocalDate> newPossibleTimes = new ArrayList<>(entity.chosenDates());

        return new UserResponseDTO(
                entity.id(),
                entity.event(),
                entity.userEntityId(),
                newPossibleTimes.subList(0, newPossibleTimes.size() - 1)
        );
    }
}
