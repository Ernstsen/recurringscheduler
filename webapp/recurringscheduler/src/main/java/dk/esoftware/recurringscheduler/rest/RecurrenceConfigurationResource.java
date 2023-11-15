package dk.esoftware.recurringscheduler.rest;

import dk.esoftware.recurringscheduler.domain.ManagerProvider;
import dk.esoftware.recurringscheduler.persistence.RecurrenceConfiguration;
import dk.esoftware.recurringscheduler.rest.dto.RecurrenceConfigurationDTO;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Path("/recurrenceConfigurations")
public class RecurrenceConfigurationResource {

    @Inject
    ManagerProvider managerProvider;

    @POST
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    @Transactional
    public Response createRecurrenceConfiguration(RecurrenceConfigurationDTO configurationDTO) {
        final RecurrenceConfiguration newConfiguration = new RecurrenceConfiguration(configurationDTO.name(), configurationDTO.timeUnit(), configurationDTO.occurrencesPerTimePeriod());
        managerProvider.getRecurranceConfigurationManager().createEntity(newConfiguration);

        return Response.status(201).entity(RecurrenceConfigurationDTO.createRecurrenceConfigurationDTO(newConfiguration)).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public List<RecurrenceConfigurationDTO> getRecurrenceConfigurations() {
        final List<RecurrenceConfiguration> entities = managerProvider.getRecurranceConfigurationManager().getEntities();
        return entities.stream().map(RecurrenceConfigurationDTO::createRecurrenceConfigurationDTO).collect(Collectors.toList());
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    @Transactional
    public Response getRecurrenceConfiguration(@PathParam("id") UUID id) {
        final RecurrenceConfiguration entity = managerProvider.getRecurranceConfigurationManager().getEntity(id);

        if(entity != null){
            return Response.ok().entity(RecurrenceConfigurationDTO.createRecurrenceConfigurationDTO(entity)).build();
        }

        return Response.status(Response.Status.NOT_FOUND).build();
    }


    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes({MediaType.APPLICATION_JSON})
    @Path("/{id}")
    @Transactional
    public Response updateRecurrenceConfiguration(@PathParam("id") UUID id, RecurrenceConfigurationDTO payload) {
        if (!id.equals(payload.id())) {
            return Response.status(400).entity("Id from path and payload must match").build();
        }

        final RecurrenceConfiguration entity = managerProvider.getRecurranceConfigurationManager().getEntity(id);
        entity.setName(payload.name());
        entity.setOccurrencesPerTimePeriod(payload.occurrencesPerTimePeriod());
        entity.setTimeUnit(payload.timeUnit());

        return Response.status(201).entity(entity).build();
    }

    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    @Transactional
    public Response deleteRecurrenceConfiguration(@PathParam("id") UUID id) {
        managerProvider.getRecurranceConfigurationManager().deleteEntity(id);
        return Response.ok().build();
    }

}
