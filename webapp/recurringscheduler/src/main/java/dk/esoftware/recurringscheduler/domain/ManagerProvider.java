package dk.esoftware.recurringscheduler.domain;

import dk.esoftware.recurringscheduler.persistence.EventType;
import dk.esoftware.recurringscheduler.persistence.RecurranceConfiguration;

public interface ManagerProvider {

    DomainEntityManager<EventType> getEventTypeManager();

    DomainEntityManager<RecurranceConfiguration> getRecurranceConfigurationManager();

}
