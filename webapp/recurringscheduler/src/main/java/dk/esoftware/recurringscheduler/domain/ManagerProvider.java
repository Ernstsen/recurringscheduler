package dk.esoftware.recurringscheduler.domain;

import dk.esoftware.recurringscheduler.persistence.EventType;
import dk.esoftware.recurringscheduler.persistence.RecurrenceConfiguration;
import dk.esoftware.recurringscheduler.persistence.UserEntity;

public interface ManagerProvider {

    DomainEntityManager<EventType> getEventTypeManager();

    DomainEntityManager<RecurrenceConfiguration> getRecurranceConfigurationManager();

    DomainEntityManager<UserEntity> getUserManager();
}
