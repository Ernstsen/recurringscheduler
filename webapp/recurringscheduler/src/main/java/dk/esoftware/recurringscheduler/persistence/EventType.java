package dk.esoftware.recurringscheduler.persistence;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
public class EventType {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "name", nullable = false)
    private String name;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "recur_config_id")
    private RecurrenceConfiguration recurrenceConfiguration;

    public RecurrenceConfiguration getRecurConfig() {
        return recurrenceConfiguration;
    }

    public void setRecurConfig(RecurrenceConfiguration recurrenceConfiguration) {
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
}
