package dk.esoftware.recurringscheduler.persistence;


import com.fasterxml.jackson.annotation.JsonIgnore;
import dk.esoftware.recurringscheduler.domain.TimeUnit;
import jakarta.persistence.*;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

@Entity
public class RecurrenceConfiguration {

    public RecurrenceConfiguration() {

    }


    public RecurrenceConfiguration(String name, TimeUnit timeUnit, Integer occurrencesPerTimePeriod) {
        this.name = name;
        this.timeUnit = timeUnit;
        this.occurrencesPerTimePeriod = occurrencesPerTimePeriod;
    }

    @Column(name = "name", nullable = false)
    private String name;

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false)
    private UUID id;

    @OneToMany(mappedBy = "recurrenceConfiguration", orphanRemoval = true)
    private Set<EventType> usedBy = new LinkedHashSet<>();

    @Enumerated
    @Column(name = "time_unit")
    private TimeUnit timeUnit;

    @Column(name = "occurrences_per_time_period")
    private Integer occurrencesPerTimePeriod;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getOccurrencesPerTimePeriod() {
        return occurrencesPerTimePeriod;
    }

    public void setOccurrencesPerTimePeriod(Integer occurrencesPerTimePeriod) {
        this.occurrencesPerTimePeriod = occurrencesPerTimePeriod;
    }

    public TimeUnit getTimeUnit() {
        return timeUnit;
    }

    public void setTimeUnit(TimeUnit timeUnit) {
        this.timeUnit = timeUnit;
    }

    @JsonIgnore
    public Set<EventType> getUsedBy() {
        return usedBy;
    }

    public void setUsedBy(Set<EventType> usedBy) {
        this.usedBy = usedBy;
    }

    public UUID getId() {
        return id;
    }
}
