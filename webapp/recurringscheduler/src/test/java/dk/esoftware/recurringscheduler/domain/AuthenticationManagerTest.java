package dk.esoftware.recurringscheduler.domain;

import dk.esoftware.recurringscheduler.persistence.UserCredential;
import dk.esoftware.recurringscheduler.persistence.UserEntity;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AuthenticationManagerTest {

    @Test
    void verifyPassword() {
        AuthenticationManager authenticationManager = new AuthenticationManager();
        UserEntity userEntity = new UserEntity();
        userEntity.getUserCredentials().add(new UserCredential(userEntity, UserCredential.CredentialType.PASSWORD, "password", "password"));
        assertTrue(authenticationManager.verifyPassword(userEntity, "password"));
        assertFalse(authenticationManager.verifyPassword(userEntity, "wrongpassword"));
        assertFalse(authenticationManager.verifyPassword(null, "password"));
    }

    @Test
    void setPassword() {
        AuthenticationManager authenticationManager = new AuthenticationManager();
        UserEntity userEntity = new UserEntity();
        authenticationManager.setPassword(userEntity, "password");
        assertTrue(authenticationManager.verifyPassword(userEntity, "password"));
        assertFalse(authenticationManager.verifyPassword(userEntity, "wrongpassword"));
    }

}
