package dk.esoftware.recurringscheduler.domain;

import dk.esoftware.recurringscheduler.persistence.*;

public interface ManagerProvider {

    DomainEntityManager<EventType> getEventTypeManager();
    DomainEntityManager<Event> getEventManager();

    DomainEntityManager<RecurrenceConfiguration> getRecurrenceConfigurationManager();

    DomainEntityManager<AuthenticatedSession> getAuthenticatedSessionManager();

    UserEntityManager getUserManager();
}
