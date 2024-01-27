package dk.esoftware.recurringscheduler.crypto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class Argon2Test {

    @Test
    void hash() {
        final Argon2 argon2 = new Argon2();
        final HashingStrategy.HashingResult hashingResult = argon2.hash("password");
        assertNotNull(hashingResult);
        assertNotNull(hashingResult.hash());
        assertNotNull(hashingResult.metadata());
    }

    @Test
    void hashIsDifferent() {
        final Argon2 argon2 = new Argon2();
        final HashingStrategy.HashingResult hashingResult = argon2.hash("password");
        final HashingStrategy.HashingResult hashingResult2 = argon2.hash("password");
        assertNotEquals(hashingResult.hash(), hashingResult2.hash());
    }

    @Test
    void verify() {
        final Argon2 argon2 = new Argon2();
        final HashingStrategy.HashingResult hashingResult = argon2.hash("password");
        assertTrue(argon2.verify("password", hashingResult));
        assertFalse(argon2.verify("password2", hashingResult));
    }

}
