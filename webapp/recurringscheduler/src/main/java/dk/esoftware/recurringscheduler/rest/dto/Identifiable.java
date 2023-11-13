package dk.esoftware.recurringscheduler.rest.dto;

import java.util.UUID;

public interface Identifiable {

    /**
     * @return identifier for the object
     */
    UUID getId();

}
