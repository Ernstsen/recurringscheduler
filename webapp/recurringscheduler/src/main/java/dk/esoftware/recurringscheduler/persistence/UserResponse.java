package dk.esoftware.recurringscheduler.persistence;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.*;

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
    @Column(name = "chosen_date")
    @CollectionTable(name = "UserResponse_chosenDates", joinColumns = @JoinColumn(name = "owner_id"))
    private List<LocalDate> chosenDates = new ArrayList<>();

    public UserResponse() {
    }

    public UserResponse(Event event, UserEntity user, List<LocalDate> chosenDates) {
        this.event = event;
        this.userEntity = user;
        this.chosenDates = chosenDates;
    }

    public List<LocalDate> getChosenDates() {
        return chosenDates;
    }

    public void setChosenDates(List<LocalDate> chosenDates) {
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
