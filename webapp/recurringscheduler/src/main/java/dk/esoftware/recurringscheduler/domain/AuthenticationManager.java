package dk.esoftware.recurringscheduler.domain;

import dk.esoftware.recurringscheduler.crypto.HashType;
import dk.esoftware.recurringscheduler.crypto.HashingStrategy;
import dk.esoftware.recurringscheduler.persistence.DefaultEntityManager;
import dk.esoftware.recurringscheduler.persistence.UserCredential;
import dk.esoftware.recurringscheduler.persistence.UserEntity;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.persistence.EntityManager;

@Singleton
public class AuthenticationManager {

    private final DefaultEntityManager<UserCredential> credentialManager;

    @Inject
    public AuthenticationManager(EntityManager entityManager) {
        credentialManager = new DefaultEntityManager<>(entityManager, UserCredential.class);
    }

    boolean verifyPassword(UserEntity user, String password) {
        if (user == null) {
            final HashingStrategy.HashingResult hashResult = HashType.ARGON2.getStrategy().hash("password");
            passwordsMatch("password", new UserCredential(null, UserCredential.CredentialType.PASSWORD, HashType.ARGON2.name(), hashResult.hash(), hashResult.metadata()));
            return false;
        }

        return user.getUserCredentials().stream()
                .filter(userCredential -> userCredential.getCredentialType() == UserCredential.CredentialType.PASSWORD)
                .anyMatch(userCredential -> passwordsMatch(password, userCredential));
    }

    public void setPassword(UserEntity user, String password) {

        user.getUserCredentials().removeIf(userCredential -> userCredential.getCredentialType() == UserCredential.CredentialType.PASSWORD);

        final HashingStrategy.HashingResult hashingResults = HashType.ARGON2.getStrategy().hash(password);

        final UserCredential credential = new UserCredential(user, UserCredential.CredentialType.PASSWORD, HashType.ARGON2.name(), hashingResults.hash(), hashingResults.metadata());
        credentialManager.createEntity(credential);
        user.getUserCredentials().add(credential);
    }

    private static boolean passwordsMatch(String password, UserCredential userCredential) {
        final HashingStrategy hashingStrategy = HashType.valueOf(userCredential.getAlgorithm()).getStrategy();
        return hashingStrategy.verify(password, new HashingStrategy.HashingResult(userCredential.getValue(), userCredential.getMetadata()));
    }
}
