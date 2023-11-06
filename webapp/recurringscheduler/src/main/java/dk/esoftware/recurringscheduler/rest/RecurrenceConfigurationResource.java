package dk.esoftware.recurringscheduler.rest;

import dk.esoftware.recurringscheduler.domain.ManagerProvider;
import dk.esoftware.recurringscheduler.persistence.RecurrenceConfiguration;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import java.util.List;

@Path("/recurrenceConfiguration")
public class RecurrenceConfigurationResource {

    @Inject
    ManagerProvider managerProvider;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public List<RecurrenceConfiguration> getRecurrenceConfigurations(){
        return managerProvider.getRecurranceConfigurationManager().getEntities();
    }

}
