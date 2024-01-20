package dk.esoftware.recurringscheduler.rest;

import dk.esoftware.recurringscheduler.domain.TimeUnit;
import dk.esoftware.recurringscheduler.rest.dto.AuthenticationResponse;
import dk.esoftware.recurringscheduler.rest.dto.RecurrenceConfigurationDTO;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.*;

@QuarkusTest
public class RecurrenceConfigurationResourceTest extends DefaultCRUDResourceTest<RecurrenceConfigurationDTO> {

    private AuthenticationResponse login;

    @BeforeEach
    void setUp() throws IOException {
        login = login();
    }

    public RecurrenceConfigurationResourceTest() {
        super("/recurrenceConfigurations");
    }

    @Override
    protected RecurrenceConfigurationDTO createNewEntity() {
        return new RecurrenceConfigurationDTO("Newly Created configuration", null, TimeUnit.WEEK, 4);
    }

    @Override
    protected RecurrenceConfigurationDTO modifyEntity(RecurrenceConfigurationDTO entity) {
        return new RecurrenceConfigurationDTO(
                entity.name(),
                entity.id(),
                TimeUnit.MONTH,
                5
        );
    }

    @Test
    void testOverviewEndpointPostInitialization() {
        given()
                .header("Authorization", "Bearer " + login.token())
                .when().get("/recurrenceConfigurations")
                .then()
                .body(
                        containsString("Once a week"),
                        containsString("Once a month"),
                        containsString("Twice a year")
                );

        given()
                .header("Authorization", "Bearer " + login.token())
                .when().get("/users")
                .then()
                .body(
                        containsString("admin@localhost"),
                        containsString("admin")
                );

    }
}
