package dk.esoftware.recurringscheduler.rest.dto;

import dk.esoftware.recurringscheduler.persistence.UserResponse;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

/**
 * DTO for {@link dk.esoftware.recurringscheduler.persistence.UserResponse}
 */
public record UserResponseDTO(UUID id, UUID eventId, UUID userEntityId,
                              Set<LocalDate> chosenDates) implements Serializable, Identifiable {

    public static UserResponseDTO createUserResponseDTO(UserResponse userResponse) {
        return new UserResponseDTO(userResponse.getId(), userResponse.getEvent().getId(),
                userResponse.getUserEntity().getId(), userResponse.getChosenDates());
    }

    @Override
    public UUID getId() {
        return id();
    }
}
