package dk.esoftware.recurringscheduler.rest.dto;

public record AuthenticationResponse(String token, UserDTO user) {
}
