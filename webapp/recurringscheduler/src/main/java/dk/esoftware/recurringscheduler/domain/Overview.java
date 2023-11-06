package dk.esoftware.recurringscheduler.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import dk.esoftware.recurringscheduler.persistence.EventType;
import dk.esoftware.recurringscheduler.persistence.RecurrenceConfiguration;

import java.util.List;

public class Overview {

    private final List<EventType> eventTypes;
    private final List<RecurrenceConfiguration> recurrenceConfigurations;

    @JsonCreator
    public Overview(List<EventType> eventTypes, List<RecurrenceConfiguration> recurrenceConfigurations) {
        this.eventTypes = eventTypes;
        this.recurrenceConfigurations = recurrenceConfigurations;
    }

    public List<EventType> getEventTypes() {
        return eventTypes;
    }

    public List<RecurrenceConfiguration> getRecurConfigs() {
        return recurrenceConfigurations;
    }
}
