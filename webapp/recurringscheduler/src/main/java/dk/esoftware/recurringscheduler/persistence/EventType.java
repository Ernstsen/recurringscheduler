package dk.esoftware.recurringscheduler.persistence;

import jakarta.persistence.*;
import org.hibernate.proxy.HibernateProxy;

import java.util.Objects;
import java.util.UUID;

@Entity
public class EventType {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "name", nullable = false)
    private String name;

    @ManyToOne()
    @JoinColumn(name = "recur_config_id")
    private RecurrenceConfiguration recurrenceConfiguration;

    @ManyToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "owner_id")
    private UserEntity owner;

    public UserEntity getOwner() {
        return owner;
    }

    public void setOwner(UserEntity owner) {
        this.owner = owner;
    }

    public EventType() {
    }

    public EventType(String name, RecurrenceConfiguration recurrenceConfiguration) {
        this.name = name;
        this.recurrenceConfiguration = recurrenceConfiguration;
    }

    public RecurrenceConfiguration getRecurrenceConfiguration() {
        return recurrenceConfiguration;
    }

    public void setRecurrenceConfiguration(RecurrenceConfiguration recurrenceConfiguration) {
        this.recurrenceConfiguration = recurrenceConfiguration;
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
        EventType eventType = (EventType) o;
        return getId() != null && Objects.equals(getId(), eventType.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}
