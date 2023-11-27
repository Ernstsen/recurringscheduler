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

    @OneToMany(mappedBy = "owner", orphanRemoval = true)
    private Set<EventType> eventTypes = new LinkedHashSet<>();

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "name", nullable = false)
    private String name;

    public void setEventTypes(Set<EventType> eventTypes) {
        this.eventTypes = eventTypes;
    }

    public Set<EventType> getEventTypes() {
        return eventTypes;
    }

    public UserEntity() {
    }

    public UserEntity(String email, String name) {
        this.email = email;
        this.name = name;
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
