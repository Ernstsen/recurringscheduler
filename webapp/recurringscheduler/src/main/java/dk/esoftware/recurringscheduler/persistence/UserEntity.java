package dk.esoftware.recurringscheduler.persistence;

import jakarta.persistence.*;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

@Entity
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false)
    private UUID id;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "owner")
    private Set<EventType> eventTypes = new LinkedHashSet<>();

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "name", nullable = false)
    private String name;

    @OneToMany(mappedBy = "user", orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<UserCredential> userCredentials = new LinkedHashSet<>();

    @OneToMany(mappedBy = "userEntity", orphanRemoval = true)
    private Set<AuthenticatedSession> authenticatedSessions = new LinkedHashSet<>();

    @OneToMany(mappedBy = "userEntity", orphanRemoval = true)
    private Set<UserResponse> userResponses = new LinkedHashSet<>();

    public Set<UserResponse> getUserResponses() {
        return userResponses;
    }

    public void setUserResponses(Set<UserResponse> userResponses) {
        this.userResponses = userResponses;
    }

    public UserEntity() {
    }

    public UserEntity(String email, String name) {
        this.email = email;
        this.name = name;
    }

    public Set<AuthenticatedSession> getAuthenticatedSessionses() {
        return authenticatedSessions;
    }

    public void setAuthenticatedSessionses(Set<AuthenticatedSession> authenticatedSessions) {
        this.authenticatedSessions = authenticatedSessions;
    }

    public Set<UserCredential> getUserCredentials() {
        return userCredentials;
    }

    public void setUserCredentials(Set<UserCredential> userCredentials) {
        this.userCredentials = userCredentials;
    }

    public Set<EventType> getEventTypes() {
        return eventTypes;
    }

    public void setEventTypes(Set<EventType> eventTypes) {
        this.eventTypes = eventTypes;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
