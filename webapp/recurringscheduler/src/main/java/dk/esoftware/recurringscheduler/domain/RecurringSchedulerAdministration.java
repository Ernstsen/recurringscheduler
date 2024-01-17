package dk.esoftware.recurringscheduler.domain;

import dk.esoftware.recurringscheduler.persistence.AuthenticatedSession;
import dk.esoftware.recurringscheduler.persistence.Event;
import dk.esoftware.recurringscheduler.persistence.EventType;
import dk.esoftware.recurringscheduler.persistence.UserEntity;
import dk.esoftware.recurringscheduler.rest.dto.AuthenticationResponse;
import dk.esoftware.recurringscheduler.rest.dto.UserDTO;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

@Singleton
public class RecurringSchedulerAdministration {

    @Inject
    AuthenticationManager authenticationManager;

    @Inject
    ManagerProvider managerProvider;

    public Event createEventFromEventType(EventType eventType) {
        Event newEventEntity = new Event(
                "Event of type: " + eventType.getName(),
                eventType,
                null,
                null,
                null
        );

        managerProvider.getEventManager().createEntity(newEventEntity);

        return newEventEntity;
    }

    public AuthenticationResponse authenticate(String email, String password) {
        final UserEntity user = managerProvider.getUserManager().getUserByEmail(email);

        final boolean correctPassword = authenticationManager.verifyPassword(user, password);

        if (!correctPassword) {
            return null;
        }

        final AuthenticatedSession session = new AuthenticatedSession(user);
        managerProvider.getAuthenticatedSessionManager().createEntity(session);

        return new AuthenticationResponse(session.getId().toString(), UserDTO.createUserDTO(user));
    }


}
