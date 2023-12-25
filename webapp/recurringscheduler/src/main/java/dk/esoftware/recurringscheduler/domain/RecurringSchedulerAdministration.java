package dk.esoftware.recurringscheduler.domain;

import dk.esoftware.recurringscheduler.persistence.Event;
import dk.esoftware.recurringscheduler.persistence.EventType;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

@Singleton
public class RecurringSchedulerAdministration {

    @Inject
    ManagerProvider managerProvider;

    public Event createEventFromEventType(EventType eventType){
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

}
