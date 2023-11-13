package dk.esoftware.recurringscheduler.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import dk.esoftware.recurringscheduler.domain.TimeUnit;
import dk.esoftware.recurringscheduler.persistence.RecurrenceConfiguration;
import dk.esoftware.recurringscheduler.rest.dto.Identifiable;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.*;

public abstract class DefaultCRUDResourceTest<T extends Identifiable> {

    public static final ObjectMapper mapper = new ObjectMapper();
    private final String endpoint;

    public DefaultCRUDResourceTest(String endpoint) {
        this.endpoint = endpoint;
    }

    protected abstract T createNewEntity();

    protected abstract T modifyEntity(T entity);

    private Class<? extends T> getEntityClass() {
        //noinspection unchecked
        return (Class<? extends T>) createNewEntity().getClass();
    }

    @Test
    void testCreateEndpoint() throws IOException {
        // Create entity
        final T creationTestEntity = createNewEntity();

        final Response response = given().contentType(ContentType.JSON)
                .when().body(creationTestEntity).post(endpoint)
                .thenReturn();

        // Assert created matches requested
        final T createdConf = mapper.readValue(response.asByteArray(), getEntityClass());
        compareFieldExceptId(creationTestEntity, createdConf);
    }

    @Test
    void testFetchAfterCreateEndpoint() throws IOException {
        // Create entity
        final T creationTestEntity = createNewEntity();

        final Response response = given().contentType(ContentType.JSON)
                .when().body(creationTestEntity).post(endpoint)
                .thenReturn();

        final T createdConf = mapper.readValue(response.asByteArray(), getEntityClass());

        // Get the created entity
        final Response getResponse = given().contentType(ContentType.JSON)
                .when().body(creationTestEntity).get(endpoint + "/" + createdConf.getId().toString())
                .thenReturn();

        getResponse.then().statusCode(200)
                .contentType(ContentType.JSON);

        // Assert fetched entity matches created one
        final T received = mapper.readValue(response.asByteArray(), getEntityClass());
        compareFieldExceptId(creationTestEntity, received);
    }

    @Test
    void testModifyAfterCreateEndpoint() throws IOException {
        // Create new entity
        final T creationTestEntity = createNewEntity();

        final Response response = given().contentType(ContentType.JSON)
                .when().body(creationTestEntity).post(endpoint)
                .thenReturn();

        // Modify the entity
        final T createdEntity = mapper.readValue(response.asByteArray(), getEntityClass());
        T modifiedEntity = modifyEntity(createdEntity);

        final Response modifyResponse = given().contentType(ContentType.JSON)
                .when().body(modifiedEntity).put(endpoint + "/" + modifiedEntity.getId())
                .thenReturn();

        // Verify the modification response matches the requested
        modifyResponse.then().statusCode(201)
                .contentType(ContentType.JSON);
        final T receivedModified = mapper.readValue(modifyResponse.asByteArray(), getEntityClass());
        compareFieldExceptId(modifiedEntity, receivedModified);

        // Fetch the modified entity
        final Response getResponse = given().contentType(ContentType.JSON)
                .when().body(creationTestEntity).get(endpoint + "/" + modifiedEntity.getId().toString())
                .thenReturn();

        getResponse.then().statusCode(200)
                .contentType(ContentType.JSON);

        // Assert that it matches the expected values
        final T getResponseEntity = mapper.readValue(getResponse.asByteArray(), getEntityClass());
        Assertions.assertEquals(receivedModified, getResponseEntity);
    }

    private void compareFieldExceptId(T expected, T actual) {
        for (Field field : expected.getClass().getFields()) {
            if (field.getName().equals("id")) continue;

            try {
                field.setAccessible(true);
                Assertions.assertEquals(field.get(expected), field.get(actual));
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }

        }
    }

}
