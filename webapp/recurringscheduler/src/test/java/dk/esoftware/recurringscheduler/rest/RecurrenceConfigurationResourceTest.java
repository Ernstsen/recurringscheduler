package dk.esoftware.recurringscheduler.rest;

import dk.esoftware.recurringscheduler.domain.TimeUnit;
import dk.esoftware.recurringscheduler.rest.dto.RecurrenceConfigurationDTO;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.*;

@QuarkusTest
public class RecurrenceConfigurationResourceTest extends DefaultCRUDResourceTest<RecurrenceConfigurationDTO> {


    public RecurrenceConfigurationResourceTest() {
        super("/recurrenceConfiguration");
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
}
