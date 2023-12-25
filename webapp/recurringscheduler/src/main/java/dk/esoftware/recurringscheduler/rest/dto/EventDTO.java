package dk.esoftware.recurringscheduler.rest.dto;

import dk.esoftware.recurringscheduler.persistence.Event;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public record EventDTO(
        UUID id,
        String name,
        EventTypeDTO type,
        UserDTO owner,
        List<LocalDate> possibleTimes,
        LocalDate chosenTime) implements Identifiable {

    public static EventDTO createEventTypeDTO(Event event) {
        if (event == null) {
            return null;
        }
        return new EventDTO(
                event.getId(),
                event.getName(),
                EventTypeDTO.createEventTypeDTO(event.getEventType()),
                UserDTO.createUserDTO(event.getOwner()),
                event.getPossibleTimes() != null ? new ArrayList<>(event.getPossibleTimes()) : new ArrayList<>(),
                event.getChosenTime()
        );
    }

    @Override
    public UUID getId() {
        return id();
    }
}
