package dk.esoftware.recurringscheduler.rest.dto;

import dk.esoftware.recurringscheduler.persistence.EventType;

import java.util.UUID;

public record EventTypeDTO(String name, UUID id, RecurrenceConfigurationDTO recurrenceConfiguration) {

    public static EventTypeDTO createEventTypeDTO(EventType eventType) {
        return new EventTypeDTO(
                eventType.getName(),
                eventType.getId(),
                RecurrenceConfigurationDTO.createRecurrenceConfigurationDTO(eventType.getRecurrenceConfiguration())
        );
    }
}
