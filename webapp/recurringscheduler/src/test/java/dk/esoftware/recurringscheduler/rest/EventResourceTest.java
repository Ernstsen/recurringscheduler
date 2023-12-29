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

import java.io.IOException;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
public class EventResourceTest extends DefaultCRUDResourceTest<EventDTO> {

    private static final Random rand = new Random();
    private List<EventTypeDTO> eventTypes;
    private List<UserDTO> users;

    public EventResourceTest() {
        super("events");
    }

    @BeforeEach
    void setUp() throws IOException {
        final byte[] byteArray = given().when().get("/recurrenceConfigurations").getBody().asByteArray();
        List<RecurrenceConfigurationDTO> recurrenceConfigurations = new ObjectMapper()
                .readValue(
                        byteArray,
                        new TypeReference<>() {
                        });

        // Create entity
        final EventTypeDTO creationTestEntity = new EventTypeDTO("eventType", null, recurrenceConfigurations.get(1), new ArrayList<>());
        final EventTypeDTO creationTestEntity2 = new EventTypeDTO("eventType2", null, recurrenceConfigurations.get(2), new ArrayList<>());

        final Response response = given().contentType(ContentType.JSON)
                .when().body(creationTestEntity).post("/eventTypes")
                .thenReturn();

        final Response response2 = given().contentType(ContentType.JSON)
                .when().body(creationTestEntity2).post("/eventTypes")
                .thenReturn();

        eventTypes = new ArrayList<>();
        eventTypes.add(mapper.readValue(response.asByteArray(), EventTypeDTO.class));
        eventTypes.add(mapper.readValue(response2.asByteArray(), EventTypeDTO.class));

        // Create 2 Users
        UserDTO createUser1 = new UserDTO(null, "User1" + rand.nextInt(), "EventTestUser1" + rand.nextInt() + "@mail.com", null);
        UserDTO createUser2 = new UserDTO(null, "User2" + rand.nextInt(), "EventTestUser2" + rand.nextInt() + "@mail.com", null);

        final Response userResponse1 = given().contentType(ContentType.JSON)
                .when().body(createUser1).post("/users")
                .thenReturn();
        final Response userResponse2 = given().contentType(ContentType.JSON)
                .when().body(createUser2).post("/users")
                .thenReturn();
        users = new ArrayList<>();
        users.add(mapper.readValue(userResponse1.asByteArray(), UserDTO.class));
        users.add(mapper.readValue(userResponse2.asByteArray(), UserDTO.class));


    }

    @Override
    protected EventDTO createNewEntity() {
        return new EventDTO(null, "eventName", eventTypes.get(0), users.get(0),
                Arrays.asList(LocalDate.of(2023, Month.DECEMBER, 2), LocalDate.of(2023, Month.DECEMBER, 9), LocalDate.of(2023, Month.DECEMBER, 16)),
                null
        );
    }

    @Override
    protected EventDTO modifyEntity(EventDTO entity) {
        final List<LocalDate> newPossibleTimes = new ArrayList<>(entity.possibleTimes());
        newPossibleTimes.add(LocalDate.of(2023, Month.DECEMBER, 3));
        newPossibleTimes.add(LocalDate.of(2023, Month.DECEMBER, 10));
        newPossibleTimes.add(LocalDate.of(2023, Month.DECEMBER, 17));

        return new EventDTO(
                entity.getId(),
                entity.name() + " changed!",
                eventTypes.get(1),
                users.get(1),
                newPossibleTimes,
                entity.possibleTimes().get(1)
        );
    }
}
