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
        return new EventTypeEntityManager(entityManager);
    }

    @Override
    public DomainEntityManager<RecurranceConfiguration> getRecurranceConfigurationManager() {
        return new RecurranceConfigurationEntityManager(entityManager);
    }
}
