package dk.esoftware.recurringscheduler.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import dk.esoftware.recurringscheduler.persistence.EventType;
import dk.esoftware.recurringscheduler.persistence.RecurConfig;

import java.util.List;

public class Overview {

    private final List<EventType> eventTypes;
    private final List<RecurConfig> recurConfigs;

    @JsonCreator
    public Overview(List<EventType> eventTypes, List<RecurConfig> recurConfigs) {
        this.eventTypes = eventTypes;
        this.recurConfigs = recurConfigs;
    }

    public List<EventType> getEventTypes() {
        return eventTypes;
    }

    public List<RecurConfig> getRecurConfigs() {
        return recurConfigs;
    }
}
