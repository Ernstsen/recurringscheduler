package dk.esoftware.recurringscheduler.persistence;

import dk.esoftware.recurringscheduler.domain.DomainEntityManager;
import dk.esoftware.recurringscheduler.domain.ManagerProvider;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.persistence.EntityManager;

@Singleton
public class PersistenceManagerProvider implements ManagerProvider {

    @Inject
    EntityManager entityManager;

    @Override
    public DomainEntityManager<EventType> getEventTypeManager() {
        return new DefaultEntityManager<>(entityManager, EventType.class);
    }

    @Override
    public DomainEntityManager<Event> getEventManager() {
        return new DefaultEntityManager<>(entityManager, Event.class);
    }

    @Override
    public DomainEntityManager<RecurrenceConfiguration> getRecurrenceConfigurationManager() {
        return new DefaultEntityManager<>(entityManager, RecurrenceConfiguration.class);
    }

    @Override
    public DomainEntityManager<AuthenticatedSession> getAuthenticatedSessionManager() {
        return new DefaultEntityManager<>(entityManager, AuthenticatedSession.class);
    }

    @Override
    public UserEntityManager getUserManager() {
        return new UserEntityManager(entityManager);
    }
}
