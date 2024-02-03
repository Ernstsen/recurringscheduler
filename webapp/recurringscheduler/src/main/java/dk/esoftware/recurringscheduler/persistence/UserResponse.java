package dk.esoftware.recurringscheduler.persistence;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

@Entity
public class UserResponse {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false)
    private UUID id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_entity_id", nullable = false)
    private UserEntity userEntity;

    @ElementCollection
    @Column(name = "chosen_dates")
    @CollectionTable(name = "UserResponse_chosenDates", joinColumns = @JoinColumn(name = "owner_id"))
    private Set<LocalDate> chosenDates = new LinkedHashSet<>();

    public UserResponse() {
    }

    public UserResponse(UUID id, Event event, UserEntity user, Set<LocalDate> chosenDates) {
        this.id = id;
        this.event = event;
        this.userEntity = user;
        this.chosenDates = chosenDates;
    }

    public Set<LocalDate> getChosenDates() {
        return chosenDates;
    }

    public void setChosenDates(Set<LocalDate> chosenDates) {
        this.chosenDates = chosenDates;
    }

    public UserEntity getUserEntity() {
        return userEntity;
    }

    public void setUserEntity(UserEntity userEntity) {
        this.userEntity = userEntity;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }
}
