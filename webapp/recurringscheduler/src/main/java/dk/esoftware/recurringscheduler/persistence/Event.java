package dk.esoftware.recurringscheduler.persistence;

import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.proxy.HibernateProxy;
import org.hibernate.type.SqlTypes;

import java.time.Instant;
import java.time.LocalDate;
import java.util.*;

@Entity
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "name", nullable = false)
    private String name;

    @ManyToOne()
    @JoinColumn(name = "event_type")
    private EventType eventType;

    @ManyToOne()
    @JoinColumn(name = "owner_id")
    private UserEntity owner;

    @ElementCollection
    @Column(name = "time_possibility")
    @CollectionTable(name = "Event_timePossibilities", joinColumns = @JoinColumn(name = "owner_id"))
    private List<LocalDate> possibleTimes = new ArrayList<>();

    @Column(name = "chosen_time")
    @JdbcTypeCode(SqlTypes.DATE)
    private LocalDate chosenTime;

    @OneToMany(mappedBy = "event")
    private Set<UserResponse> userResponses = new LinkedHashSet<>();

    public Set<UserResponse> getUserResponses() {
        return userResponses;
    }

    public void setUserResponses(Set<UserResponse> userResponses) {
        this.userResponses = userResponses;
    }

    public LocalDate getChosenTime() {
        return chosenTime;
    }

    public void setChosenTime(LocalDate chosenTime) {
        this.chosenTime = chosenTime;
    }

    public List<LocalDate> getPossibleTimes() {
        return possibleTimes;
    }

    public void setPossibleTimes(List<LocalDate> possibleTimes) {
        this.possibleTimes = possibleTimes;
    }

    public UserEntity getOwner() {
        return owner;
    }

    public void setOwner(UserEntity owner) {
        this.owner = owner;
    }

    public Event() {
    }

    public Event(String name, EventType eventType, UserEntity owner, List<LocalDate> possibleTimes, LocalDate chosenTime) {
        this.name = name;
        this.eventType = eventType;
        this.owner = owner;
        this.possibleTimes = possibleTimes;
        this.chosenTime = chosenTime;
    }

    public EventType getEventType() {
        return eventType;
    }

    public void setEventType(EventType eventType) {
        this.eventType = eventType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UUID getId() {
        return id;
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        Event eventType = (Event) o;
        return getId() != null && Objects.equals(getId(), eventType.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}
