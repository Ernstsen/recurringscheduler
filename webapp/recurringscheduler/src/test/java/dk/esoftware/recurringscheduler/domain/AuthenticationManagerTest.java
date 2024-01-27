package dk.esoftware.recurringscheduler.domain;

import dk.esoftware.recurringscheduler.crypto.HashType;
import dk.esoftware.recurringscheduler.crypto.HashingStrategy;
import dk.esoftware.recurringscheduler.persistence.UserCredential;
import dk.esoftware.recurringscheduler.persistence.UserEntity;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
class AuthenticationManagerTest {

    @Inject
    EntityManager entityManager;

    @Test
    @Transactional
    void verifyPassword() {
        AuthenticationManager authenticationManager = new AuthenticationManager(entityManager);
        UserEntity userEntity = new UserEntity();
        final HashingStrategy.HashingResult hashResult = HashType.ARGON2.getStrategy().hash("password");
        userEntity.getUserCredentials().add(new UserCredential(userEntity, UserCredential.CredentialType.PASSWORD, HashType.ARGON2.name(), hashResult.hash(), hashResult.metadata()));
        assertTrue(authenticationManager.verifyPassword(userEntity, "password"));
        assertFalse(authenticationManager.verifyPassword(userEntity, "wrongpassword"));
        assertFalse(authenticationManager.verifyPassword(null, "password"));
    }

    @Test
    @Transactional
    void setPassword() {
        AuthenticationManager authenticationManager = new AuthenticationManager(entityManager);
        UserEntity userEntity = new UserEntity("email", "name");
        entityManager.persist(userEntity);
        authenticationManager.setPassword(userEntity, "password");
        assertTrue(authenticationManager.verifyPassword(userEntity, "password"));
        assertFalse(authenticationManager.verifyPassword(userEntity, "wrongpassword"));
    }

}
