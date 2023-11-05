package dk.esoftware.recurringscheduler.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import dk.esoftware.recurringscheduler.persistence.EventType;
import dk.esoftware.recurringscheduler.persistence.RecurranceConfiguration;

import java.util.List;

public class Overview {

    private final List<EventType> eventTypes;
    private final List<RecurranceConfiguration> recurranceConfigurations;

    @JsonCreator
    public Overview(List<EventType> eventTypes, List<RecurranceConfiguration> recurranceConfigurations) {
        this.eventTypes = eventTypes;
        this.recurranceConfigurations = recurranceConfigurations;
    }

    public List<EventType> getEventTypes() {
        return eventTypes;
    }

    public List<RecurranceConfiguration> getRecurConfigs() {
        return recurranceConfigurations;
    }
}
