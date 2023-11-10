package dk.esoftware.recurringscheduler.rest;

import dk.esoftware.recurringscheduler.domain.ManagerProvider;
import dk.esoftware.recurringscheduler.persistence.RecurrenceConfiguration;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.GenericEntity;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;
import java.util.UUID;

@Path("/recurrenceConfiguration")
public class RecurrenceConfigurationResource {

    @Inject
    ManagerProvider managerProvider;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public List<RecurrenceConfiguration> getRecurrenceConfigurations() {
        return managerProvider.getRecurranceConfigurationManager().getEntities();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    @Transactional
    public RecurrenceConfiguration getRecurrenceConfiguration(@PathParam("id") UUID id) {
        return managerProvider.getRecurranceConfigurationManager().getEntity(id);
    }

    @POST
    @Produces({MediaType.APPLICATION_JSON})
    @Transactional
    public Response createRecurrenceConfiguration(RecurrenceConfiguration configuration) {
        managerProvider.getRecurranceConfigurationManager().createEntity(configuration);

        return Response.status(201).entity(configuration).build();
    }


    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    @Transactional
    public Response updateRecurrenceConfiguration(@PathParam("id") UUID id, RecurrenceConfiguration payload) {
        if(!id.equals(payload.getId())){
            return Response.status(400).entity("Id from path and payload must match").build();
        }

        final RecurrenceConfiguration entity = managerProvider.getRecurranceConfigurationManager().getEntity(id);
        entity.setName(payload.getName());
        entity.setOccurrencesPerTimePeriod(payload.getOccurrencesPerTimePeriod());
        entity.setTimeUnit(payload.getTimeUnit());

        return Response.status(201).entity(entity).build();
    }

}
