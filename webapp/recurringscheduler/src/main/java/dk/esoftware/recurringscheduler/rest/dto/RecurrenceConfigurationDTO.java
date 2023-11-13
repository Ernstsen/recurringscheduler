package dk.esoftware.recurringscheduler.rest.dto;

import dk.esoftware.recurringscheduler.domain.TimeUnit;
import dk.esoftware.recurringscheduler.persistence.RecurrenceConfiguration;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

/**
 * DTO for {@link dk.esoftware.recurringscheduler.persistence.RecurrenceConfiguration}
 */
public record RecurrenceConfigurationDTO(String name, UUID id, TimeUnit timeUnit, Integer occurrencesPerTimePeriod) implements Identifiable {

    public static RecurrenceConfigurationDTO createRecurrenceConfigurationDTO(RecurrenceConfiguration recurrenceConfiguration) {
        return new RecurrenceConfigurationDTO(
                recurrenceConfiguration.getName(),
                recurrenceConfiguration.getId(),
                recurrenceConfiguration.getTimeUnit(),
                recurrenceConfiguration.getOccurrencesPerTimePeriod()
        );
    }

    @Override
    public UUID getId() {
        return id();
    }
}
