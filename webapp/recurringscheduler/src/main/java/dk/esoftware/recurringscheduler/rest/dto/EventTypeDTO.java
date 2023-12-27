package dk.esoftware.recurringscheduler.rest.dto;

import dk.esoftware.recurringscheduler.persistence.EventType;
import dk.esoftware.recurringscheduler.persistence.UserEntity;

import java.util.*;

public record EventTypeDTO(
        String name,
        UUID id,
        RecurrenceConfigurationDTO recurrenceConfiguration,
        List<UserDTO> participatingUsers) implements Identifiable {

    public static EventTypeDTO createEventTypeDTO(EventType eventType) {
        if (eventType == null) {
            return null;
        }
        final Set<UserEntity> participatingUsers = eventType.getParticipatingUsers() != null ? (eventType.getParticipatingUsers()) : new HashSet<>();

        return new EventTypeDTO(
                eventType.getName(),
                eventType.getId(),
                RecurrenceConfigurationDTO.createRecurrenceConfigurationDTO(eventType.getRecurrenceConfiguration()),
                participatingUsers.stream().map(UserDTO::createUserDTO).toList()
        );
    }

    @Override
    public UUID getId() {
        return id();
    }
}
