package dk.esoftware.recurringscheduler.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import dk.esoftware.recurringscheduler.rest.dto.AuthenticationResponse;
import dk.esoftware.recurringscheduler.rest.dto.LoginRequest;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@QuarkusTest
public class AuthenticationResourceTest {
    private static final ObjectMapper mapper = new ObjectMapper();
    private static final String DEFAULT_ADMIN_USERNAME = "admin";
    private static final String DEFAULT_ADMIN_EMAIL = "admin@localhost";
    private static final String DEFAULT_ADMIN_PASSWORD = "superSecretPassword123";


    @Test
    void testLoginWorksWithCorrectPassword() throws IOException {
        final Response response = given()
                .when().contentType(ContentType.JSON).body(new LoginRequest(DEFAULT_ADMIN_EMAIL, DEFAULT_ADMIN_PASSWORD))
                .post("/authentication/login")
                .thenReturn();


        assertEquals(201, response.statusCode());

        final AuthenticationResponse authenticationResponse = mapper.readValue(response.asByteArray(), AuthenticationResponse.class);

        assertNotNull(authenticationResponse);
        assertNotNull(authenticationResponse.token());
        assertNotNull(authenticationResponse.user());
        assertEquals(DEFAULT_ADMIN_USERNAME, authenticationResponse.user().name());
        assertEquals(DEFAULT_ADMIN_EMAIL, authenticationResponse.user().email());
    }

    @Test
    void testLoginDoesntWorksWithIncorrectPassword() {
        final Response response = given()
                .when().contentType(ContentType.JSON).body(new LoginRequest(DEFAULT_ADMIN_EMAIL, DEFAULT_ADMIN_PASSWORD + DEFAULT_ADMIN_PASSWORD))
                .post("/authentication/login")
                .thenReturn();

        assertEquals(401, response.statusCode());
        response.then().body(containsString("Wrong username or password"));
    }

    @Test
    void testLoginGivesErrorDescriptionIfNoPasswordGiven() {
        final Response response = given()
                .when().contentType(ContentType.JSON).body(new LoginRequest(DEFAULT_ADMIN_EMAIL, null))
                .post("/authentication/login")
                .thenReturn();

        assertEquals(400, response.statusCode());
        response.then().body(containsString("Must supply both email and password to sign in"));
    }

    @Test
    void testLoginGivesErrorDescriptionIfNoEmailGiven() {
        final Response response = given()
                .when().contentType(ContentType.JSON).body(new LoginRequest(null, DEFAULT_ADMIN_PASSWORD + DEFAULT_ADMIN_PASSWORD))
                .post("/authentication/login")
                .thenReturn();

        assertEquals(400, response.statusCode());
        response.then().body(containsString("Must supply both email and password to sign in"));
    }


}
