package dk.esoftware.recurringscheduler.domain;

import dk.esoftware.recurringscheduler.persistence.UserCredential;
import dk.esoftware.recurringscheduler.persistence.UserEntity;
import jakarta.inject.Singleton;

@Singleton
public class AuthenticationManager {

    boolean verifyPassword(UserEntity user, String password) {
        if(user == null) {
            passwordsMatch("password", new UserCredential(null, UserCredential.CredentialType.PASSWORD, "password", null));
            return false;
        }

        return user.getUserCredentials().stream()
                .filter(userCredential -> userCredential.getCredentialType() == UserCredential.CredentialType.PASSWORD)
                .anyMatch(userCredential -> passwordsMatch(password, userCredential));
    }

    public void setPassword(UserEntity user, String password) {
        user.getUserCredentials().removeIf(userCredential -> userCredential.getCredentialType() == UserCredential.CredentialType.PASSWORD);
        user.getUserCredentials().add(new UserCredential(user, UserCredential.CredentialType.PASSWORD, password, "password"));
    }

    private static boolean passwordsMatch(String password, UserCredential userCredential) {
        return password.equals(userCredential.getValue());
    }

}
