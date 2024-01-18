package dk.esoftware.recurringscheduler.domain;

import dk.esoftware.recurringscheduler.persistence.DefaultEntityManager;
import dk.esoftware.recurringscheduler.persistence.UserCredential;
import dk.esoftware.recurringscheduler.persistence.UserEntity;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.persistence.EntityManager;

@Singleton
public class AuthenticationManager {

    @Inject
    public AuthenticationManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    EntityManager entityManager;

    boolean verifyPassword(UserEntity user, String password) {
        if (user == null) {
            passwordsMatch("password", new UserCredential(null, UserCredential.CredentialType.PASSWORD, "password", null));
            return false;
        }

        return user.getUserCredentials().stream()
                .filter(userCredential -> userCredential.getCredentialType() == UserCredential.CredentialType.PASSWORD)
                .anyMatch(userCredential -> passwordsMatch(password, userCredential));
    }

    public void setPassword(UserEntity user, String password) {
        final DefaultEntityManager<UserCredential> credentialManager = new DefaultEntityManager<>(entityManager, UserCredential.class);

        user.getUserCredentials().removeIf(userCredential -> userCredential.getCredentialType() == UserCredential.CredentialType.PASSWORD);

        final UserCredential credential = new UserCredential(user, UserCredential.CredentialType.PASSWORD, password, null);
        credentialManager.createEntity(credential);
        user.getUserCredentials().add(credential);
    }

    private static boolean passwordsMatch(String password, UserCredential userCredential) {
        return password.equals(userCredential.getValue());
    }

}
