package dk.esoftware.recurringscheduler.rest.dto;

import dk.esoftware.recurringscheduler.persistence.UserResponse;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * DTO for {@link dk.esoftware.recurringscheduler.persistence.UserResponse}
 */
public record UserResponseDTO(UUID id, EventDTO event, UUID userEntityId,
                              List<LocalDate> chosenDates) implements Serializable, Identifiable {

    public static UserResponseDTO createUserResponseDTO(UserResponse userResponse) {
        if (userResponse == null) {
            return null;
        }

        return new UserResponseDTO(
                userResponse.getId(),
                EventDTO.createEventTypeDTO(userResponse.getEvent()),
                userResponse.getUserEntity().getId(),
                new ArrayList<>(userResponse.getChosenDates())
        );
    }

    @Override
    public UUID getId() {
        return id();
    }
}
