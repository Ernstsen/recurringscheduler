package dk.esoftware.recurringscheduler.rest.dto;

import dk.esoftware.recurringscheduler.persistence.UserEntity;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public record UserDTO(UUID id, String name, String email, Set<EventTypeDTO> eventTypes) implements Identifiable{

    public static UserDTO createUserDTO(UserEntity userEntity) {
        return new UserDTO(
                userEntity.getId(),
                userEntity.getName(),
                userEntity.getEmail(),
                userEntity.getEventTypes().stream().map(EventTypeDTO::createEventTypeDTO).collect(Collectors.toSet())
        );
    }

    @Override
    public UUID getId() {
        return id();
    }
}
