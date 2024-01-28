package dk.esoftware.recurringscheduler.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import dk.esoftware.recurringscheduler.rest.dto.AuthenticationResponse;
import dk.esoftware.recurringscheduler.rest.dto.Identifiable;
import dk.esoftware.recurringscheduler.rest.dto.LoginRequest;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.lang.reflect.Field;

import static io.restassured.RestAssured.given;

public abstract class DefaultCRUDResourceTest<T extends Identifiable> {

    public static final ObjectMapper mapper = new ObjectMapper();
    private static final String email = "admin@localhost";
    private static final String password = "superSecretPassword123";
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

    @BeforeAll
    static void beforeAll() {
        mapper.registerModule(new JavaTimeModule());
    }

    @Test
    void testCreateEndpoint() throws IOException {
        // Create entity
        final T creationTestEntity = createNewEntity();

        final Response response = given().contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + login().token())
                .when().body(creationTestEntity).post(endpoint)
                .thenReturn();

        // Assert created matches requested
        final T createdConf = mapper.readValue(response.asByteArray(), getEntityClass());
        compareFieldExceptId(creationTestEntity, createdConf);
    }

    @Test
    void testCreateEndpointUnauthorized() throws IOException {
        // Create entity
        final T creationTestEntity = createNewEntity();

        given().contentType(ContentType.JSON)
                .when().body(creationTestEntity).post(endpoint)
                .then()
                .statusCode(401);
    }

    @Test
    void testFetchAfterCreateEndpoint() throws IOException {
        // Create entity
        final T creationTestEntity = createNewEntity();

        final Response response = given().contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + login().token())
                .when().body(creationTestEntity).post(endpoint)
                .thenReturn();

        final T createdConf = mapper.readValue(response.asByteArray(), getEntityClass());

        // Get the created entity
        final Response getResponse = given().contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + login().token())
                .when().body(creationTestEntity).get(endpoint + "/" + createdConf.getId().toString())
                .thenReturn();

        getResponse.then().statusCode(200)
                .contentType(ContentType.JSON);

        // Assert fetched entity matches created one
        final T received = mapper.readValue(response.asByteArray(), getEntityClass());
        compareFieldExceptId(creationTestEntity, received);
    }

    @Test
    void testUnauthorizedFetchAfterCreateEndpoint() throws IOException {
        // Create entity
        final T creationTestEntity = createNewEntity();

        final Response response = given().contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + login().token())
                .when().body(creationTestEntity).post(endpoint)
                .thenReturn();

        final T createdConf = mapper.readValue(response.asByteArray(), getEntityClass());

        // Get the created entity
        given().contentType(ContentType.JSON)
                .when().body(creationTestEntity).get(endpoint + "/" + createdConf.getId().toString())
                .then().statusCode(401);
    }

    @Test
    void testModifyAfterCreateEndpoint() throws IOException {
        // Create new entity
        final T creationTestEntity = createNewEntity();

        final Response response = given().contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + login().token())
                .when().body(creationTestEntity).post(endpoint)
                .thenReturn();

        // Modify the entity
        final T createdEntity = mapper.readValue(response.asByteArray(), getEntityClass());
        T modifiedEntity = modifyEntity(createdEntity);

        final Response modifyResponse = given().contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + login().token())
                .when().body(modifiedEntity).put(endpoint + "/" + modifiedEntity.getId())
                .thenReturn();

        // Verify the modification response matches the requested
        modifyResponse.then().statusCode(201)
                .contentType(ContentType.JSON);
        final T receivedModified = mapper.readValue(modifyResponse.asByteArray(), getEntityClass());
        compareFieldExceptId(modifiedEntity, receivedModified);

        // Fetch the modified entity
        final Response getResponse = given().contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + login().token())
                .when().body(creationTestEntity).get(endpoint + "/" + modifiedEntity.getId().toString())
                .thenReturn();

        getResponse.then().statusCode(200)
                .contentType(ContentType.JSON);

        // Assert that it matches the expected values
        final T getResponseEntity = mapper.readValue(getResponse.asByteArray(), getEntityClass());
        Assertions.assertEquals(receivedModified, getResponseEntity);
    }

    @Test
    void testUnauthorizedModifyAfterCreateEndpoint() throws IOException {
        // Create new entity
        final T creationTestEntity = createNewEntity();

        final Response response = given().contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + login().token())
                .when().body(creationTestEntity).post(endpoint)
                .thenReturn();

        // Modify the entity
        final T createdEntity = mapper.readValue(response.asByteArray(), getEntityClass());
        T modifiedEntity = modifyEntity(createdEntity);

        given().contentType(ContentType.JSON)
                .when().body(modifiedEntity).put(endpoint + "/" + modifiedEntity.getId())
                .then().statusCode(401);
    }

    private void compareFieldExceptId(T expected, T actual) {
        for (Field field : expected.getClass().getDeclaredFields()) {
            if (field.getName().equals("id")) continue;

            try {
                field.setAccessible(true);
                Assertions.assertEquals(field.get(expected), field.get(actual), "Unexpected value for field: " + field.getName());
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }

        }
    }

    @Test
    void testCreateAndDelete() throws IOException {
        // Create entity
        final T creationTestEntity = createNewEntity();

        final Response response = given().contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + login().token())
                .when().body(creationTestEntity).post(endpoint)
                .thenReturn();

        // Assert created matches requested
        final T createdEntity = mapper.readValue(response.asByteArray(), getEntityClass());
        compareFieldExceptId(creationTestEntity, createdEntity);

        given().contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + login().token())

                .when().delete(endpoint + "/" + createdEntity.getId().toString())
                .then().statusCode(200);

        given().contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + login().token())

                .when().get(endpoint + "/" + createdEntity.getId().toString())
                .then().statusCode(404);

    }

    @Test
    void testCreateAndUnauthorizedDelete() throws IOException {
        // Create entity
        final T creationTestEntity = createNewEntity();

        final Response response = given().contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + login().token())
                .when().body(creationTestEntity).post(endpoint)
                .thenReturn();

        // Assert created matches requested
        final T createdEntity = mapper.readValue(response.asByteArray(), getEntityClass());
        compareFieldExceptId(creationTestEntity, createdEntity);

        given().contentType(ContentType.JSON)
                .when().delete(endpoint + "/" + createdEntity.getId().toString())
                .then().statusCode(401);

        given().contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + login().token())
                .when().get(endpoint + "/" + createdEntity.getId().toString())
                .then().statusCode(200);

    }

    protected AuthenticationResponse login() throws IOException {
        final Response response = given()
                .when().contentType(ContentType.JSON).body(new LoginRequest(email, password))
                .post("/authentication/login")
                .thenReturn();

        Assertions.assertEquals(201, response.statusCode());

        return mapper.readValue(response.asByteArray(), AuthenticationResponse.class);
    }

}
